package com.novus.persistence.queries.expression.predicates;

import com.novus.persistence.queries.expression.Expression;
import com.novus.persistence.queries.expression.Predicate;

/**
 * @author Jnani
 *
 */
public class GroupedPredicate extends Predicate {
	private Predicate inner;
	
	public GroupedPredicate(Expression expression, boolean negated, Predicate inner) {
		super(expression, negated);
		
		this.inner = inner;
	}

	public Predicate getInner() {
		return inner;
	}
}
