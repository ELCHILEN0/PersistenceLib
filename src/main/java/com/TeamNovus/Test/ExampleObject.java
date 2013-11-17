package com.TeamNovus.Test;

import com.TeamNovus.Persistence.Annotations.Table;
import com.TeamNovus.Persistence.Annotations.Columns.Column;
import com.TeamNovus.Persistence.Annotations.Columns.Id;

@Table(name = "example")
public class ExampleObject {
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "health")
	private int health;
	
	@Column(name = "hunger")
	private int hunger;
	
	public int getId() {
		return id;
	}
	
	public int getHealth() {
		return health;
	}
	
	public ExampleObject setHealth(int health) {
		this.health = health;
		
		return this;
	}
	
	public int getHunger() {
		return hunger;
	}
	
	public ExampleObject setHunger(int hunger) {
		this.hunger = hunger;
		
		return this;
	}

}
