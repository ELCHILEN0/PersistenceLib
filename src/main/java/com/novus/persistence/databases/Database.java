package com.novus.persistence.databases;

import static com.novus.persistence.queries.expression.Expressions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;

import com.novus.persistence.internal.ColumnRegistration;
import com.novus.persistence.internal.RegistrationFactory;
import com.novus.persistence.internal.TableRegistration;
import com.novus.persistence.queries.queries.DeleteQuery;
import com.novus.persistence.queries.queries.InsertQuery;
import com.novus.persistence.queries.queries.SelectQuery;
import com.novus.persistence.queries.queries.UpdateQuery;

/**
 * Provides a layer for interacting with a database without writing code
 * specific to each type of database.
 * <p>
 * Supports select, insert, update and delete queries on tables in the database
 * through a fluid Java syntax. Each Database contains a DataSource and a
 * Provider. The DataSource provides connections to a database.The purpose of
 * the Provider is to provide built queries specific to the particular type of
 * database.
 * <p>
 * This class is designed to be extended for different databases so that the
 * database specific code can be implemented behind the Java layer. Extending
 * the class should be done in the following steps:
 * <ol>
 * <li>Redefine {@link #Database(DataSource, Provider)} to only require the
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
 * @see DataSource
 * @see Provider
 * @since 1.0.0
 */
public abstract class Database {
	protected DataSource	source;
	protected Provider		provider;

	private boolean			logging	= false;

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
	protected Database(DataSource source, Provider provider) {
		this.source = source;
		this.provider = provider;
	}

	/**
	 * Returns the {@link DataSource} for the {@link #Database}
	 * 
	 * @return the source
	 */
	public DataSource getDataSource() {
		return source;
	}

	/**
	 * Returns a connection from the {@link DataSource}.
	 * 
	 * @return a connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return source.getConnection();
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
	 * @throws SQLException
	 */
	public abstract void createStructure(Connection connection, Class<?> objectClass) throws SQLException;

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
	 * @throws SQLException
	 */
	public abstract void updateStructure(Connection connection, Class<?> objectClass) throws SQLException;

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
	public <T> T find(Connection connection, Class<T> objectClass, int id) {
		try {
			TableRegistration table = RegistrationFactory.getTableRegistration(objectClass);

			SelectQuery<T> query = select(objectClass).where(equal(table.getId().getName(), id));

			return query.findOne(connection);
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
	public <T> T find(Connection connection, Class<T> objectClass, long id) {
		try {
			TableRegistration table = RegistrationFactory.getTableRegistration(objectClass);

			SelectQuery<T> query = select(objectClass).where(equal(table.getId().getName(), table.getId().getValue(id)));

			return query.findOne(connection);
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
	public <T> boolean save(Connection connection, T object) {
		try {
			TableRegistration table = RegistrationFactory.getTableRegistration(object.getClass());

			String[] columns = ArrayUtils.EMPTY_STRING_ARRAY;
			Object[] values = ArrayUtils.EMPTY_OBJECT_ARRAY;

			for (int i = 0; i < table.getColumns().size(); i++) {
				ColumnRegistration column = table.getColumns().get(i);

				if (!(table.getId().getName().equals(column.getName()))) {
					columns = (String[]) ArrayUtils.add(columns, column.getName());
					values = ArrayUtils.add(values, column.getValue(object));
				}
			}

			boolean success = false;
			ResultSet generatedKeys = null;

			if (table.getId().getValue(object) == null) {
				@SuppressWarnings("unchecked")
				InsertQuery<T> query = insert((Class<T>) object.getClass()).columns(columns).values(values);

				success = query.execute(connection);
				generatedKeys = query.getGeneratedKeys();
			} else {
				@SuppressWarnings("unchecked")
				UpdateQuery<T> query = update((Class<T>) object.getClass()).columns(columns).values(values).where(equal(table.getId().getName(), table.getId().getValue(object)));

				success = query.execute(connection);
				generatedKeys = query.getGeneratedKeys();
			}

			if (generatedKeys != null && generatedKeys.next()) {
				if (table.getId().getType() == Integer.class || table.getId().getType() == int.class) {
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
	public <T> boolean drop(Connection connection, T object) {
		try {
			TableRegistration table = RegistrationFactory.getTableRegistration(object.getClass());

			@SuppressWarnings("unchecked")
			DeleteQuery<T> query = delete((Class<T>) object.getClass()).where(equal(table.getId().getName(), table.getId().getValue(object)));

			return query.execute(connection);
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
	public <T> List<T> findAll(Connection connection, Class<T> objectClass) {
		try {
			SelectQuery<T> query = select(objectClass);

			return query.findList(connection);
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
	public void saveAll(Connection connection, Iterable<?> objects) {
		Iterator<?> iterator = objects.iterator();

		while (iterator.hasNext()) {
			save(connection, iterator.next());
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
	public void dropAll(Connection connection, Iterable<?> objects) {
		Iterator<?> iterator = objects.iterator();

		while (iterator.hasNext()) {
			drop(connection, iterator.next());
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
	 * @see DeleteQuery
	 */
	public <T> DeleteQuery<T> delete(Class<T> objectClass) {
		return new DeleteQuery<T>(this, objectClass);
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
}
