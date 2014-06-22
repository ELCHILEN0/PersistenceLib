package com.novus.persistence.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import com.novus.persistence.queries.queries.DeleteQuery;
import com.novus.persistence.queries.queries.InsertQuery;
import com.novus.persistence.queries.queries.SelectQuery;
import com.novus.persistence.queries.queries.UpdateQuery;

public abstract class Database {
	protected Configuration configuration;
	protected Provider provider;
	protected Connection connection;
	
	private boolean logging = false;
	
	public Database(Configuration configuration, Provider provider) {
		this.configuration = configuration;
		this.provider = provider;
	}
	
	/**
	 * Returns the {@link Configuration} object that provides the needed details
	 * for this {@link Database} object.
	 * 
	 * @return	the configuration object
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
	
	/**
	 * Returns the {@link Provider} object for this {@link Database} object.
	 * <p>
	 * The Provider 
	 * 
	 * @return	the provider object
	 */
	public Provider getProvider() {
		return provider;
	}
	
	/**
	 * Returns the Java Connection to the database.
	 * 
	 * @return	the connection to the database
	 */
	public Connection getConnection() {
		return connection;
	}
	
	public void enableLogging() {
		logging = true;
	}
	
	public void disableLogging() {
		logging = false;
	}
	
	public boolean isLogging() {
		return logging;
	}
	
	/**
	 * Connects to particular database using the appropriate method.
	 * <p>
	 * This method must be called before any queries on the database can be run.
	 * <p>
	 * Establishes a connection with the database using the details provided in the
	 * Configuration object provided upon initialization.  Calling this method does
	 * <b>not</b> guarantee that the connection is established successfully.
	 */
	public abstract void connect();
	
	/**
	 * Disconnects from a database using the appropriate method.
	 * <p>
	 * Closes the connection to the database and nullifies the Connection object.
	 */
	public abstract void disconnect();
	
	/**
	 * Checks if the connection is connected to an active database.
	 * 
	 * @return	<code>true</code> if the connection is valid;
	 * 			<code>false</code> if the connection is invalid.
	 */
	public boolean isConnected() {
		try {
			return connection.isValid(0);
		} catch (SQLException e) {
			return false;
		}
	}
		
	/**
	 * Creates the table structure on the database for a specific class.
	 * 
	 * @param	objectClass	the class the structure is based on
	 */
	public abstract void createStructure(Class<?> objectClass);
	
	/**
	 * Updates the table structure on the database for a specific class.
	 * <p>
	 * Changes the definitions of already defined columns.  Adds columns
	 * which are not already defined in the table.
	 * 
	 * @param	objectClass	the class the structure is based on
	 */
	public abstract void updateStructure(Class<?> objectClass);
	
	/**
	 * Returns a single object from the database with the given id or <code>null</code>.
	 * <p>
	 * The object returned will be the same class as object class.
	 * 
	 * @param	objectClass	the class of the object
	 * @param	id	the id of specified object in the table
	 * @return	the object with the given id or <code>null</code>
	 */
	public abstract <T> T find(Class<T> objectClass, int  id);
	
	/**
	 * Returns a single object from the database with the given id or <code>null</code>.
	 * <p>
	 * The object returned will be the same class as object class.
	 * 
	 * @param	objectClass	the class of the object
	 * @param	id	the id of specified object in the table
	 * @return	the object with the given id or <code>null</code>
	 */
	public abstract <T> T find(Class<T> objectClass, long id);
	
	/**
	 * Saves the specified object to the database.
	 * <p>
	 * Will insert the object into the database if the id has not been set; otherwise,
	 * the object's data will be updated in the database.
	 * 
	 * @param	object	the object to save
	 * @return	<code>true</code> if the save was successful;
	 * 			<code>false</code> if the save failed.
	 */
	public abstract <T> boolean save(T object);
	
	/**
	 * Deletes the specified object from the database.
	 * @param	object	the object to drop
	 * @return	<code>true</code> if the drop was successful;
	 * 			<code>false</code> if the drop failed.
	 */
	public abstract <T> boolean drop(T object);
	
	/**
	 * Returns a List of all the objects in the specified table.
	 * 
	 * @param	objectClass	the class of the objects
	 * @return	a List of all the objects in the table
	 */
	public abstract <T> List<T> findAll(Class<T> objectClass);
	
	/**
	 * Saves multiple objects to the database.
	 * <p>
	 * Iterates over a group of objects calling the {@link #save(Object)} method
	 * for each object.
	 * 
	 * @param	objects	the objects to save
	 */
	public abstract void saveAll(Iterable<?> objects);
	
	/**
	 * Drops multiple objects from the database.
	 * <p>
	 * Iterates over a group of objects calling the {@link #drop(Object)} method
	 * for each object.
	 * 
	 * @param	objects	the objects to drop
	 */
	public abstract void dropAll(Iterable<?> objects);

	/**
	 * Create a {@link SelectQuery} for the specified object.
	 * <p>
	 * Creates a new SelectQuery that can be executed on the object class.
	 * Execution of a SelectQuery can be done in a number of ways:
	 * <ul>
	 * <li>{@link SelectQuery#findOne()}</li>
	 * <li>{@link SelectQuery#findList()}</li>
	 * <li>{@link SelectQuery#findSet()}</li>
	 * <li>{@link SelectQuery#findMap()}</li>
	 * </ul>
	 * 
	 * @param	objectClass	the object class
	 * @return	a SelectQuery on the object class
	 */
	public <T> SelectQuery<T> select(Class<T> objectClass) {
		return new SelectQuery<T>(this, objectClass);
	}
	
	/**
	 * Create an {@link InsertQuery} for the specified object.
	 * <p>
	 * Creates a new InsertQuery that can be executed on the object class.
	 * Execution of an InsertQuery can be done by calling the {@link InsertQuery#execute()}.
	 * 
	 * @param	objectClass	the object class
	 * @return	an InsertQuery on the object class
	 */
	public <T> InsertQuery<T> insert(Class<T> objectClass) {
		return new InsertQuery<T>(this, objectClass);
	}
	
	/**
	 * Create an {@link UpdateQuery} for the specified object.
	 * <p>
	 * Creates a new UpdateQuery that can be executed on the object class.
	 * Execution of an UpdateQuery can be done by calling the {@link UpdateQuery#execute()}.
	 * 
	 * @param	objectClass	the object class
	 * @return	an UpdateQuery on the object class
	 */
	public <T> UpdateQuery<T> update(Class<T> objectClass) {
		return new UpdateQuery<T>(this, objectClass);
	}
	
	/**
	 * Create a {@link DeleteQuery} for the specified object.
	 * <p>
	 * Creates a new DeleteQuery that can be executed on the object class.
	 * Execution of a DeleteQuery can be done by calling the {@link DeleteQuery#execute()}.
	 * 
	 * @param	objectClass	the object class
	 * @return	an DeleteQuery on the object class
	 */
	public <T> DeleteQuery<T> delete(Class<T> objectClass) {
		return new DeleteQuery<T>(this, objectClass);
	}
	
	/**
	 * Begin a transaction for the database.
	 */
	public abstract void beginTransaction();
	
	/**
	 * End and commit the current transaction for the database.
	 */
	public abstract void commitTransaction();
	
	/**
	 * @deprecated	Replaced by {@link #commitTransaction()}
	 */
	@Deprecated
	public void endTransaction() {
		commitTransaction();
	}
	/**
	 * Undoes all the changes made in the current transaction.
	 */
	public abstract void rollbackTransaction();
	
	/**
	 * Undoes all the changes made past the specified Savepoint in the current transaction.
	 * 
	 * @param	savepoint the Savepoint
	 */
	public abstract void rollbackTransaction(Savepoint savepoint);
	
	/**
	 * Returns a new Savepoint from the current transaction.
	 * 
	 * @return	the current Savepoint
	 */
	public abstract Savepoint setSavepoint();
}
