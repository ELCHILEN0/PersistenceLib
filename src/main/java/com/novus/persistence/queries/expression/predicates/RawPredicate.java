package com.novus.persistence.queries.expression.predicates;

import com.novus.persistence.queries.expression.Expression;
import com.novus.persistence.queries.expression.Predicate;

/**
 * @author Jnani
 *
 */
public class RawPredicate extends Predicate {
	private String sql;
	private Object[] args;
	
	/**
	 * 
	 */
	public RawPredicate(Expression expression, boolean negated, String sql, Object... args) {
		super(expression, negated);
		this.sql = sql;
		this.args = args;
	}

	/**
	 * @return	the sql
	 */
	public String getSQL() {
		return sql;
	}
	
	/**
	 * @return	the args
	 */
	public Object[] getArgs() {
		return args;
	}
	
	public String toString() {
		return sql;
	}
}
