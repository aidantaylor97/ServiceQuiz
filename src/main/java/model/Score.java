package model;

public class Score {
	//score
	private int score;
	
	//associated user
	private String username;
	
	//if anonymously submitted
	private boolean anon;
	
	//the date issued
	private String date;
	
	//establish class with entered values
	public Score(String username, int score, boolean anon, String date)
	{
		this.score = score;
		this.username = username;
		this.anon = anon;
		this.date = date;
	}

	//Getters
	public String getDate() {
		return date;
	}

	public String getUsername() {
		return username;
	}

	public int getScore() {
		return score;
	}

	public boolean isAnon() {
		return anon;
	}
	
}
