package com.novus.persistence.databases;

import java.util.HashMap;

/**
 * Provides a Configuration object used for specifying database details for
 * establishing connection and handling different variables.
 * <p>
 * A simple wrapper above a hash map that should be overwritten per class to
 * provide details needed to connecting to a database and other details that the
 * Database might need.
 * <p>
 * This class is designed to be extended for different database configuration so
 * the correct details can be provided to the Database. Extending the class
 * should be done in the following steps:
 * <ol>
 * <li>Redefine {@link #Configuration()} to include the required properties.</li>
 * </ol>
 * 
 * @author Jnani
 * @see Database
 * @since 1.0.0
 */
public abstract class Configuration {
	private HashMap<String, String> properties = new HashMap<String, String>();

	public Configuration set(HashMap<String, String> properties) {
		this.properties = properties;

		return this;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public Configuration set(String property, String value) {
		properties.put(property, value);

		return this;
	}

	public String get(String property) {
		return properties.get(property);
	}

	public Configuration remove(String property) {
		properties.remove(property);

		return this;
	}

	public boolean has(String property) {
		return properties.containsKey(property);
	}
}
