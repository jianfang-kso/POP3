/**
* @author William Heng
* @since Dec 7, 2013
*/

import java.sql.*;
import java.util.ArrayList;


/*
 * 
 * Database is a singleton class. Access is shared among all classes and threads
 * which require data from the database.
 * 
 * */

public class Database implements Model {
	
	// Only a single instance of Database class is allowed to exist
	private static Database instance = new Database();
	
	// Edit the settings below to connect to the MySQL server
	private static final String MYSQL_HOST = "localhost";
	private static final String MYSQL_PORT = "3306";
	private static final String MYSQL_TABLE = "g52apr";
	private static final String MYSQL_USERNAME = "temporary";
	private static final String MYSQL_PASSWORD = "";

	// URL to connect
	private static final String MYSQL_URL = "jdbc:mysql://" + Database.MYSQL_HOST + ":" + Database.MYSQL_PORT + "/" + Database.MYSQL_TABLE;
	
	private Connection conn = null;
	
	private Database() {
		// Try to load JDBC and connect to MySQL server
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection(Database.MYSQL_URL, Database.MYSQL_USERNAME, Database.MYSQL_PASSWORD);
		} catch (ClassNotFoundException e) {
			System.out.println("Cannot load JDBC driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Cannot retrieve connection from MySQL server!");
			e.printStackTrace();
		}
	}
	
	// Singleton class for database access
	public static Database getInstance() {
		return Database.instance;
	}

	// Check if given username is a valid user
	@Override
	public boolean isUser(String username) {
		boolean found = false;
		try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(vchUsername) FROM m_MailDrop WHERE vchUsername = '" + username + "'");
			while (rs.next()) {
				found = rs.getInt(1) != 0;
				break;
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Failed to query username!");
		}

		return found;
	}
	
	// Get the user ID for a username
	@Override
	public int getUserID(String username) {
		int id = 0;
		try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT iMaildropID FROM m_MailDrop WHERE vchUsername = '" + username + "'");
			while (rs.next()) {
				id = rs.getInt(1);
				break;
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Failed to query username!");
		}
		return id;
	}

	@Override
	public String getPassword(String username) {
		String password = null;
		try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM m_MailDrop WHERE vchUsername = '" + username + "' AND tiLocked = '0'");
			while (rs.next()) {
				password = rs.getString("vchPassword");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Failed to get password for username " + username);
		}
		
		return password;
	}

	// Get a list of mails from the database for a particular user
	@Override
	public Mail[] getMail(String username) {
		ArrayList<Mail> mails = new ArrayList<>();
		try {
			int id = this.getUserID(username);
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM m_Mail WHERE iMailDropID = '" + id + "'");
			while (rs.next()) {
				mails.add(new Mail(
						rs.getString("vchUIDL"),
						rs.getInt("iMailID"),
						rs.getString("txMailContent")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Unable to retrieve mail for username " + username);
		}
		return mails.toArray(new Mail[0]);
	}

	// Delete mail in the database permanently
	@Override
	public synchronized void deleteMail(int mailID) {
		try {
			Statement stmt = this.conn.createStatement();
			stmt.executeUpdate("DELETE FROM m_Mail WHERE iMailID = '" + mailID + "'");
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Unable to delete mail with ID " + mailID);
		}
	}
	
	// Terminate the connection to MySQL server
	public void terminate() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot terminate connection to database.");
		}
	}
}
