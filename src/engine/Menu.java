package engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;


public class Menu {
	private Main main;
	private boolean enterReleased;
	private boolean saveExists;
	private boolean gameInProgress = false;
	private enum MenuItem {
		START, RESUME, CONTINUE, ABOUT, QUIT
	}
	private MenuItem currentItem;
	private boolean downReleased;
	private boolean upReleased;
	public Menu(Main main, boolean saveExists){
		this.main = main;
		this.saveExists = saveExists;
		if(saveExists)
			currentItem = MenuItem.CONTINUE;
		else
			currentItem = MenuItem.START;
	}

	public void update(){
		if(gameInProgress){
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)){
				if(downReleased){
					if(currentItem == MenuItem.RESUME)
						currentItem = MenuItem.ABOUT;
					else if(currentItem == MenuItem.ABOUT)
						currentItem = MenuItem.QUIT;
					else if(currentItem == MenuItem.QUIT)
						currentItem = MenuItem.RESUME;
				}
				downReleased = false;
			}
			else {
				downReleased = true;
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)){
				if(upReleased){
					if(currentItem == MenuItem.QUIT)
						currentItem = MenuItem.ABOUT;
					else if(currentItem == MenuItem.ABOUT)
						currentItem = MenuItem.RESUME;
					else if(currentItem == MenuItem.RESUME)
						currentItem = MenuItem.QUIT;
				}
				upReleased = false;
			}
			else {
				upReleased = true;
			}
		}
		else{ // game not in progress, title screen
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)){
				if(downReleased){
					if(currentItem == MenuItem.START && saveExists)
						currentItem = MenuItem.CONTINUE;
					else if(currentItem == MenuItem.START && ! saveExists)
						currentItem = MenuItem.ABOUT;
					else if(currentItem == MenuItem.CONTINUE)
						currentItem = MenuItem.ABOUT;
					else if(currentItem == MenuItem.ABOUT)
						currentItem = MenuItem.QUIT;
					else if(currentItem == MenuItem.QUIT)
						currentItem = MenuItem.START;
				}
				downReleased = false;
			}
			else {
				downReleased = true;
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)){
				if(upReleased){
					if(currentItem == MenuItem.QUIT)
						currentItem = MenuItem.ABOUT;
					else if(currentItem == MenuItem.ABOUT && saveExists)
						currentItem = MenuItem.CONTINUE;
					else if(currentItem == MenuItem.ABOUT && ! saveExists)
						currentItem = MenuItem.START;
					else if(currentItem == MenuItem.CONTINUE)
						currentItem = MenuItem.START;
					else if(currentItem == MenuItem.START)
						currentItem = MenuItem.QUIT;
				}
				upReleased = false;
			}
			else {
				upReleased = true;
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
			if(enterReleased){
				if(currentItem == MenuItem.RESUME){
					main.resumeGame();
					gameInProgress = true;
				}
				else if(currentItem == MenuItem.CONTINUE){
					main.startExistingGame();
					gameInProgress = true;
					currentItem = MenuItem.RESUME;
				}
				else if(currentItem == MenuItem.START){
					main.startNewGame();
					gameInProgress = true;
					currentItem = MenuItem.RESUME;
				}
				else if(currentItem == MenuItem.ABOUT){
					// TODO
				}
				else if(currentItem == MenuItem.QUIT){
					Main.quit();
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

		int arrowHeight = 100;
		if(gameInProgress){
			switch(currentItem){
			case RESUME:
				arrowHeight = 600;
				break;
			case ABOUT:
				arrowHeight = 620;
				break;
			case QUIT:
				arrowHeight = 660;
				break;
			}
			GameFonts.courierFont20pt.drawString(280, 600, "Resume", Color.white);
			GameFonts.courierFont20pt.drawString(280, 620, "About", Color.white);
			GameFonts.courierFont20pt.drawString(280, 660, "Quit", Color.white);
		}
		else{
			switch(currentItem){
			case START:
				arrowHeight = 600;
				break;
			case CONTINUE:
				arrowHeight = 620;
				break;
			case ABOUT:
				arrowHeight = 640;
				break;
			case QUIT:
				arrowHeight = 680;
				break;
			}
			GameFonts.courierFont20pt.drawString(280, 600, "Start new game", Color.white);
			GameFonts.courierFont20pt.drawString(280, 640, "About", Color.white);
			GameFonts.courierFont20pt.drawString(280, 680, "Quit", Color.white);
			if(saveExists){
				GameFonts.courierFont20pt.drawString(280, 620, "Continue saved game", Color.white);
			}
			else{
				GameFonts.courierFont20pt.drawString(280, 620, "Continue saved game", Color.gray);
			}
		}
		GameFonts.courierFont20pt.drawString(260, arrowHeight, ">", Color.white);
	}
	
	public boolean isGameInProgress(){ return gameInProgress; }
	public void setBackgroundColor(){ GL11.glClearColor(0.46f, 0.635f, 0.91f, 0.0f); }
}
