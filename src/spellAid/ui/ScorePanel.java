package spellAid.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * This class represents a simple scoring panel for the user with a stop watch.
 * 
 * @author Luke Tudor
 */
public class ScorePanel extends GridPane {
	
	private Label[] labels;
	
	private Label currentScore;
	
	private Stopwatch stopwatch;
	
	private Label numLeft;
	
	private int numDone;
	
	private int numCorrect;

	public ScorePanel(int numWords) {
		super();
		
		labels = new Label[]{new Label("Number of words correct:"),
				new Label("Time:"), new Label("Number of words left:")};
		
		numDone = 0;
		numCorrect = 0;
		
		currentScore = new Label(numCorrect + "/" + numDone);
		
		stopwatch = new Stopwatch();
		
		numLeft = new Label(numWords + "");
		
		for (int i = 0; i < labels.length; i++) {
			add(labels[i], 0, i);
		}
		
		add(currentScore, 1, 0);
		add(stopwatch, 1, 1);
		add(numLeft, 1, 2);
		
		setHgap(5);
		setVgap(5);
		setPadding(new Insets(5));
		setAlignment(Pos.CENTER);
	}
	
	public void startTimer() {
		stopwatch.start();
	}
	
	public void stopTimer() {
		stopwatch.stop();
	}
	
	public void updateScore(boolean correct) {
		numDone++;
		if (correct) {
			numCorrect++;
		}
		currentScore.setText(numCorrect + "/" + numDone);
	}
}
