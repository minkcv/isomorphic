package game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureCube extends Cube {
	private static HashMap<String, Texture> textures;
	private static String[] textureNames = {
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
		"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
	};
	private static final int NUM_TEXTURES = textureNames.length;
	private String currentTexture;
	private float length;
	private long lastTime;
	private long waitTime;
	private long time = 0;
	private Random rand;
	private enum TextureType{
		WHITE_LINES, BLACK_LINES, GLYPHS,
	}
	private TextureType textureType;
	
	// statically load textures
	static {
		textures = new HashMap<String, Texture>();
		for (int i = 0; i < NUM_TEXTURES; i++) {
			try {
				textures.put(textureNames[i],TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("resources/blocks/" + textureNames[i] + ".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public TextureCube(float x, float y, float z, float width, float height,
			float depth, float r, float g, float b, boolean invisible, String textureName) {
		super(x, y, z, width, height, depth, r, g, b, invisible);
		this.length = width;
		rand = new Random();
		currentTexture = textureName;
		waitTime = rand.nextInt(5000);
		if((char)currentTexture.charAt(0) >= 'a' && (char)currentTexture.charAt(0) <= 'z'){
			textureType = TextureType.GLYPHS;
		}
		else if(Integer.parseInt(currentTexture) < 10){
			textureType = TextureType.WHITE_LINES;
		}
		else if(Integer.parseInt(currentTexture) > 9 && Integer.parseInt(currentTexture) < 20){
			textureType = TextureType.BLACK_LINES;
		}
	}
	
	@Override
	public void update(int delta){
		time += delta;
		if(time > lastTime + waitTime){
			switch(textureType){
			case WHITE_LINES:
				currentTexture = rand.nextInt(10) + "";
				break;
			case BLACK_LINES:
				currentTexture = rand.nextInt(10) + 10 + "";
				break;
			case GLYPHS:
				currentTexture = (char)( new Random().nextInt(26) + 97) + "";
				break;
			}
			waitTime = rand.nextInt(5000);
			lastTime = time;
		}
	}

	@Override
	public void render(float shade, boolean slice){
		if(textureType == TextureType.GLYPHS){
			weirdRender(shade, slice);
			return;
		}
		GL11.glEnable(GL11.GL_BLEND);
		//GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		textures.get(currentTexture).bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // for pixel art
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glColor3f(r - shade, g - shade, b - shade);
		GL11.glTranslatef(x, y, z);
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glBegin(GL11.GL_QUADS);
		{

			if(slice || !xyHidden){
				//GL11.glColor3f(0f, 0f, 1f);
				//XY face
				GL11.glNormal3f(0, 0, -1);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0, 0, 0);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(0, length, 0);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(length, length, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(length, 0, 0);
			}

			if(slice || !xzHidden){
				//GL11.glColor3f(0f, 1f, 0f);
				//XZ face
				GL11.glNormal3f(0, 1, 0);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(length, length, length);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(length, length, 0);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0, length, 0);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(0, length, length);
			}

			if(slice || !yzHidden){
				//GL11.glColor3f(1f, 0f, 0f);
				//YZ face
				GL11.glNormal3f(-1, 0, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0, 0, 0);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0, 0, length);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(0, length, length);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0, length, 0);
			}

			//back faces
			if(slice || !xyrHidden){
				//GL11.glColor3f(0f, 0f, 1f);
				//XY face
				GL11.glNormal3f(0, 0, 1);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(length, length, length);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0, length, length);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0, 0, length);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(length, 0, length);
			}

			if(slice || !xzrHidden){
				//GL11.glColor3f(0f, 1f, 0f);
				//XZ face
				GL11.glNormal3f(0, -1, 0);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(0, 0, 0);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(length, 0, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(length, 0, length);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0, 0, length);
			}

			if(slice || !yzrHidden){
				//GL11.glColor3f(1f, 0f, 0f);
				//YZ face
				GL11.glNormal3f(1, 0, 0);
				GL11.glTexCoord2d(1, 0);
				GL11.glVertex3d(length, length, length);
				GL11.glTexCoord2d(1, 1);
				GL11.glVertex3d(length, 0, length);
				GL11.glTexCoord2d(0, 1);
				GL11.glVertex3d(length, 0, 0);
				GL11.glTexCoord2d(0, 0);
				GL11.glVertex3d(length, length, 0);
			}
		}
		GL11.glEnd();
		GL11.glTranslatef(-x, -y, -z);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	private void weirdRender(float shade, boolean slice){
		GL11.glEnable(GL11.GL_BLEND);
		//GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		textures.get(currentTexture).bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // for pixel art
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glColor3f(r - shade, g - shade, b - shade);
		GL11.glTranslatef(x, y, z);
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glBegin(GL11.GL_QUADS);
		{

			if(slice || !xyHidden){
				//GL11.glColor3f(0f, 0f, 1f);
				//XY face
				GL11.glNormal3f(0, 0, -1);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0, 0, 0);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0, length, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(length, length, 0);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(length, 0, 0);
			}

			if(slice || !xzHidden){
				//GL11.glColor3f(0f, 1f, 0f);
				//XZ face
				GL11.glNormal3f(0, 1, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(length, length, length);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(length, length, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0, length, 0);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0, length, length);
			}

			if(slice || !yzHidden){
				//GL11.glColor3f(1f, 0f, 0f);
				//YZ face
				GL11.glNormal3f(-1, 0, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0, 0, 0);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0, 0, length);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0, length, length);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0, length, 0);
			}

			//back faces
			if(slice || !xyrHidden){
				//GL11.glColor3f(0f, 0f, 1f);
				//XY face
				GL11.glNormal3f(0, 0, 1);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(length, length, length);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0, length, length);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0, 0, length);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(length, 0, length);
			}

			if(slice || !xzrHidden){
				//GL11.glColor3f(0f, 1f, 0f);
				//XZ face
				GL11.glNormal3f(0, -1, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0, 0, 0);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(length, 0, 0);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(length, 0, length);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0, 0, length);
			}

			if(slice || !yzrHidden){
				//GL11.glColor3f(1f, 0f, 0f);
				//YZ face
				GL11.glNormal3f(1, 0, 0);
				GL11.glTexCoord2d(1, 1);
				GL11.glVertex3d(length, length, length);
				GL11.glTexCoord2d(1, 0);
				GL11.glVertex3d(length, 0, length);
				GL11.glTexCoord2d(1, 1);
				GL11.glVertex3d(length, 0, 0);
				GL11.glTexCoord2d(0, 1);
				GL11.glVertex3d(length, length, 0);
			}
		}
		GL11.glEnd();
		GL11.glTranslatef(-x, -y, -z);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
}
