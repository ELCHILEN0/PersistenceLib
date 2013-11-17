package com.TeamNovus.Persistence.Queries.Expression.Conditions;

import com.TeamNovus.Persistence.Queries.Expression.Condition;
import com.TeamNovus.Persistence.Queries.Expression.Expression;

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
