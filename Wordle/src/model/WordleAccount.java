package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * AUTHOR: Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * FILE: WordleAccount.java 
 * ASSIGNMENT: Final Project - Wordle
 * PURPOSE: This class implements the WordleAccount. This class contains
 * methods that allow users to create new account. It contains methods for
 * authenticating user credentials. The credentials are saved in a log file 
 * and retrieved to authenticate accounts.
 */
public class WordleAccount {
	private final static String FILENAME = "credentials.ser";
	private LocalDate today;

	private boolean isAccountAuthenticated;
	private String userName;
	private HashMap<String, Credentials> credentials;
	private LocalDate lastLoginDate;
	// private Alert alert = new Alert(AlertType.ERROR);

	private LocalDate currentDate = LocalDate.now();

	/**
	 * Constructor for the WordleAccount class
	 * 
	 * @param name,       String student name
	 * @param password,   String password for the WordleAccount
	 * @param newAccount, boolean True when new account is to be created
	 */
	public WordleAccount(String name, String password, boolean newAccount) {
		this.userName = name;
		today = LocalDate.now();

		// retrieve persistent account information from credentials file
		credentials = readCredentialsFromFile(FILENAME);

		if (newAccount == true) {
			// check if user already has an account
			if (credentials.containsKey(name)) {
				// alert.setHeaderText("User " + name + " already exists!");
				// alert.showAndWait();
			} else {
				Credentials user = new Credentials();
				user.setPassword(password);
				user.setLastLoginDate(null);
				user.setNumGamesPlayed(0);
				credentials.put(name, user);

				// When making new account, account is authenticated
				isAccountAuthenticated = true;

				// store new account information to credentials file
				writeCredentialsToFile(credentials, FILENAME);
			}
		} else {
			// not a new account so verify credentials are valid
			if (credentials.containsKey(name)) {
				if (!credentials.get(name).getPassword().equals(password)) {
					// alert.setHeaderText("Password invalid for user " + name);
					// alert.showAndWait();

					isAccountAuthenticated = false;
				} else {
					isAccountAuthenticated = true;

					Credentials user = credentials.get(name);
					lastLoginDate = user.getLastLoginDate();

					// user logged in for the first time after account created
					if (lastLoginDate == null) {
						lastLoginDate = currentDate;
					}
				}
			} else {
				// alert.setHeaderText("User " + name + " not found!");
				// alert.showAndWait();
				isAccountAuthenticated = false;
			}
		}
	}

	/**
	 * Default constructor for WordleAccount class
	 */
	public WordleAccount() {
		credentials = readCredentialsFromFile(FILENAME);
	}

	/**
	 * Update statistics for WordleAccount
	 * 
	 * @param isGameWon, boolean true when game is won, false otherwise
	 * @param guess, integer for number of guesses made
	 */
	public void updateStats(boolean isGameWon, int guess) {
		credentials.get(userName).updateStats(isGameWon, guess);
		
		// persist credentials
		writeCredentialsToFile(credentials, FILENAME);
	}
	
	/**
	 * Get statistics for the Wordle Account
	 * 
	 * @return Credentials, the credentials containing stats for the account
	 */
	public Credentials getStats() {
		return credentials.get(userName);
	}
	
	/**
	 * Get statistics for the specified user 
	 * 
	 * @param user, the String representing the user name
	 * 
	 * @return Credentials, the credentials containing stats for the user
	 */
	public Credentials getStats(String user) {
		return credentials.get(user);
	}
	
	/**
	 * Get all users of Wordle game
	 * 
	 * @return HashMap, the HashMap containing user and statistics
	 */
	public HashMap<String, Credentials> getAllUsers() {
		return credentials;
	}
	
	/**
	 * Get string representation of the WordleAccount object
	 * 
	 * @return str, string representation of the object
	 */
	public String toString() {
		String str = "";
		str += "\nUser: " + userName;
		str += credentials.get(userName);
		return str;
	}
	
	/**
	 * Reset statistics for all user accounts
	 */
	public void deleteJUnitTestAccounts() {

		// retrieve persistent account information from credentials file
		HashMap<String, Credentials> userCreds = readCredentialsFromFile(
				FILENAME);

		HashMap<String, Credentials> userCreds2 = new HashMap<>();
		
		for(String name: userCreds.keySet()) {
			if(!name.contains("JUnitTestAccount:")) {
				userCreds2.put(name, userCreds.get(name));
			}
		}
		writeCredentialsToFile(userCreds2, FILENAME);
	}

	/**
	 * Reset statistics for all user accounts
	 */
	public void resetStatsForJUnitTestUsers() {

		// retrieve persistent account information from credentials file
		HashMap<String, Credentials> userCreds = readCredentialsFromFile(
				FILENAME);

		for (String name : userCreds.keySet()) {
			if(name.contains("JUnitTestAccount:")) {
				Credentials creds = userCreds.get(name);
				creds.setNumGamesPlayed(0);
				userCreds.put(name, creds);
			}
		}

		writeCredentialsToFile(userCreds, FILENAME);
	}

	/**
	 * Get account authentication status
	 * 
	 * @return isAccountAuthenticated, boolean true if account is authenticated
	 */
	public boolean getAccountAuthenticationStatus() {
		return isAccountAuthenticated;
	}
	


	/**
	 * Read serialized credentials from file
	 * 
	 * @param filename, the String filename for reading credentials
	 * 
	 * @return HashMap, the HashMap with credentials
	 */
	public HashMap<String, Credentials> readCredentialsFromFile(
			String filename) {

		HashMap<String, Credentials> readCredentials = new HashMap<>();

		try {
			FileInputStream rawBytes = new FileInputStream(filename);

			// Read the file
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);

			// Read one serialized object from file
			readCredentials = (HashMap<String, Credentials>) inFile
					.readObject();

			inFile.close();

		} catch (Exception e) {
			// Exception can occur for the first time
		}

		return readCredentials;
	}

	/**
	 * Write serialized credentials to file
	 * 
	 * @param hmap, the HashMap containing credentials
	 * @param filename, the file name where credentials are to be written
	 * 
	 */
	public void writeCredentialsToFile(HashMap<String, Credentials> hmap,
			String filename) {
		try {

			// Write serialized object to file
			FileOutputStream bytesToDisk = new FileOutputStream(filename);
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);

			// Make the object persist so it can be read later
			outFile.writeObject(hmap);

			// close the output file
			outFile.close();

		} catch (Exception e) {
			// Exception
		}
	}

	/**
	 * Get user name
	 * 
	 * @return userName, the String user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set Last login date
	 */
	public void setLastLogin() {
		credentials.get(userName).setLastLoginDate(LocalDate.now());
	}
}