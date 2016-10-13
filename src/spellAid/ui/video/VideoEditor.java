package spellAid.ui.video;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import spellAid.ui.AppDim;

public class VideoEditor extends Application {
	
	private Scene scene;
	
	public void start(Stage primaryStage) {
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public VideoEditor() {
		super();
		
		VideoPanel vp = new VideoPanel();
		
		VideoManipulator vm = new VideoManipulator() {
			
			@Override
			void doWhenStarting() {
				vp.stop();
			}
			
			@Override
			void doWhenFinished() {
				vp.setVideo("videos/out.mp4");
				vp.start();
			}
		};
		
		BorderPane root = new BorderPane();
		root.setTop(vp);
		root.setBottom(vm);
		root.setPrefSize(AppDim.WIDTH, AppDim.HEIGHT);;
		
		scene = new Scene(root);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
