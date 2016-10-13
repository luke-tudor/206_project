package spellAid.test;

import javafx.application.Application;
import javafx.stage.Stage;
import spellAid.ui.video.VideoEditor;

public class TestVideoEditor extends Application {
	
	public void start(Stage primaryStage) {
		VideoEditor ve = new VideoEditor(null);
		
		ve.start(primaryStage);
	}
	public static void main(String[] args) {
		launch(args);
	}
}
