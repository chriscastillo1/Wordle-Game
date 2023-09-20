package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * AUTHOR: Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * FILE: WordleDictionary.java 
 * ASSIGNMENT: Final Project - Wordle
 * PURPOSE: Defines the WordleDictionary class. This will statically load 
 * words from the file and be responsible for other word management.
 */
public class WordleDictionary {

	/**
	 * This is the word that the user will be trying to guess
	 */
	private String wordOfTheDay;

	/**
	 * A boolean to tell if the word was guessed of not. true if it is guessed.
	 */
	private boolean wordGuessed;

	private static ArrayList<String> words = new ArrayList<>(); // All possible Wordle words.

	/**
	 * CONSTRUCTOR
	 */
	public WordleDictionary() {
		// set the word guessed to false
		wordGuessed = false;
		// set the word of the day/game
		wordOfTheDay = null;
	}

	/**
	 * This is essentially a setter for the word of the day. This must only take
	 * place after the dictionary is initialized.
	 */
	public void setWord() {
		if (!words.isEmpty()) {
			wordOfTheDay = getRandomWord();
			
			// Word injection for testing
			// wordOfTheDay = "eater";
			// System.out.println("Injecting word of the day: " + wordOfTheDay);
		}
	}

	/**
	 * initialize function. Should be called when a Wordle instance is created;
	 * duplicate calls won't do anything. Loads the valid wordle words into the
	 * list.
	 */
	public void initialize() {
		if (words.isEmpty()) {
			try {
				BufferedReader file = new BufferedReader(new FileReader("valid-wordle-words.txt"));
				String word = file.readLine();

				while (word != null) {
					words.add(word);
					word = file.readLine();
				}

				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * getRandomWord function. Should be called when starting a game, to determine
	 * the random word.
	 * 
	 * @return String, the random word for the Wordle game
	 */
	public String getRandomWord() {
		return words.get(ThreadLocalRandom.current().nextInt(0, words.size() - 1));
	}

	/**
	 * isValidWord function. Checks if a word is a valid word or guess.
	 * 
	 * @param word, String for the word to check
	 * @return boolean, true if word is valid, false otherwise
	 */
	public boolean isValidWord(String word) {
		return words.stream().anyMatch(test -> test.equalsIgnoreCase(word));
	}

	/***
	 * Checks a user's guess to see how accurate it is to the actual word
	 * 
	 * @param guess - the string guess from the user
	 * 
	 * @return - An Array of integers that will contain the information about the
	 *         guess's characters and their locations using the following integers
	 * 
	 */
	public int[] checkAccuracy(String guess) {
		int[] guessCheck = new int[5];
		for (int i = 0; i <= 4; i++) {
			// if the character is in the right place
			if (guess.charAt(i) == wordOfTheDay.charAt(i)) {
				guessCheck[i] = 1;
				// if the character is in the wrong place
			} else if (wordOfTheDay.contains(guess.subSequence(i, i + 1))) {
				guessCheck[i] = 0;
			} else {
				// if the character isn't in the word.
				guessCheck[i] = -1;
			}
		}
		
		//	Scenario #1: 
		//		Correct word is "stink" and guess is "iliac"
		//		First I is in gray, second I is green background
		
		//	Scenario #2: 
		//		Correct word is "eater" and guess is "lever"
		//		First E is in yellow, second E is green background
		
		// 2nd pass
		for (int i = 0; i <= 4; i++) {
			if (guessCheck[i] == 1) {
				if(getNumMatchingLetters(guess.charAt(i)) == 1) {
					for (int j = 0; j <= 4; j++) {
						if (guess.charAt(i) != guess.charAt(j)) {
							continue;
						}
						if (guessCheck[j] == 0) {
							guessCheck[j] = -1;
						}
					}
				}
			}
		}
		isGuessCorrect(guessCheck);
		return guessCheck;
	}
	
	/**
	 * Helper method to count number of matching letters in word of the day
	 * 
	 * @param letter, the character to count
	 * @return int, the count of number of matching letters
	 */
	private int getNumMatchingLetters(char letter) {
		int count = 0;
		for (int i = 0; i <= 4; i++) {
			if (wordOfTheDay.charAt(i) == letter) {
				count++;
			}
		}
		return count;
	}

	/***
	 * This will iterate through the guessCheck array of ints and determine if the
	 * guess was correct, if so, then set the wordGuessed to true, false if not.
	 * 
	 * @param guessResults - an array of ints that represent the status of the
	 *                     characters in the guess
	 */
	private void isGuessCorrect(int[] guessResults) {
		// if any of the positions are not correct reutrn false
		for (int i = 0; i <= 4; i++) {
			if (guessResults[i] == 0 || guessResults[i] == -1) {
				wordGuessed = false;
				return;
			}
		}
		wordGuessed = true;
	}

	/***
	 * getter for the word of the game for testing purposes
	 * 
	 * @return - the word of the game
	 */
	public String getWord() {
		return wordOfTheDay;
	}
	
	/***
	 * setter for the word of the game for testing purposes
	 * 
	 * @param word - String word of the game
	 */
	public void setWord(String word) {
		wordOfTheDay = word;
	}

	/***
	 * getter for if the wordGuessed
	 * 
	 * @return - true if the word was guessed, false otherwise
	 */
	public boolean isGuessCorrect() {
		return wordGuessed;
	}

}