package model;

/***
 * 
 * @author Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * 
 * This is the wordle class for the model and will ultimately serve as the game master.
 * 
 * Responsibilities:
 *  - set up the game's dictionary and random word
 *  - direct guess to the class to process them then relay the results obtained from that class
 *  - manage the game (determine if the game is still running/keep guess count/etc)
 *
 */


public class Wordle {
	
	
	/***
	 * This is the class that will check the correction of a guess
	 */
	public WordleDictionary wordCorrectionChecker = new WordleDictionary();;
	
	/***
	 * MAY BE REMOVED
	 * 
	 * can be used to keep track of the logged in user.
	 */
	private WordleAccount loggedIn; 
	
	/***
	 * The number of guesses made in the game
	 */
	private int guessCount;
	
	/***
	 * Check if the game is won
	 */
	private boolean isWon;
	
	/***
	 * Check if the game is running
	 */
	private boolean isStillRunning;
	
	/***
	 * Setter for current logged in user
	 * 
	 * @param account - The use Account being logged in
	 */
	public void setLoggedIn(WordleAccount account) {
		loggedIn = account;
	}
	
	/***
	 * Getter for the current logged in user
	 * 
	 * @return WordleAccount, the Wordle Account for the logged in user
	 */
	public WordleAccount getLoggedIn() {
		return loggedIn;
	}
	
	/***
	 * CONSTRUCTOR
	 */
	public Wordle() {
		//initialize the wordle dictionary
		wordCorrectionChecker.initialize();
		//start the game
		startGame();
	}
	
	/***
	 * This will be used to start a game of wordle. 
	 */
	public void startGame() {
		//get a random word from wordle dictionary
		wordCorrectionChecker.setWord();		
		//Start the guess count over, or set it to zero if a game has not happened yet
		guessCount = 0;
		
		//Set the game to show it is running
		isStillRunning = true;
		//Set the game to show is has not been won, it just started
		isWon = false;
	}
	
	/***
	 * make guess
	 * 
	 * @param guess, String containing the guess letters
	 * @return int[], the guess status
	 */
	public int[] makeGuess(String guess) {
		//increase guess count
		guessCount++;
		//check the guess for GUI
		guess = guess.toLowerCase();
		int[] guessStatus = wordCorrectionChecker.checkAccuracy(guess);
		//check if the guess was right to determine if the game is still running
		if (wordCorrectionChecker.isGuessCorrect()) {
			isStillRunning = false;
			isWon = true;
		} else if (guessCount == 6) {
			isStillRunning = false;
		}
		//return the guess status
		return guessStatus;
	}
	
	/***
	 * getter for isStillRunning
	 * 
	 * @return - true if the game is not over, false otherwise
	 */
	public boolean checkGameRunning() {
		return isStillRunning;
	}
	
	/***
	 * getter for the isWon boolean
	 * 
	 * @return - true if the game is won, false otherwise
	 */
	public boolean checkGameWon() {
		return isWon;
	}
	
}
