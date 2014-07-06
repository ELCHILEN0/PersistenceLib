package com.novus.persistence.queries.queries;

import java.sql.Connection;
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

import org.apache.commons.lang.ArrayUtils;

import com.novus.persistence.databases.Database;
import com.novus.persistence.enums.Order;
import com.novus.persistence.exceptions.ColumnRegistrationException;
import com.novus.persistence.exceptions.TableRegistrationException;
import com.novus.persistence.internal.ColumnRegistration;
import com.novus.persistence.internal.RegistrationFactory;
import com.novus.persistence.internal.TableRegistration;
import com.novus.persistence.queries.Query;
import com.novus.persistence.queries.clauses.GroupByClause;
import com.novus.persistence.queries.clauses.HavingClause;
import com.novus.persistence.queries.clauses.LimitClause;
import com.novus.persistence.queries.clauses.OrderByClause;
import com.novus.persistence.queries.clauses.WhereClause;
import com.novus.persistence.queries.expression.Predicate;

public class SelectQuery<T> extends Query<T> {
	private boolean distinct = false;
	private final LinkedHashSet<String> columns = new LinkedHashSet<String>();
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

	public SelectQuery<T> groupBy(String... columns) {
		groupBy = new GroupByClause(columns);

		return this;
	}

	public SelectQuery<T> where(Predicate predicate) {
		where = new WhereClause(predicate);

		return this;
	}

	public SelectQuery<T> having(Predicate predicate) {
		having = new HavingClause(predicate);

		return this;
	}

	public SelectQuery<T> orderBy(Order order, String... columns) {
		orderBy = new OrderByClause(order, columns);

		return this;
	}

	public SelectQuery<T> filter(String... columns) {
		this.columns.addAll(Arrays.asList(columns));

		return this;
	}

	public T findOne(Connection connection) throws SQLException {
		try (PreparedStatement statement = database.getProvider().prepareQuery(connection, this)) {
			TableRegistration table = RegistrationFactory.getTableRegistration(objectClass);

			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = (String[]) ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			if (result.first()) {
				T object = objectClass.newInstance();

				for (ColumnRegistration column : table.getColumns()) {
					if (ArrayUtils.contains(validColumns, column.getName())) {
						column.setValue(object, result.getObject(column.getName()));
					}
				}

				return object;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (TableRegistrationException | ColumnRegistrationException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<T> findList(Connection connection) throws SQLException {
		try (PreparedStatement statement = database.getProvider().prepareQuery(connection, this)) {
			TableRegistration table = RegistrationFactory.getTableRegistration(objectClass);

			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = (String[]) ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			LinkedList<T> objects = new LinkedList<>();

			while (result.next()) {
				T object = objectClass.newInstance();

				for (ColumnRegistration column : table.getColumns()) {
					if (ArrayUtils.contains(validColumns, column.getName())) {
						column.setValue(object, result.getObject(column.getName()));
					}
				}

				objects.add(object);
			}

			return objects;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (TableRegistrationException | ColumnRegistrationException e) {
			e.printStackTrace();
		}

		return new ArrayList<T>();
	}

	public Set<T> findSet(Connection connection) throws SQLException {
		try (PreparedStatement statement = database.getProvider().prepareQuery(connection, this)) {
			TableRegistration table = RegistrationFactory.getTableRegistration(objectClass);

			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = (String[]) ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			LinkedHashSet<T> objects = new LinkedHashSet<>();

			while (result.next()) {
				T object = objectClass.newInstance();

				for (ColumnRegistration column : table.getColumns()) {
					if (ArrayUtils.contains(validColumns, column.getName())) {
						column.setValue(object, result.getObject(column.getName()));
					}
				}

				objects.add(object);
			}

			return objects;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (TableRegistrationException | ColumnRegistrationException e) {
			e.printStackTrace();
		}

		return new HashSet<T>();
	}

	public HashMap<String, Object> findMap(Connection connection) throws SQLException {
		try (PreparedStatement statement = database.getProvider().prepareQuery(connection, this)) {
			TableRegistration table = RegistrationFactory.getTableRegistration(objectClass);

			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = (String[]) ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			if (result.first()) {
				HashMap<String, Object> map = new HashMap<>();

				for (ColumnRegistration column : table.getColumns()) {
					if (ArrayUtils.contains(validColumns, column.getName())) {
						map.put(column.getName(), result.getObject(column.getName()));
					}
				}

				return map;
			}
		} catch (TableRegistrationException | ColumnRegistrationException e) {
			e.printStackTrace();
		}

		return new HashMap<String, Object>();
	}

	public List<HashMap<String, Object>> findMapList(Connection connection) throws SQLException {
		try (PreparedStatement statement = database.getProvider().prepareQuery(connection, this)) {
			TableRegistration table = RegistrationFactory.getTableRegistration(objectClass);

			ResultSet result = statement.executeQuery();
			ResultSetMetaData meta = result.getMetaData();

			String[] validColumns = ArrayUtils.EMPTY_STRING_ARRAY;

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				validColumns = (String[]) ArrayUtils.add(validColumns, meta.getColumnLabel(i));
			}

			List<HashMap<String, Object>> list = new LinkedList<>();
			while (result.next()) {
				HashMap<String, Object> map = new HashMap<>();

				for (ColumnRegistration column : table.getColumns()) {
					if (ArrayUtils.contains(validColumns, column.getName())) {
						map.put(column.getName(), result.getObject(column.getName()));
					}
				}

				list.add(map);
			}

			return list;
		} catch (TableRegistrationException | ColumnRegistrationException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
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
