package com.novus.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Table is an annotated interface for classes which maps a database table to a Java Class.
 * <p>
 * A Table object encapsulates some basic information needed for preparing, accessing, 
 * and modifying the database.	This includes:
 * <ul>
 * <li>The name of the table</li>
 * </ul>
 * 
 * @author Jnani Weibel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	
	/**
	 * Returns the name of the table to be referenced from a database.	This method should return
	 * a unique string for every object when working with a single database.  Multiple definitions
	 * may cause errors with database methods.
	 * 
	 * @return 	the name of the table
	 */
	String name();
	
}
