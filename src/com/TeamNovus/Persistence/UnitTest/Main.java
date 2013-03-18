package com.TeamNovus.Persistence.UnitTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Databases.MySQL.MySQLConfiguration;
import com.TeamNovus.Persistence.Databases.MySQL.MySQLDatabase;
import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;
import com.TeamNovus.Persistence.UnitTest.Models.Employee;
import com.TeamNovus.Persistence.UnitTest.Models.EmployeeDailyStatistics;
import com.TeamNovus.Persistence.UnitTest.Models.EmployeeInformation;

public class Main {
	public static Database db;
	public static List<Employee> employees = new ArrayList<Employee>();
	
	public static Employee getEmployee(String name) {
		for(Employee employee : employees) {
			if(employee.getName().equals(name)) {
				return employee;
			}
		}
		
		employees.add(new Employee(name, 0, new EmployeeInformation(new Date(36000)), new LinkedList<EmployeeDailyStatistics>()));
		
		return getEmployee(name);
	}

	public static void main(String[] args) {
		MySQLConfiguration configuration = new MySQLConfiguration();
		
		configuration.setHost("localhost")
						.setPort("3306")
						.setDatabase("database")
						.setUsername("root")
						.setPassword("root");
		
		db = new MySQLDatabase(configuration);
		
		try {			
			db.registerTable(Employee.class);
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		}
				
		db.connect();
		
		// Load
		employees = db.findAll(Employee.class);
		
		// Manipulate the objects!
		
		// Save
		db.saveAll(employees);
		
		db.disconnect();
	}
}
