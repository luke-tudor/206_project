package spellAid.ui;

import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * This class creates and shows a video using VLC media player.
 * 
 * @author Luke Tudor
 *
 */
public class VideoPanel extends BorderPane {

	private final MediaPlayer player;

	private final ButtonPanel btnPanel;

	public VideoPanel () {
		super();
		
		setPadding(new Insets(5));

		// Create a panel which will contain the media component
		BorderPane contentPanel = new BorderPane();
		contentPanel.setPrefSize(600, 400);

		Media video = null;
		Path path = FileSystems.getDefault().getPath("big_buck_bunny_1_minute.mp4");
		try {
			video = new Media(path.toUri().toURL().toString());
		} catch (MalformedURLException e) {}

		player = new MediaPlayer(video);

		MediaView mediaView = new MediaView(player);
		mediaView.setFitWidth(contentPanel.getPrefWidth());
		mediaView.setFitHeight(contentPanel.getPrefHeight());

		contentPanel.setCenter(mediaView);

		// Create and configure a panel for the buttons
		btnPanel = new ButtonPanel();
		btnPanel.setStyle("-fx-background-color: lightgrey;");
		for (Node n : btnPanel.getChildren())
			n.setDisable(true);

		setCenter(contentPanel);
		setBottom(btnPanel);
	}

	public void start() {
		for (Node n : btnPanel.getChildren())
			n.setDisable(false);

		player.play();
	}

	public void release() {
		player.dispose();
	}

	private class ButtonPanel extends FlowPane implements EventHandler<ActionEvent> {

		private static final String MUTEXT = "Mute";
		private static final String PATEXT = "Pause";
		private static final String PLTEXT = "Play";
		private static final String STTEXT = "Stop";

		private Button play;
		private Button mute;
		private Button stop;

		private ButtonPanel() {

			setAlignment(Pos.CENTER);
			setPadding(new Insets(5));
			setHgap(5);

			play = new Button(PATEXT);
			mute = new Button(MUTEXT);
			stop = new Button(STTEXT);

			Button[] buttons = new Button[]{play, mute, stop};
			for(Button btn : buttons) {
				btn.setOnAction(this);
				getChildren().add(btn);
			}
		}

		@Override
		public void handle(ActionEvent e) {
			if (e.getSource() == play) {
				if (play.getText().equals(PLTEXT)){
					player.play();
					play.setText(PATEXT);
				} else {
					player.pause();
					play.setText(PLTEXT);
				}
			} else if (e.getSource() == mute){
				if (player.isMute())
					player.setMute(false);
				else 
					player.setMute(true);
			} else {
				player.stop();
				play.setText(PLTEXT);
			}
		}
	}
}
