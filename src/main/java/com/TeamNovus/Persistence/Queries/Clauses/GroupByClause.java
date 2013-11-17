package com.TeamNovus.Persistence.Queries.Clauses;

import com.TeamNovus.Persistence.Queries.Clause;

public class GroupByClause extends Clause {
	private String[] columns;
	
	public GroupByClause(String... columns) {
		this.columns = columns;
	}

	public String[] getColumns() {
		return columns;
	}
}