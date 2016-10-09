package spellAid.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import spellAid.ui.NewList;

public class TestFileChooser extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		NewList nl = new NewList();
		primaryStage.setScene(new Scene(new BorderPane()));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
