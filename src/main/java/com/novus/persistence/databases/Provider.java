package com.novus.persistence.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.novus.persistence.enums.Comparator;
import com.novus.persistence.enums.DataType;
import com.novus.persistence.enums.Order;
import com.novus.persistence.exceptions.ColumnRegistrationException;
import com.novus.persistence.exceptions.TableRegistrationException;
import com.novus.persistence.queries.Query;

/**
 * Provides a Provider class used for creating the appropriate database specific
 * queries.
 * <p>
 * Converts queries built through fluid Java code into raw code specific to the
 * database in which it is specified to interact with.
 * <p>
 * This class is designed to be extended for different database types so the
 * correct queries can be provided to the connection. Extending the class should
 * be done in the following steps:
 * <ol>
 * <li>Redefine {@link #prepareQuery(Query)} to return an appropriate
 * PreparedStatement for the database type.</li>
 * <li>Redefine {@link #getOrder(Order)} to return the appropriate order for the
 * database type.</li>
 * <li>Redefine {@link #getComparator(Comparator)} to return the appropriate
 * comparator for the database type.</li>
 * <li>Redefine {@link #getDataType(DataType)} to return the appropriate data
 * type for the database type.</li>
 * </ol>
 * 
 * @author Jnani Weibel
 * @see Database
 * @since 1.0.0
 */
public interface Provider {

	/**
	 * Returns a prepared statement based on the composition of the query.
	 * <p>
	 * Converts the fluid Java code into raw database specific code to be sent
	 * to the connection for execution.
	 * 
	 * @param q
	 *            the query
	 * @return a prepared statement based or <code>null</code> if the query was
	 *         unable to be processed
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public <T> PreparedStatement prepareQuery(Connection connection, Query<T> q) throws SQLException;

	/**
	 * Returns a string representation of the order for a database type.
	 * 
	 * @param order
	 *            the order
	 * @return the string form of the order
	 */
	public String getOrder(Order order);

	/**
	 * Returns a string representation of the comparator for a database type.
	 * 
	 * @param comparator
	 *            the comparator
	 * @return the string form of the comparator
	 */
	public String getComparator(Comparator comparator);

	/**
	 * The string representation of the data type for a database type.
	 * 
	 * @param type
	 *            the data type
	 * @return the string form of the data type
	 */
	public String getDataType(DataType type);

}
