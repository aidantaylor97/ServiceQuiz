package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QuizSearcherTest {

	@Test
	void testQuizSearcher() {
		//Debug and see if search works correctly
		QuizSearcher qSearch = new QuizSearcher("pingu");
	}
	
	@Test
	void testQuizSearcherMostPopular() {
		//Debug and see if search works correctly
		QuizSearcher qSearch = new QuizSearcher("numbers");
		//Debug and see most popular method works as intended
		qSearch.getMostPopular();
	}

}
