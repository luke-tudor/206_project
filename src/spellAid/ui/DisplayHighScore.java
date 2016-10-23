package spellAid.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import spellAid.util.string.URLString;

/**
 * This class represents a simple high score screen. The high score screen contains
 * a heading, which displays the current list and sublist. The high score screen also
 * contains a label which displays the top 3 times for the sublist. Usernames are also
 * provided if available.
 * 
 * @author Luke Tudor
 */
public class DisplayHighScore extends Application {
	
	private static final String STYLESHEET = new URLString("style/mainstyle.css").getURL();
	
	private Scene scene;
	
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("High Scores");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public DisplayHighScore(Scene parent, String score, String heading) {
		super();
		
		Label headingLabel = new Label(heading);
		
		Label scoreLabel = new Label(score);
		
		VBox vbox = new VBox(headingLabel, scoreLabel);
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

}
