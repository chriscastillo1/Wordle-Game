package tests;

/***
 * AUTHOR: Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta 
 */

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import model.Wordle;
import model.WordleAccount;

public class WordleTest {
	
	Wordle wordleGame = new Wordle();
	String guess1 = "crane";
	String guess2 = "happy";
	String guess3 = "hello";
	String guess4 = "apple";
	String guess5 = "whelp";
	String guess6 = "while";
	String guess7 = "treat";
	WordleAccount account = new WordleAccount("michael", "password", true);

	
	@Test
	void test() {
		//guess1
		assertFalse(wordleGame.checkGameWon());
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess1);
		}
		//guess2
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess2);
		}
		//guess3
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess3);
		}
		//guess4
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess4);
		}
		//guess5
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess5);
		}
		//guess6
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess6);
		}
		assertFalse(wordleGame.checkGameRunning());
		//guess7 (game should be over by now so this shouldn't run)
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess7);
		}
		
		wordleGame.setLoggedIn(account);
		wordleGame.getLoggedIn();
		
		/***
		 * Set 2, this is a case where we get the correct word before guesses are up
		 */
		wordleGame = new Wordle();
		//guess1
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess1);
		}
		//guess2
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess2);
		}
		//guess3
		assertTrue(wordleGame.checkGameRunning());
		if (wordleGame.checkGameRunning()) {
			wordleGame.makeGuess(guess3);
		}
		//word should have been guessed correctly in guess 3
		assertTrue(wordleGame.checkGameRunning());
	}
}
