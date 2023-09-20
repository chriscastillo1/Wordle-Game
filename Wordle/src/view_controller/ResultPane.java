package view_controller;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Credentials;
import model.WordleAccount;

/***
 * 
 * @author Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * 
 * This is the ResultPane class for the GUI interface.
 * 
 * Responsibilities:
 *  - create the results pane
 *  - display the bar graph of the user's guess distribution
 *
 */

public class ResultPane {
	
	/***
	 * The main stage for the GUI pane elements
	 */
	private BorderPane window = new BorderPane();
	
	/***
	 * The stage
	 */
	Stage stage;
	
	/***
	 * Setup stat box for endgame
	 */
	private VBox statBox;
	
	/***
	 * Setup top pane for endgame
	 */
	private VBox header;
	
	/***
	 * Setup the other labels for stats box
	 */
	private Text stats = new Text("STATISTICS:");
	private Label gamesPlayedText = new Label();
	private Label winStreakDetails = new Label();
	private Label gamesWonPercentage = new Label();
	private Label maxStreak = new Label();

	/***
	 * the correct word
	 */
	private String finalWord;
	
	/***
	 * Constructor for ResultPane class
	 * 
	 * @param stage - the stage to show the scene on
	 * @param account - the Wordle Account 
	 * @param win - boolean, true if game has been won, false otherwise
	 */
	public ResultPane(Stage stage, WordleAccount account, boolean win, String correctWord) {
		this.stage = stage;
		finalWord = correctWord;
		// create new scene
		Scene scene = new Scene(window, 600, 600);
		if (win) {
			setUpPaneWin(account);
			setUpRightPaneWin();
		} else {
			setUpPaneLose(account);
			setUpRightPaneLost();
		}
						
		// set title for the stage
		stage.setTitle("WORDLE RESULTS");
		stage.setScene(scene);
		
		stage.showAndWait();
		
	}

	/***
	 * This method displays a confused man when the user loses the game
	 */
	private void setUpRightPaneLost() {
	    try {
	        Image loseImage = new Image(new FileInputStream("src/resources/lostMan.png"));
	        ImageView view = new ImageView(loseImage);
	        view.setFitHeight(300);
	        view.setFitWidth(250);
	        // center the image in the right pane
	        BorderPane.setAlignment(view, Pos.CENTER);
	        window.setRight(view);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    };
	}
	
	/***
	 * This method will display a jumping animation when the user wins the game
	 */
	private void setUpRightPaneWin() {
	    try {
	        Image onGroundImage = new Image(new FileInputStream("src/resources/onGround.png"));
	        ImageView onGroundView = new ImageView(onGroundImage);
	        onGroundView.setFitHeight(300);
	        onGroundView.setFitWidth(250);
	        BorderPane.setAlignment(onGroundView, Pos.BOTTOM_CENTER);

	        Image inAirImage = new Image(new FileInputStream("src/resources/inAir.jpg"));
	        ImageView inAirView = new ImageView(inAirImage);
	        inAirView.setFitHeight(300);
	        inAirView.setFitWidth(250);
	        BorderPane.setAlignment(inAirView, Pos.BOTTOM_CENTER); // center the image in the right pane

	        // create a translate transition for the in-air image
	        TranslateTransition jumpTransition = new TranslateTransition(Duration.seconds(1.5), inAirView);
	        //move the image up 
	        jumpTransition.setByY(-150); 
	        //reverse it
	        jumpTransition.setAutoReverse(true);
	        //repeat the animation twice
	        jumpTransition.setCycleCount(2); 

	        //switch images and start the animation when the first animation ends
	        jumpTransition.setOnFinished(event -> {
	            window.setRight(onGroundView);
	            PauseTransition pause = new PauseTransition(Duration.seconds(1)); // wait for 1 second
	            pause.setOnFinished(e -> {
	                window.setRight(inAirView); // switch back to the in-air image
	                jumpTransition.play(); 
	            });
	            pause.play();
	        });

	        window.setRight(onGroundView); // start with the on-ground image
	        jumpTransition.play(); // start the jump animation
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}

	/***
	 * This will create/setup the pane when the user loses a game.
	 * 
	 * @param account - null if there is no user signed in, not null if there is
	 */
	private void setUpPaneLose(WordleAccount account) {
		Text topper = new Text("Darn! You'll get it next time!");
		Text correctWord = new Text("Correct Word: " + finalWord);
		topper.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
		
		header = new VBox(topper, correctWord);
		header.setAlignment(Pos.TOP_CENTER);
		
		window.setTop(header);
		window.setStyle("-fx-background-color: #8f8f8f");
		//If someone is signed in, show their guess distribution
		if (account != null) {
			window.setCenter(setUpGraph(account));
		} else {
			window.setCenter(new Text("Create an account or log in to see your guess distribution!"));
		}
		
		if (account != null) {
			//Setup labels for bottom of border pane
			stats.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			gamesPlayedText.setText("  Games Played: " + account.getStats().getNumGamesPlayed());
			gamesPlayedText.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			double percent = (account.getStats().getNumGamesWon() *100 / account.getStats().getNumGamesPlayed());
			gamesWonPercentage.setText("  Games Won: " + percent + "%");
			gamesWonPercentage.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			winStreakDetails.setText("  Current Winstreak: " + account.getStats().getCurrentWinningStreak());
			winStreakDetails.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			maxStreak.setText("  Max Winstreak: "+ account.getStats().getMaxWinningStreak());
			maxStreak.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			statBox = new VBox(stats, gamesWonPercentage, winStreakDetails, maxStreak);
			
			window.setBottom(statBox);
		}	
	}

	/***
	 * This will create/setup the pane when the user wins a game.
	 * 
	 * @param account - null if there is no user signed in, not null if there is
	 */
	private void setUpPaneWin(WordleAccount account) {
		Text topper = new Text("Darn! You'll get it next time!");
		Text correctWord = new Text("Correct Word: " + finalWord);
		topper.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
		
		header = new VBox(topper, correctWord);
		header.setAlignment(Pos.TOP_CENTER);
		
		window.setTop(header);
		window.setStyle("-fx-background-color: #8f8f8f");
		//If someone is signed in, show their guess distribution
		if (account != null) {
			window.setCenter(setUpGraph(account));
		} else {
			window.setCenter(new Text("Create an account or log in to see your guess distribution!"));
		}
		
		if (account != null) {
			//Setup labels for bottom of border pane
			stats.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			gamesPlayedText.setText("  Games Played: " + account.getStats().getNumGamesPlayed());
			gamesPlayedText.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			double percent = (account.getStats().getNumGamesWon() *100 / account.getStats().getNumGamesPlayed());
			gamesWonPercentage.setText("  Games Won: " + percent + "%");
			gamesWonPercentage.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			winStreakDetails.setText("  Current Winstreak: " + account.getStats().getCurrentWinningStreak());
			winStreakDetails.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			maxStreak.setText("  Max Winstreak: "+ account.getStats().getMaxWinningStreak());
			maxStreak.setStyle("-fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
			statBox = new VBox(stats, gamesWonPercentage, winStreakDetails, maxStreak);
			
			window.setBottom(statBox);
		}	
	}

	/***
	 * Setup the guess distribution cahrt to go in the center of the border pane to show the 
	 * current logged in user's guess distribution
	 * 
	 * @return - The BarChart Node to go in the center of the border pane 
	 */
	private BarChart<String, Number> setUpGraph(WordleAccount account) {
		//get credentials for account
		Credentials stats = account.getStats();
		int[] guessDistribution = stats.getGuessDistribution();
		//create bar chart, category so we can have names on the xAxis and a numberAxis for the y
		NumberAxis yAxis = new NumberAxis();
		yAxis.setUpperBound(stats.getNumGamesPlayed());
		yAxis.setTickUnit(1);
		yAxis.setAutoRanging(false);
		yAxis.setMinorTickVisible(false);
		yAxis.setMinorTickCount(0);
		yAxis.setStyle("-fx-text-fill: white;");
		//create x-Axis
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setStyle("-fx-text-fill: white;");
		BarChart<String, Number> guessChart = new BarChart<>(xAxis, yAxis);
		guessChart.setTitle("Guess Distribution");
		guessChart.getYAxis().setLabel("Total Games");
		//create guess distribution series
		XYChart.Series<String, Number> playerGuessDistribution = new XYChart.Series<>();
		playerGuessDistribution.getData().add(new XYChart.Data<>("Guess1", guessDistribution[0]));
		playerGuessDistribution.getData().add(new XYChart.Data<>("Guess2", guessDistribution[1]));
		playerGuessDistribution.getData().add(new XYChart.Data<>("Guess3", guessDistribution[2]));
		playerGuessDistribution.getData().add(new XYChart.Data<>("Guess4", guessDistribution[3]));
		playerGuessDistribution.getData().add(new XYChart.Data<>("Guess5", guessDistribution[4]));
		playerGuessDistribution.getData().add(new XYChart.Data<>("Guess6", guessDistribution[5]));
		//add series to chart
		guessChart.getData().add(playerGuessDistribution);
		guessChart.setStyle("-fx-background-color: #8f8f8f");
		//return chart
		return guessChart;
	}
	
	
}
