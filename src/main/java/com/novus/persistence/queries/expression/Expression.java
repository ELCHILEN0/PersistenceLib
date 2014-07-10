package com.novus.persistence.queries.expression;

import java.util.LinkedList;

import com.novus.persistence.enums.Comparator;
import com.novus.persistence.queries.expression.predicates.BinaryPredicate;
import com.novus.persistence.queries.expression.predicates.GroupedPredicate;
import com.novus.persistence.queries.expression.predicates.RawPredicate;

/**
 * @author Jnani
 */
public class Expression {
	private LinkedList<Predicate> predicates = new LinkedList<>();

	private boolean negateNext = false;

	/**
	 * Negate the next prefix of the expression.
	 * <p>
	 * 
	 * @return the current expression
	 */
	public Expression not() {
		this.negateNext = true;

		return this;
	}

	// Grouped Predicate
	public GroupedPredicate group(Predicate predicate) {
		return new GroupedPredicate(this, negateNext, predicate);
	}

	// Binary Predicate
	public BinaryPredicate equal(String column, Object value) {
		return new BinaryPredicate(this, negateNext, column, Comparator.EQUAL, value);
	}

	public BinaryPredicate lessThan(String column, Object value) {
		return new BinaryPredicate(this, negateNext, column, Comparator.LESS_THAN, value);
	}

	public BinaryPredicate lessThanOrEqual(String column, Object value) {
		return new BinaryPredicate(this, negateNext, column, Comparator.LESS_THAN_OR_EQUAL, value);
	}

	public BinaryPredicate greaterThan(String column, Object value) {
		return new BinaryPredicate(this, negateNext, column, Comparator.GREATER_THAN, value);
	}

	public BinaryPredicate greaterThanOrEqual(String column, Object value) {
		return new BinaryPredicate(this, negateNext, column, Comparator.GREATER_THAN_OR_EQUAL, value);
	}

	// Raw Predicate
	public RawPredicate raw(String sql, Object... args) {
		return new RawPredicate(this, negateNext, sql, args);
	}
	
	/**
	 * Set the value of whether the expression should negate the next Predicate.
	 * 
	 * @param	negateNext	the negateNext to set
	 */
	public void setNegateNext(boolean negateNext) {
		this.negateNext = negateNext;
	}

	/**
	 * Returns a list of all the predicates in this expression.
	 * 
	 * @return the predicates
	 */
	public LinkedList<Predicate> getPredicates() {
		return predicates;
	}

}
