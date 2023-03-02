package model;

import java.sql.PreparedStatement;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;

import java.sql.ResultSet;

import jakarta.servlet.http.Part;
import util.DBUtil;


public class QuizCreation {
  
    private ResultSet rs = null;
    //2 prepared statements prepared for Login and Registration
    private PreparedStatement pstmtLogin = null;
    private PreparedStatement pstmtRegister = null;
    private Connection connection=null;
    private String quizName;

   
    public QuizCreation(String quizName) {
    	this.quizName = quizName;
    }

    //Creates a quiz with a blob insert for quiz image
	public int createQuiz(int userId, String desc, String diff, int qAmount, InputStream image) {
       
        try {
        	//Establish connection to the database
        	Connection connection = DBUtil.getConnection();
        	//prepare a statement
        	pstmtRegister = connection.prepareStatement("INSERT INTO quiz (createdBy, quizName, Description, Difficulty, quizImage, QuestionAmount) VALUES (?,?,?,?,?,?);", pstmtRegister.RETURN_GENERATED_KEYS);;

            if (connection != null) {         
            	//Sets the values of the insert, for the file type, I have converted it to an input stream to insert as binary data to the database as longblob
    			pstmtRegister.setInt(1, userId);
    			pstmtRegister.setString(2, this.quizName);
    			pstmtRegister.setString(3, desc);
    			pstmtRegister.setString(4, diff);
    			pstmtRegister.setBlob(5, image);
    			pstmtRegister.setInt(6, qAmount);
    			// int declared for execuye update return
    			int row = pstmtRegister.executeUpdate();
    			// Will return the id of the inserted quiz
    			if (row==1) {  // If there is a row in the ResultSet 
    				rs = pstmtRegister.getGeneratedKeys();
    				int quizId = -1;
    				if (rs.next()) {
    					quizId = rs.getInt(1);
    				}    				
                    connection.close();
                    return quizId;
                    
                } else {	      
                	//Failure to get quizID
                    connection.close();
                    return -1;
                }

            }            
           
        } catch(Exception e) {
                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return -1;
        }
		return -1;              
    }
	
	 //Creates a quiz without a blob insert for quiz image
		public int createQuiz(int userId, String desc, String diff, int qAmount) {
	       
	        try {
	        	//Establish connection to the database
	        	Connection connection = DBUtil.getConnection();
	        	//prepare a statement
	        	pstmtRegister = connection.prepareStatement("INSERT INTO quiz (createdBy, quizName, Description, Difficulty, QuestionAmount) VALUES (?,?,?,?,?);", pstmtRegister.RETURN_GENERATED_KEYS);

	            if (connection != null) {         
	            	//Sets the values of the insert, for the file type, I have converted it to an input stream to insert as binary data to the database as longblob
	            	pstmtRegister.setInt(1, userId);
	    			pstmtRegister.setString(2, this.quizName);
	    			pstmtRegister.setString(3, desc);
	    			pstmtRegister.setString(4, diff);
	    			pstmtRegister.setInt(5, qAmount);
	    			// int declared for execute update return
	    			int row = pstmtRegister.executeUpdate();
	    			// Will return the id of the inserted quiz
	    			if (row==1) {  // If there is a row in the ResultSet 
	    				rs = pstmtRegister.getGeneratedKeys();
	    				int quizId = -1;
	    				if (rs.next()) {
	    					quizId = rs.getInt(1);
	    				}    				
	                    connection.close();
	                    return quizId;
	                    
	                } else {	      
	                	//Failure to get quizID
	                    connection.close();
	                    return -1;
	                }

	            }            
	           
	        } catch(Exception e) {
	                    
	            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
	            return -1;
	        }
			return -1;              
	    }
	
	public int QuestionCreate(int quizID, String questionText, String questionType) {
	       
        try {
        	//Establish connection to the database
        	Connection connection = DBUtil.getConnection();
        	//prepare a statement
        	pstmtRegister = connection.prepareStatement("INSERT INTO questions (QuizId, Question, QuestionType) VALUES (?,?,?);", pstmtRegister.RETURN_GENERATED_KEYS);;

            if (connection != null) {            	
            	//Sets the values of the insert
            	pstmtRegister.setInt(1, quizID);
    			pstmtRegister.setString(2, questionText);
    			pstmtRegister.setString(3, questionType);
    			// int declared for execuye update return
    			int row = pstmtRegister.executeUpdate();
    			// Will return the id of the inserted question
    			if (row==1) {  // If there is a row in the ResultSet 
    				rs = pstmtRegister.getGeneratedKeys();
    				int questionId = -1;
    				if (rs.next()) {
    					questionId = rs.getInt(1);
    				}    				
                    connection.close();
                    return questionId;
                    
                } else {	      
                	//Failure to get quizID
                    connection.close();
                    return -1;
                }

            }            
           
        } catch(Exception e) {
                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return -1;
        }
		return -1;              
    }
	
	
	
	
	
	 public String getName() {
	        return this.quizName;
	    }

	//Creates an answer for the question, this method is used for true/false questions
	public void AnswerCreate(int questionID, String tfValue) {
		try {
        	//Establish connection to the database
        	Connection connection = DBUtil.getConnection();
        	//prepare a statement
        	pstmtRegister = connection.prepareStatement("INSERT INTO answers (QuestionId, IsCorrect) VALUES (?,?);");;

            if (connection != null) {            	
            	//Sets the values of the insert
    			pstmtRegister.setInt(1, questionID);
    			if(tfValue.equals("True"))
    			{
    				pstmtRegister.setBoolean(2, true);
    			}
    			else if(tfValue.equals("False"))
    			{
    				pstmtRegister.setBoolean(2, false);
    			}
    			// int declared for execuye update return
    			int row = pstmtRegister.executeUpdate();
    			// Will return the id of the inserted question
    			if (row==1) {  // If there is a row in the ResultSet 			
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
	
	//Creates an answer for the question, this method is used for multiple choice questions
		public void AnswerCreate(int questionID, String answerValue, boolean correct) {
			try {
	        	//Establish connection to the database
	        	Connection connection = DBUtil.getConnection();
	        	//prepare a statement
	        	pstmtRegister = connection.prepareStatement("INSERT INTO answers (QuestionId, answer, IsCorrect) VALUES (?,?,?);");;

	            if (connection != null) {            	
	            	//Sets the values of the insert
	    			pstmtRegister.setInt(1, questionID);
	    			pstmtRegister.setString(2, answerValue);
	    			pstmtRegister.setBoolean(3, correct);
	    		
	    			// int declared for execuye update return
	    			int row = pstmtRegister.executeUpdate();
	    			// Will return the id of the inserted question
	    			if (row==1) {  // If there is a row in the ResultSet 			
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
}
    
