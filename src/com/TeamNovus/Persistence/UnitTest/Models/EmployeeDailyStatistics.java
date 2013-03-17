package com.TeamNovus.Persistence.UnitTest.Models;

import com.TeamNovus.Persistence.Annotations.Table;
import com.TeamNovus.Persistence.Annotations.Columns.Column;
import com.TeamNovus.Persistence.Annotations.Columns.ForeignKey;
import com.TeamNovus.Persistence.Annotations.Columns.Id;

@Table(name = "daily_stats")
public class EmployeeDailyStatistics {
	@Id
	@Column(name = "id")
	private Integer id;
	
	@ForeignKey
	@Column(name = "employee_id")
	private Integer employeeId;
	
	@Column(name = "arrived_at_work")
	private Boolean arrivedAtWork;

}
