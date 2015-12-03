package meloApp;

import java.util.LinkedList;


public class Location {
	private String nameOfPlace;
	private LinkedList<Room> rooms;
	private Profile owner;
	
	public Location(Profile owner, String name) {
		this.setOwner(owner);
		setNameOfPlace(name);
		rooms = new LinkedList<>();
	}

	public String getName() {
		return nameOfPlace;
	}

	private void setNameOfPlace(String nameOfPlace) {
		this.nameOfPlace = nameOfPlace;
	}
	
	public void addRoom(Room room) {
		rooms.add(room);
	}
	
	public void removeRoom(Room room) {
		if(rooms.contains(room))
			rooms.remove(room);
	}
	
	public LinkedList<Room> getRooms() {
		return this.rooms;
	}

	public Profile getOwner() {
		return owner;
	}

	public void setOwner(Profile owner) {
		this.owner = owner;
	}
}
