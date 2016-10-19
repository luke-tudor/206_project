package spellAid.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * This class creates graphics objects that represent ticks, crosses or circles.
 * 
 * @author Luke Tudor
 */
public class GraphicsFactory {

	private static final CircleFactory CIRCLE_FAC = new CircleFactory();
	
	private static final CrossFactory CROSS_FAC = new CrossFactory();
	
	private static final TickFactory TICK_FAC = new TickFactory();
	
	public Shape getNewCircleShape() {
		return CIRCLE_FAC.getNewShape();
	}

	public Shape getNewCrossShape() {
		return CROSS_FAC.getNewShape();
	}
	
	public Shape getNewTickShape() {
		return TICK_FAC.getNewShape();
	}
	
	// Creates new grey circles.
	private static class CircleFactory {
		
		private static final Color CIRCLE_COLOUR = Color.LIGHTGREY;
		
		private static final int CIRCLE_RADIUS = 15;
		
		private Shape getNewShape() {
			return new Circle(CIRCLE_RADIUS, CIRCLE_COLOUR);
		}
	}
	
	private abstract static class AbstractStrokeGraphic {
		static final int HEIGHT = 30;

		static final int WIDTH = 30; 

		static final int STROKEWIDTH = 6;
		
		static final int DELTA = (int) Math.ceil(STROKEWIDTH / Math.sqrt(2));
	}
	
	// Creates new red crosses.
	private static class CrossFactory extends AbstractStrokeGraphic {
		
		private static final Line CROSSLINE1 = new Line(DELTA, DELTA, WIDTH - DELTA, HEIGHT - DELTA);
		
		private static final Line CROSSLINE2 = new Line(WIDTH - DELTA, DELTA, DELTA, HEIGHT - DELTA);
		
		private static final Color CROSS_COLOUR = Color.RED;
		
		private Shape getNewShape() {
			Shape cross = Shape.union(CROSSLINE1, CROSSLINE2);
			cross.setStrokeWidth(STROKEWIDTH);
			cross.setStroke(CROSS_COLOUR);
			return cross;
		}
	}
	
	// Creates new green ticks.
	private static class TickFactory extends AbstractStrokeGraphic {
		
		private static final Line TICKLINE1 = new Line(WIDTH/3 - DELTA/2, HEIGHT - DELTA/2, DELTA/2, 2*HEIGHT/3 + DELTA/2);
		
		private static final Line TICKLINE2 = new Line(WIDTH, HEIGHT/3, WIDTH/3, HEIGHT);
		
		private static final Color TICK_COLOUR = Color.GREEN;
		
		private Shape getNewShape() {
			Shape tick = Shape.union(TICKLINE1, TICKLINE2);
			tick.setStrokeWidth(STROKEWIDTH);
			tick.setStroke(TICK_COLOUR);
			return tick;
		}
	}
}
