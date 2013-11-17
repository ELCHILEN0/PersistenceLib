package com.TeamNovus.Persistence.Internal;

import java.util.LinkedList;

import com.TeamNovus.Persistence.Annotations.Table;
import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;

public class TableRegistrationFactory {
	
	public static TableRegistration getTableRegistration(Class<?> tableClass) throws TableRegistrationException {
		
		// Check if the Table annotation is present
		if(!(tableClass.isAnnotationPresent(Table.class))) {
			throw new TableRegistrationException("Class '" + tableClass.getCanonicalName() + "' does not have a Table annotation present.");
		}
		
		// Check if the table has an Id annotation
		if(ColumnRegistrationFactory.getIdRegistration(tableClass) == null) {
			throw new TableRegistrationException("Class '" + tableClass.getCanonicalName() + "' does not have an Id annotation present.");
		}
		
		Table annotation = tableClass.getAnnotation(Table.class);
		ColumnRegistration id = ColumnRegistrationFactory.getIdRegistration(tableClass);
		LinkedList<ColumnRegistration> columns = ColumnRegistrationFactory.getColumnRegistrations(tableClass);

		TableRegistration tableRegistration = new TableRegistration(annotation, tableClass, id, columns);
		
		return tableRegistration;
	}

}
