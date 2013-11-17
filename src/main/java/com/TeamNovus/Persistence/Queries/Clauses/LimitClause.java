package com.TeamNovus.Persistence.Queries.Clauses;

import com.TeamNovus.Persistence.Queries.Clause;

public class LimitClause extends Clause {
	int min, max;
	
	public LimitClause(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
}