package spellAid.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import spellAid.ui.Stopwatch;

public class TestStopwatch extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Scene root = new Scene(new Stopwatch());
		primaryStage.setScene(root);
		primaryStage.show();
	}
}
