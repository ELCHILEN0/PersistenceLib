package com.novus.persistence.databases.mysql;

import com.novus.persistence.databases.Configuration;

public class MySQLConfiguration extends Configuration {

	public MySQLConfiguration(String host, String port, String database, String username, String password) {
		set("host", host);
		set("port", port);
		set("database", database);
		set("username", username);
		set("password", password);
	}
	
}
