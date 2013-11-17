package com.TeamNovus.Persistence.Queries.Clauses;

import com.TeamNovus.Persistence.Queries.Clause;
import com.TeamNovus.Persistence.Queries.Expression.Condition;

public class HavingClause extends Clause {
	private Condition condition;
	
	public HavingClause(Condition condition) {
		this.condition = condition;
	}
	
	public Condition getCondition() {
		return condition;
	}
}
