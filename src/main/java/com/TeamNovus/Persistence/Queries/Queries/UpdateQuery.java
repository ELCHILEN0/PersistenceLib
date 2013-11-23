package com.TeamNovus.Persistence.Queries.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.ArrayUtils;

import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;
import com.TeamNovus.Persistence.Queries.Query;
import com.TeamNovus.Persistence.Queries.Clauses.WhereClause;
import com.TeamNovus.Persistence.Queries.Expression.Condition;

public class UpdateQuery<T> extends Query<T> {
	private LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	protected WhereClause where;
	
	private ResultSet generatedKeys;

	public UpdateQuery(Database database, Class<T> objectClass) {
		super(database, objectClass);
	}

	public UpdateQuery<T> where(Condition condition) {
		where = new WhereClause(condition);
		
		return this;
	}
	
	public UpdateQuery<T> columns(String... columns) {
		for (String column : columns) {
			map.put(column, null);
		}
		
		return this;
	}
	
	public UpdateQuery<T> values(Object... values) {
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
	
	public UpdateQuery<T> map(LinkedHashMap<String, Object> map) {
		this.map = map;
		
		return this;
	}
	
	public boolean execute() {		
		try {
			PreparedStatement statement = database.getProvider().prepareQuery(this);
			
			statement.executeUpdate();
			statement.close();
			
			return true;
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public ResultSet executeQuery() {
		try {
			PreparedStatement statement = database.getProvider().prepareQuery(this);
			
			ResultSet result = statement.executeQuery();
			
			generatedKeys = statement.getGeneratedKeys();
			
			statement.close();
			
			return result;
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public HashMap<String, Object> getMap() {
		return map;
	}
	
	public String[] getColumns() {
		String[] columns = ArrayUtils.EMPTY_STRING_ARRAY;
		
		for (String column : map.keySet()) {
			columns = ArrayUtils.add(columns, column);
		}
		
		return columns;
	}
	
	public Object[] getValues() {
		return map.values().toArray();
	}
	
	public WhereClause getWhere() {
		return where;
	}
	
	public ResultSet getGeneratedKeys() {
		return generatedKeys;
	}
}
