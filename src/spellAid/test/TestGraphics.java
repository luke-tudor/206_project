package spellAid.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import spellAid.ui.GraphicsFactory;
import spellAid.ui.GraphicsPanel;

public class TestGraphics extends Application {
	
	GraphicsPanel panel;
	
	static GraphicsFactory gf = new GraphicsFactory();

	@Override
	public void start(final Stage primaryStage) throws Exception {
		
		panel = new GraphicsPanel(3);
		panel.updateGraphic(1, gf.getNewCrossShape(), "TEST");
		panel.updateGraphic(2, gf.getNewTickShape(), "text");

		Scene root = new Scene(panel);
		primaryStage.setScene(root);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
