package com.novus.persistence.annotations.columns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Id is an annotated interface for class fields which defines a column as the
 * identifier of the table.	An Id object is required for every table in order
 * for the library to function properly.
 * 
 * @author Jnani Weibel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {  }
