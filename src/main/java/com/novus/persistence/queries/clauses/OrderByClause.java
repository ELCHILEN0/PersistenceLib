package com.novus.persistence.queries.clauses;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.novus.persistence.enums.Order;
import com.novus.persistence.queries.Clause;

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
