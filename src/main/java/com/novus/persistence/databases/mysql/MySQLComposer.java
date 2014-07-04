package com.novus.persistence.databases.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.novus.persistence.databases.Database;
import com.novus.persistence.databases.Composer;
import com.novus.persistence.enums.Comparator;
import com.novus.persistence.enums.DataType;
import com.novus.persistence.enums.Junction;
import com.novus.persistence.enums.Order;
import com.novus.persistence.exceptions.ColumnRegistrationException;
import com.novus.persistence.exceptions.TableRegistrationException;
import com.novus.persistence.internal.RegistrationFactory;
import com.novus.persistence.internal.TableRegistration;
import com.novus.persistence.queries.Clause;
import com.novus.persistence.queries.Query;
import com.novus.persistence.queries.clauses.GroupByClause;
import com.novus.persistence.queries.clauses.HavingClause;
import com.novus.persistence.queries.clauses.LimitClause;
import com.novus.persistence.queries.clauses.OrderByClause;
import com.novus.persistence.queries.clauses.WhereClause;
import com.novus.persistence.queries.expression.Predicate;
import com.novus.persistence.queries.expression.predicates.BinaryPredicate;
import com.novus.persistence.queries.expression.predicates.GroupedPredicate;
import com.novus.persistence.queries.expression.predicates.RawPredicate;
import com.novus.persistence.queries.queries.DeleteQuery;
import com.novus.persistence.queries.queries.InsertQuery;
import com.novus.persistence.queries.queries.SelectQuery;
import com.novus.persistence.queries.queries.UpdateQuery;

public class MySQLComposer implements Composer {

	private String predicateToString(Predicate p) {
		StringBuilder builder = new StringBuilder();

		for (Predicate pre : p.getExpression().getPredicates()) {
			if (pre.isNegated()) {
				builder.append("NOT ");
			}

			builder.append("(");

			if (pre instanceof GroupedPredicate) {
				GroupedPredicate predicate = (GroupedPredicate) pre;

				builder.append(predicateToString(predicate.getInner()));
			} else if (pre instanceof BinaryPredicate) {
				BinaryPredicate predicate = (BinaryPredicate) pre;

				builder.append(predicate.getColumn() + "");
			} else if (pre instanceof RawPredicate) {
				RawPredicate predicate = (RawPredicate) pre;

				builder.append(predicate.getSQL());
			}

			builder.append(")");

			if (pre.getJunction() != null) {
				Junction junction = pre.getJunction();

				if (junction == Junction.AND) {
					System.out.print(" OR ");
				} else if (junction == Junction.OR) {
					System.out.print(" AND ");
				}
			}

		}

		return builder.toString();
	}
	
	private Object[] predicateToParams(Predicate p) {
		Object[] objects = ArrayUtils.EMPTY_OBJECT_ARRAY;
		
		for (Predicate pre : p.getExpression().getPredicates()) {
			if (pre instanceof GroupedPredicate) {
				GroupedPredicate predicate = (GroupedPredicate) pre;

				objects = ArrayUtils.addAll(objects, predicateToParams(predicate.getInner()));
			} else if (pre instanceof BinaryPredicate) {
				BinaryPredicate predicate = (BinaryPredicate) pre;

				objects = ArrayUtils.add(objects, predicate.getValue());
			} else if (pre instanceof RawPredicate) {
				RawPredicate predicate = (RawPredicate) pre;

				objects = ArrayUtils.addAll(objects, predicate.getArgs());
			}
		}
		
		return objects;
	}

	private String clauseSQL(Clause c) {
		if (c instanceof WhereClause) {
			WhereClause clause = (WhereClause) c;

			StringBuilder builder = new StringBuilder();

			builder.append("WHERE ");
			builder.append(predicateToString(clause.getPredicate()));

			return builder.toString();
		} else if (c instanceof GroupByClause) {
			GroupByClause clause = (GroupByClause) c;

			StringBuilder builder = new StringBuilder();

			builder.append("GROUP BY " + StringUtils.join(clause.getColumns(), ", "));

			return builder.toString();
		} else if (c instanceof HavingClause) {
			HavingClause clause = (HavingClause) c;

			StringBuilder builder = new StringBuilder();

			builder.append("HAVING ");
			builder.append(predicateToString(clause.getPredicate()));

			return builder.toString();
		} else if (c instanceof OrderByClause) {
			OrderByClause clause = (OrderByClause) c;

			StringBuilder builder = new StringBuilder();

			builder.append("ORDER BY " + StringUtils.join(clause.getColumns(), ", ") + " " + getOrder(clause.getOrder()));

			return builder.toString();
		} else if (c instanceof LimitClause) {
			LimitClause clause = (LimitClause) c;

			StringBuilder builder = new StringBuilder();

			builder.append("LIMIT " + clause.getMin() + ", " + clause.getMax());

			return builder.toString();
		}

		return "";
	}

	public Object[] clauseParams(Clause c) {
		if (c instanceof WhereClause) {
			WhereClause clause = (WhereClause) c;

			return predicateToParams(clause.getPredicate());
		} else if (c instanceof HavingClause) {
			HavingClause clause = (HavingClause) c;

			return predicateToParams(clause.getPredicate());
		}

		return ArrayUtils.EMPTY_OBJECT_ARRAY;
	}

	@Override
	public <T> PreparedStatement prepareQuery(Connection connection, Query<T> q) throws SQLException {
		Database database = q.getDatabase();

		TableRegistration table = null;
		try {
			table = RegistrationFactory.getTableRegistration(q.getObjectClass());
		} catch (TableRegistrationException | ColumnRegistrationException e) {
			e.printStackTrace();
		}

		if (q instanceof InsertQuery<?>) {
			InsertQuery<T> query = (InsertQuery<T>) q;
			String sql = "INSERT INTO { TABLE } ({ COLUMNS }) VALUES ({ VALUES })";

			sql = sql.replace("{ TABLE }", table.getName());
			sql = sql.replace("{ COLUMNS }", StringUtils.join(query.getColumns(), ", "));
			sql = sql.replace("{ VALUES }", StringUtils.join(query.getColumns(), ", ").replaceAll("[^\\s*,]+", "?"));

			if (database.isLogging()) {
				System.out.println(sql);
			}

			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			Object[] objects = ArrayUtils.EMPTY_OBJECT_ARRAY;
			objects = ArrayUtils.addAll(objects, query.getValues());

			for (int i = 1; i <= objects.length; i++) {
				if (database.isLogging()) {
					System.out.println(objects[i - 1]);
				}

				statement.setObject(i, objects[i - 1]);
			}

			return statement;
		} else if (q instanceof UpdateQuery<?>) {
			UpdateQuery<T> query = (UpdateQuery<T>) q;
			String sql = "UPDATE { TABLE } SET { COLUMNS_AND_VALUES } { WHERE }";

			sql = sql.replace("{ TABLE }", table.getName());
			sql = sql.replace("{ COLUMNS_AND_VALUES }", StringUtils.join(query.getColumns(), " = ?, ") + " = ?");
			sql = sql.replace("{ WHERE }", clauseSQL(query.getWhere()));

			if (database.isLogging()) {
				System.out.println(sql);
			}

			PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

			Object[] objects = ArrayUtils.EMPTY_OBJECT_ARRAY;
			objects = ArrayUtils.addAll(objects, query.getValues());
			objects = ArrayUtils.addAll(objects, clauseParams(query.getWhere()));

			for (int i = 1; i <= objects.length; i++) {
				if (database.isLogging()) {
					System.out.println(objects[i - 1]);
				}

				statement.setObject(i, objects[i - 1]);
			}

			return statement;
		} else if (q instanceof DeleteQuery<?>) {
			DeleteQuery<T> query = (DeleteQuery<T>) q;
			String sql = "DELETE FROM { TABLE } { WHERE }";

			sql = sql.replace("{ TABLE }", table.getName());
			sql = sql.replace("{ WHERE }", clauseSQL(query.getWhere()));

			if (database.isLogging()) {
				System.out.println(sql);
			}

			PreparedStatement statement = connection.prepareStatement(sql);

			Object[] objects = ArrayUtils.EMPTY_OBJECT_ARRAY;
			objects = ArrayUtils.addAll(objects, clauseParams(query.getWhere()));

			for (int i = 1; i <= objects.length; i++) {
				if (database.isLogging()) {
					System.out.println(objects[i - 1]);
				}

				statement.setObject(i, objects[i - 1]);
			}

			return statement;
		} else if (q instanceof SelectQuery<?>) {
			SelectQuery<T> query = (SelectQuery<T>) q;
			String sql = "SELECT { FILTERS } FROM { TABLE } { WHERE } { GROUP_BY } { HAVING } { ORDER_BY } { LIMIT }";

			sql = sql.replace("{ TABLE }", table.getName());
			sql = sql.replace("{ FILTERS }", (query.getColumns().size() > 0 ? StringUtils.join(query.getColumns(), ", ") : "*"));
			sql = sql.replace("{ WHERE }", clauseSQL(query.getWhere()));
			sql = sql.replace("{ GROUP_BY }", clauseSQL(query.getGroupBy()));
			sql = sql.replace("{ HAVING }", clauseSQL(query.getHaving()));
			sql = sql.replace("{ ORDER_BY }", clauseSQL(query.getOrderBy()));
			sql = sql.replace("{ LIMIT }", clauseSQL(query.getLimit()));

			PreparedStatement statement = connection.prepareStatement(sql);

			Object[] objects = ArrayUtils.EMPTY_OBJECT_ARRAY;
			objects = ArrayUtils.addAll(objects, clauseParams(query.getWhere()));

			for (int i = 1; i <= objects.length; i++) {
				if (database.isLogging()) {
					System.out.println(objects[i - 1]);
				}

				statement.setObject(i, objects[i - 1]);
			}

			return statement;
		}

		return null;
	}

	@Override
	public String getOrder(Order order) {
		switch (order) {
			case ASC:
				return "ASC";
			case DESC:
				return "DESC";
		}

		return null;
	}

	@Override
	public String getComparator(Comparator comparator) {
		switch (comparator) {
			case EQUAL:
				return "=";
			case GREATER_THAN:
				return ">";
			case GREATER_THAN_OR_EQUAL:
				return ">=";
			case LESS_THAN:
				return "<";
			case LESS_THAN_OR_EQUAL:
				return "<=";
		}

		return null;
	}

	@Override
	public String getDataType(int length, DataType type) {
		if (length == -1) {
			switch (type) {
				case CHAR:
					return "CHAR";
				case STRING:
					return "VARCHAR";
				case BOOLEAN:
					return "BIT";
				case BYTE:
					return "TINYINT";
				case NUMERIC:
					return "NUMERIC";
				case SHORT:
					return "SMALLINT";
				case INTEGER:
					return "INT";
				case LONG:
					return "BIGINT";
				case FLOAT:
					return "REAL";
				case DOUBLE:
					return "DOUBLE";
				case BINARY:
					return "VARBINARY";
				case DATE:
					return "DATE";
				case TIME:
					return "TIME";
				case TIMESTAMP:
					return "TIMESTAMP";
				case CLOB:
					return "CLOB";
				case BLOB:
					return "BLOB";
				case ARRAY:
					return "ARRAY";
				case REF:
					return "REF";
				case STRUCT:
					return "STRUCT";
			}
		} else {
			switch (type) {
				case CHAR:
					return "CHAR(" + length + ")";
				case STRING:
					return "VARCHAR(" + length + ")";
				case BOOLEAN:
					return "BIT";
				case BYTE:
					return "TINYINT";
				case NUMERIC:
					return "NUMERIC";
				case SHORT:
					return "SMALLINT";
				case INTEGER:
					return "INT";
				case LONG:
					return "BIGINT";
				case FLOAT:
					return "REAL";
				case DOUBLE:
					return "DOUBLE";
				case BINARY:
					return "VARBINARY(" + length + ")";
				case DATE:
					return "DATE";
				case TIME:
					return "TIME";
				case TIMESTAMP:
					return "TIMESTAMP";
				case CLOB:
					return "CLOB";
				case BLOB:
					return "BLOB";
				case ARRAY:
					return "ARRAY";
				case REF:
					return "REF";
				case STRUCT:
					return "STRUCT";
			}
		}

		return null;
	}

}
