package game;
import java.awt.Rectangle;

// a side and top object
public class Wall {
	protected int x, y, width, height;
	protected Rectangle bounding;
	public Wall(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		bounding = new Rectangle(x, y, width, height);
	}
	
	public Rectangle getBounding(){
		return bounding;
	}
}
