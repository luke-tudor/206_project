package spellAid.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import spellAid.ui.ScorePanel;

public class TestScorePanel extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ScorePanel sp = new ScorePanel();
		
		Scene scene = new Scene(sp);
		primaryStage.setScene(scene);
		primaryStage.show();
		sp.startTimer();
	}
}
