package com.novus.persistence.queries.expression;

import com.novus.persistence.enums.Junction;
import com.novus.persistence.queries.expression.Expression;

/**
 * @author Jnani
 */
public abstract class Predicate {
	private Expression	expression;

	private boolean		negated	= false;
	private Junction	junction;

	public Predicate(Expression expression, boolean negated) {
		this.expression = expression;
		this.negated = negated;

		this.expression.getPredicates().add(this);
	}

	/**
	 * Extend the predicate with an AND junction.
	 * 
	 * @return
	 */
	public Expression and() {
		this.junction = Junction.AND;

		return expression;
	}

	/**
	 * Extend the predicate with an OR junction.
	 * 
	 * @return
	 */
	public Expression or() {
		this.junction = Junction.OR;

		return expression;
	}

	/**
	 * Returns whether or not the predicate is negated.
	 * 
	 * @return <code>true</code> if the predicate is negated; <code>false</code>
	 *         otherwise.
	 */
	public boolean isNegated() {
		return negated;
	}

	/**
	 * Returns the junction following this predicate or <code>null</code> if no
	 * junction follows this Predicate.
	 * 
	 * @return the junction
	 */
	public Junction getJunction() {
		return junction;
	}

	/**
	 * Returns the expression that this predicate belongs to.
	 * 
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}

}
