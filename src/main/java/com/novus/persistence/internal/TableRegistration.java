package com.novus.persistence.internal;

import java.util.LinkedList;
import java.util.List;

import com.novus.persistence.annotations.Table;

/**
 * Provides a mapping between Java classes and database tables.
 * <p>
 * Each table registration contains the necessary information to map individual Java classes to a
 * database table. A table registration contains the Table annotation for a specific class, the
 * class that the table is mapped to, the ColumnRegistration associated with the id of the table and
 * a list of registered columns in the class.
 * <p>
 * The TableRegistration can be used to generate queries by accessing by accessing table data in
 * specified in the annotation. For example, some queries read the name of the table from the
 * annotation and insert that value in a generated query.
 * 
 * @author Jnani
 * @see Table
 * @see ColumnRegistration
 */
public class TableRegistration {
	private Table annotation = null;
	private Class<?> tableClass = null;
	private ColumnRegistration id = null;
	private List<ColumnRegistration> columns = new LinkedList<ColumnRegistration>();

	public TableRegistration(Table annotation, Class<?> tableClass, ColumnRegistration id, List<ColumnRegistration> columns) {
		this.annotation = annotation;
		this.tableClass = tableClass;
		this.id = id;
		this.columns = columns;
	}

	/**
	 * Returns the Table annotation for this table registration.
	 * <p>
	 * This includes data such as the table name which is used to map the table class to a specific
	 * table in a database.
	 * 
	 * @return the annotation
	 */
	public Table getAnnotation() {
		return annotation;
	}

	/**
	 * Returns the table name for this table registration.
	 * <p>
	 * This is simply a utility method and is equivalent to calling:
	 * <code>getAnnotation().getName()</code>
	 * 
	 * @return the table name
	 */
	public String getName() {
		return annotation.name();
	}

	/**
	 * Returns the class for this table registration.
	 * 
	 * @return the table class
	 */
	public Class<?> getTableClass() {
		return tableClass;
	}

	/**
	 * Returns a column registration that references the id of a table.
	 * 
	 * @return the id
	 * @see ColumnRegistration
	 */
	public ColumnRegistration getId() {
		return id;
	}

	/**
	 * Returns a list of column registrations that reference the columns in the table.
	 * 
	 * @return the columns
	 * @see ColumnRegistration
	 */
	public List<ColumnRegistration> getColumns() {
		return columns;
	}

}
