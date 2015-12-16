package meloApp;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

public class LoginTest {
	static Connector conn;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		conn = new Connector();
	}

	@Test
	public void loginExistingUser() {
		String status = conn.login("dev1", "work");
		assertEquals(true, !status.contains("failed"));
	}

	@Test
	public void loginUnknownUser() {
		String status = conn.login("test1", "test1");
		assertEquals(true, status.contains("failed"));
	}
	
	@Test
	public void signUpNewUser() {
		Random rand = new Random();
		String name = String.valueOf(rand.nextInt());
		String suffix = String.valueOf(rand.nextDouble());
		String username = name + rand.nextFloat() + suffix;
		String password = "mash";
		String status = conn.signup(username, password);
		System.out.println();
		assertEquals(true, !status.contains("failed"));
	}
	
	@Test
	public void signUpExistingUser() {
		String status = conn.signup("dev1", "work");
		assertEquals(true, status.contains("failed"));
	}
	
	@Test
	public void loginInCorrectPassword() {
		String status = conn.login("dev1", "WORK");
		assertEquals(true, status.contains("failed"));
	}
}
