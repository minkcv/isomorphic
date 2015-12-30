
import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 * @author wsmith
 *
 * example code to draw a string: <br> <code>
 * GameFonts.courierFont20pt.drawString(20, 20, "FPS: " + fps, Color.white);
 * </code> <br>
 * This draws the fps at the x, y coordinates 20, 20 in white
 */
public class GameFonts {
	private static Font awtCourierFont20pt = new Font("Courier", Font.PLAIN, 20);
	public static TrueTypeFont courierFont20pt = new TrueTypeFont(awtCourierFont20pt, false);
	
	/**
	 * Enables and disables settings that make drawing fonts possible outside of Menu.render()
	 * Transforms the OpenGL matrix into a flat plane of the window to draw text to.
	 */
	public static void glFontSettigns(){
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Main.WIDTH, Main.HEIGHT, 0, 0, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
}
