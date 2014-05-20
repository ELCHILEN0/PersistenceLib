package com.novus.persistence.queries.expression.conditions;

import com.novus.persistence.queries.expression.Condition;
import com.novus.persistence.queries.expression.Expression;

public class RawCondition extends Condition {
	private String sql;
	private Object[] params;
	
	public RawCondition(Expression expression, String sql, Object[] params) {
		super(expression);
		
		this.sql = sql;
		this.params = params;
	}
	
	public String getSql() {
		return sql;
	}
	
	public Object[] getParams() {
		return params;
	}
}
