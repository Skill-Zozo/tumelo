package meloApp;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class DataManipulationTest {

	static Device dev;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dev = new Device("testing device", new Room("lab", new Location(new Profile("dev1"), "campus")));
	}

	@Test
	public void addDeviceToNoLogin() {
		Connector conn = new Connector();
		String status = conn.addDeviceTo("dev1", "work1", dev);
		assertEquals(true, status.contains("failed"));
	}
	
	@Test
	public void addDeviceToIncorrectPassword() {
		Connector conn = new Connector();
		String status = conn.login("dev1", "wor5k");
		assertEquals(true, status.contains("failed"));
		status = conn.addDeviceTo("dev1", "wo5rk", dev);
		assertEquals(true, status.contains("failed"));
	}
	
	
	@Test
	public void pullDataAfterNoLogin() {
		Connector conn = new Connector();
		Profile user = conn.getUser("dev1", "work");
		assertNull(user);
	}
	
	@Test
	public void pullDataAfterUnsuccesfulLogin() {
		Connector conn = new Connector();
		String status = conn.login("dev1", "wo5rk");
		assertEquals(true, status.contains("failed"));
		Profile user = conn.getUser("dev1", "work");
		assertNull(user);
	}
	
	@Test
	public void addDeviceToKnownuser() {
		Connector conn = new Connector();
		String status = conn.login("dev1", "work");
		assertEquals(true, status.contains("success"));
		status = conn.addDeviceTo("dev1", "work", dev);
		assertEquals(true, status.contains("success"));
	}
	
	@Test
	public void pullDataAfterLogin() {
		Connector conn = new Connector();
		String status = conn.login("dev1", "work");
		assertEquals(true, status.contains("success"));
		Profile user = conn.getUser("dev1", "work");
		assertEquals(true, user.hasLocation("Work"));
		Location loc = user.getLocation("work");
		assertEquals(true, loc.hasRoom("office"));
		Room room = loc.getRoom("office");
		assertEquals(true, room.hasDevice("lamp"));
		assertEquals(true, user.hasLocation("campus"));
		loc = user.getLocation("campus");
		assertEquals(true, loc.hasRoom("lab"));
		room = loc.getRoom("lab");
		assertEquals(true, room.hasDevice("testing device"));
	}

}
