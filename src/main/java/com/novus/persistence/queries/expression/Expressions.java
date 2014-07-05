package com.novus.persistence.queries.expression;

import com.novus.persistence.queries.expression.predicates.GroupedPredicate;

/**
 * @author Jnani
 */
public class Expressions {
	// Expressions
	public static Expression not() {
		return new Expression().not();
	}

	// Grouped Predicate
	public static GroupedPredicate group(Predicate predicate) {
		return new Expression().group(predicate);
	}

	// Binary Predicate
	public static Predicate equal(String col, Object val) {
		return new Expression().equal(col, val);
	}

	public static Predicate lessThan(String col, Object val) {
		return new Expression().lessThan(col, val);
	}

	public static Predicate lessThanOrEqual(String col, Object val) {
		return new Expression().lessThanOrEqual(col, val);
	}

	public static Predicate greaterThan(String col, Object val) {
		return new Expression().greaterThan(col, val);
	}

	public static Predicate greaterThanOrEqual(String col, Object val) {
		return new Expression().greaterThanOrEqual(col, val);
	}

	// Condition
}
