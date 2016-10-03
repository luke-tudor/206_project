package spellAid.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Stopwatch extends Label {
	
	private long startTime;
	
	private Timeline timer;

	public Stopwatch() {
		super("00:00:00");
		
		timer = new Timeline(new KeyFrame(Duration.millis(50), e -> updateTime()));
		timer.setCycleCount(Animation.INDEFINITE);
	}
	
	public void start() {
		startTime = System.nanoTime();
		timer.play();
	}
	
	public void stop() {
		timer.stop();
	}

	private void updateTime() {
		long secDiff = (System.nanoTime() - startTime)/1_000_000_000;
		setText(String.format("%02d:%02d:%02d", secDiff/3600%24, secDiff/60%60, secDiff%60));
	}
}
