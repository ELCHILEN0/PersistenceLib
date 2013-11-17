package com.TeamNovus.Persistence.Queries.Expression;

import java.util.LinkedList;

import com.TeamNovus.Persistence.Databases.Enums.Comparator;
import com.TeamNovus.Persistence.Queries.Expression.Conditions.BinaryCondition;
import com.TeamNovus.Persistence.Queries.Expression.Conditions.RawCondition;

/**
 * This class consists of an assortment of static initializers to quickly create 
 * Predicates without the use of the new keyword.
 * 
 * The methods should be statically imported with: import static Predicate.*;
 * 
 * @author Jnani
 *
 */
public class Expression  {
	// A boolean representing if the expression is currently negating.
	public boolean negating = false;
	
	// A list of the conditions
	public LinkedList<Condition> conditions = new LinkedList<Condition>();

	// Expressions
	public Expression not() {
		this.negating = true;
		
		return this;
	}
	
	// Predicates
	public Condition equal(String col, Object val) {
		return new BinaryCondition(this, col, val, Comparator.EQUAL);
	}
	
	public Condition lessThan(String col, Object val) {
		return new BinaryCondition(this, col, val, Comparator.LESS_THAN);
	}
	
	public Condition lessThanOrEqual(String col, Object val) {
		return new BinaryCondition(this, col, val, Comparator.LESS_THAN_OR_EQUAL);
	}
	
	public Condition greaterThan(String col, Object val) {
		return new BinaryCondition(this, col, val, Comparator.GREATER_THAN);
	}
	
	public Condition greaterThanOrEqual(String col, Object val) {
		return new BinaryCondition(this, col, val, Comparator.GREATER_THAN_OR_EQUAL);
	}
	
	public Condition raw(String sql, Object... params) {
		return new RawCondition(this, sql, params);
	}
}
