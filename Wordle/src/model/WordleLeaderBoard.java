package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * AUTHOR: Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * FILE: WordleLeaderBoard.java 
 * ASSIGNMENT: Final Project - Wordle
 * PURPOSE: This class implements the WordleLeaderBoard. This class contains
 * methods for retrieving game leaders.
 */
public class WordleLeaderBoard {
	private ArrayList<String> leaders = new ArrayList<>();
	private WordleAccount account = new WordleAccount();

	/**
	 * Default constructor for the class
	 */
	public WordleLeaderBoard() {
		String leader = "";
		int maxWins = 0;
		int numGamesWon = 0;
		HashMap<String, Credentials> userHashMap = new HashMap<>(
				account.getAllUsers());

		while (!userHashMap.isEmpty()) {
			maxWins = 0;
			numGamesWon = 0;
			for (String user : userHashMap.keySet()) {
				numGamesWon = userHashMap.get(user).getNumGamesWon();
				if (numGamesWon >= maxWins) {
					leader = user;
					maxWins = numGamesWon;
				}
			}
			leaders.add(leader + " >> " + maxWins + " games");
			userHashMap.remove(leader);
		}
		System.out.println("\nLeaders: " + leaders);
	}

	/**
	 * Get a list of leaders in the game sorted by number of wins
	 * 
	 * @return ArrayList, list of leaders sorted by number of wins
	 */
	public ArrayList<String> getLeaders() {
		return leaders;
	}
	
	/***
	 * returns the first top ten people from the leaders list
	 * 
	 * @return retArray - an array list of strings of the top 10 leaders in 
	 * 	the leader list
	 */
	public ArrayList<String> getFirstTen() {
		ArrayList<String> retArray = new ArrayList<String>();
		for (int i = 0; i <= 9; i++) {
			if (i < leaders.size()) {
				retArray.add(leaders.get(i));
			}
		}
		return retArray;
	}
	
	/**
	 * Get statistics for the specified user
	 * 
	 * @param user, String for the user
	 * @return Credentials, the credentials containing stats for the user
	 */
	public Credentials getStats(String user) {
		return account.getStats(user);
	}
}
