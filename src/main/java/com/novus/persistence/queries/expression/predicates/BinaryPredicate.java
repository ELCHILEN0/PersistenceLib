package com.novus.persistence.queries.expression.predicates;

import com.novus.persistence.enums.Comparator;
import com.novus.persistence.queries.expression.Expression;
import com.novus.persistence.queries.expression.Predicate;

/**
 * @author Jnani
 */
public class BinaryPredicate extends Predicate {
	private String column;
	private Object value;
	private Comparator comparator;

	public BinaryPredicate(Expression expression, boolean negated, String column,
			Comparator comparator, Object value) {
		super(expression, negated);

		this.column = column;
		this.value = value;
		this.comparator = comparator;
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the comparator
	 */
	public Comparator getComparator() {
		return comparator;
	}

}
