package spellAid.ui.video;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VideoEditor extends Application {
	
	private Scene scene;
	
	public void start(Stage primaryStage) {
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public VideoEditor() {
		super();
		
		VideoPanel vp = new VideoPanel();
		
		VideoManipulator vm = new VideoManipulator();
		
		BorderPane root = new BorderPane();
		root.setTop(vp);
		root.setBottom(vm);
		
		scene = new Scene(root);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
