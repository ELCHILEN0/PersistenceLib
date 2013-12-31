package com.TeamNovus.Persistence.Queries.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang.ArrayUtils;

import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;
import com.TeamNovus.Persistence.Queries.Query;

public class InsertQuery<T> extends Query<T> {
	private LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	private ResultSet generatedKeys;

	public InsertQuery(Database database, Class<T> objectClass) {
		super(database, objectClass);
	}
	
	public InsertQuery<T> columns(String... columns) {
		for (String column : columns) {
			map.put(column, null);
		}
		
		return this;
	}
	
	public InsertQuery<T> values(Object... values) {
		
		int i = 0;
		for (String column : map.keySet()) {
			if(values.length > i) {
				map.put(column, values[i]);
			} else {
				break;
			}
			i++;
		}
		
		return this;
	}
	
	public InsertQuery<T> map(LinkedHashMap<String, Object> map) {
		this.map = map;
		
		return this;
	}
	
	public boolean execute() {		
		try {
            PreparedStatement statement = database.getProvider().prepareQuery(this);

            statement.executeUpdate();
            generatedKeys = statement.getGeneratedKeys();

			return true;
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public HashMap<String, Object> getMap() {
		return map;
	}
	
	public String[] getColumns() {
		String[] columns = ArrayUtils.EMPTY_STRING_ARRAY;
		
		for (String column : map.keySet()) {
			columns = (String[]) ArrayUtils.add(columns, column);
		}
		
		return columns;
	}
	
	public Object[] getValues() {
		return map.values().toArray();
	}
	
	public ResultSet getGeneratedKeys() {
		return generatedKeys;
	}
}
