package spellAid.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class GraphicsFactory {
	
	private static final Color CIRCLE_COLOUR = Color.LIGHTGREY;
	
	private static final int CIRCLE_RADIUS = 15;

	private static final int HEIGHT = 30;

	private static final int WIDTH = 30; 

	private static final int STROKEWIDTH = 6;
	
	private static final Line CROSSLINE1;
	
	private static final Line CROSSLINE2;
	
	private static final Line TICKLINE1;
	
	private static final Line TICKLINE2;
	
	static {		
		int delta = (int) Math.ceil(STROKEWIDTH / Math.sqrt(2));
		
		CROSSLINE1 = new Line(delta, delta, WIDTH - delta, HEIGHT - delta);
		
		CROSSLINE2 = new Line(WIDTH - delta, delta, delta, HEIGHT - delta);
		
		TICKLINE1 = new Line(WIDTH/3 - delta/2, HEIGHT - delta/2, delta/2, 2*HEIGHT/3 + delta/2);
		
		//Line tickline1 = new Line(WIDTH/3, HEIGHT, 0, 2*HEIGHT/3);
		
		//Line tickline2 = new Line(WIDTH - delta/2, HEIGHT/3 + delta/2, WIDTH/3 + delta/2, HEIGHT - delta/2);
		TICKLINE2 = new Line(WIDTH, HEIGHT/3, WIDTH/3, HEIGHT);
	}
	
	public Shape getNewCircleShape() {
		return new Circle(CIRCLE_RADIUS, CIRCLE_COLOUR);
	}

	public Shape getNewCrossShape() {
		Shape cross = Shape.union(CROSSLINE1, CROSSLINE2);
		cross.setStrokeWidth(STROKEWIDTH);
		cross.setStroke(Color.RED);
		return cross;
	}
	
	public Shape getNewTickShape() {
		Shape tick = Shape.union(TICKLINE1, TICKLINE2);
		tick.setStrokeWidth(STROKEWIDTH);
		tick.setStroke(Color.GREEN);
		return tick;
	}
}
