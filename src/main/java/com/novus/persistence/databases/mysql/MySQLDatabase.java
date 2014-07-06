package com.novus.persistence.databases.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.sql.DataSource;

import com.novus.persistence.databases.Database;
import com.novus.persistence.enums.DataType;
import com.novus.persistence.exceptions.ColumnRegistrationException;
import com.novus.persistence.exceptions.TableRegistrationException;
import com.novus.persistence.internal.ColumnRegistration;
import com.novus.persistence.internal.RegistrationFactory;
import com.novus.persistence.internal.TableRegistration;

public class MySQLDatabase extends Database {

	public MySQLDatabase(DataSource source) throws InstantiationException {
		super(source, new MySQLComposer());
	}

	@Override
	public void createStructure(Connection connection, Class<?> objectClass) throws SQLException {
		TableRegistration table = null;
		try {
			table = RegistrationFactory.getTableRegistration(objectClass);
		} catch (TableRegistrationException | ColumnRegistrationException e) {
			e.printStackTrace();
		}

		String query = String.format("CREATE TABLE IF NOT EXISTS %s (", table.getName());

		for (int i = 0; i < table.getColumns().size(); i++) {
			ColumnRegistration column = table.getColumns().get(i);

			// Build the column parameters:
			String type =
					" "
							+ composer.getDataType(column.getLength(),
									DataType.getDataType(column.getType()));
			String notNull = column.isNullable() ? " NOT NULL" : "";
			String unique = column.isUnique() ? " UNIQUE" : "";
			String autoIncrement =
					(table.getId().getName().equals(column.getName()) ? " AUTO_INCREMENT PRIMARY KEY"
							: "");

			if (i == 0) {
				query += column.getName() + type + notNull + unique + autoIncrement;
			} else {
				query += ", " + column.getName() + type + notNull + unique + autoIncrement;
			}
		}

		query += ")";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.execute();
		}
	}

	@Override
	public void updateStructure(Connection connection, Class<?> objectClass) throws SQLException {
		TableRegistration table = null;
		try {
			table = RegistrationFactory.getTableRegistration(objectClass);
		} catch (TableRegistrationException | ColumnRegistrationException e) {
			e.printStackTrace();
		}

		String query = String.format("SELECT * FROM %s LIMIT 1", table.getName());

		LinkedList<ColumnRegistration> toChange = new LinkedList<ColumnRegistration>();
		LinkedList<ColumnRegistration> toAdd = new LinkedList<ColumnRegistration>();

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSetMetaData meta = statement.executeQuery().getMetaData();

			for (ColumnRegistration column : table.getColumns()) {
				boolean found = false;

				for (int i = 1; i <= meta.getColumnCount(); i++) {
					if (column.getName().equals(meta.getColumnName(i))) {
						found = true;

						if (!(composer.getDataType(column.getLength(),
								DataType.getDataType(column.getType())).equals(meta
								.getColumnTypeName(i)))) {
							toChange.add(column);
							break;
						}
					}
				}

				if (!(found)) {
					toAdd.add(column);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (ColumnRegistration column : toChange) {
			String type =
					" "
							+ composer.getDataType(column.getLength(),
									DataType.getDataType(column.getType()));
			String notNull = column.isNullable() ? " NOT NULL" : "";
			String unique = column.isUnique() ? " UNIQUE" : "";
			String autoIncrement = (table.getId().equals(column) ? " AUTO_INCREMENT" : "");

			String modify =
					String.format("ALTER TABLE %s MODIFY COLUMN %s" + type + notNull + unique
							+ autoIncrement, table.getName(), column.getName());
			try (PreparedStatement statement = connection.prepareStatement(modify)) {
				statement.execute();
			}
		}

		for (ColumnRegistration column : toAdd) {
			String type =
					" "
							+ composer.getDataType(column.getLength(),
									DataType.getDataType(column.getType()));
			String notNull = column.isNullable() ? " NOT NULL" : "";
			String unique = column.isUnique() ? " UNIQUE" : "";
			String autoIncrement = (table.getId().equals(column) ? " AUTO_INCREMENT" : "");

			String add =
					String.format("ALTER TABLE %s ADD COLUMN %s" + type + notNull + unique
							+ autoIncrement, table.getName(), column.getName());

			try (PreparedStatement statement = connection.prepareStatement(add)) {
				statement.execute();
			}
		}
	}
}
