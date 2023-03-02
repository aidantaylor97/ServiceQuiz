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
import java.util.ArrayList;
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

public class Quiz {
	
	//Quiz
	private int quizID;
	private String owner;
	private byte image[];
	private String quizName;
	private String quizDesc;
	private String quizDiff;
	private int qAmount;
	private String creationDate;
	//Establish a Hashmap of questions
	private Map questions = null;
	//custom class leaderboard
	private ArrayList<Score> scores;
	
	//SQL
    private ResultSet rs = null;
    private PreparedStatement pstmtLogin = null;
    private PreparedStatement pstmtRegister = null;
    private Statement st = null;
    private Connection connection=null;

	
    //Initialise the class with quizID
	public Quiz(int quizID)
	{
		this.quizID = quizID;
		//Call a method to get the quiz owner username
		establishQuiz();
		
		
	}

	private void establishQuiz() {
		try {
			//Acquire DB connection
	        connection = DBUtil.getConnection();
	      //Create query string
	        String query = ("select u.username, q.quizname, q.Description, q.Difficulty, q.quizImage, q.dateCreated, q.QuestionAmount from users u right join quiz q on u.id = q.createdBy where q.ID = " 
	        + this.quizID + ";");
	        st = connection.createStatement();
	        try 
	        {
	        	if (connection != null) 
	        	{               	
                	//Execute query
                	rs = st.executeQuery(query); 	
                	//Loop through returned rows
                	if(rs.next()) 
                	{
                		this.owner = rs.getString(1);
                		this.quizName = rs.getString(2);
                		this.quizDesc = rs.getString(3);
                		this.quizDiff = rs.getString(4);
                		//Return image as blob, and convert to byte array for class to store
                		Blob imageBlob = rs.getBlob(5);
                		//If an image was uploaded
                		if(imageBlob != null)
                		{
                			//Store blob image as encoded byte array
	                		this.image = imageBlob.getBytes(1, (int)imageBlob.length());
	                		Base64 image1 = new Base64();
	                		this.image = image1.encode(image);
                		}
                		else
                		{
                			//Store buffered img from project, convert to encoded byte array
                			BufferedImage img = ImageIO.read(getClass().getResource("/images/no_image.png"));
                			byte[] bytes = toByteArray(img, "png");
                			this.image = bytes;
                			Base64 image1 = new Base64();
	                		this.image = image1.encode(image);
                		}
                		this.creationDate = rs.getString(6);
                		this.qAmount = rs.getInt(7);
                		connection.close();
                		//establish scores for leaderboard
                		establishScores();
                		return;
                	}
                	//Update the lesson count        	
                	connection.close();
                	return;
                    
                }
			}
			catch(SQLException e) 
	        {
		
		        System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
		        return;
		    }    
		}
		catch(Exception e)
        {
	
	        System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
	        return;
        }  
	}
	
	//method will populate the scores array
	private void establishScores() {
		try {
			//Acquire DB connection
	        connection = DBUtil.getConnection();
	        //Create query string
	        String query = ("select u.username, s.score, s.anonymous, s.datecreated from score s" 
	        		+ " join users u on s.userID = u.Id where quizID = "+this.quizID+" Order by s.score DESC");
	        st = connection.createStatement();
	        try 
	        {
	        	if (connection != null) 
	        	{               	
	        		//establish scores ArrayList
	        		this.scores = new ArrayList<Score>();
                	//Execute query
                	rs = st.executeQuery(query); 	
                	//Loop through returned rows
                	while(rs.next()) 
                	{
                		//get the the values
                		String username = rs.getString(1);
                		int score = rs.getInt(2);
                		Boolean anon = rs.getBoolean(3);
                		String dateCreated = rs.getString(4);
                		// if user is anonymous
                		if(anon == true)
                		{
                			username = "anonymous";
                		}
                		//trim so its just the date
                		dateCreated = dateCreated.substring(0, 10);
                		//Set Score object
                		Score s = new Score(username, score, anon, dateCreated);
                		//add to the array
                		scores.add(s);	
                	}
                	//Update the lesson count        	
                	connection.close();
                	return;
                    
                }
			}
			catch(SQLException e) 
	        {
		
		        System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
		        return;
		    }    
		}
		catch(Exception e)
        {
	
	        System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
	        return;
        }
		
	}

	// convert BufferedImage to byte[], copied from online https://mkyong.com/java/how-to-convert-bufferedimage-to-byte-in-java/
    public static byte[] toByteArray(BufferedImage bi, String format)
        throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }
    
    //Load questions into the quiz
    public void loadQuestions() 
    {
		try
	    {
		    	Connection connection = DBUtil.getConnection();
		    	pstmtLogin = connection.prepareStatement("Select * from questions where quizID = " + quizID + ";");
		    	
		    	if (connection != null) {
		
		        	//Implemented using a ready made prepared Statement in class Constructor
		           // executes to add all answers for said question to a MAP             
		        	
					rs = pstmtLogin.executeQuery();
					this.questions = new HashMap<String, Question>();
						
					while (rs.next()) 
					{  
							// If there is a row in the ResultSet 
			                int id = rs.getInt(1); //returns the id of the matching credentials
			                String questionText = rs.getString(3);
			                String questionType = rs.getString(4);
			                //creates Answer Object using the ID
			                Question q = new Question(String.valueOf(id), questionText, questionType);
			                
			                //Adds Answer to the map
			                questions.put(String.valueOf(id),q);
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
    
    //submit quiz scores to the database, either anonymously or not
    public void submitScores(int score, String anon, int userId)
    {
    	try {
        	//Establish connection to the database
        	Connection connection = DBUtil.getConnection();
        	//prepare a statement
        	pstmtRegister = connection.prepareStatement("INSERT INTO Score (UserId, QuizId, Score, Anonymous) VALUES (?,?,?,?);");

            if (connection != null) {         
            	//Sets the values of the insert
    			pstmtRegister.setInt(1, userId);
    			pstmtRegister.setInt(2, this.quizID);
    			pstmtRegister.setInt(3, score);
    			//if anonymously submitted
    			if(anon.equals("anon"))
    			{
    				pstmtRegister.setBoolean(4, true);
    			}
    			else
    			{
    				pstmtRegister.setBoolean(4, false);
    			}
    			// int declared for execute update return
    			int row = pstmtRegister.executeUpdate();
    			// Will return the id of the inserted quiz
    			if (row==1) {  // If there is a row in the ResultSet 
    				//succesful
    				establishScores();
                    connection.close();
                    return;                    
                } else {	      
                	//Failure to get quizID
                    connection.close();
                    return;
                }
            }            
           
        } catch(Exception e) {
                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return;
        }
		return;
    }

	public String getOwner() {
		return owner;
	}
	
	public int getQuizID() {
		return this.quizID;
	}

	public String getImage() {
		return new String(image);
	}

	public String getQuizName() {
		return quizName;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public int getqAmount() {
		return qAmount;
	}

	public String getQuizDiff() {
		return quizDiff;
	}

	public String getQuizDesc() {
		return quizDesc;
	}
	
	
	public Map getQuestions() {
		return questions;
	}
	
	public ArrayList<Score> getScores(){
		return scores;
		
	}

}
