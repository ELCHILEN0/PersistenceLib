package com.novus.persistence.internal;

import java.lang.reflect.Field;

import com.novus.persistence.annotations.Column;

/**
 * Provides a mapping between class fields and table columns.
 * <p>
 * Each column registration contains the necessary information to map individual fields to a table
 * column. A column registration object contains the class Field and the Column annotation
 * associated with that field.
 * <p>
 * The ColumnRegistration can be used to generate queries in by accessing column data associated
 * with the field. For example, some queries will automatically populate the annotated fields with
 * the name and value for an object when generating the query.
 * 
 * @author Jnani
 * @see Column
 */
public class ColumnRegistration {
	private Field field = null;
	private Column annotation = null;

	public ColumnRegistration(Field field, Column annotation) {
		this.field = field;
		this.annotation = annotation;
	}

	/**
	 * Returns the field for this column registration.
	 * <p>
	 * This provides the basis for all further reflection on objects of the same class. By
	 * manipulating this field for other objects the data can be manipulated at runtime depending on
	 * the data from a database.
	 * 
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Returns the Column annotation for this column registration.
	 * <p>
	 * This includes data associated with this column such as the name, if it is nullable, unique
	 * and the length.
	 * 
	 * @return the annotation
	 */
	public Column getAnnotation() {
		return annotation;
	}

	/**
	 * Returns the column name for this registration.
	 * <p>
	 * This is simply a utility method and is equivalent to calling:
	 * <code>getAnnotation().name()</code>
	 *
	 * @return the name
	 */
	public String getName() {
		return annotation.name();
	}

	/**
	 * Returns whether or not the column data is unique.
	 * <p>
	 * This is simply a utility method and is equivalent to calling:
	 * <code>getAnnotation().unique()</code>
	 * 
	 * @return <code>true</code> if the column is unique; <code>false</code> otherwise.
	 */
	public boolean isUnique() {
		return annotation.unique();
	}

	/**
	 * Returns whether or not the column data can be null.
	 * <p>
	 * This is simply a utility method and is equivalent to calling:
	 * <code>getAnnotation().nullable()</code>
	 * 
	 * @return <code>true</code> if the column is nullable; <code>false</code> otherwise.
	 */
	public boolean isNullable() {
		return annotation.nullable();
	}

	/**
	 * Returns the length of the column or <code>-1</code> if no length is specified.
	 * <p>
	 * This is simply a utility method and is equivalent to calling:
	 * <code>getAnnotation().length()</code>
	 * 
	 * @return the length or <code>-1</code>
	 */
	public int getLength() {
		return annotation.length();
	}

	/**
	 * Returns the value of the field for an object.
	 * <p>
	 * This method allows runtime accessing of an objects field. The method works for private,
	 * protected and public methods.
	 * 
	 * @param o
	 *            the object
	 * @return the value of the objects field
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object getValue(Object o) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);

		return field.get(o);
	}

	/**
	 * Sets the value of the field for an object.
	 * <p>
	 * This method allows runtime setting of an objects field. The method works for private,
	 * protected and public methods.
	 * 
	 * @param o
	 *            the object
	 * @param value
	 *            the value to set
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void setValue(Object o, Object value) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);

		field.set(o, value);
	}

	/**
	 * Returns the type of the field.
	 * 
	 * @return the field type
	 */
	public Class<?> getType() {
		return field.getType();
	}

}
