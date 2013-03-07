package com.TeamNovus.Persist.Databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.TeamNovus.Persist.Database;
import com.TeamNovus.Persist.Exceptions.TableRegistrationException;
import com.TeamNovus.Persist.Internal.ColumnRegistration;
import com.TeamNovus.Persist.Internal.SubTableRegistration;
import com.TeamNovus.Persist.Internal.TableRegistration;

public class MySQLDatabase extends Database {
	private MySQLConfiguration configuration;
	private Connection connection;

	public MySQLDatabase(MySQLConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void connect() {
		super.connect();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = String.format("jdbc:mysql://%s:%s/%s", 
					configuration.getHost(), 
					configuration.getPort(), 
					configuration.getDatabase());

			connection = DriverManager.getConnection(url, configuration.getUsername(), configuration.getPassword());				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		super.disconnect();

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> T find(Class<T> objectClass, Integer id) {
		try {
			TableRegistration table = getTableRegistration(objectClass);

			String query = String.format("SELECT * FROM %s WHERE %s = ?", table.getName(), table.getId().getName());

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			ResultSet result = statement.executeQuery();

			while(result.next()) {
				T object = objectClass.newInstance(); 

				for(ColumnRegistration column : table.getColumns()) {
					column.setValue(object, result.getObject(column.getName()));
				}

				result.close();
				statement.close();

				loadRelationshipObjects(object);
				
				return object;
			}
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public <T> List<T> findBy(Class<T> objectClass, String condition, Object... params) {
		LinkedList<T> entries = new LinkedList<T>();

		try {
			TableRegistration table = getTableRegistration(objectClass);

			String query = String.format("SELECT * FROM %s", table.getName(), table.getId().getName());
			if(condition != "" || condition != null) {
				query += " WHERE " + condition;
			}

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}

			ResultSet result = statement.executeQuery();


			while(result.next()) {
				T object = objectClass.newInstance(); 

				for(ColumnRegistration column : table.getColumns()) {
					column.setValue(object, result.getObject(column.getName()));
				}

				loadRelationshipObjects(object);

				entries.add(object);
			}

			result.close();
			statement.close();

			return entries;
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return entries;
	}

	@Override
	public void save(Object object) {		
		try {
			TableRegistration table = getTableRegistration(object.getClass());
			
			if(table.getId().getValue(object) == null) {
				LinkedList<Object> arguments = new LinkedList<Object>();
				String query = String.format("INSERT INTO %s (", table.getName());

				for (int i = 0; i < table.getColumns().size(); i++) {		
					ColumnRegistration column = table.getColumns().get(i);

					if(i == 0) {
						query += String.format("%s", column.getName());
					} else {
						query += String.format(", %s", column.getName());
					}					
				}

				query += String.format(")");

				query += String.format(" VALUES (");

				for (int i = 0; i < table.getColumns().size(); i++) {		
					ColumnRegistration column = table.getColumns().get(i);

					if(i == 0) {
						query += String.format("?");
					} else {
						query += String.format(", ?");
					}

					arguments.add(column.getValue(object));
				}

				query += String.format(")");

				// Prepare and execute.
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

				for (int i = 0; i < arguments.size(); i++) {
					statement.setObject(i + 1, arguments.get(i));
				}

				statement.execute();

				ResultSet result = statement.getGeneratedKeys();

				if (result.next()){
					table.getId().setValue(object, result.getInt(1));
				}

				result.close();
				statement.close();
			} else {
				LinkedList<Object> arguments = new LinkedList<Object>();
				String query = String.format("UPDATE %s SET ", table.getName());

				for (int i = 0; i < table.getColumns().size(); i++) {	
					ColumnRegistration column = table.getColumns().get(i);

					if(i == 0) {
						query += String.format(" %s = ?", column.getName());
					} else {
						query += String.format(", %s = ?", column.getName());
					}

					arguments.add(column.getValue(object));
				}

				query += String.format(" WHERE %s = ?", table.getId().getName());

				arguments.add(table.getId().getValue(object));

				// Prepare and execute.
				PreparedStatement statement = connection.prepareStatement(query);

				for (int i = 0; i < arguments.size(); i++) {
					statement.setObject(i + 1, arguments.get(i));
				}

				statement.execute();

				statement.close();
			}
			
			saveRelationshipObjects(object);
			dropRemovedObjects(object);
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void dropRemovedObjects(Object object) {
		try {
			TableRegistration table = getTableRegistration(object.getClass());

			for(SubTableRegistration subTable : table.getSubTables()) {
				switch (subTable.getRelationshipType()) {
				case ONE_TO_ONE:
					subTable.getParentField().setAccessible(true);

					// Load the stored data:
					Object storedData = null;
					
					try {
						storedData = findBy(subTable.getTableClass(), subTable.getForeignKey().getName() + " = ?", (Integer) table.getId().getValue(object)).get(0);
					} catch(IndexOutOfBoundsException ignored) { }

					// Load the current object data:
					Object currentData = null;
					
					try {
						currentData = subTable.getParentField().get(object);
					} catch (Exception e) { }
					
					// Check if the insertion id's are the same:
					if(storedData != null && currentData != null && !(subTable.getId().getValue(currentData).equals(subTable.getId().getValue(storedData)))) {
						drop(storedData);
					}
					break;

				case ONE_TO_MANY:
					subTable.getParentField().setAccessible(true);

					List<?> storedChildren = findBy(subTable.getTableClass(), subTable.getForeignKey().getName() + " = ?", table.getId().getValue(object));
					List<?> currentChildren = (List<?>) subTable.getParentField().get(object);
					
					List<Object> toRemove = new ArrayList<Object>();
					
					for(Object storedObject : storedChildren) {
						boolean remove = true;
						
						for(Object currentObject : currentChildren) {
							if(subTable.getId().getValue(currentObject).equals(subTable.getId().getValue(storedObject))) {
								remove = false;
								break;
							}
						}
						
						if(remove)
							toRemove.add(storedObject);
					}
					
					for(Object o : toRemove) {
						drop(o);
					}
					break;
				}
			}
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadRelationshipObjects(Object object) {
		try {
			TableRegistration table = getTableRegistration(object.getClass());

			for(SubTableRegistration subTable : table.getSubTables()) {
				switch (subTable.getRelationshipType()) {
				case ONE_TO_ONE:
					subTable.getParentField().setAccessible(true);

					Object data = null;
					
					try {
						data = findBy(subTable.getTableClass(), subTable.getForeignKey().getName() + " = ?", (Integer) table.getId().getValue(object)).get(0);
					} catch(IndexOutOfBoundsException ignored) { }
					
					if(data != null)
						subTable.getParentField().set(object, data);
					break;

				case ONE_TO_MANY:
					subTable.getParentField().setAccessible(true);

					List<?> children = findBy(subTable.getTableClass(), subTable.getForeignKey().getName() + " = ?", table.getId().getValue(object));

					if(!(children.equals(null)))
						subTable.getParentField().set(object, children);
					break;
				}
			}
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveRelationshipObjects(Object object) {
		try {
			TableRegistration table = getTableRegistration(object.getClass());

			for(SubTableRegistration subTable : table.getSubTables()) {
				// Prepare the child for saving.
				Object child = null;

				// Prepare the field.
				subTable.getParentField().setAccessible(true);

				// Determine what type of relationship to save.
				switch (subTable.getRelationshipType()) {
				case ONE_TO_ONE:
					child = subTable.getParentField().get(object);

					// Update the child's foreign key
					subTable.getForeignKey().setValue(child, table.getId().getValue(object));
					
					save(child);
					break;

				case ONE_TO_MANY:
					List<?> children = (List<?>) subTable.getParentField().get(object);

					Iterator<?> iterator = children.iterator();
					
					while(iterator.hasNext()) {
						child = iterator.next();

						// Update the child's foreign key
						subTable.getForeignKey().setValue(child, table.getId().getValue(object));
						
						save(child);
					}
					break;
				}
			}
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void dropRelationshipObjects(Object object) {
		try {
			TableRegistration table = getTableRegistration(object.getClass());

			for(SubTableRegistration subTable : table.getSubTables()) {
				// Prepare the child for saving.
				Object child = null;

				// Prepare the field.
				subTable.getParentField().setAccessible(true);

				// Determine what type of relationship to save.
				switch (subTable.getRelationshipType()) {
				case ONE_TO_ONE:
					child = subTable.getParentField().get(object);

					// Update the child's foreign key
					subTable.getForeignKey().setValue(child, table.getId().getValue(object));
					
					drop(child);				
					break;

				case ONE_TO_MANY:
					List<?> children = (List<?>) subTable.getParentField().get(object);

					Iterator<?> iterator = children.iterator();

					while(iterator.hasNext()) {
						child = iterator.next();

						// Update the child's foreign key
						subTable.getForeignKey().setValue(child, table.getId().getValue(object));
						
						drop(child);
					}
					break;
				}
			}
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void drop(Object object) {
		try {
			TableRegistration table = getTableRegistration(object.getClass());

			String query = String.format("DELETE FROM %s WHERE %s = ?", table.getName(), table.getId().getName());

			PreparedStatement statement = connection.prepareStatement(query);

			statement.setObject(1, table.getId().getValue(object));

			statement.execute();
			statement.close();
			
			dropRelationshipObjects(object);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> List<T> findAll(Class<T> objectClass) {
		try {
			TableRegistration table = getTableRegistration(objectClass);

			LinkedList<T> entries = new LinkedList<T>();
			String query = String.format("SELECT * FROM %s", table.getName());

			PreparedStatement statement = connection.prepareStatement(query);

			ResultSet result = statement.executeQuery();

			while(result.next()) {
				T object = objectClass.newInstance();

				for(ColumnRegistration column : table.getColumns()) {
					column.setValue(object, result.getObject(column.getName()));
				}

				loadRelationshipObjects(object);

				entries.add(object);
			}

			result.close();
			statement.close();

			return entries;
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return new LinkedList<T>();
	}

	@Override
	public void saveAll(List<?> objects) {
		Iterator<?> iterator = objects.iterator();

		while(iterator.hasNext()) {
			save(iterator.next());
		}
	}

	@Override
	public void dropAll(List<?> objects) {
		Iterator<?> iterator = objects.iterator();

		while(iterator.hasNext()) {
			drop(iterator.next());
		}
	}

}
