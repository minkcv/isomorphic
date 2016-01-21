
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;


public class Menu {
	private Main main;
	private boolean enterReleased;
	public Menu(Main main){
		this.main = main;
	}

	public void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
			if(enterReleased)
				main.setOnMenu(false);
			enterReleased = false;
		}
		else{
			enterReleased = true;
		}


	}

	public void render(){
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// enable alpha blending (transparency)
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Main.WIDTH, Main.HEIGHT, 0, 0, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GameFonts.courierFont20pt.drawString(20, 20, "Press enter to start or esc to quit", Color.white);
		GameFonts.courierFont20pt.drawString(20, 40, "Arrows to rotate the world", Color.white);
		GameFonts.courierFont20pt.drawString(20, 60, "", Color.white);
	}
}
