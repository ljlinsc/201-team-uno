package teamuno_CSCI201L_GroupProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBC_Core {
	public static User verifySignIn(String username, String password) throws ClassNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Users?user=root&password=root&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps = conn.prepareStatement("SELECT * FROM UserInfo WHERE username=? AND password=?");
			ps.setString(1, username); // set first variable in prepared statement
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (!rs.next()) {
				System.out.println("not found");
				return null;
			}
			user = new User(rs.getString("username"), rs.getString("nickname"), true);
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return user;
	}
	
	public static User createUser(String username, String password, String nickname) throws ClassNotFoundException {
		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		User user = null;
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Users?user=root&password=ZhuanX13&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
			ps1 = conn.prepareStatement("SELECT * FROM UserInfo WHERE username=?");
			ps1.setString(1, username);
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				return null;
			} 
			user = new User(username, nickname, true);
			ps2 = conn.prepareStatement("INSERT INTO UserInfo (username, password, nickname) VALUES (?, ?, ?)");
			ps2.setString(1, username); 
			ps2.setString(2, password);
			ps2.setString(3, nickname);
			ps2.execute();
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (ps1 != null) {
					ps1.close();
				}
				if (ps2 != null) {
					ps2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return user;
	}
}
