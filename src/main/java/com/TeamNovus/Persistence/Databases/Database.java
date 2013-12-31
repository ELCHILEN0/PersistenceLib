package com.TeamNovus.Persistence.Databases;

import java.sql.Connection;
import java.util.List;

import com.TeamNovus.Persistence.Queries.Queries.DeleteQuery;
import com.TeamNovus.Persistence.Queries.Queries.InsertQuery;
import com.TeamNovus.Persistence.Queries.Queries.SelectQuery;
import com.TeamNovus.Persistence.Queries.Queries.UpdateQuery;

public abstract class Database {
	protected Configuration configuration;
	protected Provider provider;
	protected Connection connection;
	
	private boolean connected = false;
	private boolean logging = false;
	
	public Database(Configuration configuration, Provider provider) {
		this.configuration = configuration;
		this.provider = provider;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public Provider getProvider() {
		return provider;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void connect() {
		connected = true;
	}
	
	public void disconnect() {
		connected = false;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public boolean isDisconnected() {
		return !(connected);
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
	
	// Database Creation/Modification:
	public abstract void createStructure(Class<?> objectClass);
	public abstract void updateStructure(Class<?> objectClass);
	
	// Basic Object Manipulation
	public abstract <T> T find(Class<T> objectClass, int  id);
	public abstract <T> T find(Class<T> objectClass, long id);
	public abstract <T> boolean save(T object);
	public abstract <T> boolean drop(T object);
	
	// Advanced Object Manipulation
	public abstract <T> List<T> findAll(Class<T> objectClass);
	public abstract void saveAll(Iterable<?> objects);
	public abstract void dropAll(Iterable<?> objects);

	// Queries
	public <T> SelectQuery<T> select(Class<T> objectClass) {
		return new SelectQuery<T>(this, objectClass);
	}
	
	public <T> InsertQuery<T> insert(Class<T> objectClass) {
		return new InsertQuery<T>(this, objectClass);
	}
	
	public <T> UpdateQuery<T> update(Class<T> objectClass) {
		return new UpdateQuery<T>(this, objectClass);
	}
	
	public <T> DeleteQuery<T> delete(Class<T> objectClass) {
		return new DeleteQuery<T>(this, objectClass);
	}
	
	// Transactions
	public abstract void beginTransaction();
	public abstract void endTransaction();
}
