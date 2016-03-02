package game;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import gui.Message;

public class Sign implements ActiveObject {
	private float x, y, z;
	private float r, g, b;
	private float width, height, depth;
	private float margin = 2;
	private Message message;
	private SideSign sideSign;
	private TopSign topSign;
	private Camera.Direction previousDirection;
	private static Texture texture;
	public Sign(float x, float y, float z, 
			float width, float height, float depth,
			float r, float g, float b, String messageText){
		this.x = x + margin / 2;
		this.y = y;
		this.z = z + margin / 2;
		this.r = r;
		this.g = g;
		this.b = b;
		this.width = width - margin;
		this.height = height - 2 * margin;
		this.depth = depth - margin;
		message = new Message(messageText);
		topSign = new TopSign((int)this.x, (int)this.z, (int)this.width, (int)this.depth);
		sideSign = new SideSign((int)this.x, (int)this.y, (int)this.width, (int)this.height);
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("resources/sign.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		texture.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // for pixel art
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(r - shade, g - shade, b - shade);
			GL11.glNormal3f(0, 0, -1);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0, height, 0);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(width, height, 0);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(width, 0, 0);

			// bottom
			//GL11.glColor3f(0, 1, 0);
			GL11.glNormal3f(0, -1, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(width, 0, 0);
			GL11.glVertex3f(width, 0, depth - 0);
			GL11.glVertex3f(0, 0, depth - 0);

			//GL11.glColor3f(0, 0, 1);
			GL11.glNormal3f(-1, 0, 0);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(0, 0, depth);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(0, height, depth);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0, height, 0);

			//GL11.glColor3f(1, 0, 0);
			GL11.glNormal3f(0, 0, 1);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(0, height, depth);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(0, 0, depth);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(width, 0, depth);

			// top
			//GL11.glColor3f(0, 1, 0);
			GL11.glNormal3f(0, 1, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, height, 0);
			GL11.glVertex3f(0, height, 0);
			GL11.glVertex3f(0, height, depth);

			//GL11.glColor3f(0, 0, 1);
			GL11.glNormal3f(1, 0, 0);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(width, 0, depth);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(width, 0, 0);
			GL11.glTexCoord2f(1, 0);
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
	@Override
	public int getGridX() { return (int)x / World.CUBE_SIZE; }
	@Override
	public int getGridY() { return (int)y / World.CUBE_SIZE; }
	@Override
	public int getGridZ() { return (int)z / World.CUBE_SIZE; }
}
