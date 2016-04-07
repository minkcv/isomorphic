package gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import game.Cube;
import game.Light;
import game.Player;

public class DemoScene {
	private Player player;
	private float scale = 80;
	private float sideMargin = 25;
	private float topMargin = 10;
	private static Light light;
	private Cube[][][] cubes;
	private Texture titleImage;
	
	static {
		light = new Light(-60, 50, -30);		
	}
	
	public DemoScene(){
		try {
			titleImage = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("resources/title.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		player = new Player(null, 20, 10, 20);
		cubes = new Cube[5][1][5];
		int size = 10;
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[i].length; j++) {
				for (int j2 = 0; j2 < cubes[i][j].length; j2++) {
					cubes[i][j][j2] = new Cube(i * size, j * size, j2 * size, size, size, size, 106 / 255f, 208 / 255f, 135 / 255f, false);
				}
			}
		}
	}
	public void render(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-scale, scale, -scale, scale, 2000, -2000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		titleImage.bind();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // for pixel art
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glNormal3f(0, 0, -1);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-scale + sideMargin, scale - topMargin, 1);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(scale -sideMargin, scale - topMargin, 1);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(scale - sideMargin, scale - topMargin - 26, 1);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(-scale + sideMargin, scale - topMargin - 26, 1);
		}
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		
		GL11.glRotatef(-45, 1, 0, 0);
		GL11.glRotatef(-45, 0, 1, 0);
		GL11.glTranslatef(-player.getX(), -player.getY(), -player.getZ());
		light.render();
		player.render();
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[i].length; j++) {
				for (int j2 = 0; j2 < cubes[i][j].length; j2++) {
					cubes[i][j][j2].render(0, false);
				}
			}
		}
	}
}
