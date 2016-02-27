package game;

import java.awt.Rectangle;

public class TopSign extends Wall {
	private Rectangle leftRectangle;
	private Rectangle rightRectangle;
	private Rectangle topRectangle;
	private Rectangle bottomRectangle;
	public TopSign(int x, int y, int width, int height) {
		super(x, y, width, height);
		rightRectangle = new Rectangle(x + width, y, width, height);
		leftRectangle = new Rectangle(x - width, y, width, height);
		topRectangle = new Rectangle(x, y - height, width, height);
		bottomRectangle = new Rectangle(x, y + height, width, height);
	}
	public Rectangle getLeftRectangle(){ return leftRectangle; }
	public Rectangle getRightRectangle(){ return rightRectangle; }
	public Rectangle getTopRectangle(){ return topRectangle; }
	public Rectangle getBottomRectangle(){ return bottomRectangle; }
}
