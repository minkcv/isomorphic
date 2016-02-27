package game;

import org.lwjgl.opengl.GL11;
import gui.Message;

public class Sign implements ActiveObject {
	private float x, y, z;
	private float r, g, b;
	private float width, height, depth;
	private Message message;
	private SideSign sideSign;
	private TopSign topSign;
	private Camera.Direction previousDirection;
	public Sign(float x, float y, float z, 
			float width, float height, float depth,
			float r, float g, float b, String messageText){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.width = width;
		this.depth = height;
		this.height = depth;
		message = new Message(messageText);
		topSign = new TopSign((int)x, (int)z, (int)width, (int)height);
		sideSign = new SideSign((int)x, (int)y, (int)width, (int)height);
	}

	@Override
	public void update(World world, Camera.Direction direction){
		if(previousDirection != direction){
			if(direction == Camera.Direction.Y){
				//topSign.setPosition((int)x, (int)z);
				//topSign.updateRectangles();
			}
			else if(direction == Camera.Direction.X){
				sideSign.setPosition((int)z, (int)y);
				sideSign.updateRectangles();
			}
			else if(direction == Camera.Direction.Z){
				sideSign.setPosition((int)x, (int)y);
				sideSign.updateRectangles();
			}
			previousDirection = direction;
		}
	}

	@Override
	public void alignPosition() {
	}

	@Override
	public void render(float shade) {
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(r - shade, g - shade, b - shade);
			GL11.glNormal3f(0, 0, -1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, height, 0);
			GL11.glVertex3f(width, height, 0);
			GL11.glVertex3f(width, 0, 0);

			//GL11.glColor3f(0, 1, 0);
			GL11.glNormal3f(0, -1, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(width, 0, 0);
			GL11.glVertex3f(width, 0, depth);
			GL11.glVertex3f(0, 0, depth);

			//GL11.glColor3f(0, 0, 1);
			GL11.glNormal3f(-1, 0, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, 0, depth);
			GL11.glVertex3f(0, height, depth);
			GL11.glVertex3f(0, height, 0);

			//GL11.glColor3f(1, 0, 0);
			GL11.glNormal3f(0, 0, 1);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(0, height, depth);
			GL11.glVertex3f(0, 0, depth);
			GL11.glVertex3f(width, 0, depth);

			//GL11.glColor3f(0, 1, 0);
			GL11.glNormal3f(0, 1, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, height, 0);
			GL11.glVertex3f(0, height, 0);
			GL11.glVertex3f(0, height, depth);

			//GL11.glColor3f(0, 0, 1);
			GL11.glNormal3f(1, 0, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, 0, depth);
			GL11.glVertex3f(width, 0, 0);
			GL11.glVertex3f(width, height, 0);
		}
		GL11.glEnd();
		GL11.glTranslatef(-x, -y, -z);
	}

	public SideSign getSideSign(){ return sideSign; }
	public TopSign getTopSign(){ return topSign; }
	public Message getMessage(){ return message; }
	@Override
	public float getX(){ return x; }
	@Override
	public float getY(){ return y; }
	@Override
	public float getZ(){ return z; }
}
