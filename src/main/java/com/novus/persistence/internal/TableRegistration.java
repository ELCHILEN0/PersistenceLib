package com.novus.persistence.internal;

import java.util.LinkedList;

import com.novus.persistence.annotations.Table;

public class TableRegistration {
	private Table							annotation	= null;
	private Class<?>						tableClass	= null;
	private ColumnRegistration				id			= null;
	private LinkedList<ColumnRegistration>	columns		= new LinkedList<ColumnRegistration>();

	public TableRegistration(Table annotation, Class<?> tableClass, ColumnRegistration id, LinkedList<ColumnRegistration> columns) {
		this.annotation = annotation;
		this.tableClass = tableClass;
		this.id = id;
		this.columns = columns;
	}

	public Table getAnnotation() {
		return annotation;
	}

	public String getName() {
		return annotation.name();
	}

	public Class<?> getTableClass() {
		return tableClass;
	}

	public ColumnRegistration getId() {
		return id;
	}

	public LinkedList<ColumnRegistration> getColumns() {
		return columns;
	}

}
