package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.tomcat.util.codec.binary.Base64;

import util.DBUtil;

//Class will build a map hosting all Quiz entries on the database based off the users search
public class QuizSearcher {
	
	//create hashMap for returned list of quiz's
	private Map quizSearch = null;
	
	//SQL
    private ResultSet rs = null;
    private PreparedStatement pstmtLogin = null;
    private PreparedStatement pstmtRegister = null;
    private Statement st = null;
    private Connection connection=null;

    //constructor for no search string
    public QuizSearcher()
    {
    	getMostPopular();
    }
    
    public QuizSearcher(String search)
    {
    try
    {
	    	Connection connection = DBUtil.getConnection();
	    	pstmtLogin = connection.prepareStatement("Select * from Quiz where quizname LIKE '" + "%" + search + "%" + "';");
	    	
	    	if (connection != null) {
	
	        	//Implemented using a ready made prepared Statement in class Constructor
	           // If the search string matches a string in the quizzes title, a quiz object will be created and added to a map           
	        	
				rs = pstmtLogin.executeQuery();
				this.quizSearch = new HashMap<String, Quiz>();
					
				while (rs.next()) 
				{  
						// If there is a row in the ResultSet 
		                int id = rs.getInt(1); //returns the id of the matching credentials
		                //creates Quiz Object using the ID
		                Quiz q = new Quiz(rs.getInt(1));
		                
		                //Adds Quiz to the Array
		                quizSearch.put(String.valueOf(id),q);
				}
				connection.close();
				return;
	        }
	        else {
	            return; // query failed
	        }
	    } catch(Exception e) {
	                
	        System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
	        return;
	    }
    }
    
    //Search for most popular Quizzes 
    public void getMostPopular()
    {
    	try
        {
    	    	Connection connection = DBUtil.getConnection();
    	    	//Should return the most popular quizzes based on leaderboard population
    	    	pstmtLogin = connection.prepareStatement("Select q.* from Quiz q\r\n"
    	    			+ "join (select quizid, COUNT(*) as cnt\r\n"
    	    			+ "	from score\r\n"
    	    			+ "    group by quizid\r\n"
    	    			+ "    ) s on (q.id = s.quizid)\r\n"
    	    			+ "order by s.cnt desc\r\n"
    	    			+ "limit 3");
    	    	
    	    	if (connection != null) {
    	
    	        	//Implemented using a ready made prepared Statement in class Constructor
    	           // If the search string matches a string in the quizzes title, a quiz object will be created and added to a map           
    	        	
    				rs = pstmtLogin.executeQuery();
    				this.quizSearch = new HashMap<String, Quiz>();
    					
    				while (rs.next()) 
    				{  
    						// If there is a row in the ResultSet 
    		                int id = rs.getInt(1); //returns the id of the matching credentials
    		                //creates Quiz Object using the ID
    		                Quiz q = new Quiz(rs.getInt(1));
    		                
    		                //Adds Quiz to the Array
    		                quizSearch.put(String.valueOf(id),q);
    				}
    				connection.close();
    				return;
    	        }
    	        else {
    	            return; // query failed
    	        }
    	    } catch(Exception e) {
    	                
    	        System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
    	        return;
    	    }
        }
    
    
    
    public Quiz getQuiz(String quizID) {
        
        return (Quiz)this.quizSearch.get(quizID);
    }

    public Map getquizSearch() {
        
        return this.quizSearch;
        
    }
    
    
}
	


