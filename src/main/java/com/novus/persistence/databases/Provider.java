package com.novus.persistence.databases;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.novus.persistence.enums.Comparator;
import com.novus.persistence.enums.DataType;
import com.novus.persistence.enums.Order;
import com.novus.persistence.exceptions.TableRegistrationException;
import com.novus.persistence.queries.Query;

public abstract class Provider {
	
	/**
	 * Returns the query of a given @QueryType including the specific clause fields to be bound at runtime.
	 * 
	 * @param type
	 * @return
	 */
	public abstract <T> PreparedStatement prepareQuery(Query<T> q) throws TableRegistrationException, SQLException;
	
	/**
	 * Returns the order of a given @Order specific to the type of database.
	 * 
	 * @param order
	 * @return
	 */
	public abstract String getOrder(Order order);
	
	/**
	 * Returns the comparator of a given @Comparator specific to type type of database.
	 * 
	 * @param comparator
	 * @return
	 */
	public abstract String getComparator(Comparator comparator);
	
	/**
	 * Returns the data type of a given @DataType specific to the type the database.
	 * 
	 * @param type
	 * @return
	 */
	public abstract String getDataType(DataType type);
	
	
}
