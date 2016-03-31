package game;

import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;

import game.Camera.Direction;

public class Item implements ActiveObject{
	private float x, y, z;
	private int gridX, gridY, gridZ;
	private float width, depth, height;
	private float r, g, b;
	private Camera.Direction direction;
	private Rectangle xyRect;
	private Rectangle xzRect;
	private Rectangle yzRect;
	private Rectangle sideRectangle;
	private int worldID;
	
	public Item(float x, float y, float z, 
			float width, float height, float depth, 
			float red, float green, float blue, int worldID){
		this.worldID = worldID;
		this.x = x + World.CUBE_SIZE / 2;
		this.y = y;
		this.z = z + World.CUBE_SIZE / 2;
		gridX = (int)(x / World.CUBE_SIZE);
		gridY = (int)(y / World.CUBE_SIZE);
		gridZ = (int)(z / World.CUBE_SIZE);
		this.width = width / 4;
		this.height = height / 4;
		this.depth = depth / 4;
		r = red;
		g = green;
		b = blue;
		xyRect = new Rectangle((int)x, (int)y, (int)width, (int)depth);
		xzRect = new Rectangle((int)x, (int)z, (int)width, (int)height);
		yzRect = new Rectangle((int)z, (int)y, (int)depth, (int)height);
	}
	@Override
	public void update(World world, Direction direction) {
		this.direction = direction;
		if(direction == Camera.Direction.X){
			sideRectangle = yzRect;
		}
		else if(direction == Camera.Direction.Z){
			sideRectangle = xyRect;
		}
	}

	@Override
	public void alignPosition() {
	}

	@Override
	public void render(float shade) {
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(45, 1, 0, 0);
		long time = System.currentTimeMillis() / 30;
		GL11.glRotatef(time % 360, 0, 1, 0);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(r - shade, g - shade, b - shade);
			
			GL11.glNormal3f(0, 0, -1);
			GL11.glVertex3f(-width, -height, -depth);
			GL11.glVertex3f(-width, height, -depth);
			GL11.glVertex3f(width, height, -depth);
			GL11.glVertex3f(width, -height, -depth);

			//			GL11.glColor3f(0, 1, 0);
			GL11.glNormal3f(0, -1, 0);
			GL11.glVertex3f(-width, -height, -depth);
			GL11.glVertex3f(width, -height, -depth);
			GL11.glVertex3f(width, -height, depth);
			GL11.glVertex3f(-width, -height, depth);

			//			GL11.glColor3f(0, 0, 1);
			GL11.glNormal3f(-1, 0, 0);
			GL11.glVertex3f(-width, -height, -depth);
			GL11.glVertex3f(-width, -height, depth);
			GL11.glVertex3f(-width, height, depth);
			GL11.glVertex3f(-width, height, -depth);

			//			GL11.glColor3f(1, 0, 0);
			GL11.glNormal3f(0, 0, 1);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(-width, height, depth);
			GL11.glVertex3f(-width, -height, depth);
			GL11.glVertex3f(width, -height, depth);

			//			GL11.glColor3f(0, 1, 0);
			GL11.glNormal3f(0, 1, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, height, -depth);
			GL11.glVertex3f(-width, height, -depth);
			GL11.glVertex3f(-width, height, depth);

			//			GL11.glColor3f(0, 0, 1);
			GL11.glNormal3f(1, 0, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, -height, depth);
			GL11.glVertex3f(width, -height, -depth);
			GL11.glVertex3f(width, height, -depth);
		}
		GL11.glEnd();
		GL11.glRotatef(-time % 360, 0, 1, 0);
		GL11.glRotatef(-45, 1, 0, 0);
		GL11.glTranslatef(-x, -y, -z);
	}

	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	@Override
	public float getZ() { return z; }
	
	public int getGridX(){ return gridX; }
	public int getGridY(){ return gridY; }
	public int getGridZ(){ return gridZ; }
	
	public Rectangle getXYRectangle(){ return xyRect; }
	public Rectangle getXZRectangle(){ return xzRect; }
	public Rectangle getYZRectangle(){ return yzRect; }
	public Rectangle getSideRectangle(){ return sideRectangle; }
	public int getWorldID(){ return worldID; }
}
