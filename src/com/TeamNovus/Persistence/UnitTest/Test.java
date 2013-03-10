package com.TeamNovus.Persistence.UnitTest;

import java.util.ArrayList;
import java.util.List;

import com.TeamNovus.Persistence.Databases.Database;
import com.TeamNovus.Persistence.Databases.SQLite.SQLiteConfiguration;
import com.TeamNovus.Persistence.Databases.SQLite.SQLiteDatabase;
import com.TeamNovus.Persistence.Exceptions.TableRegistrationException;

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
		db = new SQLiteDatabase(new SQLiteConfiguration());
		
		try {			
			db.registerTable(SNPlayer.class);
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		}
		
		db.createStructure(SNPlayer.class);
		
		db.connect();
				
		// Load:
		players = db.findAll(SNPlayer.class);	

		getPlayer("ELCHILEN0");
		
		// Save:
		db.saveAll(players);

	}

}
