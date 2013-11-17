package com.TeamNovus.Persistence.Queries.Expression;

public class Expressions {
	// Expressions
	public static Expression not() {
		return new Expression().not();
	}
	
	// Predicates
	public static Condition equal(String col, Object val) {
		return new Expression().equal(col, val);
	}
	
	public static Condition lessThan(String col, Object val) {
		return new Expression().lessThan(col, val);
	}
	
	public static Condition lessThanOrEqual(String col, Object val) {
		return new Expression().lessThanOrEqual(col, val);
	}
	
	public static Condition greaterThan(String col, Object val) {
		return new Expression().greaterThan(col, val);
	}
	
	public static Condition greaterThanOrEqual(String col, Object val) {
		return new Expression().greaterThanOrEqual(col, val);
	}
}
