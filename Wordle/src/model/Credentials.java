package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * AUTHOR: Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * FILE: Credentials.java 
 * ASSIGNMENT: Final Project - Wordle
 * COURSE: CSC 335 Spring 2023
 * PURPOSE: This class creates a Credentials object that contains user password,
 * user last login date and number of games played by user.
 */
public class Credentials implements Serializable {
	private final static int MAX_GUESSES = 6;
	
	/**
	 * User password
	 */
	private String password;
	
	/**
	 * Last login date for the user
	 */
	private LocalDate lastLoginDate;
	
	/**
	 * Number of games played by user
	 */
	private int numGamesPlayed;
	
	/**
	 * Number of games won by user
	 */
	private int numGamesWon;
	
	/**
	 * Current winning streak for the user
	 */
	private int currentWinningStreak;
	
	/**
	 * Max winning streak for the user
	 */
	private int maxWinningStreak;
	
	/**
	 * Guess distribution for user wins
	 */
	private int guessDistribution[] = new int[MAX_GUESSES];

	/**
	 * Update statistics for credentials
	 * 
	 * @param isGameWon, boolean true when game is won, false otherwise
	 * @param guess, integer for number of guesses made
	 */
	public void updateStats(boolean isGameWon, int guess) {
		numGamesPlayed++;
		if(isGameWon) {
			numGamesWon++;
			currentWinningStreak++;
			if(currentWinningStreak > maxWinningStreak) {
				maxWinningStreak = currentWinningStreak;
			}
			// update guess distribution only if player wins
			guessDistribution[guess]++;
		} else {
			currentWinningStreak = 0;
		}
		System.out.print(this);
	}
	
	/**
	 * Get string representation of the Credentials object
	 * 
	 * @return str, string representation of the object
	 */
	public String toString() {
		String str = "";
		str += "\nLast login date:\t" + getLastLoginDate();
		str += "\nNumber of games played:\t" + getNumGamesPlayed();
		str += "\nNumber of games won:\t" + getNumGamesWon();
		str += "\nCurrent Winning Streak:\t" + getCurrentWinningStreak();
		str += "\nMax Winning Streak:\t" + getMaxWinningStreak();
		str += "\nGuess Distribution:\t";
		for(int i = 0; i < getGuessDistribution().length; i++) {
			str += "\n\tGuess #" + (i + 1) + ":" + getGuessDistribution()[i];
		}
		return str;
	}
	
	/**
	 * Get guess distribution
	 * 
	 * @return guessDistribution, integer array containing guess distribution
	 */
	public int[] getGuessDistribution() {
		return guessDistribution;
	}
	
	/**
	 * Get max winning streak
	 * 
	 * @return maxWinningStreak, integer for max winning streak 
	 */
	public int getMaxWinningStreak() {
		return maxWinningStreak;
	}
	
	/**
	 * Get current winning streak
	 * 
	 * @return currentWinningStreak, integer for current winning streak
	 */
	public int getCurrentWinningStreak() {
		return currentWinningStreak;
	}
	
	/**
	 * Get number of games won
	 * 
	 * @return numGamesWon, Integer number of games won
	 */ 
	public int getNumGamesWon() {
		return numGamesWon;
	}
	
	/**
	 * Get user password
	 * 
	 * @return String, the user password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Get user last login date
	 * 
	 * @return lastLoginDate, the last login date for the user
	 */
	public LocalDate getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * Get number of games played by the user
	 * 
	 * @return numGamesPlayed, integer number of games last played by user
	 */
	public int getNumGamesPlayed() {
		return numGamesPlayed;
	}

	/**
	 * Set user password
	 * 
	 * @param password, the String password for the user account
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Set last login date for the user account
	 * 
	 * @param lastLoginDate, last login date for the user
	 */
	public void setLastLoginDate(LocalDate lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	/**
	 * Set number of games last played by user
	 * 
	 * @param numGamesPlayed, the integer number of games last played
	 */
	public void setNumGamesPlayed(int numGamesPlayed) {
		this.numGamesPlayed = numGamesPlayed;
	}
}