package com.novus.persistence.queries.queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang.ArrayUtils;

import com.novus.persistence.databases.Database;
import com.novus.persistence.exceptions.TableRegistrationException;
import com.novus.persistence.queries.Query;
import com.novus.persistence.queries.clauses.WhereClause;
import com.novus.persistence.queries.expression.Condition;

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

            statement.execute();
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
	
	public WhereClause getWhere() {
		return where;
	}
	
	public ResultSet getGeneratedKeys() {
		return generatedKeys;
	}
}
