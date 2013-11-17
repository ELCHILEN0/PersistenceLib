package com.TeamNovus.Persistence.Queries.Clauses;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.TeamNovus.Persistence.Databases.Enums.Order;
import com.TeamNovus.Persistence.Queries.Clause;

public class OrderByClause extends Clause {
	private Order order;
	private LinkedHashSet<String> columns = new LinkedHashSet<String>();
	
	public OrderByClause(Order order, String... columns) {
		this.order = order;
		this.columns.addAll(Arrays.asList(columns));
	}
	
	public Order getOrder() {
		return order;
	}
	
	public HashSet<String> getColumns() {
		return columns;
	}
}