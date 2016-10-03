package spellAid.ui;

import java.util.ArrayList;
import java.util.List;

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
 * Creates a Frame that allows the user to choose what voice they want
 * and what difficulty level they want.
 * 
 * @author Aprajit Gandhi and Luke Tudor
 *
 */
public abstract class DisplayOptions extends Application implements EventHandler<ActionEvent> {
	
	private final ComboBox<String> voiceCombo;
	private final ComboBox<String> levelCombo;
	
	private final Button submitButton;
	
	private Scene root;
	
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Options");
		primaryStage.setScene(root);
		primaryStage.show();
	}
	
	
	public DisplayOptions(int numOfLevels, int currentLevel, String currentSpeech){
		super();
		
		List<String> voices = new ArrayList<>();
		voices.add("NZ voice");
		voices.add("USA voice");
		
		voiceCombo = new ComboBox<>(FXCollections.observableList(voices));
		
		List<String> levels = new ArrayList<>();

		for (int i = 0; i < numOfLevels; i++){
			levels.add("NZCER Level " + (i+1));
		}
		
		levelCombo = new ComboBox<>(FXCollections.observableList(levels));
		
		Label voiceLabel = new Label("Voice:");
		Label levelLabel = new Label("List:");
		
		submitButton = new Button("Submit");	
		
		levelCombo.getSelectionModel().select(currentLevel - 1);
		voiceCombo.getSelectionModel().select(currentSpeech);
		
		voiceCombo.setOnAction(this);
		levelCombo.setOnAction(this);
		submitButton.setOnAction(this);
		
		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5));
		grid.add(voiceLabel, 0, 0);
		grid.add(voiceCombo, 2, 0);
		grid.add(levelLabel, 0, 1);
		grid.add(levelCombo, 2, 1);
		voiceCombo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		levelCombo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		grid.add(submitButton, 1, 3);
		
		root = new Scene(grid);
	}
	
	// Overridden by the SpellingAid class to change the voice
	protected abstract void changeSpeech(String voice);
	
	// Overridden by the SpellingAid class to change level
	protected abstract void changeLevel(int level);

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == voiceCombo) {
			changeSpeech(voiceCombo.getSelectionModel().getSelectedItem());
		} else if (e.getSource() == levelCombo) {
			changeLevel(levelCombo.getSelectionModel().getSelectedIndex() + 1);
		} else if (e.getSource() == submitButton) {
			primaryStage.hide();
		}
		
	}

}
