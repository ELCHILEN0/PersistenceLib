package com.TeamNovus.Persistence.Queries.Queries;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;
import com.TeamNovus.Persistence.Queries.Query;
import com.TeamNovus.Persistence.Queries.Clauses.WhereClause;
import com.TeamNovus.Persistence.Queries.Expression.Condition;

public class DeleteQuery<T> extends Query<T> {
	protected WhereClause where;

	public DeleteQuery(Database database, Class<T> objectClass) {
		super(database, objectClass);
	}
	

	public DeleteQuery<T> where(Condition condition) {
		where = new WhereClause(condition);
		
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
	
	public WhereClause getWhere() {
		return where;
	}
}
