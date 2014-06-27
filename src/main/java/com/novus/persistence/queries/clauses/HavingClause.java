package com.novus.persistence.queries.clauses;

import com.novus.persistence.queries.Clause;
import com.novus.persistence.queries.expression.Condition;

public class HavingClause extends Clause {
	private final Condition	condition;

	public HavingClause(Condition condition) {
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}
}
