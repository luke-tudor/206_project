package spellAid.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class Stopwatch extends GridPane {
	
	private long startTime;
	
	private Label time;

	public Stopwatch() {
		super();

		time = new Label("0:0:0");
			
		add(time, 0, 0);
		
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(50), e -> updateTime()));
		timer.setCycleCount(Animation.INDEFINITE);
		timer.play();
		
		startTime = System.nanoTime();
	}

	private void updateTime() {
		
		long secDiff = (System.nanoTime() - startTime)/1_000_000_000;
		
		time.setText(String.format("%d:%d:%d", secDiff/3600, secDiff/60%60, secDiff%60));
	}
}
