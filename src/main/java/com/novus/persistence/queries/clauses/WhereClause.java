package com.novus.persistence.queries.clauses;

import com.novus.persistence.queries.Clause;
import com.novus.persistence.queries.expression.Condition;

public class WhereClause extends Clause {
	private Condition	condition;

	public WhereClause(Condition condition) {
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}
}
