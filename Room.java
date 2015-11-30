package meloApp;

import java.util.LinkedList;

public class Room {
	private Location place;
	private String roomName;
	private LinkedList<Device> devices;
	
	public Room (String room, Location place) {
		setRoomName(room);
		this.setPlace(place);
		devices = new LinkedList<>();
	}

	public String getRoomName() {
		return roomName;
	}

	private void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Location getPlace() {
		return place;
	}

	private void setPlace(Location place) {
		this.place = place;
	}
	
	public void addDevice(Device d) {
		devices.add(d);
	}
	
	public void removeDevice(Device d) {
		if(devices.contains(d))
			devices.remove(d);
	}
	
	public LinkedList<Device> getDevices() {
		return this.devices;
	}
}