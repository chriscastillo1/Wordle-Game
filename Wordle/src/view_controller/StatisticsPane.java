package view_controller;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Credentials;
import model.WordleAccount;
import model.WordleLeaderBoard;


/***
 * 
 * @author Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * 
 * This is the Statistics Pane for the GUI interface.
 * 
 * Responsibilities:
 *  - create the statistics pane
 *  - display statistics for the currently logged in user.
 *  
 *  Potential improvements:
 *  - Add the ability to view statistics for other users.
 *
 */


public class StatisticsPane {
	
	/***
	 * The main panel
	 */
	private BorderPane window = new BorderPane();
	
	/***
	 * Border pane header text
	 */
	private Text headerText = new Text();
	
	/***
	 * Grid of statistics details
	 */
	private GridPane statsPane = new GridPane();
	private Label gamesPlayedText = new Label();
	private Label gamesWonText = new Label();
	private Label winStreakDetails = new Label();
	private BarChart<String, Number> guessDistribute;
	
	/***
	 * The stage
	 */
	Stage stage;
	
	/***
	 * The user for this statistics pane
	 */
	WordleAccount user;
	
	/***
	 * CONSTRUCTOR
	 * 
	 * @param stage - the stage to show the scene on
	 * @param user - the user to display statistics on
	 */
	public StatisticsPane(Stage stage, WordleAccount user) {
		this.stage = stage;
		this.user = user;
		// create new scene
		Scene scene = new Scene(window, 400, 600);
		setUpPane();

		// set title for the stage
		stage.setTitle("Statistics");
		stage.setScene(scene);
		
		stage.show();
		
	}

	/***
	 * Setup the leadeBoard pane to show the top ten players of wordle sorted by score
	 */
	private void setUpPane() {
		// Set up visuals
		window.setStyle("-fx-background-color: #e6e6e6; -fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");
		window.setTop(headerText);
		window.setPadding(new Insets(10));
		headerText.setText("Statistics for user " + user.getUserName());
		gamesPlayedText.setText("Games Played: " + user.getStats().getNumGamesPlayed());
		gamesWonText.setText("Games Won: " + user.getStats().getNumGamesWon());
		winStreakDetails.setText("Cur Winstreak: " + user.getStats().getCurrentWinningStreak() + ". Best: " + user.getStats().getMaxWinningStreak());
		statsPane.add(gamesPlayedText, 1, 1);
		statsPane.add(gamesWonText, 1, 2);
		statsPane.add(winStreakDetails, 1, 3);
		statsPane.setHgap(10);
		statsPane.setVgap(10);
		window.setCenter(statsPane);
		guessDistribute = setUpGraph(user);
		window.setBottom(guessDistribute);
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
