import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;


public class Wall {
	private int x, y, width, height;
	private Rectangle bounding;
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
