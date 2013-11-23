package com.TeamNovus.Persistence.Queries;

import com.TeamNovus.Persistence.Databases.Database;

public class Query<T> {
	protected Database database;
	protected Class<T> objectClass;
	
	public Query(Database database, Class<T> objectClass) {
		this.database = database;
		this.objectClass = objectClass;
	}
	
	/**
	 * Allows dynamic runtime changing of the database.  This enables a query to be reused with
	 * multiple database instances.
	 * 
	 * @param database
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public Class<T> getObjectClass() {
		return objectClass;
	}	
}
