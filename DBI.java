/**
 * Database Interface - connects to the a database on nightmare
 * **/



import java.sql.*;

public class DBI {

	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://137.158.160.145:3306/mtsban004";
	private final String uname = "mtsban004";
	private final String password = "naemaeng";
	
	public String getData(String usr) {
		Connection conn = null;
		Statement stat = null;
		String fin = "";
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, uname, password);
			stat = conn.createStatement();
			String sql = "select userID, location, room, deviceID from doors where userID like '" 
						+ usr + "'";
			ResultSet rs = stat.executeQuery(sql);
			while(rs.next()) {
				fin = fin + rs.getString("userID") + "," + rs.getString("location") + "," +
						rs.getString("room") + "," + rs.getString("deviceID") + ";;";
			}
			rs.close();
			stat.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				stat.close();
			} catch (SQLException se) {
			} try {
				if(conn!=null) 
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return fin;
	}
	
	public String authenticate(String user, String pwd) {
		Connection conn = null;
		Statement stat = null;
		String fin = "";
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, uname, password);
			stat = conn.createStatement();
			String sql = "select password from door_users where userID like '" 
						+ user +"'";
			ResultSet rs = stat.executeQuery(sql);
			while(rs.next()) {
				fin = rs.getString("password");
			}
			if(fin.isEmpty()) return "user does not exist";
			rs.close();
			stat.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				stat.close();
			} catch (SQLException se) {
			} try {
				if(conn!=null) 
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		if(BCrypt.checkpw(pwd, fin)) {
			return "success";
		} 
		return "failed to login";
	}
	
	
	public void registerUser(String usr, String pwd) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, uname, password);
			pwd = BCrypt.hashpw(pwd, BCrypt.gensalt(12));
			String sql = "insert into door_users (userID, password) values (?,?)";
			stat = conn.prepareStatement(sql);
			stat.setString(1, usr);
			stat.setString(2, pwd);
			stat.executeUpdate();
			stat.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				stat.close();
			} catch (SQLException se) {
			} try {
				if(conn!=null) 
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public void appendRow(String usr, String loc, String room, String dev) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, uname, password);
			String sql = "insert into doors (userID, location, room, deviceID) values (?,?,?,?)";
			stat = conn.prepareStatement(sql);
			stat.setString(1, usr);
			stat.setString(2, loc);
			stat.setString(3, room);
			stat.setString(4, dev);
			stat.executeUpdate();
			stat.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				stat.close();
			} catch (SQLException se) {
			} try {
				if(conn!=null) 
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
}