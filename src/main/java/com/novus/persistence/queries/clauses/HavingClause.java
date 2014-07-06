package com.novus.persistence.queries.clauses;

import com.novus.persistence.queries.Clause;
import com.novus.persistence.queries.expression.Predicate;

public class HavingClause extends Clause {
	private final Predicate predicate;

	public HavingClause(Predicate predicate) {
		this.predicate = predicate;
	}

	public Predicate getPredicate() {
		return predicate;
	}
}
