package com.TeamNovus.Persistence.Databases;

import java.util.HashMap;

public abstract class Configuration {
	private String name;
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	public Configuration(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Configuration set(HashMap<String, String> properties) {
		this.properties = properties;
		
		return this;
	}
	
	public HashMap<String, String> getProperties() {
		return properties;
	}
	
	public Configuration set(String property, String value) {
		properties.put(property, value);
		
		return this;
	}
	
	public String get(String property) {
		return properties.get(property);
	}
	
	public Configuration remove(String property) {
		properties.remove(property);
		
		return this;
	}
	
	public boolean has(String property) {
		return properties.containsKey(property);
	}
}
