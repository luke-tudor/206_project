package spellAid.ui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class VideoManipulator extends BorderPane implements EventHandler<ActionEvent> {

	private Button submit;
	
	public VideoManipulator() {
		super();
		
		submit = new Button("Submit");
		submit.setOnAction(this);
		
		setCenter(submit);
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == submit) {
			createVideo();
		}
	}
	
	private void createVideo() {
		String options = "-vf negate";
		String cmd = "ffmpeg -y -i big_buck_bunny_1_minute.avi " + options + " out.avi";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
		try {
			pb.start();
		} catch (IOException e) {}
	}
}
