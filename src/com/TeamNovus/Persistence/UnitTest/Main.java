package com.TeamNovus.Persistence.UnitTest;

import java.sql.Date;
import java.util.LinkedList;

import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Databases.MySQL.MySQLConfiguration;
import com.TeamNovus.Persistence.Databases.MySQL.MySQLDatabase;
import com.TeamNovus.Persistence.Databases.SQLite.SQLiteConfiguration;
import com.TeamNovus.Persistence.Databases.SQLite.SQLiteDatabase;
import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;
import com.TeamNovus.Persistence.UnitTest.Models.Employee;
import com.TeamNovus.Persistence.UnitTest.Models.EmployeeDailyStatistics;
import com.TeamNovus.Persistence.UnitTest.Models.EmployeeInformation;
import com.TeamNovus.Persistence.UnitTest.Models.SNPlayer;

public class Main {
	public static Database db;
	public static LinkedList<Employee> employees = new LinkedList<Employee>();
	
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
		
		db = new MySQLDatabase(configuration);
		
		try {			
			db.registerTable(Employee.class);
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		}
				
		db.connect();
		
		employees = (LinkedList<Employee>) db.findAll(Employee.class);
		
		// TODO: Database queries in here!
		Employee jnani = getEmployee("Jnani");
		System.out.println(jnani.getInformation().getDateOfBirth());
		jnani.getInformation().setDateOfBirth(new Date(843192000001l));
		
		Employee bob = getEmployee("bob");
		System.out.println(bob.getInformation().getDateOfBirth());
		
		for(Employee employee : employees) {
			db.save(employee);
		}
		
		db.disconnect();
	}
}
