package com.novus.persistence.internal;

import java.util.HashMap;
import java.util.LinkedList;

import com.novus.persistence.annotations.Table;
import com.novus.persistence.exceptions.TableRegistrationException;

/**
 * @author Jnani
 */
public class RegistrationFactory {
	private static HashMap<Class<?>, TableRegistration>	tableRegistrations	= new HashMap<Class<?>, TableRegistration>();

	/**
	 * Converts a Java class
	 * 
	 * @param tableClass
	 * @return
	 * @throws TableRegistrationException
	 */
	public static TableRegistration getTableRegistration(Class<?> tableClass) throws TableRegistrationException {
		if (tableRegistrations.containsKey(tableClass)) {
			return tableRegistrations.get(tableClass);
		}

		if (!(tableClass.isAnnotationPresent(Table.class))) {
			throw new TableRegistrationException("Class '" + tableClass.getCanonicalName() + "' does not have a valid Table annotation.");
		}

		if (ColumnRegistrationFactory.getIdRegistration(tableClass) == null) {
			throw new TableRegistrationException("Class '" + tableClass.getCanonicalName() + "' does not have a valid Id annotation.");
		}

		Table annotation = tableClass.getAnnotation(Table.class);
		ColumnRegistration id = ColumnRegistrationFactory.getIdRegistration(tableClass);
		LinkedList<ColumnRegistration> columns = ColumnRegistrationFactory.getColumnRegistrations(tableClass);

		TableRegistration registration = new TableRegistration(annotation, tableClass, id, columns);
		tableRegistrations.put(tableClass, registration);

		return registration;
	}
}
