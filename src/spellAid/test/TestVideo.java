package spellAid.test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import spellAid.ui.VideoPanel;

public class TestVideo extends Application {
	
	VideoPanel video;

	@Override
	public void start(final Stage primaryStage) throws Exception {
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				video.release();
				primaryStage.hide();
			}
			
		});
		video = new VideoPanel();
		BorderPane pane = new BorderPane();
		pane.setCenter(video);
		
		Scene root = new Scene(pane);
		primaryStage.setScene(root);
		primaryStage.show();
		video.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
