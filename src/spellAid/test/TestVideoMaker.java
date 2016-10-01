package spellAid.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import spellAid.ui.VideoManipulator;

public class TestVideoMaker extends Application {
	
	@Override
	public void start(final Stage primaryStage) throws Exception {
		
		VideoManipulator man = new VideoManipulator();
		
		Scene root = new Scene(man);
		primaryStage.setScene(root);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
