package game;

import java.awt.Rectangle;

public class SideSign extends Wall{
	private Rectangle leftRectangle;
	private Rectangle rightRectangle;
	public SideSign(int x, int y, int width, int height) {
		super(x, y, width, height);
		leftRectangle = new Rectangle(x + width, y, width, height);
		rightRectangle = new Rectangle(x - width, y, width, height);
	}
	
	public void updateRectangles(){
		bounding = new Rectangle(x, y, width, height);
		leftRectangle = new Rectangle(x + width, y, width, height);
		rightRectangle = new Rectangle(x - width, y, width, height);
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getLeftRectangle(){ return leftRectangle; }
	public Rectangle getRightRectangle(){ return rightRectangle; }
}
