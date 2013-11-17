package com.TeamNovus.Persistence.Queries.Clauses;

import com.TeamNovus.Persistence.Databases.Enums.Order;
import com.TeamNovus.Persistence.Queries.Clause;

public class OrderByClause extends Clause {
	private Order order;
	private String[] columns;
	
	public OrderByClause(Order order, String... columns) {
		this.order = order;
		this.columns = columns;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public String[] getColumns() {
		return columns;
	}
}