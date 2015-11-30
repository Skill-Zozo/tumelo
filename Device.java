package meloApp;

public class Device {
	private boolean on;
	private String nameofDevice;
	private Room parentRoom;
	
	public Device(String name,Room room ,boolean state) {
		setNameofDevice(name);
		setParentRoom(room);
		setOn(state);
	}

	public String getNameofDevice() {
		return nameofDevice;
	}

	private void setNameofDevice(String nameofDevice) {
		this.nameofDevice = nameofDevice;
	}

	public Room getParentRoom() {
		return parentRoom;
	}

	private void setParentRoom(Room parentRoom) {
		this.parentRoom = parentRoom;
	}

	public boolean isOn() {
		return on;
	}

	private void setOn(boolean state) {
		this.on = state;
	}
}
