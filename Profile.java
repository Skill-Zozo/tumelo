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
	
	public LinkedList<Location> getLocations() {
		return this.locations;
	}
	
	public boolean hasLocations() {
		return !locations.isEmpty();
	}
	
	public LinkedList<Device> getDevices() {
		for(int i = 0; i < locations.size(); i++) {
			Location loc = locations.get(i);
			for(int j = 0; j < loc.getRooms().size(); j++) {
				Room room = loc.getRooms().get(j);
				devices.addAll(room.getDevices());
			}
		}
		return devices;
	}
}
