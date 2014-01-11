package com.TeamNovus.Persistence.Databases.MySQL;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.TeamNovus.Persistence.Databases.Configuration;
import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Enums.DataType;
import com.TeamNovus.Persistence.Internal.ColumnRegistration;
import com.TeamNovus.Persistence.Internal.TableRegistration;
import com.TeamNovus.Persistence.Internal.TableRegistrationFactory;
import com.TeamNovus.Persistence.Queries.Queries.DeleteQuery;
import com.TeamNovus.Persistence.Queries.Queries.InsertQuery;
import com.TeamNovus.Persistence.Queries.Queries.SelectQuery;
import com.TeamNovus.Persistence.Queries.Queries.UpdateQuery;

import static com.TeamNovus.Persistence.Queries.Expression.Expressions.*;

public class MySQLDatabase extends Database {
	
	public MySQLDatabase(Configuration configuration) throws InstantiationException {
		super(configuration, new MySQLProvider());
		
		if (!(configuration instanceof MySQLConfiguration))
			throw new InstantiationException("Configuration is not a MySQLConfiguration object.");
	}

	public void connect() {
		try {
			String url = String.format("jdbc:mysql://%s:%s/%s", 
					configuration.get("host"), 
					configuration.get("port"), 
					configuration.get("database"));

			Class.forName("com.mysql.jdbc.Driver").newInstance();

			connection = DriverManager.getConnection(url, configuration.get("username"), configuration.get("password"));	
			
			super.connect();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			connection.close();
			
			super.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createStructure(Class<?> objectClass) {
		if(isDisconnected()) {
			connect();
		}

		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			String query = String.format("CREATE TABLE IF NOT EXISTS %s (", table.getName());

			for (int i = 0; i < table.getColumns().size(); i++) {
				ColumnRegistration column = table.getColumns().get(i);

				// Build the column parameters:
				String type = " " + provider.getDataType(DataType.getDataType(column.getType()));
				String notNull = column.isNotNull() ? " NOT NULL" : "";
				String unique =  column.isUnique() ? " UNIQUE" : "";
				String autoIncrement = (table.getId().getName().equals(column.getName()) ? " AUTO_INCREMENT PRIMARY KEY" : "");

				if(i == 0) {
					query += column.getName() + type + notNull + unique + autoIncrement;
				} else {
					query += ", " + column.getName() + type + notNull + unique + autoIncrement;
				}
			}

			query += ")";
						
			PreparedStatement statement = connection.prepareStatement(query);

			statement.execute();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateStructure(Class<?> objectClass) {
		if(isDisconnected()) {
			connect();
		}

		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			String query = String.format("SELECT * FROM %s LIMIT 1", table.getName());
			PreparedStatement statement = connection.prepareStatement(query);

			ResultSetMetaData meta = statement.executeQuery().getMetaData();

			LinkedList<ColumnRegistration> toChange = new LinkedList<ColumnRegistration>();
			LinkedList<ColumnRegistration> toAdd = new LinkedList<ColumnRegistration>();

			for(ColumnRegistration column : table.getColumns()) {
				boolean found = false;

				for (int i = 1; i <= meta.getColumnCount(); i++) {
					if(column.getName().equals(meta.getColumnName(i))) {
						found = true;

						if(!(provider.getDataType(DataType.getDataType(column.getType())).equals(meta.getColumnTypeName(i)))) {
							toChange.add(column);
							break;
						}						
					}
				}

				if(!(found)) {
					toAdd.add(column);
				}
			}
			
			statement.close();

			for(ColumnRegistration column : toChange) {
				String type = " " + provider.getDataType(DataType.getDataType(column.getType()));
				String notNull = column.isNotNull() ? " NOT NULL" : "";
				String unique =  column.isUnique() ? " UNIQUE" : "";
				String autoIncrement = (table.getId().equals(column) ? " AUTO_INCREMENT" : "");

				String modify = String.format("ALTER TABLE %s MODIFY COLUMN %s" + type + notNull + unique + autoIncrement, table.getName(), column.getName());
				PreparedStatement modifyStatement = connection.prepareStatement(modify);

				modifyStatement.execute();
			}

			for(ColumnRegistration column : toAdd) {
				String type = " " + provider.getDataType(DataType.getDataType(column.getType()));
				String notNull = column.isNotNull() ? " NOT NULL" : "";
				String unique =  column.isUnique() ? " UNIQUE" : "";
				String autoIncrement = (table.getId().equals(column) ? " AUTO_INCREMENT" : "");

				String add = String.format("ALTER TABLE %s ADD COLUMN %s" + type + notNull + unique + autoIncrement, table.getName(), column.getName());

				PreparedStatement addStatement = connection.prepareStatement(add);

				addStatement.execute();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public <T> T find(Class<T> objectClass, int id) {
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			SelectQuery<T> query = select(objectClass).where(equal(table.getId().getName(), id));
			
			return query.findOne();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}

	public <T> T find(Class<T> objectClass, long id) {
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			SelectQuery<T> query = select(objectClass).where(equal(table.getId().getName(), table.getId().getValue(id)));
			
			return query.findOne();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public <T> boolean save(T object) {
		if(isDisconnected()) {
			connect();
		}
		
		try {			
			TableRegistration table = TableRegistrationFactory.getTableRegistration(object.getClass());

			String[] columns = ArrayUtils.EMPTY_STRING_ARRAY;
			Object[] values = ArrayUtils.EMPTY_OBJECT_ARRAY;
			
			for (int i = 0; i < table.getColumns().size(); i++) {
				ColumnRegistration column = table.getColumns().get(i);
				
				if(!(table.getId().getName().equals(column.getName()))) {
					columns = (String[]) ArrayUtils.add(columns, column.getName());
					values = ArrayUtils.add(values, column.getValue(object));
				}
			}
				
			boolean success = false;
			ResultSet generatedKeys = null;
			
			if(table.getId().getValue(object) == null) {
				@SuppressWarnings("unchecked")
				InsertQuery<T> query = insert((Class<T>) object.getClass()).columns(columns).values(values);
				
				success = query.execute();
				generatedKeys = query.getGeneratedKeys();
			} else {
				@SuppressWarnings("unchecked")
				UpdateQuery<T> query = update((Class<T>) object.getClass()).columns(columns).values(values).where(equal(table.getId().getName(), table.getId().getValue(object)));
			
				success = query.execute();
				generatedKeys = query.getGeneratedKeys();
			}
			
			if(generatedKeys != null && generatedKeys.next()) {
				if(table.getId().getType() == Integer.class || table.getId().getType() == int.class) {
					table.getId().setValue(object, generatedKeys.getInt(1));
				} else {
					table.getId().setValue(object, generatedKeys.getObject(1));
				}
			}
			
			return success;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public <T> boolean drop(T object) {
		if(isDisconnected()) {
			connect();
		}
		
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(object.getClass());

			@SuppressWarnings("unchecked")
			DeleteQuery<T> query = delete((Class<T>) object.getClass()).where(equal(table.getId().getName(), table.getId().getValue(object)));
			
			return query.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public <T> List<T> findAll(Class<T> objectClass) {
		if(isDisconnected()) {
			connect();
		}

		try {
			SelectQuery<T> query = select(objectClass);
			
			return query.findList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<T>();
	}

	public void saveAll(Iterable<?> objects) {
		Iterator<?> iterator = objects.iterator();
		
		while(iterator.hasNext()) {
			save(iterator.next());
		}
	}

	public void dropAll(Iterable<?> objects) {
		Iterator<?> iterator = objects.iterator();
		
		while(iterator.hasNext()) {
			drop(iterator.next());
		}
	}

	public void beginTransaction() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void endTransaction() {
		try {
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
