package spellAid.test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import spellAid.ui.video.VideoPanel;

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
		
		Scene root = new Scene(video);
		primaryStage.setScene(root);
		primaryStage.show();
		video.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
