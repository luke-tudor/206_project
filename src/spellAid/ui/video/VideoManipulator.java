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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
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

		submit = new Button("Make Video");
		submit.setOnAction(e -> createVideo());

		progressBar = new ProgressBar(0);

		cPanel = new ControlPanel();

		originalFileSize = FileSystems.getDefault().getPath("videos/big_buck_bunny_1_minute.mp4").toFile().length();

		newFile = FileSystems.getDefault().getPath("videos/out.mp4").toFile();

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
				String cmd = "ffmpeg -y -i videos/big_buck_bunny_1_minute.mp4" 
						+ options + " -strict -2 videos/out.mp4";
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
				try {
					Process process = pb.start();
					process.waitFor();
				} catch (IOException | InterruptedException e) {}
				Platform.runLater(() -> asynchronousFinish());
			}

		});

		worker.setDaemon(true);

		doWhenStarting();

		worker.start();
	}

	private void asynchronousFinish() {
		submit.setDisable(false);
		processFinished = true;
		doWhenFinished();
	}

	void doWhenStarting() {}

	void doWhenFinished() {}

	private void updateProgress() {
		if (processFinished) {
			progressBar.setProgress(1);
		} else {
			progressBar.setProgress(newFile.length()/originalFileSize);
		}
	}

	private class ControlPanel extends VBox {
		
		private RadioButton negate;

		private Slider fps;

		private Slider[] sliders;

		private ControlPanel() {
			Label[] labels = new Label[]{new Label("Rendering Progress:"), new Label("Invert Colour:"),
					new Label("Change Framerate:")};
			negate = new RadioButton();
			fps = new Slider(1, 24, 24);
			//fps.setShowTickLabels(true);

			GridPane root = new GridPane();
			root.setVgap(10);
			root.setHgap(10);
			root.setAlignment(Pos.CENTER);
			setPadding(new Insets(5));

			for (int i = 0; i < labels.length; i++) {
				root.add(labels[i], 0, i);
			}
			
			labels = new Label[] {new Label("Change Contrast:"), new Label("Change Brightness:"),
					new Label("Change Saturation:")};
			
			for (int i = 0; i < labels.length; i++) {
				root.add(labels[i], 2, i);
			}
			
			sliders = new Slider[3];
			sliders[0] = new Slider(-2, 2, 1);
			sliders[1] = new Slider(-1, 1, 0);
			sliders[2] = new Slider(0, 3, 1);

			for (int i = 0; i < sliders.length; i++) {
				root.add(sliders[i], 3, i);
				//sliders[i].setShowTickLabels(true);
			}

			root.add(progressBar, 1, 0);
			root.add(negate, 1, 1);
			root.add(fps, 1, 2);
			
			for (Node n : root.getChildren()) {
				((Control) n).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			}
			
			getChildren().add(root);
			setAlignment(Pos.CENTER);
		}

		private String getSettings() {
			StringBuilder sb = new StringBuilder();
			sb.append(" -vf eq=contrast=" + sliders[0].getValue() 
					+ ":brightness=" + sliders[1].getValue() 
					+ ":saturation=" + sliders[2].getValue());
			if (negate.isSelected()) {
				sb.append(" -vf negate");
			}
			sb.append(" -r " + fps.getValue());
			return sb.toString();
		}
	}
}
