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
	private int gridX, gridY, gridZ;
	private float margin = 0;
	private Message message;
	private SideSign sideSign;
	private TopSign topSign;
	private Camera.Direction previousDirection;
	private int textureColor;
	private static Texture[] sideTextures;
	private static Texture[] topTextures;
	private static final int NUM_TEXTURES = 3;
	private Texture sideTexture;
	private Texture topTexture;
	// statically load textures
	static {
		sideTextures = new Texture[NUM_TEXTURES];
		topTextures = new Texture[NUM_TEXTURES];
		for (int i = 0; i < NUM_TEXTURES; i++) {
			try {
				sideTextures[i] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("resources/sign" + i + ".png"));
				topTextures[i] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("resources/sign_top" + i + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public Sign(float x, float y, float z, 
			float width, float height, float depth, int textureColor, String messageText){
		this.x = x + margin / 2;
		this.y = y;
		this.z = z + margin / 2;
		r = 1;
		g = 1;
		b = 1;
		this.width = width - margin;
		this.height = height - 2 * margin;
		this.depth = depth - margin;
		this.textureColor = textureColor;
		sideTexture = sideTextures[textureColor];
		topTexture = topTextures[textureColor];
		gridX = (int)(x / World.CUBE_SIZE);
		gridY = (int)(y / World.CUBE_SIZE);
		gridZ = (int)(z / World.CUBE_SIZE);
		message = new Message(messageText);
		topSign = new TopSign((int)this.x, (int)this.z, (int)this.width, (int)this.depth);
		sideSign = new SideSign((int)this.x, (int)this.y, (int)this.width, (int)this.height);
		
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
		sideTexture.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // for pixel art
		GL11.glColor3f(r - shade, g - shade, b - shade);
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_QUADS);
		{
			
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
//			GL11.glNormal3f(0, -1, 0);
//			GL11.glVertex3f(0, 0, 0);
//			GL11.glVertex3f(width, 0, 0);
//			GL11.glVertex3f(width, 0, depth - 0);
//			GL11.glVertex3f(0, 0, depth - 0);

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

		topTexture.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // for pixel art
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glNormal3f(0, 1, 0);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(width, height, 0);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(0, height, 0);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(0, height, depth);
		}
		GL11.glEnd();
		GL11.glTranslatef(-x, -y, -z);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
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
	public int getGridX() { return gridX; }
	@Override
	public int getGridY() { return gridY; }
	@Override
	public int getGridZ() { return gridZ; }
}
