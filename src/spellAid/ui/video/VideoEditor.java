package spellAid.ui.video;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import spellAid.ui.AppDim;
import spellAid.ui.BackButton;

public class VideoEditor extends Application {
	
	private Scene scene;
	
	private Stage primaryStage;
	
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public VideoEditor(Scene parent) {
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
		
		BackButton back = new BackButton();
		back.setOnAction(e -> primaryStage.setScene(parent));
		
		HBox backPanel = new HBox(back);
		backPanel.setPadding(new Insets(5));
		backPanel.setAlignment(Pos.TOP_LEFT);
		
		BorderPane root = new BorderPane();
		root.setTop(backPanel);
		root.setCenter(vp);
		root.setBottom(vm);
		root.setPrefSize(AppDim.WIDTH.getValue(), AppDim.HEIGHT.getValue());
		
		scene = new Scene(root);
	}

}
