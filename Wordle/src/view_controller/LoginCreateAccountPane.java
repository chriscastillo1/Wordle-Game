package view_controller;

import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.WordleAccount;

/**
 * AUTHOR: Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * FILE: LoginCreateAccountPane.java 
 * ASSIGNMENT: Final Project - Wordle
 * PURPOSE: This class implements the Login Create Account Pane view.
 */
public class LoginCreateAccountPane extends GridPane {

	private Label labelUsername = new Label("Username");
	private Label labelPassword = new Label("Password");
	private Button buttonLogin = new Button("Login");
	private Button buttonCreateAccount = new Button("Create Account");
	private TextField textUsername = new TextField();
	private PasswordField textPassword = new PasswordField();

	private GridPane pane;
	private WordleAccount account;
	private WordleGUI parent;

	Stage stage;

	/**
	 * Constructor for LoginCreateAccountPane class
	 * 
	 * @param newStage, the Stage for adding LoginCreateAccountPane
	 * @param parent, the WordleGUI parent for creating login create pane
	 */
	public LoginCreateAccountPane(Stage newStage, WordleGUI parent) {
		this.stage = newStage;
		this.parent = parent;
		setupLoginPane();

		// create new scene
		Scene scene = new Scene(new StackPane(this), 200, 100);

		// set title for the stage
		stage.setTitle("Login or Create Account");
		stage.setScene(scene);

		this.add(pane, 0, 0);

		// register handlers
		registerHandlers();
		stage.show();
	}

	/**
	 * Get username and password from text files and authenticate user
	 * 
	 * @param newAccount, boolean true if new account is to be created
	 */
	private void authenticateUser(boolean newAccount) {
		String name = textUsername.getText();
		String password = textPassword.getText();

		// validate length of username and password entered by student
		if (name.length() == 0 || password.length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Please Fill in the Username and Password Fields. Try Again");
			alert.show();
			return;
		}
		account = new WordleAccount(name, password, newAccount);

		if (account.getAccountAuthenticationStatus() == false) {
			Alert alert = new Alert(AlertType.INFORMATION);
			String resultText;
			if (newAccount) {
				resultText = "Failed to create account " + name + ". Try Again";
			}
			else {
				resultText = "Failed to log in as " + name + ". Try Again";
			}
			alert.setContentText(resultText);
			alert.show();
			account = null;
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			String resultText;
			if (newAccount) {
				resultText = "Created account: " + name + " successfully.";
			}
			else {
				resultText = "Logged in as " + name + " successfully.";
			}
			alert.setContentText(resultText);
			alert.show();
			stage.close();
			
			parent.receiveLoggedIn();
		}
	}

	/**
	 * Get current user that is logged in
	 * 
	 * @return WordleAccount, the Wordle Account of user that is logged in
	 */
	public WordleAccount getCurrentUserLoggedIn() {
		return account;
	}

	/**
	 * Register handlers for LoginCreateAccountPane
	 */
	private void registerHandlers() {
		buttonLogin.setOnAction((event) -> {
			// authenticate existing user
			authenticateUser(false);
		});

		buttonCreateAccount.setOnAction((event) -> {
			// create new Wordle account
			authenticateUser(true);
		});
	}

	/**
	 * Setup layout for obtaining student user name and password
	 */
	private void setupLoginPane() {
		// create new GridPane
		pane = new GridPane();

		// set horizontal gap on the GridPane
		pane.setHgap(10);

		// set vertical gap on the GridPane
		pane.setVgap(10);

		// add UI controls to the GridPane
		pane.add(labelUsername, 1, 1);
		pane.add(labelPassword, 1, 2);
		pane.add(textUsername, 2, 1);
		pane.add(textPassword, 2, 2);
		pane.add(buttonLogin, 1, 3);
		pane.add(buttonCreateAccount, 2, 3);
	}
}