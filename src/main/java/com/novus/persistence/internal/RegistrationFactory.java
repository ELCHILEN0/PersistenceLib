package com.novus.persistence.internal;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.novus.persistence.annotations.Column;
import com.novus.persistence.annotations.Id;
import com.novus.persistence.annotations.Table;
import com.novus.persistence.exceptions.ColumnRegistrationException;
import com.novus.persistence.exceptions.TableRegistrationException;

/**
 * Provides a factory to create Registration object for appropriate classes
 * 
 * @author Jnani
 */
public class RegistrationFactory {
	private static HashMap<Class<?>, TableRegistration> tableRegistrations =
			new HashMap<Class<?>, TableRegistration>();
	private static HashMap<Field, ColumnRegistration> columnRegistrations =
			new HashMap<Field, ColumnRegistration>();
	private static HashMap<Class<?>, ColumnRegistration> idRegistrations =
			new HashMap<Class<?>, ColumnRegistration>();

	/**
	 * Returns a TableRegistration object based on a properly annotated Java class.
	 * 
	 * @param clazz
	 *            the Java class to be registered
	 * @return the registered table
	 * @throws TableRegistrationException
	 * @throws ColumnRegistrationException
	 */
	public static TableRegistration getTableRegistration(Class<?> clazz)
			throws TableRegistrationException, ColumnRegistrationException {
		if (tableRegistrations.containsKey(clazz)) {
			return tableRegistrations.get(clazz);
		}

		if (!(clazz.isAnnotationPresent(Table.class))) {
			throw new TableRegistrationException("Class '" + clazz.getCanonicalName()
					+ "' does not have a valid Table annotation.");
		}

		if (RegistrationFactory.getIdRegistration(clazz) == null) {
			throw new TableRegistrationException("Class '" + clazz.getCanonicalName()
					+ "' does not have a valid Id annotation.");
		}

		Table annotation = clazz.getAnnotation(Table.class);
		ColumnRegistration id = RegistrationFactory.getIdRegistration(clazz);
		List<ColumnRegistration> columns = RegistrationFactory.getColumnRegistrations(clazz);

		TableRegistration registration = new TableRegistration(annotation, clazz, id, columns);
		tableRegistrations.put(clazz, registration);

		return registration;
	}

	/**
	 * Returns a ColumnRegistration based on a properly annotated Field.
	 * 
	 * @param field
	 *            the field to register
	 * @return the registered column
	 * @throws ColumnRegistrationException
	 */
	public static ColumnRegistration getColumnRegistration(Field field)
			throws ColumnRegistrationException {
		if (columnRegistrations.containsKey(field)) {
			return columnRegistrations.get(field);
		}

		if (!(field.isAnnotationPresent(Column.class))) {
			throw new ColumnRegistrationException("Field '" + field.getClass().getCanonicalName()
					+ "' does not have a Column annotation present.");
		}

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]*$");
		if (!(pattern.matcher(field.getAnnotation(Column.class).name()).find())) {
			throw new ColumnRegistrationException(
					"Field '"
							+ field.getClass().getCanonicalName()
							+ "' column name has illegal characters in it. The name is limited to alphanumeric characters and '_'.");
		}

		ColumnRegistration registration =
				new ColumnRegistration(field, field.getAnnotation(Column.class));
		columnRegistrations.put(field, registration);

		return registration;
	}

	/**
	 * Returns a List of all the ColumnRegistrations in a Java class.
	 * 
	 * @param clazz
	 *            the Java class to fetch all the registrations from
	 * @return a list of registered columns
	 * @throws ColumnRegistrationException
	 */
	public static List<ColumnRegistration> getColumnRegistrations(Class<?> clazz)
			throws ColumnRegistrationException {
		List<ColumnRegistration> columns = new LinkedList<ColumnRegistration>();

		for (Field field : clazz.getDeclaredFields()) {
			columns.add(RegistrationFactory.getColumnRegistration(field));
		}

		return columns;
	}

	/**
	 * Returns the ColumnRegistration that is designated as the Id of a Java class.
	 * 
	 * @param clazz
	 *            the Java class to find the id
	 * @return a registered id or <code>null</code>
	 * @throws ColumnRegistrationException
	 */
	public static ColumnRegistration getIdRegistration(Class<?> clazz)
			throws ColumnRegistrationException {
		if (idRegistrations.containsKey(clazz)) {
			return idRegistrations.get(clazz);
		}

		for (ColumnRegistration registration : RegistrationFactory.getColumnRegistrations(clazz)) {
			if (registration.getField().isAnnotationPresent(Id.class)) {
				idRegistrations.put(clazz, registration);

				return registration;
			}
		}

		return null;
	}
}
