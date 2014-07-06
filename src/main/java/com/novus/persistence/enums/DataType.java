package com.novus.persistence.enums;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Provides mappings from SQL data types to the corresponding Java data types.
 * 
 * @author Jnani Weibel
 * @since 1.0.0
 */
public enum DataType {
	CHAR(Character.class, char.class),
	STRING(String.class),
	BOOLEAN(Boolean.class, boolean.class),
	NUMERIC(BigDecimal.class),
	BYTE(Byte.class, byte.class),
	SHORT(Short.class, short.class),
	INTEGER(Integer.class, int.class),
	LONG(Long.class, long.class),
	FLOAT(Float.class, float.class),
	DOUBLE(Double.class, double.class),
	BINARY(Byte[].class, byte[].class),
	DATE(Date.class),
	TIME(Time.class),
	TIMESTAMP(Timestamp.class),
	CLOB(Clob.class),
	BLOB(Blob.class),
	ARRAY(Array.class),
	REF(Ref.class),
	STRUCT(Struct.class);

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
