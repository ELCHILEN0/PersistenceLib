package com.novus.persistence.annotations.columns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Column is an annotated interface for class fields which allows database attributes
 * to be specified when defining variables in a Java class.
 * <p>
 * A Column object encapsulates some basic information needed for preparing, accessing, 
 * and modifying the database.	This includes:
 * <ul>
 * <li>The name of the column</li>
 * <li>If the column is unique or not</li>
 * <li>If the column can be null or not</li>
 * <li>The length of the column
 * </ul>
 * 
 * @author Jnani Weibel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	
	/**
	 * Returns the name of the column to be referenced from a database.	This method must return
	 * a unique string for every object in a single class.	Multiple definitions with the
	 * same return value will cause errors in registration.
	 * 
	 * @return 	the name of the column
	 */
	String name();
	
	/**
	 * Returns whether or not the data stored in the column is unique.
	 * 
	 * @return 	<code>true</code> if the data in the column is unique;
	 *			<code>false</code> if the data in the column is not unique.
	 */
	boolean unique() default false;
	
	/**
	 * Returns whether or not the data stored in the column can be stored as NULL.
	 * 
	 * @return	<code>true</code> if the data in the column can be null;
	 * 			<code>false</code> if the data in the column cannot be null.
	 */
	boolean nullable() default true;
	
	/**
	 * Returns the length of the column in its corresponding table.
	 * 
	 * @return	the length of the column
	 */
	int length() default 255;
}
