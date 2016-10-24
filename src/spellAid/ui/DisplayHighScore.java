package spellAid.ui;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import spellAid.util.io.ExtendedIOHelper;
import spellAid.util.string.URLString;
import spellAid.util.string.UnqualifiedFileString;

/**
 * This class represents a simple high score screen. The high score screen contains
 * a heading, which displays the current list and sublist. The high score screen also
 * contains a label which displays the top 3 times for the sublist. User names are also
 * provided if available.
 * 
 * @author Luke Tudor
 */
public class DisplayHighScore extends Application {
	
	private static final String STYLESHEET = new URLString("style/mainstyle.css").getURL();
	
	private ExtendedIOHelper ioHelper;
	
	private ComboBox<String> sublistCombo;
	
	private Label headingLabel;
	
	private Label scoreLabel;
	
	private Scene scene;
	
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public DisplayHighScore(Scene parent, String currentWordList, String currentSubList, List<String> sublists) {
		super();
		
		ioHelper = new ExtendedIOHelper();
		
		headingLabel = new Label();
		
		scoreLabel = new Label();
		
		changeDisplay(currentWordList, currentSubList);
		
		sublistCombo = new ComboBox<>(FXCollections.observableList(sublists));
		sublistCombo.setOnAction(e ->
			changeDisplay(currentWordList, sublistCombo.getSelectionModel().getSelectedItem())
		);
		sublistCombo.getSelectionModel().select(currentSubList);
		
		VBox vbox = new VBox(sublistCombo, headingLabel, scoreLabel);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(5));
		vbox.setAlignment(Pos.CENTER);
		
		Button back = new BackButton();
		back.setOnAction(e -> primaryStage.setScene(parent));

		HBox hbox = new HBox(back);
		hbox.setPadding(new Insets(5));
		hbox.setAlignment(Pos.TOP_LEFT);
		
		BorderPane root = new BorderPane();
		root.setTop(hbox);
		root.setCenter(vbox);
		root.setPrefSize(AppDim.WIDTH.getValue(), AppDim.HEIGHT.getValue());
		root.getStylesheets().add(STYLESHEET);
		
		scene = new Scene(root);
	}
	
	// This method updates the labels to use the correct word list and sub list for display
	private void changeDisplay(String currentWordList, String currentSubList) {
		String unqWordList = new UnqualifiedFileString(currentWordList).getUnqualifiedFile();
		List<String> scores = ioHelper.readAllLines("user_lists/." 
				+ unqWordList + "." + currentSubList + ".score.txt");

		String heading = "Fastest Times to Complete\nList: " 
				+ unqWordList + "\nSublist: " + currentSubList + "\n";
		
		StringBuilder score = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			String line = null;
			try {
				String[] chunks = scores.get(i).split("\t");
				line = " User name: " + chunks[0] + "\t" + "Time: " + chunks[1];
			} catch (Exception e) {
				line = " --";
			}
			score.append(i+1 + "." + line + "\n");
		}
		
		headingLabel.setText(heading);
		
		scoreLabel.setText(score.toString());
	}

}
