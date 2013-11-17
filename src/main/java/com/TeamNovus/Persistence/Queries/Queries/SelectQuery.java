package com.TeamNovus.Persistence.Queries.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;
import com.TeamNovus.Persistence.Internal.ColumnRegistration;
import com.TeamNovus.Persistence.Internal.TableRegistration;
import com.TeamNovus.Persistence.Internal.TableRegistrationFactory;
import com.TeamNovus.Persistence.Queries.Query;
import com.TeamNovus.Persistence.Queries.Clauses.GroupByClause;
import com.TeamNovus.Persistence.Queries.Clauses.HavingClause;
import com.TeamNovus.Persistence.Queries.Clauses.LimitClause;
import com.TeamNovus.Persistence.Queries.Clauses.OrderByClause;
import com.TeamNovus.Persistence.Queries.Clauses.WhereClause;
import com.TeamNovus.Persistence.Queries.Expression.Condition;

public class SelectQuery<T> extends Query<T> {
	private LinkedHashSet<String> columns = new LinkedHashSet<String>();
	private WhereClause where;
	private GroupByClause groupBy;
	private HavingClause having;
	private OrderByClause orderBy;
	private LimitClause limit;

	public SelectQuery(Database database, Class<T> objectClass) {
		super(database, objectClass);
	}

	public SelectQuery<T> limit(int amount) {
		limit = new LimitClause(0, amount);

		return this;
	}

	public SelectQuery<T> limit(int min, int max) {
		limit = new LimitClause(min, max);

		return this;
	}

	public SelectQuery<T> delimit() {
		limit = null;

		return this;
	}

	public SelectQuery<T> where(Condition condition) {
		where = new WhereClause(condition);

		return this;
	}

	public SelectQuery<T> having(Condition condition) {
		having = new HavingClause(condition);

		return this;
	}

	public SelectQuery<T> filter(String... columns) {
		this.columns.addAll(Arrays.asList(columns));

		return this;
	}

	public T findOne() {
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			PreparedStatement statement = database.getProvider().prepareQuery(this);
			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			if(result.first()) {
				T object = objectClass.newInstance(); 

				for(ColumnRegistration column : table.getColumns()) {
					if(ArrayUtils.contains(validColumns, column.getName())) {
						column.setValue(object, result.getObject(column.getName()));
					}
				}

				result.close();
				statement.close();
				
				return object;
			}
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<T> findList() {
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			PreparedStatement statement = database.getProvider().prepareQuery(this);
			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			LinkedList<T> objects = new LinkedList<T>();

			while(result.next()) {
				T object = objectClass.newInstance(); 

				for(ColumnRegistration column : table.getColumns()) {
					if(ArrayUtils.contains(validColumns, column.getName())) {
						column.setValue(object, result.getObject(column.getName()));
					}
				}

				objects.add(object);
			}

			result.close();
			statement.close();
			
			return objects;
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return new ArrayList<T>();
	}

	public Set<T> findSet() {
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			PreparedStatement statement = database.getProvider().prepareQuery(this);
			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			LinkedHashSet<T> objects = new LinkedHashSet<T>();

			while(result.next()) {
				T object = objectClass.newInstance(); 

				for(ColumnRegistration column : table.getColumns()) {
					if(ArrayUtils.contains(validColumns, column.getName())) {
						column.setValue(object, result.getObject(column.getName()));
					}
				}

				objects.add(object);
			}

			result.close();
			statement.close();
			
			return objects;
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return new HashSet<T>();
	}

	public HashMap<String, Object> findMap() {
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			PreparedStatement statement = database.getProvider().prepareQuery(this);
			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			if(result.first()) {
				HashMap<String, Object> map = new HashMap<String, Object>();

				for(ColumnRegistration column : table.getColumns()) {
					if(ArrayUtils.contains(validColumns, column.getName())) {
						map.put(column.getName(), result.getObject(column.getName()));
					}
				}

				result.close();
				statement.close();
				
				return map;
			}
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new HashMap<String, Object>();
	}

	public List<HashMap<String, Object>> findMapList() {
		try {
			TableRegistration table = TableRegistrationFactory.getTableRegistration(objectClass);

			PreparedStatement statement = database.getProvider().prepareQuery(this);
			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
			while(result.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();

				for(ColumnRegistration column : table.getColumns()) {
					if(ArrayUtils.contains(validColumns, column.getName())) {
						map.put(column.getName(), result.getObject(column.getName()));
					}
				}
				
				list.add(map);
			}
			

			result.close();
			statement.close();
			
			return list;
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ArrayList<HashMap<String,Object>>();
	}
	
	
	// Getters
	public LinkedHashSet<String> getColumns() {
		return columns;
	}

	public WhereClause getWhere() {
		return where;
	}

	public GroupByClause getGroupBy() {
		return groupBy;
	}

	public HavingClause getHaving() {
		return having;
	}

	public OrderByClause getOrderBy() {
		return orderBy;
	}

	public LimitClause getLimit() {
		return limit;
	}
}
