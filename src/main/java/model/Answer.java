package model;


//A class to establish an answer object
public class Answer {
	
	//A string to hold the answer text, and then a boolean value to determine if corrrect
	public String answerString;
	public boolean isCorrect;
	public String answerID;
	
	public Answer(String answerID, String answer, boolean isCorrect)
	{
		this.answerID = answerID;
		this.answerString = answer;
		this.isCorrect = isCorrect;
	}
	
	//Getters
	public boolean getIsCorrect()
	{
		return this.isCorrect;
	}
	
	//Getters
	public String getAnswerString()
	{
		return this.answerString;
	}
	
	//Getters
	public String getAnswerID()
	{
		return this.answerID;
	}
	
}
