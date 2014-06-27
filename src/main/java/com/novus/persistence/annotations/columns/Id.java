package com.novus.persistence.annotations.columns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides an annotated interface for class fields which defines a single
 * Column as the unique identifier of a Table. Represents the id of a table.
 * <p>
 * An Id annotation must be present in every class to be persisted to ensure
 * that the class can be registered correctly. Fields which are annotated with
 * this interface should not be tampered with as it disrupts the integrity of
 * the object and mappings to the database.
 * 
 * @author Jnani Weibel
 * @see Column
 * @see Table
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
}
