package com.TeamNovus.Persistence.Queries.Expression.Conditions;

import com.TeamNovus.Persistence.Enums.Comparator;
import com.TeamNovus.Persistence.Queries.Expression.Condition;
import com.TeamNovus.Persistence.Queries.Expression.Expression;

public class BinaryCondition extends Condition {
	private String column;
	private Object value;
	private Comparator comparator;
	
	public BinaryCondition(Expression expression, String column, Object value, Comparator comparator) {
		super(expression);
		
		this.column = column;
		this.value = value;
		this.comparator = comparator;
	}

	public String getColumn() {
		return column;
	}
	
	public Object getValue() {
		return value;
	}
	
	public Comparator getComparator() {
		return comparator;
	}
}
