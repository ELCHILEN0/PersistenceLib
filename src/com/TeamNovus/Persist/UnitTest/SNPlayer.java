package com.TeamNovus.Persist.UnitTest;

import java.util.ArrayList;
import java.util.List;

import com.TeamNovus.Persist.Annotations.Table;
import com.TeamNovus.Persist.Annotations.Columns.Column;
import com.TeamNovus.Persist.Annotations.Columns.Id;
import com.TeamNovus.Persist.Annotations.Relationships.OneToMany;
import com.TeamNovus.Persist.Annotations.Relationships.OneToOne;

@Table(name = "players")
public class SNPlayer {
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@OneToOne
	private SNPlayerStatistic statistic = new SNPlayerStatistic(1);
	
	@OneToMany
	private List<SNCooldown> cooldowns = new ArrayList<SNCooldown>();
	
	public SNPlayer() {
		// TODO Auto-generated constructor stub
	}
	
	public SNPlayer(String name) {
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public SNPlayerStatistic getStatistic() {
		return statistic;
	}
	
	public List<SNCooldown> getCooldowns() {
		return cooldowns;
	}
	
	public void setCooldowns(List<SNCooldown> cooldowns) {
		this.cooldowns = cooldowns;
	}
	
	public void addCooldown(SNCooldown cooldown) {
		cooldowns.add(cooldown);
	}
}
