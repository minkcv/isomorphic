package game;
import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;


public class Portal implements ActiveObject {
	private float x, y, z;
	private int gridX, gridY, gridZ;
	private float width, depth, height;
	private float r, g, b;
	private Camera.Direction direction;
	private int destinationID;
	private int destinationWorld;
	private int currentID;
	private Rectangle xyPortalRect;
	private Rectangle xzPortalRect;
	private Rectangle yzPortalRect;
	private char cameraDirection;
	public Portal(float x, float y, float z, 
			float width, float height, float depth, 
			float red, float green, float blue,
			int destID, int destWorld, int currID, char cameraDirection){
		this.x = x;
		this.y = y;
		this.z = z;
		gridX = (int)(x / World.CUBE_SIZE);
		gridY = (int)(y / World.CUBE_SIZE);
		gridZ = (int)(z / World.CUBE_SIZE);
		this.width = width;
		this.height = height;
		this.depth = depth;
		xyPortalRect = new Rectangle((int)x, (int)y, (int)width, (int)depth);
		xzPortalRect = new Rectangle((int)x, (int)z, (int)width, (int)height);
		yzPortalRect = new Rectangle((int)z, (int)y, (int)depth, (int)height);
		r = red;
		g = green;
		b = blue;
		destinationID = destID;
		destinationWorld = destWorld;
		currentID = currID;
		this.cameraDirection = cameraDirection;
	}
	@Override
	public void update(World world, Camera.Direction direction) {
		this.direction = direction;
	}

	@Override
	public void alignPosition() {

	}

	@Override
	public void render(float shade) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslatef(x, y, z);
		if(direction == Camera.Direction.ISO || direction == Camera.Direction.FREE || direction == Camera.Direction.Y){
			renderISO();
		}
		else if(direction == Camera.Direction.X || direction == Camera.Direction.Z){
			renderSide(shade);
		}
		GL11.glTranslatef(-x, -y, -z);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	private void renderSide(float shade){

		GL11.glBegin(GL11.GL_QUADS);
		{
			//GL11.glNormal3f(0, 0, -1);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(0, 0, -1);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(0, height, -1);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(width, height, -1);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(width, 0, -1);

			//GL11.glNormal3f(-1, 0, 0);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(0, 0, depth);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(-1, height, depth);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(-1, height, 0);
		}
		GL11.glEnd();

	}

	private void renderISO(){
		GL11.glBegin(GL11.GL_QUADS);
		{
			//GL11.glNormal3f(0, 0, 1);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(width + 1, height, depth + 1);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(-1, height, depth + 1);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(0, 0, depth);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(width, 0, depth);

			//GL11.glNormal3f(1, 0, 0);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(width + 1, height, depth + 1);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(width, 0, depth);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(width, 0, 0);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(width + 1, height, -1);

			// front faces (to isometric view) drawn last so transparency works better
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(0, 0, depth);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(-1, height, depth + 1);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(-1, height, -1);

			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(-1, height, -1);
			GL11.glColor4f(r, g, b, 0);
			GL11.glVertex3f(width + 1, height, -1);
			GL11.glColor4f(r, g, b, 1);
			GL11.glVertex3f(width, 0, 0);
		}
		GL11.glEnd();
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
	
	public Rectangle getXYRectangle(){ return xyPortalRect; }
	public Rectangle getXZRectangle(){ return xzPortalRect; }
	public Rectangle getYZRectangle(){ return yzPortalRect; }
	
	public int getDestinationID(){ return destinationID; }
	public int getDestinationWorld(){ return destinationWorld; }
	public int getCurrentID(){ return currentID; }
	public char getCameraDirection(){ return cameraDirection; }
}
