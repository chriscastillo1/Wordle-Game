package view_controller;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.WordleLeaderBoard;


/***
 * 
 * @author Chris Castillo, Anisha Munjal, Michael Beccarelli, Edan Uccetta
 * 
 * This is the LeaderBoardPane class for the GUI interface.
 * 
 * Responsibilities:
 *  - create the leaderboard pane
 *  - display the top ten highest scoring players for the user to see
 *
 */


public class LeaderBoardPane {

	private WordleLeaderBoard visualLeaderBoard = new WordleLeaderBoard();
	
	/***
	 * The main stage for the GUI pane elements
	 */
	private BorderPane window = new BorderPane();
	
	/***
	 * The listview to display 
	 */
	private ListView<String> leaderListView;
	
	/***
	 * The observable list to create teh listview
	 */
	private ObservableList<String> leaderList;
	
	/***
	 * The header text for the top of the borderpane
	 */
	private Text headerText = new Text("LEADERBOARD");
	
	/***
	 * The stage
	 */
	Stage stage;
	
	/***
	 * CONSTRUCTOR
	 * 
	 * @param stage - the stage to show the scene on
	 */
	public LeaderBoardPane(Stage stage) {
		this.stage = stage;
		// create new scene
		Scene scene = new Scene(window, 400, 480);
		setUpPane();

		// set title for the stage
		stage.setTitle("TOP 10 LEADERBOARD");
		stage.setScene(scene);
		
		stage.show();
		
	}

	/***
	 * Setup the leadeBoard pane to show the top ten players of wordle sorted by score
	 */
	private void setUpPane() {
		//get listview for leaderboard
		ArrayList<String> leaders = visualLeaderBoard.getFirstTen();
		leaderList = FXCollections.observableArrayList(leaders);
		leaderListView = new ListView<>();
		leaderListView.setItems(leaderList);
		
		// Set the background color, font family, and font size of the ListView
		window.setStyle("-fx-background-color: #e6e6e6; -fx-font-family: \"Gill Sans\"; -fx-font-size: 14pt;");

		window.setTop(headerText);
		window.setCenter(leaderListView);	
	}
	
}
