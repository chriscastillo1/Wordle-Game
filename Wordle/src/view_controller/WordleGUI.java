package view_controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Wordle;
import model.WordleAccount;

/**
 * 
 * @author Chris Castillo This is a GUI view model for the game Wordle
 */
public class WordleGUI extends Application {
	/**
	 * Main method for Wordle GUI. Starts the Wordle Application
	 * 
	 * @param args, String array for command line arguments (not used)
	 */
	public static void main(String[] args) {
		launch(args);
	}

	// This is the wordle game controller. Essentially the gameMaster
	private Wordle wordleGame = new Wordle();

	// Stage container for all elements
	private BorderPane container = new BorderPane();

	// Contains header elements (title, login, and stats button)
	private HBox header = new HBox();
	private Label title = new Label("Wordle");

	// MENU IMPLEMENTATION
	private MenuButton menu = new MenuButton();
	private MenuItem login = new MenuItem("Login");
	private MenuItem darkLightMode = new MenuItem("Light Mode");
	private boolean darkModeOn = true;
	private MenuItem stats = new MenuItem("Statistics");
	private MenuItem newGame = new MenuItem("New Game");
	private MenuItem leaderBoard = new MenuItem("LeaderBoard");

	// The container for the Wordle Board.
	// Has an associated Label array to update as letters typed
	private GridPane buttonContainer = new GridPane();
	private Label[][] tileNodes = new Label[6][5];

	// Keyboard Containers and elements in virtual keyboard
	private VBox keyboardContainer = new VBox(7);
	private HBox topRow = new HBox(6);
	private HBox midRow = new HBox(6);
	private HBox bottomRow = new HBox(6);
	private Button enter = new Button("ENTER");
	private Button delete = new Button("DELETE");
	private boolean disableBoardAndKeyBoard = false;

	// Pop-up for Login Info
	private view_controller.LoginCreateAccountPane loginPane;

	// Pop-up for leaderBoard info
	private view_controller.LeaderBoardPane leaderPane;

	// Pop-up for results info
	private view_controller.ResultPane resultPane;

	// Current User and Primary Stage to passin pop-up information
	private WordleAccount currentUserLoggedIn;
	private Stage primaryStage;

	// IMPORTANT - Keeps track of Where you are at in Wordle Board.
	// Guess Row 0-6 ; currLetter represents what letter of the word youre on
	private int currGuessRow = 0;
	private int currLetter = 0;

	@Override
	/**
	 * Start method for the Wordle GUI
	 * 
	 * @param Stage, the stage for the GUI
	 * 
	 */
	public void start(Stage stage) throws Exception {
		initializeHeader();
		initializeWordleBoard();
		initializeKeyboard();

		Scene scene = new Scene(container, 900, 1019);

		setupStageView(stage, scene);
		registerKeyInputs();
		registerVirtualKeyboardInputs();
		registerHandlers();
		darkMode();
		letterCurrentStyle(tileNodes[currGuessRow][currLetter]);
		enter.requestFocus();
	}

	/**
	 * Receive logged in user
	 */
	public void receiveLoggedIn() {
		currentUserLoggedIn = loginPane.getCurrentUserLoggedIn();

		if (currentUserLoggedIn != null) {
			primaryStage.setTitle("Wordle: Welcome " + currentUserLoggedIn.getUserName().toUpperCase() + "!");
			stats.setVisible(true);
			login.setText("Logout");
			currentUserLoggedIn.setLastLogin();

			// launch new game upon login
			newGame.fire();
		}
		enter.requestFocus();
	}

	/**
	 * Register event handlers for Wordle GUI
	 */
	private void registerHandlers() {
		login.setOnAction((event) -> {
			if (login.getText().equals("Logout")) {
				primaryStage.setTitle("Wordle");
				login.setText("Login");
				stats.setVisible(false);
				title.setText("Wordle");
				currentUserLoggedIn = null;
				newGame.fire();
				return;
			}

			Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.setWidth(300F);
			popup.setHeight(250F);
			loginPane = new LoginCreateAccountPane(popup, this);
			enter.requestFocus();
		});

		// TODO FIX GUESS RETURNS NULL IF ITS NOT LONG ENOUGH (aka MUST BE 5)
		enter.setOnKeyPressed((e) -> {
			if (e.getCode() == KeyCode.ENTER) {

				String guess = getGuessInput(currGuessRow);

				if (guess != null && wordleGame.wordCorrectionChecker.isValidWord(guess)) {

					// get the status of the guess just made
					int[] guessStatus = wordleGame.makeGuess(guess);

					// if the game is won, trigger the animation, win panel and disable the keyboard
					if (wordleGame.checkGameWon() == true) {
						// if a user is logged in, update their starts
						if (currentUserLoggedIn != null) {
							currentUserLoggedIn.updateStats(true, currGuessRow);
						}

						flipRow(guessStatus);
						disableBoardAndKeyBoard = true;
						buttonContainer.setDisable(true);
						keyboardContainer.setDisable(true);
						winSound();
						alertPopUpWin();
						return;
					}

					// if the game is lost, trigger the animation, lose panel and disable the
					// keyboard
					if (currGuessRow == 5) {

						// update the player stats with the loss if a user is logged in
						if (currentUserLoggedIn != null) {
							currentUserLoggedIn.updateStats(false, 0);
						}

						flipRow(guessStatus);
						disableBoardAndKeyBoard = true;
						buttonContainer.setDisable(true);
						keyboardContainer.setDisable(true);
						loseSound();
						alertPopUpLost();
						return;
					}
					// increment guess level and reset current letter position
					validSoundEffect();
					flipRow(guessStatus);
					currGuessRow++;
					currLetter = 0;
					letterCurrentStyle(tileNodes[currGuessRow][currLetter]);
				} else {
					// Guess isn't a valid Wordle guess (either too short, or not in the dictionary.
					shake();
				}
			}
			enter.requestFocus();
		});

		newGame.setOnAction((e) -> {
			// Initializes a new Wordle Game
			wordleGame = new Wordle();

			// Enables the Board and Keyboard
			disableBoardAndKeyBoard = false;
			buttonContainer.setDisable(false);
			keyboardContainer.setDisable(false);

			// Initializes a new Board and resets counters
			buttonContainer = new GridPane();
			currGuessRow = 0;
			currLetter = 0;
			initializeWordleBoard();
			if (darkModeOn) {
				darkMode();
			} else {
				lightMode();
			}
			letterCurrentStyle(tileNodes[currGuessRow][currLetter]);
			enter.requestFocus();
		});

		// Set leaderBoard menu item action
		leaderBoard.setOnAction((e) -> {
			Stage stage = new Stage();
			leaderPane = new LeaderBoardPane(stage);
			enter.requestFocus();
		});

		stats.setOnAction((e) -> {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			new StatisticsPane(stage, currentUserLoggedIn);
			enter.requestFocus();
		});

		delete.setOnMouseClicked((e) -> {
			deleteBoardWithLetter();
			enter.requestFocus();
		});

		// Toggles Light and Dark Mode for Wordle Board
		darkLightMode.setOnAction((event) -> {
			if (darkModeOn) {
				lightMode();
				darkLightMode.setText("Dark Mode");
				darkModeOn = false;
			} else {
				darkMode();
				darkLightMode.setText("Light Mode");
				darkModeOn = true;
			}
			enter.requestFocus();
		});
	}

	/***
	 * This is the alert that will show when the wordle game is won
	 */
	private void alertPopUpWin() {
		Stage stage = new Stage();
		stage.centerOnScreen();
		resultPane = new ResultPane(stage, currentUserLoggedIn, true, wordleGame.wordCorrectionChecker.getWord());
	}

	/***
	 * This is the alert that will show when the wordle game is lost
	 */
	private void alertPopUpLost() {
		Stage stage = new Stage();
		stage.centerOnScreen();
		resultPane = new ResultPane(stage, currentUserLoggedIn, false, wordleGame.wordCorrectionChecker.getWord());
	}

	/**
	 * Gets the guess the user inputed
	 * 
	 * @param guessRow, the integer for the guess row
	 * @return String, the string for the guess input
	 */
	private String getGuessInput(int guessRow) {
		String result = "";
		for (Label tile : tileNodes[guessRow]) {
			result += tile.getText();
		}
		if (result.length() == 5) {
			return result;
		}
		return null;
	}

	/**
	 * Register if a key has been typed or deleted. The typed key will show up in
	 * the board
	 */
	private void registerKeyInputs() {
		container.setOnKeyPressed((event) -> {
			// If the key hit is a letter
			if (event.getCode().isLetterKey()) {
				updateBoardWithLetter(event.getCode().toString());
			}

			// Deletes a Letter from Guess
			if (event.getCode() == KeyCode.BACK_SPACE) {
				deleteBoardWithLetter();
			}
		});

		// If someone selects menu then clicks back to game
		container.setOnMouseClicked(e -> {
			enter.requestFocus();
		});
	}

	/**
	 * Registers virtual keystrokes and updates wordle board
	 */
	private void registerVirtualKeyboardInputs() {
		for (Node letter : topRow.getChildren()) {
			Button button = (Button) letter;
			button.setOnMouseClicked((e) -> {
				updateBoardWithLetter(button.getText());
			});
		}

		for (Node letter : midRow.getChildren()) {
			Button button = (Button) letter;
			button.setOnMouseClicked((e) -> {
				updateBoardWithLetter(button.getText());
			});
		}

		for (Node letter : bottomRow.getChildren()) {
			Button button = (Button) letter;
			button.setOnMouseClicked((e) -> {
				if (button.getText().equals("ENTER")) {
					KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.ENTER, false, false,
							false, false);
					enter.fireEvent(keyEvent);
				} else if (!button.getText().equals("DELETE")) {
					updateBoardWithLetter(button.getText());
				}
			});
		}
	}

	/**
	 * Registers virtual keystrokes and updates wordle board
	 * 
	 * @param letter, the String letter
	 */
	private void updateBoardWithLetter(String letter) {
		if (disableBoardAndKeyBoard) {
			return;
		}
		if (currLetter < 5) {
			typingSoundEffect();
			tileNodes[currGuessRow][currLetter].setText(letter);
			letterInputStyle(tileNodes[currGuessRow][currLetter]);
			tileAnimation(tileNodes[currGuessRow][currLetter]);

			currLetter++;
			if (currLetter < 5) {
				letterCurrentStyle(tileNodes[currGuessRow][currLetter]);
			}
		}
	}

	/**
	 * Deletes letter from Wordle Board
	 */
	private void deleteBoardWithLetter() {
		if (disableBoardAndKeyBoard) {
			return;
		}
		if (currLetter == 0) {
			return;
		}
		deleteSoundEffect();
		tileNodes[currGuessRow][currLetter - 1].setText("");
		if (currLetter < 5)
			letterDeleteStyle(tileNodes[currGuessRow][currLetter]);
		letterCurrentStyle(tileNodes[currGuessRow][currLetter - 1]);
		currLetter--;
	}

	/**
	 * Changes border color when user inputs a letter for a guess
	 * 
	 * @param tile, the Label for letter input style
	 */
	private void letterInputStyle(Label tile) {
		if (darkModeOn) {
			tile.setStyle("-fx-border-color: #848884;" + "-fx-border-width: 2.5;" + "-fx-background-color: #111111;"
					+ "-fx-text-fill: white;");
		} else {
			tile.setStyle("-fx-border-color: #848884;" + "-fx-border-width: 2.5;" + "-fx-background-color: #FAFAFA;"
					+ "-fx-text-fill: black;");
		}
	}

	// Changes border color to show which tile the user will next input into
	private void letterCurrentStyle(Label tile) {
		if (darkModeOn) {
			tile.setStyle("-fx-border-color: #9da39d;" + "-fx-border-width: 2.5;" + "-fx-background-color: #111111;"
					+ "-fx-text-fill: white;");
		} else {
			tile.setStyle("-fx-border-color: #9da39d;" + "-fx-border-width: 2.5;" + "-fx-background-color: #FAFAFA;"
					+ "-fx-text-fill: black;");
		}
	}

	/**
	 * Reverts border color when user deletes a letter from guess
	 * 
	 * @param tile, the label for the operation
	 */
	private void letterDeleteStyle(Label tile) {
		if (darkModeOn) {
			tile.setStyle("-fx-border-color: #787c7f;" + "-fx-border-width: 1.5;" + "-fx-background-color: #111111;"
					+ "-fx-text-fill: white;");
		} else {
			tile.setStyle("-fx-border-color: #787c7f;" + "-fx-border-width: 1.5;" + "-fx-background-color: #FAFAFA;"
					+ "-fx-text-fill: black;");
		}
	}

	/**
	 * Setup the stage view, align the nodes, and shows scene
	 * 
	 * @param stage, the Stage to setup
	 * @param scene, the scene for the stage
	 */
	private void setupStageView(Stage stage, Scene scene) {
		primaryStage = stage;
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Initializes the header that has the Login button, Stats Button, and Title
	 */
	private void initializeHeader() {
		title.setFont(new Font("Comic Sans MS", 45));
		title.setStyle("-fx-text-fill: white;");

		initializeMenu();

		header.getChildren().add(menu);
		header.getChildren().add(title);

		header.setSpacing(277);
		header.setAlignment(Pos.TOP_LEFT);
		header.setPadding(new Insets(20, 0, 20, 0));
		header.setStyle("-fx-border-color: #787c7f;" + "-fx-border-style: hidden hidden dashed hidden;"
				+ "-fx-border-width: 4;");

		container.setTop(header);
	}

	/**
	 * Setup the Menu that has Login, Dark Mode, Stats
	 */
	private void initializeMenu() {
		FileInputStream input = null;
		try {
			input = new FileInputStream("src/resources/test.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Image image = new Image(input);
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(60);
		imageView.setFitWidth(60);
		menu.setGraphic(imageView);
		menu.setStyle("-fx-padding: 0;" + "-fx-background-color: transparent;" + "-fx-mark-color: black;");

		menu.getItems().add(login);
		menu.getItems().add(newGame);
		menu.getItems().add(darkLightMode);
		menu.getItems().add(leaderBoard);
		menu.getItems().add(stats);
		stats.setVisible(false);
	}

	/**
	 * Initializes the Board by creating new Empty Labels with appropriate
	 */
	private void initializeWordleBoard() {
		// Loops through and creates a new Label for the Board
		for (int col = 0; col < 5; col++) {
			for (int row = 0; row < 6; row++) {
				Label box = new Label();
				box.setFont(Font.font("Lucida Console", FontWeight.EXTRA_BOLD, 60));
				box.setAlignment(Pos.CENTER);
				box.setMinSize(75, 75);
				tileNodes[row][col] = box;
				buttonContainer.add(box, col, row);
			}
		}
		// Formatting the GridPane to have all buttons aligned nicely
		buttonContainer.setAlignment(Pos.TOP_CENTER);
		buttonContainer.setHgap(8);
		buttonContainer.setVgap(10);
		buttonContainer.setPadding(new Insets(30, 10, 10, 10));
		container.setCenter(buttonContainer);
	}

	/**
	 * Constructs onscreen keyboard
	 */
	private void initializeKeyboard() {
		char[] top = "qwertyuiop".toCharArray();
		char[] mid = "asdfghjkl".toCharArray();
		char[] bot = "zxcvbnm".toCharArray();

		fillKeyboardRow(topRow, top);
		fillKeyboardRow(midRow, mid);

		bottomRow.getChildren().add(enter);
		fillKeyboardRow(bottomRow, bot);
		bottomRow.getChildren().add(delete);

		// Sets alignment for each keyboard row
		topRow.setAlignment(Pos.TOP_CENTER);
		midRow.setAlignment(Pos.TOP_CENTER);
		bottomRow.setAlignment(Pos.TOP_CENTER);

		enter.setMinSize(68, 52);
		delete.setMinSize(68, 52);
		enter.setFont(Font.font("Lucida Console", FontWeight.BOLD, 20));
		delete.setFont(Font.font("Lucida Console", FontWeight.BOLD, 20));

		keyboardContainer.getChildren().addAll(topRow, midRow, bottomRow);
		keyboardContainer.setPadding(new Insets(20, 10, 100, 10));
		container.setBottom(keyboardContainer);
	}

	/**
	 * Fill keyboard row
	 * 
	 * @param box,     the HBox for the fill
	 * @param letters, the character array containing the letters
	 */
	private void fillKeyboardRow(HBox box, char[] letters) {
		for (char letter : letters) {
			String let = String.valueOf(letter).toUpperCase();
			Button button = new Button(let);

			button.setMinSize(40, 50);
			button.setFont(Font.font("Lucida Console", FontWeight.BOLD, 30));
			button.setFocusTraversable(false);
			box.getChildren().add(button);
		}
	}

	/**
	 * Set dark mode for Wordle GUI
	 */
	private void darkMode() {
		container.setStyle("-fx-background-color: #111111;");
		title.setStyle("-fx-text-fill: white;");

		// Dark Mode for Wordle Board
		for (int i = currGuessRow; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				tileNodes[i][j].setStyle("-fx-border-color: #787c7f;" + "-fx-border-width: 1.5;"
						+ "-fx-background-color: #111111;" + "-fx-text-fill: white;");
			}
		}

		for (Node button : topRow.getChildren()) {
			button.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: white;");
		}
		for (Node button : midRow.getChildren()) {
			button.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: white;");
		}
		for (Node button : bottomRow.getChildren()) {
			button.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: white;");
		}

		enter.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: white;");
		delete.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: white;");
		darkModeOn = true;
	}

	/**
	 * Set light mode for Wordle GUI
	 */
	private void lightMode() {
		container.setStyle("-fx-background-color: #FAFAFA;");
		title.setStyle("-fx-text-fill: black;");

		// Light Mode for Wordle Board
		for (int i = currGuessRow; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				tileNodes[i][j].setStyle("-fx-border-color: #787c7f;" + "-fx-border-width: 1.5;"
						+ "-fx-background-color: #FAFAFA;" + "-fx-text-fill: black;");
			}
		}

		for (Node button : topRow.getChildren()) {
			button.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: black;");
		}
		for (Node button : midRow.getChildren()) {
			button.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: black;");
		}
		for (Node button : bottomRow.getChildren()) {
			button.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: black;");
		}

		enter.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: black;");
		delete.setStyle("-fx-border-width: 1.5;" + "-fx-background-color: #949494;" + "-fx-text-fill: black;");
		darkModeOn = false;
	}

	/**
	 * Perform tile animation
	 * 
	 * @param tile, the tile to perform animation for
	 */
	private void tileAnimation(Node tile) {

		ScaleTransition scaleTransition = new ScaleTransition();
		scaleTransition.setNode(tile);
		scaleTransition.setDuration(Duration.millis(45));
		scaleTransition.setByX(0.15);
		scaleTransition.setByY(0.15);
		scaleTransition.setCycleCount(2);
		scaleTransition.setAutoReverse(true);
		scaleTransition.play();
		disableBoardAndKeyBoard = true;
		scaleTransition.setOnFinished(e -> {
			disableBoardAndKeyBoard = false;
		});
	}

	/**
	 * Flip row
	 * 
	 * @param status, the integer array
	 */
	private void flipRow(int[] status) {
		// Disables Keyboard so user cant type until animation finished
		disableBoardAndKeyBoard = true;
		RotateTransition rotate = setFlipAnimation(tileNodes[currGuessRow][0], status[0]);

		RotateTransition rotate2 = setFlipAnimation(tileNodes[currGuessRow][1], status[1]);

		RotateTransition rotate3 = setFlipAnimation(tileNodes[currGuessRow][2], status[2]);

		RotateTransition rotate4 = setFlipAnimation(tileNodes[currGuessRow][3], status[3]);

		RotateTransition rotate5 = setFlipAnimation(tileNodes[currGuessRow][4], status[4]);

		SequentialTransition sq = new SequentialTransition(rotate, rotate2, rotate3, rotate4, rotate5);
		sq.play();
		// Enables Keyboard Again
		sq.setOnFinished(e -> {
			disableBoardAndKeyBoard = false;
		});
	}

	/**
	 * Set flip animation for the specified tile
	 * 
	 * @param tile, the Node tile to flip
	 * @param num,  the integer for background color
	 * @return RotateTransition, the rotate transition
	 */
	private RotateTransition setFlipAnimation(Node tile, int num) {
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(tile);
		rotate.setDuration(Duration.millis(215));
		rotate.setAxis(Rotate.X_AXIS);
		rotate.setFromAngle(0);
		rotate.setToAngle(95);
		rotate.setCycleCount(2);
		rotate.setAutoReverse(true);

		rotate.setOnFinished(e -> {
			setStyleForGuess(tile, num);
			if (tile instanceof Label) {
				setKeyboardLetterStyle(((Label) tile).getText().charAt(0), num);
			}
		});

		return rotate;
	}

	/**
	 * Set style for user guess
	 * 
	 * @param tile, the Node tile to set the style for user guess
	 * @param num,  the integer representing the background color
	 */
	private void setStyleForGuess(Node tile, int num) {
		if (num == -1) {
			tile.setStyle("-fx-background-color: #4f4f4f; -fx-text-fill: white; -fx-font-weight: bold;");
		} else if (num == 0) {
			tile.setStyle("-fx-background-color: #c8b653; -fx-text-fill: white; -fx-font-weight: bold;");
		} else if (num == 1) {
			tile.setStyle("-fx-background-color: #6ca965; -fx-text-fill: white; -fx-font-weight: bold;");
		}
	}

	/**
	 * Set keyboard letter style
	 * 
	 * @param type, the character type
	 * @param num,  the integer representing background color to display
	 */
	private void setKeyboardLetterStyle(char type, int num) {
		List<HBox> keyboards = Arrays.asList(topRow, midRow, bottomRow);
		for (HBox keyboardRow : keyboards) {
			for (Node button : keyboardRow.getChildren()) {
				if (button instanceof Button) {
					if (((Button) button).getText().charAt(0) == type) {
						if (num == -1) {
							button.setStyle(
									"-fx-background-color: #4f4f4f; -fx-text-fill: white; -fx-font-weight: bold;");
						} else if (num == 0) {
							button.setStyle(
									"-fx-background-color: #c8b653; -fx-text-fill: white; -fx-font-weight: bold;");
						} else if (num == 1) {
							button.setStyle(
									"-fx-background-color: #6ca965; -fx-text-fill: white; -fx-font-weight: bold;");
						}
						return;
					}
				}
			}
		}
	}

	/**
	 * Shakes the Tiles if invalid word
	 */
	private void shake() {
		wrongSound();
		shakeTile(tileNodes[currGuessRow][0]);
		shakeTile(tileNodes[currGuessRow][1]);
		shakeTile(tileNodes[currGuessRow][2]);
		shakeTile(tileNodes[currGuessRow][3]);
		shakeTile(tileNodes[currGuessRow][4]);
	}

	/**
	 * Shake tile
	 * 
	 * @param tile, the Node tile to shake
	 */
	private void shakeTile(Node tile) {
		disableBoardAndKeyBoard = true;
		TranslateTransition t = new TranslateTransition();
		t.setNode(tile);
		t.setToX(5);
		t.setCycleCount(4);
		t.setDuration(Duration.millis(50));
		t.setAutoReverse(true);
		t.play();
		t.setOnFinished(e -> {
			TranslateTransition te = new TranslateTransition();
			te.setNode(tile);
			te.setToX(0);
			te.play();
			disableBoardAndKeyBoard = false;
		});
	}
	
	private void winSound() {
		String path = "src/resources/win.mp3";

		// Need a File and URI object so the path works on all OSs
		File file = new File(path);
		URI uri = file.toURI();

		// Play one mp3 and and have code run when the song ends
		Media media = new Media(uri.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

	private void loseSound() {
		String path = "src/resources/lose.mp3";

		// Need a File and URI object so the path works on all OSs
		File file = new File(path);
		URI uri = file.toURI();

		// Play one mp3 and and have code run when the song ends
		Media media = new Media(uri.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

	private void typingSoundEffect() {
		String path = "src/resources/punch.mp3";

		// Need a File and URI object so the path works on all OSs
		File file = new File(path);
		URI uri = file.toURI();

		// Play one mp3 and and have code run when the song ends
		Media media = new Media(uri.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setRate(1.06);
		mediaPlayer.play();
	}

	private void validSoundEffect() {
		String path = "src/resources/valid.mp3";

		// Need a File and URI object so the path works on all OSs
		File file = new File(path);
		URI uri = file.toURI();

		// Play one mp3 and and have code run when the song ends
		Media media = new Media(uri.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setRate(1.12);
		mediaPlayer.play();
	}

	private void deleteSoundEffect() {
		String path = "src/resources/woosh.mp3";

		// Need a File and URI object so the path works on all OSs
		File file = new File(path);
		URI uri = file.toURI();

		// Play one mp3 and and have code run when the song ends
		Media media = new Media(uri.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setRate(1.15);
		mediaPlayer.play();
	}

	private void wrongSound() {
		String path = "src/resources/wrong.mp3";

		// Need a File and URI object so the path works on all OSs
		File file = new File(path);
		URI uri = file.toURI();

		// Play one mp3 and and have code run when the song ends
		Media media = new Media(uri.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setRate(1.15);
		mediaPlayer.play();
	}
}