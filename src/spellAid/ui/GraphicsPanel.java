package spellAid.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Shape;

/**
 * This class is used to create the graphics that the user sees while the test
 * is running. The graphics are various objects which correspond to how well the user
 * spelled a word, with text underneath which tell the user what word that result
 * was for.
 * 
 * Most of this class is reused from assignment 2.
 * 
 * @author Luke Tudor
 */
public class GraphicsPanel extends GridPane {
	
	private static final GraphicsFactory GRAPHICS_FACTORY = new GraphicsFactory();

	// The graphics take the form of JLabels with Icons.
	private Label[] labels;

	/*
	 * Create and layout the panel based on how many graphics objects are
	 * requested.
	 */
	public GraphicsPanel(int numberOfGraphics) {
		super();

		setPadding(new Insets(5));
		setVgap(5);
		
		labels = new Label[numberOfGraphics];

		/*
		 * Set the Labels to a neutral colour and unknown text. Layout the 
		 * individual labels.
		 */
		for (int i = 0; i < labels.length; i++){
			labels[i] =	new Label("" + (i+1), GRAPHICS_FACTORY.getNewCircleShape());
			labels[i].setContentDisplay(ContentDisplay.TOP);
			labels[i].setAlignment(Pos.BOTTOM_CENTER);
			labels[i].setPrefWidth(200);
			add(labels[i], 0, i);
		}
		
	}

	/*
	 * Set the individual graphics to the desired type and text when the user
	 * does the test. 
	 */
	public void updateGraphic(int graphicNum, Shape graphic, String newText) {
		labels[graphicNum].setGraphic(graphic);
		labels[graphicNum].setText(newText);
	}
}
