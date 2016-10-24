package spellAid.ui.video;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import spellAid.ui.AppDim;
import spellAid.ui.BackButton;
import spellAid.util.string.URLString;

/**
 * This class is a combined video viewer and editor.
 * 
 * @author Luke Tudor
 */
public class VideoEditor extends Application {
	
	private static final String STYLESHEET = new URLString("style/mainstyle.css").getURL();
	
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
		
		// Prompt the user if they want to go back to the quiz screen
		BackButton back = new BackButton();
		back.setOnAction(e ->
			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				
				ButtonType yes = new ButtonType("Yes");
				ButtonType no = new ButtonType("No");
				
				alert.getButtonTypes().setAll(yes, no);
				alert.setTitle("Alert!");
				alert.setContentText("Are you sure you want to go back to the quiz?");
				Optional<ButtonType> reply = alert.showAndWait();
				if (reply.get() == yes) {
					vp.stop();
					primaryStage.setScene(parent);
				}
			})
		);
		
		HBox backPanel = new HBox(back);
		backPanel.setPadding(new Insets(5));
		backPanel.setAlignment(Pos.TOP_LEFT);
		
		BorderPane root = new BorderPane();
		root.setTop(backPanel);
		root.setCenter(vp);
		root.setBottom(vm);
		root.setPrefSize(AppDim.WIDTH.getValue(), AppDim.HEIGHT.getValue());
		root.getStylesheets().add(STYLESHEET);
		
		scene = new Scene(root);
	}

}
