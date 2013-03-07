package com.TeamNovus.Persist.UnitTest;

import java.util.ArrayList;
import java.util.List;

import com.TeamNovus.Persist.Database;
import com.TeamNovus.Persist.Databases.MySQLConfiguration;
import com.TeamNovus.Persist.Databases.MySQLDatabase;
import com.TeamNovus.Persist.Exceptions.TableRegistrationException;

public class Test {
	public static Database db;
	public static List<SNPlayer> players = new ArrayList<SNPlayer>();
	
	public static SNPlayer getPlayer(String name) {
		for(SNPlayer player : players) {
			if(player.getName().equals(name)) {
				return player;
			}
		}
		
		players.add(new SNPlayer(name));
		
		return getPlayer(name);
	}
	
	public static void main(String[] args) {
		db = new MySQLDatabase(new MySQLConfiguration());
		
		try {			
			db.registerTable(SNPlayer.class);
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		}
		
		db.connect();
				
		// Load:
		players = db.findAll(SNPlayer.class);	

		
		
		// Save:
		db.saveAll(players);

	}

}
