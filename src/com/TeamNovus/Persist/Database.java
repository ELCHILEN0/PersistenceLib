package com.TeamNovus.Persist;

import java.util.ArrayList;
import java.util.List;

import com.TeamNovus.Persist.Exceptions.TableRegistrationException;
import com.TeamNovus.Persist.Internal.TableRegistration;
import com.TeamNovus.Persist.Internal.TableRegistrationFactory;

public abstract class Database {
	// Key, SubObject
	private boolean connected = false;
	private boolean checkTableOnRegistration = true;
	private ArrayList<TableRegistration> tables = new ArrayList<TableRegistration>();
	
	public TableRegistration registerTable(Class<?> tableClass) throws TableRegistrationException {
		TableRegistration table = TableRegistrationFactory.getTableRegistration(tableClass);
		
		if(tables != null && !(tables.contains(table))) {
			tables.add(table);
		}
		
		return table;
	}
	
	public TableRegistration getTableRegistration(Class<?> tableClass) throws TableRegistrationException {
		for(TableRegistration table : tables) {
			if(table.getTableClass().equals(tableClass)) {
				return table;
			}
		}
		
		return registerTable(tableClass);
	}

	public boolean isRegistered(Class<?> tableClass) {
		try {
			return getTableRegistration(tableClass) != null;
		} catch (TableRegistrationException ignored) { }
		
		return false;
	}
	
	public void setCheckTableOnRegistration(Boolean checkTableOnRegistration) {
		this.checkTableOnRegistration = checkTableOnRegistration;
	}

	public Boolean getCheckTableOnRegistration() {
		return checkTableOnRegistration;
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
	
	/*
	 * Abstract Methods to Implement Per Database Schema
	 * Represents the majority of CRUD database methods.
	 * 
	 */
	
	// Basic Object Manipulation
	public abstract <T> T find(Class<T> objectClass, Integer id);
	public abstract <T> List<T> findBy(Class<T> objectClass, String condition, Object... params);
	public abstract void save(Object object);
	public abstract void drop(Object object);
	
	// Advanced Object Manipulation
	public abstract <T> List<T> findAll(Class<T> objectClass);
	public abstract void saveAll(List<?> objects);
	public abstract void dropAll(List<?> objects);
	
	// Relationship Object Manipulation
	public abstract void loadRelationshipObjects(Object object);
	public abstract void saveRelationshipObjects(Object object);
	public abstract void dropRelationshipObjects(Object object);
	public abstract void dropRemovedObjects(Object object);

}
