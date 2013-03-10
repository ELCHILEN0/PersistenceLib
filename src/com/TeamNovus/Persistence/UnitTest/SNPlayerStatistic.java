package com.TeamNovus.Persistence.UnitTest;

import com.TeamNovus.Persistence.Annotations.Table;
import com.TeamNovus.Persistence.Annotations.Columns.Column;
import com.TeamNovus.Persistence.Annotations.Columns.ForeignKey;
import com.TeamNovus.Persistence.Annotations.Columns.Id;

@Table(name = "statistics")
public class SNPlayerStatistic {
	@Id
	@Column(name = "id")
	private Integer id;
	
	@ForeignKey
	@Column(name = "player_id_fk")
	private Integer playerId;
	
	@Column(name = "strength")
	private Integer strength;
	
	public SNPlayerStatistic() {
		// TODO Auto-generated constructor stub
	}
	
	public SNPlayerStatistic(Integer strength) {
		this.strength = strength;
	}
	
	public Integer getStrength() {
		return strength;
	}
	
	public SNPlayerStatistic setStrength(Integer strength) {
		this.strength = strength;
		
		return this;
	}
}
