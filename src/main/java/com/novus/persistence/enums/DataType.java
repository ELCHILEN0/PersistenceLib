package com.novus.persistence.enums;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public enum DataType {
	INT(Integer.class, int.class),
	LONG(Long.class, long.class),
	DOUBLE(Double.class, double.class),
	FLOAT(Float.class, float.class),
	BOOLEAN(Boolean.class, boolean.class),
	STRING(String.class),
	DATE(Date.class),
	TIME(Time.class),
	TIMESTAMP(Timestamp.class);
	
	private Class<?>[] classes;
	
	private DataType(Class<?>... classes) {
		this.classes = classes;
	}
	
	public boolean isClass(Class<?> clazz) {
		for (Class<?> typeClass : this.classes) {
			if (clazz.equals(typeClass)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static DataType getDataType(Class<?> clazz) {
		for (DataType type : DataType.values()) {
			if (type.isClass(clazz)) {
				return type;
			}
		}
		
		return null;
	}

}
