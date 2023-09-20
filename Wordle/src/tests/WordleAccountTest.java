/**
 * AUTHOR: Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * FILE: WordleAccountTest.java 
 * ASSIGNMENT: Final Project - Wordle
 * PURPOSE: This class implements the JUnit tests for the WordleAccount class.
 */
package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import model.WordleAccount;
import model.WordleLeaderBoard;

class WordleAccountTest {
	private final String USER1 = "JUnitTestAccount:Chris";
	private final String USER2 = "JUnitTestAccount:Anisha";
	private final String USER3 = "JUnitTestAccount:Michael";
	private final String USER4 = "JUnitTestAccount:Edan";

	@Test
	void test() {
		// create new WordleAccount (default constructor)
		WordleAccount wa = new WordleAccount();
		wa.deleteJUnitTestAccounts();

		// create multiple new WordleAccount(s)
		WordleAccount wa1 = new WordleAccount(USER1, "1", true);
		WordleAccount wa2 = new WordleAccount(USER2, "2", true);
		WordleAccount wa3 = new WordleAccount(USER3, "3", true);
		WordleAccount wa4 = new WordleAccount(USER4, "4", true);
		wa.resetStatsForJUnitTestUsers();
		wa1.setLastLogin();

		assertFalse(wa.getAccountAuthenticationStatus());
		assertNotEquals(wa3.toString(), null);

		wa1.updateStats(false, 0); // wa1: game lost
		assertEquals(wa1.getStats().getNumGamesPlayed(), 1);
		assertEquals(wa1.getStats().getNumGamesWon(), 0);

		wa2.updateStats(true, 3); // wa2: game won, guess 3
		assertEquals(wa2.getStats().getNumGamesPlayed(), 1);
		assertEquals(wa2.getStats().getNumGamesWon(), 1);

		wa3.updateStats(true, 3); // wa3: game won, guess 3
		assertEquals(wa3.getStats().getNumGamesPlayed(), 1);
		assertEquals(wa3.getStats().getNumGamesWon(), 1);

		wa4.updateStats(true, 3); // wa4: game won, guess 3
		assertEquals(wa4.getStats().getNumGamesPlayed(), 1);
		assertEquals(wa4.getStats().getNumGamesWon(), 1);
		assertEquals(wa4.getStats().getCurrentWinningStreak(), 1);
		assertEquals(wa4.getStats().getMaxWinningStreak(), 1);

		wa4.updateStats(true, 3); // wa4: game won, guess 3
		assertEquals(wa4.getStats().getNumGamesPlayed(), 2);
		assertEquals(wa4.getStats().getNumGamesWon(), 2);
		assertEquals(wa4.getStats().getCurrentWinningStreak(), 2);
		assertEquals(wa4.getStats().getMaxWinningStreak(), 2);

		wa4.updateStats(false, 0); // wa4: game lost
		assertEquals(wa4.getStats().getNumGamesPlayed(), 3);
		assertEquals(wa4.getStats().getNumGamesWon(), 2);
		assertEquals(wa4.getStats().getCurrentWinningStreak(), 0);
		assertEquals(wa4.getStats().getMaxWinningStreak(), 2);

		wa4.updateStats(false, 0); // wa4: game lost
		assertEquals(wa4.getStats().getNumGamesPlayed(), 4);
		assertEquals(wa4.getStats().getNumGamesWon(), 2);
		assertEquals(wa4.getStats().getCurrentWinningStreak(), 0);
		assertEquals(wa4.getStats().getMaxWinningStreak(), 2);
		
		wa4.updateStats(true, 3); // wa4: game won, guess 3
		assertEquals(wa4.getStats().getNumGamesPlayed(), 5);
		assertEquals(wa4.getStats().getNumGamesWon(), 3);
		assertEquals(wa4.getStats().getCurrentWinningStreak(), 1);
		assertEquals(wa4.getStats().getMaxWinningStreak(), 2);
		
		// verify user name for each of the new WordleAccount(s)
		assertEquals(wa1.getUserName(), USER1);
		assertEquals(wa2.getUserName(), USER2);
		assertEquals(wa3.getUserName(), USER3);
		assertEquals(wa4.getUserName(), USER4);

		WordleAccount wa5 = new WordleAccount(USER1, "1", false);
		WordleAccount wa6 = new WordleAccount(USER2, "x", false);
		WordleAccount wa7 = new WordleAccount("Unknown", "3", false);
		WordleAccount wa8 = new WordleAccount(USER4, "4", true);

		// Test coverage for exceptions during persistent read/write
		wa.readCredentialsFromFile("Unknown");
		wa.writeCredentialsToFile(null, null);
		wa.deleteJUnitTestAccounts();
		
		WordleLeaderBoard board = new WordleLeaderBoard();
		assertNotEquals(board.getLeaders().size(), 0);
		assertEquals(board.getStats(USER1),null);
		assertNotEquals(board.getFirstTen(), null);
	}

}
