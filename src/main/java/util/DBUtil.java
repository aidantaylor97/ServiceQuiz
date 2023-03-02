package util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



/* Database is not the focus of this module。 A simple Database Util is created here to get the database connection. Please feel free to change if you want to adopt any database connection pattern or framework. */

public class DBUtil {
	
	
	
	public static Connection getConnection() throws
	SQLException, ClassNotFoundException{
		
		 String dbDriver = "com.mysql.cj.jdbc.Driver"; 
	        //String dbURL = "jdbc:mysql://localhost:3306/"; 
	        String dbURL = "jdbc:mysql://quizservice.mysql.database.azure.com:3306/";
	        // Database name to access 
	        String dbName = "quizservice"; 
	        String dbUsername = "admin123"; 
	        String dbPassword = "$h1t0nthevilla"; 
	  
	        
	        Class.forName(dbDriver); 
	        Connection con = DriverManager.getConnection(dbURL + dbName, 
	                                                     dbUsername,  
	                                                     dbPassword); 
	        return con;
	}
	
}
