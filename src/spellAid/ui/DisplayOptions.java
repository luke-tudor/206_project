package spellAid.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import spellAid.ui.speaker.AsynchronousComponentEnabler;
import spellAid.ui.speaker.ConcurrentAsynchronousSpeaker;
import spellAid.ui.speaker.Speaker;
import spellAid.util.string.URLString;
import spellAid.util.string.UnqualifiedFileString;

/**
 * Creates a Frame that allows the user to choose what voice they want
 * and what difficulty level they want.
 * 
 * @author Aprajit Gandhi and Luke Tudor
 *
 */
public abstract class DisplayOptions extends Application {

	private static final String STYLESHEET = new URLString("style/mainstyle.css").getURL();

	private ComboBox<String> voiceCombo;
	ComboBox<String> listCombo;
	ComboBox<String> sublistCombo;

	private Scene scene;

	Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Options");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/*
	 * Constructs the options frame providing all the dependencies from the parent class.
	 */
	public DisplayOptions(Scene parent, String[] lists, String currentList, List<String> sublists, String currentSubList, String currentSpeech) {
		super();
		
		List<String> voices = new ArrayList<>();
		voices.add("NZ voice");
		voices.add("USA voice");

		voiceCombo = new ComboBox<>(FXCollections.observableList(voices));

		listCombo = new ComboBox<>(FXCollections.observableList(getAllVisibleFiles(lists)));

		sublistCombo = new ComboBox<>(FXCollections.observableList(sublists));

		Label voiceLabel = new Label("Voice:");
		Label listLabel = new Label("List:");
		Label sublistLabel = new Label("Sub-list:");

		voiceCombo.getSelectionModel().select(currentSpeech);
		listCombo.getSelectionModel().select(new UnqualifiedFileString(currentList).getUnqualifiedFile());
		sublistCombo.getSelectionModel().select(currentSubList);

		voiceCombo.setOnAction(e -> changeSpeech(voiceCombo.getSelectionModel().getSelectedItem()));
		listCombo.setOnAction(e -> changeList(listCombo.getSelectionModel().getSelectedItem()));
		sublistCombo.setOnAction(e -> changeSublist(sublistCombo.getSelectionModel().getSelectedItem()));
		
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
		grid.setAlignment(Pos.CENTER);

		Button back = new BackButton();
		back.setOnAction(e -> primaryStage.setScene(parent));

		HBox hbox = new HBox(back);
		hbox.setPadding(new Insets(5));
		hbox.setAlignment(Pos.TOP_LEFT);
		
		Button addList = new Button("Add List");
		addList.setOnAction(e -> addList());
		
		Button testVoice = new Button("Test Voice");
		testVoice.setOnAction(e -> {
			Speaker speaker = new ConcurrentAsynchronousSpeaker(
					new AsynchronousComponentEnabler(new Node[]{testVoice}, true), getVoice(voiceCombo.getSelectionModel().getSelectedItem())) {
				@Override
				protected void asynchronousFinish() {}
			};
			speaker.speak("This is the " + voiceCombo.getSelectionModel().getSelectedItem());
		});
		
		VBox vbox = new VBox(grid, addList, testVoice);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(5);

		BorderPane root = new BorderPane();
		root.setTop(hbox);
		root.setCenter(vbox);
		root.setPrefSize(AppDim.WIDTH.getValue(), AppDim.HEIGHT.getValue());
		root.getStylesheets().add(STYLESHEET);

		scene = new Scene(root);

	}

	// Overridden by the SpellingAid class to change the voice, sublist and List.
	protected abstract void changeSpeech(String voice);
	protected abstract void changeSublist(String sublist);
	protected abstract void changeList(String list);
	protected abstract void addList();
	protected abstract String getVoice(String selection);
	
	List<String> getAllVisibleFiles(String[] lists) {
		List<String> files = new ArrayList<>();
		for (String file : lists) {
			if (!file.startsWith(".")) {
				files.add(file);
			}
		}
		return files;
	}
	
}