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
	private final ComboBox<String> listCombo;
	private final ComboBox<String> sublistCombo;

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


	public DisplayOptions(String[] lists, String currentList, List<String> sublists, String currentSubList, String currentSpeech) {
		super();

		List<String> voices = new ArrayList<>();
		voices.add("NZ voice");
		voices.add("USA voice");

		voiceCombo = new ComboBox<>(FXCollections.observableList(voices));

		listCombo = new ComboBox<>(FXCollections.observableArrayList(lists));

		sublistCombo = new ComboBox<>(FXCollections.observableList(sublists));

		Label voiceLabel = new Label("Voice:");
		Label listLabel = new Label("List:");
		Label sublistLabel = new Label("Sub-list:");

		submitButton = new Button("Submit");	

		voiceCombo.getSelectionModel().select(currentSpeech);
		listCombo.getSelectionModel().select(currentList);
		sublistCombo.getSelectionModel().select(currentSubList);

		voiceCombo.setOnAction(this);
		listCombo.setOnAction(this);
		sublistCombo.setOnAction(this);
		submitButton.setOnAction(this);

		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5));
		grid.add(voiceLabel, 0, 0);
		grid.add(voiceCombo, 2, 0);
		grid.add(listLabel, 0, 1);
		grid.add(listCombo, 2, 1);
		grid.add(sublistLabel, 0, 2);
		grid.add(sublistCombo, 2, 2);
		voiceCombo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		listCombo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		sublistCombo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		grid.add(submitButton, 1, 3);

		root = new Scene(grid);
	}

	// Overridden by the SpellingAid class to change the voice
	protected abstract void changeSpeech(String voice);

	// Overridden by the SpellingAid class to change level
	protected abstract void changeSublist(String sublist);

	protected abstract void changeList(String list, ComboBox<String> sublistCombo);

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == voiceCombo) {
			changeSpeech(voiceCombo.getSelectionModel().getSelectedItem());
		} else if (e.getSource() == listCombo) {
			changeList(listCombo.getSelectionModel().getSelectedItem(), sublistCombo);
		} else if (e.getSource() == sublistCombo) {
			changeSublist(sublistCombo.getSelectionModel().getSelectedItem());
		} else if (e.getSource() == submitButton) {
			primaryStage.hide();
		}

	}

}
