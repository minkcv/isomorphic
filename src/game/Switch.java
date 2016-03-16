package game;

import java.awt.Rectangle;
import org.lwjgl.opengl.GL11;

public class Switch implements ActiveObject {
	private float x, y, z;
	private float width, height, depth;
	private float r, g, b;
	private int gridX, gridY, gridZ;
	private int switchID;
	private long startTime;
	private long time; // time until switching off, 0 = hold down to activate
	private boolean pressed;
	private boolean active;
	private float margin = 2;
	private Rectangle topRectangle;
	private Rectangle sideRectangle;
	
	// activates if player or box or box on top 
	public Switch(float x, float y, float z, float width, float height, float depth, float r, float g, float b, int id, float time){
		this.x = x;
		this.y = y;
		this.z = z;
		gridX = (int)(x / World.CUBE_SIZE);
		gridY = (int)(y / World.CUBE_SIZE);
		gridZ = (int)(z / World.CUBE_SIZE);
		this.width = width;
		this.depth = depth;
		this.height = height;
		this.r = r;
		this.g = g;
		this.b = b;
		switchID = id;
		topRectangle = new Rectangle((int)x, (int)z, (int)width, (int)depth);
		sideRectangle = new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	@Override
	public void update(World world, Camera.Direction direction) {
		if(direction == Camera.Direction.X){
			sideRectangle = new Rectangle((int)z, (int)y, (int)depth, (int)height);
		}
		else if(direction == Camera.Direction.Z){
			sideRectangle = new Rectangle((int)x, (int)y, (int)width, (int)height);
		}
		
		if(pressed){
			active = true;
			startTime = System.currentTimeMillis();
		}
		else{
			if(startTime + time > System.currentTimeMillis()){
				active = false;
			}
		}
	}

	@Override
	public void alignPosition() {
		
	}

	@Override
	public void render(float shade) {
		if(pressed){
			height = World.CUBE_SIZE / 6;
		}
		else{
			height = World.CUBE_SIZE / 4;
		}
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(r - shade, g - shade, b - shade);
			
			GL11.glNormal3f(0, 0, -1);
			GL11.glVertex3f(margin, 0, margin);
			GL11.glVertex3f(margin, height, margin);
			GL11.glVertex3f(width - margin, height, margin);
			GL11.glVertex3f(width - margin, 0, margin);

			GL11.glNormal3f(0, -1, 0);
			GL11.glVertex3f(margin, 0, margin);
			GL11.glVertex3f(width - margin, 0, margin);
			GL11.glVertex3f(width - margin, 0, depth - margin);
			GL11.glVertex3f(margin, 0, depth - margin);

			GL11.glNormal3f(-1, 0, 0);
			GL11.glVertex3f(margin, 0, margin);
			GL11.glVertex3f(margin, 0, depth - margin);
			GL11.glVertex3f(margin, height, depth - margin);
			GL11.glVertex3f(margin, height, margin);

			GL11.glNormal3f(0, 0, 1);
			GL11.glVertex3f(width - margin, height, depth - margin);
			GL11.glVertex3f(margin, height, depth - margin);
			GL11.glVertex3f(margin, 0, depth - margin);
			GL11.glVertex3f(width - margin, 0, depth - margin);

			GL11.glNormal3f(0, 1, 0);
			GL11.glVertex3f(width - margin, height, depth - margin);
			GL11.glVertex3f(width - margin, height, margin);
			GL11.glVertex3f(margin, height, margin);
			GL11.glVertex3f(margin, height, depth - margin);

			GL11.glNormal3f(1, 0, 0);
			GL11.glVertex3f(width - margin, height, depth - margin);
			GL11.glVertex3f(width - margin, 0, depth - margin);
			GL11.glVertex3f(width - margin, 0, margin);
			GL11.glVertex3f(width - margin, height, margin);
		}
		GL11.glEnd();
		GL11.glTranslatef(-x, -y, -z);
	}
	
	public boolean isPressed(){ return pressed; }
	public void setPressed(boolean p){
		pressed = p;
	}
	
	public boolean isActive(){ return active; }
	public Rectangle getTopRectangle(){ return topRectangle; }
	public Rectangle getSideRectangle(){ return sideRectangle; }

	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	@Override
	public float getZ() { return z; }
	@Override
	public int getGridX() { return gridX; }
	@Override
	public int getGridY() { return gridY; }
	@Override
	public int getGridZ() { return gridZ; }
	public int getSwitchID(){ return switchID; }
}
