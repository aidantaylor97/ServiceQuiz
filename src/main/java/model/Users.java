package model;

import java.sql.PreparedStatement;
import java.sql.Connection;

import java.sql.ResultSet;


import util.DBUtil;


public class Users {
  
    private ResultSet rs = null;
    //2 prepared statements prepared for Login and Registration
    private PreparedStatement pstmtLogin = null;
    private PreparedStatement pstmtRegister = null;
    private Connection connection=null;

   
    public Users() {
    	
    }

    public int isValid(String name, String pwd) {
       
        try {
        	
        	Connection connection = DBUtil.getConnection();
        	pstmtLogin = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");

            if (connection != null) {

            	//Implemented using a ready made prepared Statement in class Constructor
               // If the username and password are correct, it should return the 'clientID' value from the database.             
            	
    			pstmtLogin.setString(1, name);
    			pstmtLogin.setString(2, pwd);
    			rs = pstmtLogin.executeQuery();
    				
    			if (rs.next()) {  // If there is a row in the ResultSet 
                   
                    int id = rs.getInt(1); //returns the id of the matching credentials
                    connection.close();
                    return id;
                    
                } else {	// Returns null
                    
                    connection.close();
                    return -1;
                }

            }
            else {
                return -1; // query failed
            }
        } catch(Exception e) {
                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return -1;
        }
        
        
    }
    
    // TODO  add a user with specified username and password
    public void addUser(String name, String pwd) {
    	
    	try {
        	
        	Connection connection = DBUtil.getConnection();
        	pstmtRegister = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?,?);");

            if (connection != null) {         
            	
    			pstmtRegister.setString(1, name);
    			pstmtRegister.setString(2, pwd);
    			// int declared for execture update return
    			int row = pstmtRegister.executeUpdate();
    				
    			if (row==1) {  // If there is a row in the ResultSet 

                    connection.close();
                    
                } else {	
                    
                    connection.close();
                }

            }            
           
        } catch(Exception e) {
                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
        }      
    }

    //Validates whether a username already exists
	public boolean validateUsername(String username) {
			try {
        	
        	Connection connection = DBUtil.getConnection();
        	pstmtRegister = connection.prepareStatement("SELECT * FROM users WHERE username = ?;");

            if (connection != null) {

            	//Implemented using a ready made prepared Statement in class Constructor
               // If registration is succesful, true is returned.            
            	
    			pstmtRegister.setString(1, username);
    			rs = pstmtRegister.executeQuery();

    			if (rs.next()) {  // If there is a row in the ResultSet 

                    connection.close();
                    //Returns true as a row was returned with a matching username
                    return true;                   		                   
                } else {	
                    
                    connection.close();
                    //returns false
                    return false;
                }
            }                       
        } catch(Exception e) {                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
        }      
		return false;
	}
}
