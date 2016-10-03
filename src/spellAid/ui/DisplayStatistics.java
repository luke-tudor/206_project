package spellAid.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
/**
 * 
 * Displays the statistics window and allows users to select what statistics level they wish to view
 * @author Aprajit Gandhi and Luke Tudor
 *
 */
public class DisplayStatistics extends Application implements EventHandler<ActionEvent> {

	private final ComboBox<String> levelSelectCombo;
	private final Button displayButton;
	private final Label displayArea;

	private final List<Set<String>> wordlist;
	private final List<List<String>> masteredList;
	private final List<List<String>> failedList;
	
	private Scene root;
	
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Statistics");
		primaryStage.setScene(root);
		primaryStage.show();
	}

	public DisplayStatistics(List<Set<String>> wordlist,List<List<String>> masteredList, List<List<String>> failedList, int currentLevel) {
		super();
		
		this.wordlist = wordlist;
		this.masteredList = masteredList;
		this.failedList = failedList;
		
		List<String> levels = new ArrayList<>();
		for (int i = 0; i < wordlist.size(); i++){
			levels.add("Level " + (i+1) + " Statistics");
		}
		levelSelectCombo = new ComboBox<>(FXCollections.observableList(levels));

		displayButton = new Button("Display Statistics");
		displayArea = new Label();
		
		levelSelectCombo.setOnAction(this);
		displayButton.setOnAction(this);
		
		updateStatisticsDisplay(currentLevel);
		levelSelectCombo.getSelectionModel().select(currentLevel - 1);
		
		GridPane controls = new GridPane();
		controls.setHgap(5);
		controls.add(levelSelectCombo, 0, 0);
		controls.add(displayButton, 1, 0);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5));
		grid.setVgap(5);
		grid.add(controls, 0, 0);
		grid.add(displayArea, 0, 1);
		
		root = new Scene(grid);
	}

	private void updateStatisticsDisplay(int levelToBeShown){
		/*
		 * This is a local class that is used as a helper object to count the 
		 * number of times a word appears in a list.
		 */
		class WordCounter {
			private int wordCount(String word, List<String> list){
				int numWords = 0;
				for (String entry : list){
					if (word.equals(entry)){
						numWords++;
					}
				}
				return numWords;
			}
		}
		
		/*
		 * If there are no statistics (all the files are empty or don't exist), 
		 * the program simply tells the user with a message.
		 */
		if (masteredList.get(levelToBeShown-1).isEmpty() 
				&& failedList.get(levelToBeShown-1).isEmpty()) {
			displayArea.setText("No Statistics to Display");
			
			return;
		}
		/*
		 * This creates a string which contains all the statistics for each word
		 * in the word list.
		 */
		StringBuilder statsList = new StringBuilder();

		WordCounter wordCounter = new WordCounter();	

		for (String word : wordlist.get(levelToBeShown-1)){

			int numMastered = wordCounter.wordCount(word,
					masteredList.get(levelToBeShown-1));
			int numFailed = wordCounter.wordCount(word,
					failedList.get(levelToBeShown-1));

			/*
			 * Only attempted words are added to the statistics, hence the if
			 * statement that evaluates as false if a word has never been tested.
			 */
			if (numMastered != 0 || numFailed != 0){
				// Format the string to something easy to read for a human.
				statsList.append(
						String.format(
								"%20s: mastered(%d) failed(%d)\n",
								word, numMastered, numFailed));
			}
		}

		displayArea.setText(statsList.toString());

	}

	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == displayButton){
			updateStatisticsDisplay(levelSelectCombo.getSelectionModel().getSelectedIndex() + 1);
		}
	}
}
