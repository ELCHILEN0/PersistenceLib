package com.TeamNovus.Persistence.Queries.Clauses;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.TeamNovus.Persistence.Queries.Clause;

public class GroupByClause extends Clause {
	private LinkedHashSet<String> columns = new LinkedHashSet<String>();
	
	public GroupByClause(String... columns) {
		this.columns.addAll(Arrays.asList(columns));
	}

	public HashSet<String> getColumns() {
		return columns;
	}
}