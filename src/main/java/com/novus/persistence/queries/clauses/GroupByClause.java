package com.novus.persistence.queries.clauses;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.novus.persistence.queries.Clause;

public class GroupByClause extends Clause {
	private final LinkedHashSet<String> columns = new LinkedHashSet<String>();

	public GroupByClause(String... columns) {
		this.columns.addAll(Arrays.asList(columns));
	}

	public HashSet<String> getColumns() {
		return columns;
	}
}
