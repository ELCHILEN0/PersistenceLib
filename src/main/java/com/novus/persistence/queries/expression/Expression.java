package com.novus.persistence.queries.expression;

import java.util.LinkedList;

import com.novus.persistence.enums.Comparator;
import com.novus.persistence.queries.expression.conditions.BinaryCondition;
import com.novus.persistence.queries.expression.conditions.RawCondition;

/**
 * This class consists of an assortment of static initializers to quickly create 
 * Predicates without the use of the new keyword.
 * 
 * The methods should be statically imported with: import static Predicate.*;
 * 
 * @author Jnani Weibel
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
