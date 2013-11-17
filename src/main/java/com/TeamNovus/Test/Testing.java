package com.TeamNovus.Test;

import com.TeamNovus.Persistence.Databases.Configuration;
import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Databases.MySQL.MySQLConfiguration;
import com.TeamNovus.Persistence.Databases.MySQL.MySQLDatabase;
import com.TeamNovus.Persistence.Databases.MySQL.MySQLProvider;
import com.TeamNovus.Persistence.Queries.Queries.SelectQuery;

import static com.TeamNovus.Persistence.Queries.Expression.Expressions.*;

public class Testing {

	public static void main(String[] args) {
		Configuration configuration = new MySQLConfiguration("127.0.0.1", "3306", "minecraft", "root", "root");
		
		Database database = null;
		try {
			database = new MySQLDatabase(configuration, new MySQLProvider());
			database.connect();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		

		ExampleObject t = database.find(ExampleObject.class, 23);
		t.setHealth(t.getHealth() + 1);
		System.out.println(database.save(t));
		
	}

}
