package com.novus.persistence.databases;

import static com.novus.persistence.queries.expression.Expressions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.novus.persistence.internal.ColumnRegistration;
import com.novus.persistence.internal.TableRegistration;
import com.novus.persistence.internal.TableRegistrationFactory;
import com.novus.persistence.queries.queries.DeleteQuery;
import com.novus.persistence.queries.queries.InsertQuery;
import com.novus.persistence.queries.queries.SelectQuery;
import com.novus.persistence.queries.queries.UpdateQuery;

/**
 * Provides a layer for interacting with a database without writing code
 * specific to each type of database.
 * <p>
 * Supports select, insert, update and delete queries on tables in the database
 * through a fluid Java syntax. Each Database has a single a Configuration and
 * single a Provider. The purpose of the Configuration is to handle database
 * specific details which should be passed to establish connection and
 * additional variables specific to the particular type of database. The purpose
 * of the Provider is to provide built queries specific to the particular type
 * of database.
 * <p>
 * This class is designed to be extended for different databases so that the
 * database specific code can be implemented behind the Java layer. Extending
 * the class should be done in the following steps:
 * <ol>
 * <li>Redefine {@link #Database(Configuration, Provider)} to only require the
 * Configuration object.</li>
 * <li>Redefine {@link #connect()} to correctly establish a connection to a
 * database.
 * <li>
 * <li>Redefine the {@link #disconnect()} to correctly relinquish the connection
 * from the database.</li>
 * <li>Redefine the {@link #createTableStructure(Class)} to correctly creates in
 * the database.</li>
 * <li>Redefine the {@link #updateTableStructure(Class)} to correctly update
 * table structure in the database.</li>
 * </ol>
 * 
 * @author Jnani Weibel
 * @see Configuration
 * @see Provider
 * @since 1.0.0
 */
public abstract class Database {
	protected Configuration configuration;
	protected Provider provider;
	// TODO: Change to a connection pool
	protected Connection connection;

	private boolean logging = false;

	/**
	 * Constructs a new {@link Database} object with the specified
	 * {@link Configuration} and {@link Provider}.
	 * 
	 * @param configuration
	 *            the configuration provides connection details to the Database
	 * @param provider
	 *            the provider dictates which queries to execute in different
	 *            situations for the Database
	 */
	public Database(Configuration configuration, Provider provider) {
		this.configuration = configuration;
		this.provider = provider;
	}

	/**
	 * Returns the {@link Configuration} for the {@link Database}.
	 * 
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Returns the {@link Provider} for the {@link Database}.
	 * 
	 * @return the provider
	 */
	public Provider getProvider() {
		return provider;
	}

	/**
	 * Returns the Java {@link Connection} to the database.
	 * 
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Enables logging for the {@link Database}.
	 */
	public void enableLogging() {
		logging = true;
	}

	/**
	 * Disables logging for the {@link Database}.
	 */
	public void disableLogging() {
		logging = false;
	}

	/**
	 * Returns <code>true</code> if logging is enabled.
	 * 
	 * @return <code>true</code> if logging is enabled; <code>false</code>
	 *         otherwise.
	 */
	public boolean isLogging() {
		return logging;
	}

	/**
	 * Attempts to establish a connection to a database using the appropriate
	 * method for the database type.
	 * <p>
	 * This method should be called before any further {@link Database} methods
	 * are called. Calling this method does <b>not</b> guarantee that the
	 * connection is established successfully.
	 */
	public abstract void connect();

	/**
	 * Attempts to disconnect from a database using the appropriate method for
	 * the database type.
	 * <p>
	 * This method should be called when accessing the database is no longer
	 * needed. The connection will be stopped and the instance removed.
	 */
	public abstract void disconnect();

	/**
	 * Returns <code>true</code> if the connection to the database is valid.
	 * 
	 * @return <code>true</code> if the connection is valid; <code>false</code>
	 *         otherwise.
	 */
	public boolean isConnected() {
		try {
			return connection.isValid(0);
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Creates a table in the database based on the table structure defined in a
	 * specific class.
	 * <p>
	 * The class should be properly annotated in order to be able to create the
	 * structure correctly. This method should only be called once if the table
	 * does not already exist in the database; otherwise, use
	 * {@link #updateStructure(Class)}.
	 * 
	 * @param objectClass
	 *            the class the table definition is based on
	 */
	public abstract void createStructure(Class<?> objectClass);

	/**
	 * Updates an existing table in the database based on the table structure
	 * defined in a specific class.
	 * <p>
	 * The class should be properly annotated in order to be able to update the
	 * structure correctly. This method should only be called if the table not
	 * already exists in the database; otherwise, use
	 * {@link #createStructure(Class)}.
	 * <p>
	 * Changes the definitions of already defined columns. Adds columns which
	 * are not already defined in the table.
	 * 
	 * @param objectClass
	 *            the class the table definition is based on
	 */
	public abstract void updateStructure(Class<?> objectClass);

	/**
	 * Returns a single object from the database with the given id or
	 * <code>null</code> if the object doesn't exist.
	 * 
	 * @param objectClass
	 *            the class of the object that will be returned
	 * @param id
	 *            the id of the object
	 * @return the object with the given id or <code>null</code>
	 */
	public <T> T find(Class<T> objectClass, int id) {
		try {
			TableRegistration table = TableRegistrationFactory
					.getTableRegistration(objectClass);

			SelectQuery<T> query = select(objectClass).where(
					equal(table.getId().getName(), id));

			return query.findOne();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns a single object from the database with the given id or
	 * <code>null</code> if the object doesn't exist.
	 * 
	 * @param objectClass
	 *            the class of the object that will be returned
	 * @param id
	 *            the id of the object
	 * @return the object with the given id or <code>null</code>
	 */
	public <T> T find(Class<T> objectClass, long id) {
		try {
			TableRegistration table = TableRegistrationFactory
					.getTableRegistration(objectClass);

			SelectQuery<T> query = select(objectClass).where(
					equal(table.getId().getName(), table.getId().getValue(id)));

			return query.findOne();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Saves the object to the appropriate table in the database.
	 * <p>
	 * If the object's id has not yet been set then the object will be inserted
	 * into the database; otherwise, the object's data will be updated.
	 * 
	 * @param object
	 *            the object to save
	 * @return <code>true</code> if the save was successful; <code>false</code>
	 *         otherwise.
	 */
	public <T> boolean save(T object) {
		try {
			TableRegistration table = TableRegistrationFactory
					.getTableRegistration(object.getClass());

			String[] columns = ArrayUtils.EMPTY_STRING_ARRAY;
			Object[] values = ArrayUtils.EMPTY_OBJECT_ARRAY;

			for (int i = 0; i < table.getColumns().size(); i++) {
				ColumnRegistration column = table.getColumns().get(i);

				if (!(table.getId().getName().equals(column.getName()))) {
					columns = (String[]) ArrayUtils.add(columns,
							column.getName());
					values = ArrayUtils.add(values, column.getValue(object));
				}
			}

			boolean success = false;
			ResultSet generatedKeys = null;

			if (table.getId().getValue(object) == null) {
				@SuppressWarnings("unchecked")
				InsertQuery<T> query = insert((Class<T>) object.getClass())
						.columns(columns).values(values);

				success = query.execute();
				generatedKeys = query.getGeneratedKeys();
			} else {
				@SuppressWarnings("unchecked")
				UpdateQuery<T> query = update((Class<T>) object.getClass())
						.columns(columns)
						.values(values)
						.where(equal(table.getId().getName(), table.getId()
								.getValue(object)));

				success = query.execute();
				generatedKeys = query.getGeneratedKeys();
			}

			if (generatedKeys != null && generatedKeys.next()) {
				if (table.getId().getType() == Integer.class
						|| table.getId().getType() == int.class) {
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

	/**
	 * Deletes the object from the appropriate table in the database.
	 * 
	 * @param object
	 *            the object to drop
	 * @return <code>true</code> if the drop was successful; <code>false</code>
	 *         otherwise.
	 */
	public <T> boolean drop(T object) {
		try {
			TableRegistration table = TableRegistrationFactory
					.getTableRegistration(object.getClass());

			@SuppressWarnings("unchecked")
			DeleteQuery<T> query = delete((Class<T>) object.getClass()).where(
					equal(table.getId().getName(),
							table.getId().getValue(object)));

			return query.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Returns all the objects in the appropriate table in the database.
	 * 
	 * @param objectClass
	 *            the class of the object that will be returned
	 * @return all the objects in the table
	 */
	public <T> List<T> findAll(Class<T> objectClass) {
		try {
			SelectQuery<T> query = select(objectClass);

			return query.findList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<T>();
	}

	/**
	 * Saves multiple objects to their appropriate tables in the database.
	 * <p>
	 * Iterates over a group of objects calling the {@link #save(Object)} method
	 * for each individual object.
	 * 
	 * @param objects
	 *            the objects to save
	 */
	public void saveAll(Iterable<?> objects) {
		Iterator<?> iterator = objects.iterator();

		while (iterator.hasNext()) {
			save(iterator.next());
		}
	}

	/**
	 * Drops multiple objects from their appropriate tables in the database.
	 * <p>
	 * Iterates over a group of objects calling the {@link #drop(Object)} method
	 * for each individual object.
	 * 
	 * @param objects
	 *            the objects to drop
	 */
	public void dropAll(Iterable<?> objects) {
		Iterator<?> iterator = objects.iterator();

		while (iterator.hasNext()) {
			drop(iterator.next());
		}
	}

	/**
	 * Creates a {@link SelectQuery} that can be used to build a select
	 * statement to query the appropriate table in the database. The SelectQuery
	 * will fetch data from the table specified in the object classes table
	 * definition.
	 * <p>
	 * Calling this method does not immediately query the database. Execution of
	 * a SelectQuery can be done in any of the following ways:
	 * <ol>
	 * <li>{@link SelectQuery#findOne()}</li>
	 * <li>{@link SelectQuery#findList()}</li>
	 * <li>{@link SelectQuery#findSet()}</li>
	 * <li>{@link SelectQuery#findMap()}</li>
	 * </ol>
	 * 
	 * @param objectClass
	 *            the class of the object for the SelectQuery
	 * @return a SelectQuery built around the object class
	 * 
	 * @see SelectQuery
	 */
	public <T> SelectQuery<T> select(Class<T> objectClass) {
		return new SelectQuery<T>(this, objectClass);
	}

	/**
	 * Creates an {@link InsertQuery} that can be used to build an insert
	 * statement for the appropriate table in the database. The InsertQuery will
	 * insert data into the table specified in the object classes table
	 * definition.
	 * <p>
	 * Calling this method does not immediately execute the statement. Execution
	 * of an InsertQuery can be done by calling {@link InsertQuery#execute()}.
	 * 
	 * @param objectClass
	 *            the class of the object for the InsertQuery
	 * @return a InsertQuery built around the object class
	 * 
	 * @see InsertQuery
	 */
	public <T> InsertQuery<T> insert(Class<T> objectClass) {
		return new InsertQuery<T>(this, objectClass);
	}

	/**
	 * Creates an {@link UpdateQuery} that can be used to build an update
	 * statement for the appropriate table in the database. The UpdateQuery will
	 * update data in the table specified in the object classes table
	 * definition.
	 * <p>
	 * Calling this method does not immediately execute the statement. Execution
	 * of an UpdateQuery can be done by calling {@link UpdateQuery#execute()}.
	 * 
	 * @param objectClass
	 *            the class of the object for the UpdateQuery
	 * @return a UpdateQuery built around the object class
	 * 
	 * @see UpdateQuery
	 */
	public <T> UpdateQuery<T> update(Class<T> objectClass) {
		return new UpdateQuery<T>(this, objectClass);
	}

	/**
	 * Creates a {@link DeleteQuery} that can be used to build a delete
	 * statement for the appropriate table in the database. The DeleteQuery will
	 * delete records from the table specified in the object classes table
	 * definition.
	 * <p>
	 * Calling this method does not immediately execute the statement. Execution
	 * of an DeleteQuery can be done by calling {@link DeleteQuery#execute()}.
	 * 
	 * @param objectClass
	 *            the class of the object for the DeleteQuery
	 * @return a DeleteQuery built around the object class
	 * 
	 * @see DeleteQuery
	 */
	public <T> DeleteQuery<T> delete(Class<T> objectClass) {
		return new DeleteQuery<T>(this, objectClass);
	}

	/**
	 * Begins a transaction for queries on the database connection.
	 */
	public void begin() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @deprecated Replaced by {@link #begin()}
	 * @since 2.0.6
	 */
	@Deprecated
	public void beginTransaction() {
		begin();
	}

	/**
	 * Ends the current transaction and commits the changes to the database.
	 */
	public void commit() {
		try {
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @deprecated Replaced by {@link #commit()}
	 * @since 2.0.6
	 */
	@Deprecated
	public void endTransaction() {
		commit();
	}

	/**
	 * Undoes all the changes made in the current transaction.
	 */
	public void rollback() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Undoes all the changes made past the savepoint in the current
	 * transaction.
	 * 
	 * @param savepoint
	 *            the savepoint
	 */
	public void rollback(Savepoint savepoint) {
		try {
			connection.rollback(savepoint);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a new Savepoint at the current position in the current
	 * transaction.
	 * 
	 * @return the current savepoint
	 */
	public Savepoint setSavepoint() {
		try {
			return connection.setSavepoint();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
