package teamuno_CSCI201L_GroupProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBC_Core {
	public static boolean verifySignIn(String username, String password) throws ClassNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Users?user=root&password=root");
			ps = conn.prepareStatement("SELECT * FROM UserInfo WHERE username=? AND pass=?");
			ps.setString(1, username); // set first variable in prepared statement
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (!rs.next()) {
				System.out.println("not found");
				return false;
			}
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
		return true;
	}
}
