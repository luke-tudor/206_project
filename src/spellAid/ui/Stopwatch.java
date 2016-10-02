package spellAid.ui;

import java.time.Duration;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Stopwatch extends GridPane {
	
	private long startTime;
	
	private Label time;

	public Stopwatch() {
		super();

		time = new Label("0:0:0");
			
		add(time, 0, 0);
		
		Timeline timer = new Timeline(new KeyFrame(javafx.util.Duration.millis(50), e -> doSomething()));
		timer.setCycleCount(Animation.INDEFINITE);
		timer.play();
		
		startTime = System.nanoTime();
	}

	private void doSomething() {
		
		Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
		
		time.setText(String.format("%d:%d:%d", duration.toHours(), duration.toMinutes(), duration.getSeconds()%60));
	}
}
