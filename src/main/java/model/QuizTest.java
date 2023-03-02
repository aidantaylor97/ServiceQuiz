package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QuizTest {

	@Test
	void testQuiz() {
		Quiz quiz = new Quiz(1);
		String a = "aidantaylor97";
		String b = "Numbers";
		String c = "2023-02-11 11:37:28";
		
		quiz.loadQuestions();
		assertEquals(a, quiz.getOwner());
		assertEquals(b, quiz.getQuizName());
		assertEquals(c, quiz.getCreationDate());
	}
	
	void testAnswers()
	{
		Quiz quiz = new Quiz(1);
	}
	
	void testLeaderboard()
	{
		Quiz quiz = new Quiz(1);
	}
	
	
}
