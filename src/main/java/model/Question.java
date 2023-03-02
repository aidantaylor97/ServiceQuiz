package model;

import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Set;

import util.DBUtil;

//Class Question will hold a question with answers
public class Question {
	
	//Establish a Hashmap of answers
	private Map answers = null;

	//Other important question properties
	public String questionText;
	public String qType;
	public String questionID;
	
	//SQL
    private ResultSet rs = null;
    private PreparedStatement pstmtLogin = null;
    private PreparedStatement pstmtRegister = null;
    private Statement st = null;
    private Connection connection=null;
	
	public Question(String questionID, String questionText, String qType)
	{
		this.questionID = questionID;
		this.questionText = questionText;
		this.qType = qType;
		
		loadAnswers(questionID);
	}

	private void loadAnswers(String questionID) {
		try
	    {
		    	Connection connection = DBUtil.getConnection();
		    	pstmtLogin = connection.prepareStatement("Select * from answers where questionId = " + questionID + ";");
		    	
		    	if (connection != null) {
		
		        	//Implemented using a ready made prepared Statement in class Constructor
		           // executes to add all answers for said question to a MAP             
		        	
					rs = pstmtLogin.executeQuery();
					this.answers = new HashMap<String, Answer>();
						
					while (rs.next()) 
					{  
							// If there is a row in the ResultSet 
			                int id = rs.getInt(1); //returns the id of the matching credentials
			                String answerText = rs.getString(3);
			                boolean isCorrect = rs.getBoolean(4);
			                //creates Answer Object using the ID
			                Answer a = new Answer(String.valueOf(id), answerText, isCorrect);
			                
			                //Adds Answer to the map
			                answers.put(String.valueOf(id),a);
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
	
	//getters
	public Map getAnswers() 
	{
		return this.answers;
	}

	//getters
	public String getQuestionText() 
	{
		return this.questionText;
	}
	
	public String getQType()
	{
		return this.qType;
	}
	
	public String getQuestionID()
	{
		return this.questionID;
	}
	
}
