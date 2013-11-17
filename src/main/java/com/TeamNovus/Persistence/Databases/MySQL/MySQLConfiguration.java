package com.TeamNovus.Persistence.Databases.MySQL;

import com.TeamNovus.Persistence.Databases.Configuration;

public class MySQLConfiguration extends Configuration {

	public MySQLConfiguration(String host, String port, String database, String username, String password) {
		super("mysql");

		set("host", host);
		set("port", port);
		set("database", database);
		set("username", username);
		set("password", password);
	}
	
}
