package com.novus.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides an annotated interface for classes which associates the Java class
 * to a database table.
 * <p>
 * A Table annotation must be present in every class to be persisted to ensure
 * that the class can be registered correctly. The interface provides essential
 * information for executing queries and is integral to the functioning of the
 * database. This information includes includes:
 * <ul>
 * <li>The name of the table</li>
 * </ul>
 * 
 * @author Jnani Weibel
 * @see Id
 * @see Column
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

	/**
	 * Returns the name of the table associated with the Java class.
	 * <p>
	 * This method should return a unique string for every object when working
	 * with a single database. Multiple definitions may cause errors with
	 * database methods.
	 * 
	 * @return the name of the table
	 */
	String name();

}
