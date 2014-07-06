package com.novus.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides an annotated interface for class fields which defines a single Column as the unique
 * identifier of a Table. Represents the id of a table.
 * <p>
 * A Column annotation must be present for every variable that is to be mapped to a column in a
 * table to ensure that the data can be saved, accessed, and registered properly. This interface
 * provides the information needed for queries and table definitions including:
 * <ul>
 * <li>The name of the column</li>
 * <li>If the column is unique or not</li>
 * <li>If the column can be null or not</li>
 * <li>The length of the column
 * </ul>
 * 
 * @author Jnani Weibel
 * @see Id
 * @see Table
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

	/**
	 * Returns the name of the column to be referenced from a database.
	 * <p>
	 * This method should return a unique string for every object in a single class. Multiple
	 * definitions with the same return value can cause errors in registration and conflicts with
	 * queries on the database.
	 * 
	 * @return the name
	 */
	String name();

	/**
	 * Returns whether or not the data stored in the column is unique.
	 * 
	 * @return <code>true</code> if the data in the column is unique; <code>false</code> otherwise.
	 */
	boolean unique() default false;

	/**
	 * Returns whether or not the column can store NULL values.
	 * 
	 * @return <code>true</code> if the data in the column can be null; <code>false</code>
	 *         otherwise.
	 */
	boolean nullable() default true;

	/**
	 * Returns the length of the column.
	 * <p>
	 * A length of <code>-1</code> signifies that the length is not set. Any other positive integer
	 * will specify the length of the column.
	 * 
	 * @return the length
	 */
	int length() default -1;
}
