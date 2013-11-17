package com.TeamNovus.Persistence.Databases;

import java.util.ArrayList;

import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;
import com.TeamNovus.Persistence.Internal.TableRegistration;
import com.TeamNovus.Persistence.Internal.TableRegistrationFactory;

public class TableRegistry {
	private static TableRegistry instance = null;
	
	protected TableRegistry() { }
	
	public static TableRegistry getInstance() {
		if(instance == null)
			instance = new TableRegistry();
		
		return instance;
	}
	
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

}
