package com.TeamNovus.Persist.UnitTest;

import com.TeamNovus.Persist.Annotations.Table;
import com.TeamNovus.Persist.Annotations.Columns.Column;
import com.TeamNovus.Persist.Annotations.Columns.ForeignKey;
import com.TeamNovus.Persist.Annotations.Columns.Id;

@Table(name = "cooldowns")
public class SNCooldown {
	@Id
	@Column(name = "id")
	private Integer id;
	
	@ForeignKey
	@Column(name = "player_id_fk")
	private Integer playerId;
	
	@Column(name = "spellz")
	private String spell;
	
	public SNCooldown() {
		// TODO Auto-generated constructor stub
	}

	public SNCooldown(String spell) {
		this.spell = spell;
	}
	
	public String getSpell() {
		return spell;
	}
	
	public SNCooldown setSpell(String s) {
		this.spell = s;
		
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((playerId == null) ? 0 : playerId.hashCode());
		result = prime * result + ((spell == null) ? 0 : spell.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SNCooldown other = (SNCooldown) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (playerId == null) {
			if (other.playerId != null) {
				return false;
			}
		} else if (!playerId.equals(other.playerId)) {
			return false;
		}
		if (spell == null) {
			if (other.spell != null) {
				return false;
			}
		} else if (!spell.equals(other.spell)) {
			return false;
		}
		return true;
	}
	
	
	
}
