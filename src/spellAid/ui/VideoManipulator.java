package spellAid.ui;

import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class VideoManipulator extends BorderPane {
	
	private Button submit;
	
	private ControlPanel cPanel;
	
	public VideoManipulator() {
		super();
		
		submit = new Button("Submit");
		submit.setOnAction(e -> createVideo());
		
		cPanel = new ControlPanel();
		cPanel.setAlignment(Pos.CENTER);
		
		FlowPane flow = new FlowPane(submit);
		flow.setAlignment(Pos.CENTER);
		
		setCenter(cPanel);
		setBottom(flow);
		setPadding(new Insets(5));
	}
	
	private void createVideo() {
		String options = cPanel.getSettings();
		String cmd = "ffmpeg -y -i big_buck_bunny_1_minute.mp4 "+options+" -strict -2 out.mp4";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
		try {
			pb.start();
		} catch (IOException e) {}
	}
	
	private class ControlPanel extends GridPane {
		
		private RadioButton negate;
		
		private Slider fps;
		
		private ControlPanel() {
			negate = new RadioButton("Negate");
			fps = new Slider(1, 24, 24);
			fps.setShowTickLabels(true);
			
			setVgap(5);
			setHgap(5);
			setPadding(new Insets(5));

			add(negate, 0, 0);
			add(fps, 1, 0);
		}
		
		private String getSettings() {
			StringBuilder sb = new StringBuilder();
			if (negate.isSelected()) {
				sb.append("-vf negate");
			}
			sb.append(" -r " + fps.getValue());
			return sb.toString();
		}
	}
}
