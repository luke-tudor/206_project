package spellAid.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ScorePanel extends GridPane {
	
	private Label[] labels;
	
	private Label currentScore;
	
	private Stopwatch stopwatch;
	
	private Label numLeft;

	public ScorePanel() {
		super();
		
		labels = new Label[]{new Label("Number of words correct:"),
				new Label("Time:"), new Label("Number of words left:")};
		
		currentScore = new Label("0/0");
		
		stopwatch = new Stopwatch();
		
		numLeft = new Label("10");
		
		for (int i = 0; i < labels.length; i++) {
			add(labels[i], 0, i);
		}
		
		add(currentScore, 1, 0);
		add(stopwatch, 1, 1);
		add(numLeft, 1, 2);
		
		setHgap(5);
		setVgap(5);
		setPadding(new Insets(5));
	}
	
	public void startTimer() {
		stopwatch.start();
	}
}
