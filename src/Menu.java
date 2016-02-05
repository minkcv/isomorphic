
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;


public class Menu {
	private Main main;
	private boolean enterReleased;
	private boolean saveExists;
	private boolean gameInProgress = false;;
	public Menu(Main main, boolean saveExists){
		this.main = main;
		this.saveExists = saveExists;
	}

	public void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
			if(enterReleased){
				if(gameInProgress){
					main.resumeGame();
				}
				else {
					if(saveExists){
						main.startExistingGame();
						gameInProgress = true;
					}
					else{
						main.startNewGame();
						gameInProgress = true;
					}
				}
			}
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

		if(gameInProgress){
			GameFonts.courierFont20pt.drawString(20, 20, "Press enter to resume or esc to quit", Color.white);
		}
		else{
			if(saveExists){
				GameFonts.courierFont20pt.drawString(20, 20, "Press enter to continue or esc to quit.", Color.white);
			}
			else{
				GameFonts.courierFont20pt.drawString(20, 20, "Press enter to start a new game or esc to quit.", Color.white);
			}
		}
		GameFonts.courierFont20pt.drawString(20, 40, "WASD to move. Arrows to rotate the world.", Color.white);
	}
	
	public boolean isGameInProgress(){ return gameInProgress; }
	public void setBackgroundColor(){ GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); }
}
