package com.railway.dbconnection;

//Import the sql package
import java.sql.*;

public class DBUtil {
	
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/railway_management_system";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "admin";
	
	public static Connection getDBConnection() throws ClassNotFoundException, SQLException {
		
		//Load & register the driver
		Class.forName(DRIVER);
		
		//Establish the connection
		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		
		return conn;
		
	}

}
