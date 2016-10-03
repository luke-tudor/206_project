package spellAid.ui.video;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class VideoManipulator extends VBox {

	private Button submit;
	
	private ProgressBar progressBar;

	private ControlPanel cPanel;
	
	private double originalFileSize;
	
	private File newFile;
	
	private boolean processFinished;

	public VideoManipulator() {
		super();

		submit = new Button("Submit");
		submit.setOnAction(e -> createVideo());

		progressBar = new ProgressBar(0);
		
		cPanel = new ControlPanel();
		
		originalFileSize = FileSystems.getDefault().getPath("big_buck_bunny_1_minute.mp4").toFile().length();
		
		newFile = FileSystems.getDefault().getPath("out.mp4").toFile();
		
		processFinished = true;
		
		getChildren().add(cPanel);
		getChildren().add(submit);
		setAlignment(Pos.CENTER);

		setPadding(new Insets(5));
		
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(200), e -> updateProgress()));
		timer.setCycleCount(Animation.INDEFINITE);
		timer.play();
	}

	private void createVideo() {

		submit.setDisable(true);
		
		processFinished = false;

		Thread worker = new Thread(new Runnable() {

			@Override
			public void run() {
				String options = cPanel.getSettings();
				String cmd = "ffmpeg -y -i big_buck_bunny_1_minute.mp4" 
						+ options + " -strict -2 out.mp4";
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
				try {
					Process process = pb.start();
					process.waitFor();
				} catch (IOException | InterruptedException e) {}
				Platform.runLater(() -> asynchronousFinish());
			}

		});

		worker.setDaemon(true);

		worker.start();
	}

	private void asynchronousFinish() {
		submit.setDisable(false);
		processFinished = true;
	}
	
	private void updateProgress() {
		if (processFinished) {
			progressBar.setProgress(1);
		} else {
			progressBar.setProgress(newFile.length()/originalFileSize);
		}
	}

	private class ControlPanel extends GridPane {
		
		private Label[] labels;

		private RadioButton negate;

		private Slider fps;

		private TextField text;

		private ControlPanel() {
			labels = new Label[]{new Label("Rendering Progress:"), new Label("Invert Colour:"),
					new Label("Change Framerate:"), new Label("Add text:")};
			negate = new RadioButton();
			fps = new Slider(1, 24, 24);
			fps.setShowTickLabels(true);
			text = new TextField();
			text.setPrefWidth(100);

			setVgap(5);
			setHgap(5);
			setPadding(new Insets(5));
			
			for (int i = 0; i < labels.length; i++){
				add(labels[i], 0, i);
			}

			add(progressBar, 1, 0);
			add(negate, 1, 1);
			add(fps, 1, 2);
			add(text, 1, 3);
		}

		private String getSettings() {
			StringBuilder sb = new StringBuilder();
			sb.append(" -vf \"drawtext=/usr/share/fonts/truetype/abyssinica/AbyssinicaSIL-R.ttf"
					+ ":text=" + text.getText() +":fontsize=30:x=(w-text_w)/2:y=(h-text_h)/2\"");
			if (negate.isSelected()) {
				sb.append(",negate");
			}
			sb.append(" -r " + fps.getValue());
			return sb.toString();
		}
	}
}
