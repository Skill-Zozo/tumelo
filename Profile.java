package meloApp;

import java.util.LinkedList;

public class Profile {
	private String name;
	private LinkedList<Location> locations;
	private LinkedList<Device> devices;
	
	public Profile(String name) {
		this.setName(name);
		locations = new LinkedList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addLocation(Location loc) {
		locations.add(loc);
	}
	
	public void removeLocation(Location loc) {
		if(locations.contains(loc)) 
			locations.remove(loc);
	}
	
	public LinkedList<Device> getDevices() {
		/**
		 * read from db
		 * **/
		return devices;
	}
}