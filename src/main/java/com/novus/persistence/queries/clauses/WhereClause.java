package com.novus.persistence.queries.clauses;

import com.novus.persistence.queries.Clause;
import com.novus.persistence.queries.expression.Predicate;

public class WhereClause extends Clause {
	private Predicate predicate;

	public WhereClause(Predicate predicate) {
		this.predicate = predicate;
	}

	public Predicate getPredicate() {
		return predicate;
	}
}
