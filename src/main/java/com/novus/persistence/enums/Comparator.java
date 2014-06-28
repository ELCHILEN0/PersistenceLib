package com.novus.persistence.enums;

/**
 * Provides an enum representation of common comparators used in building
 * different aspects of queries.
 * <p>
 * This class is an internal representation of the comparators used in different
 * expressions. The enum is translated to the proper SQL code in the appropriate
 * Provider for the database.
 * 
 * @author Jnani Weibel
 * @since 1.0.0
 */
public enum Comparator {
	EQUAL,
	LESS_THAN,
	LESS_THAN_OR_EQUAL,
	GREATER_THAN,
	GREATER_THAN_OR_EQUAL;
}
