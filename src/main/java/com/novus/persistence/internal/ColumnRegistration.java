package com.novus.persistence.internal;

import java.lang.reflect.Field;

import com.novus.persistence.annotations.columns.Column;

public class ColumnRegistration {
	private Field	field		= null;
	private Column	annotation	= null;

	public ColumnRegistration(Field field, Column annotation) {
		this.field = field;
		this.annotation = annotation;
	}

	public Field getField() {
		return field;
	}

	public Column getAnnotation() {
		return annotation;
	}

	public String getName() {
		return annotation.name();
	}

	public boolean isUnique() {
		return annotation.unique();
	}

	public boolean isNullable() {
		return annotation.nullable();
	}

	public int length() {
		return annotation.length();
	}

	public Object getValue(Object o) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);

		return field.get(o);
	}

	public void setValue(Object o, Object value) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);

		field.set(o, value);
	}

	public Class<?> getType() {
		return field.getType();
	}

}
