package com.TeamNovus.Persistence.UnitTest.Models;

import java.sql.Date;

import com.TeamNovus.Persistence.Annotations.Table;
import com.TeamNovus.Persistence.Annotations.Columns.Column;
import com.TeamNovus.Persistence.Annotations.Columns.ForeignKey;
import com.TeamNovus.Persistence.Annotations.Columns.Id;

@Table(name = "info")
public class EmployeeInformation {
	@Id
	@Column(name = "id")
	private Integer id;
		
	@ForeignKey
	@Column(name = "employee_id")
	private Integer employeeId;

	@Column(name = "dob")
	private Date dateOfBirth;
	
	public EmployeeInformation() {	}
	
	public EmployeeInformation(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(Date date) {
		this.dateOfBirth = date;
	}
}
