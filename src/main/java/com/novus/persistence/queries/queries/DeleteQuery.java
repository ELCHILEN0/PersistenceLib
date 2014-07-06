package com.novus.persistence.queries.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.novus.persistence.databases.Database;
import com.novus.persistence.queries.Query;
import com.novus.persistence.queries.clauses.WhereClause;
import com.novus.persistence.queries.expression.Condition;

public class DeleteQuery<T> extends Query<T> {
	protected WhereClause where;

	public DeleteQuery(Database database, Class<T> objectClass) {
		super(database, objectClass);
	}

	public DeleteQuery<T> where(Condition condition) {
		where = new WhereClause(condition);

		return this;
	}

	public boolean execute(Connection connection) throws SQLException {
		try (PreparedStatement statement = database.getProvider().prepareQuery(connection, this)) {
			statement.executeUpdate();

			return true;
		}
	}

	public WhereClause getWhere() {
		return where;
	}
}
