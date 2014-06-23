package com.novus.persistence.databases.mysql;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;

import com.novus.persistence.databases.Configuration;
import com.novus.persistence.databases.Database;
import com.novus.persistence.enums.DataType;
import com.novus.persistence.internal.ColumnRegistration;
import com.novus.persistence.internal.TableRegistration;
import com.novus.persistence.internal.TableRegistrationFactory;

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		connection = null;
	}
	
	public void createStructure(Class<?> objectClass) {
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			String query = String.format("CREATE TABLE IF NOT EXISTS %s (", table.getName());

			for (int i = 0; i < table.getColumns().size(); i++) {
				ColumnRegistration column = table.getColumns().get(i);

				// Build the column parameters:
				String type = " " + provider.getDataType(DataType.getDataType(column.getType()));
				String notNull = column.isNullable() ? " NOT NULL" : "";
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
				String notNull = column.isNullable() ? " NOT NULL" : "";
				String unique =  column.isUnique() ? " UNIQUE" : "";
				String autoIncrement = (table.getId().equals(column) ? " AUTO_INCREMENT" : "");

				String modify = String.format("ALTER TABLE %s MODIFY COLUMN %s" + type + notNull + unique + autoIncrement, table.getName(), column.getName());
				PreparedStatement modifyStatement = connection.prepareStatement(modify);

				modifyStatement.execute();
			}

			for(ColumnRegistration column : toAdd) {
				String type = " " + provider.getDataType(DataType.getDataType(column.getType()));
				String notNull = column.isNullable() ? " NOT NULL" : "";
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
}
