package com.TeamNovus.Persistence.UnitTest.Models;

import java.util.LinkedList;

import com.TeamNovus.Persistence.Annotations.Table;
import com.TeamNovus.Persistence.Annotations.Columns.Column;
import com.TeamNovus.Persistence.Annotations.Columns.Id;
import com.TeamNovus.Persistence.Annotations.Relationships.OneToMany;
import com.TeamNovus.Persistence.Annotations.Relationships.OneToOne;

@Table(name = "employees")
public class Employee {
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "salary")
	private Integer salary;
	
	// Relationships:
	@OneToOne
	private EmployeeInformation information;
	
	@OneToMany
	private LinkedList<EmployeeDailyStatistics> dailyStatistics;
	
	// Handles construction when loaded from the database.
	public Employee() {  }
	
	public Employee(String name, Integer salary, EmployeeInformation information, LinkedList<EmployeeDailyStatistics> dailyStatistics) {
		this.name = name;
		this.salary = salary;
		this.information = information;
		this.dailyStatistics = dailyStatistics;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getSalary() {
		return salary;
	}
	
	public EmployeeInformation getInformation() {
		return information;
	}
	
	public LinkedList<EmployeeDailyStatistics> getDailyStatistics() {
		return dailyStatistics;
	}
}
