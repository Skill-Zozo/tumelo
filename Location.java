package meloApp;

import java.util.LinkedList;


public class Location {
	private String nameOfPlace;
	private LinkedList<Room> rooms;
	
	public Location(String name) {
		setNameOfPlace(name);
		rooms = new LinkedList<>();
	}

	public String getNameOfPlace() {
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
}
