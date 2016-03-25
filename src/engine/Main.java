package engine;

import game.Game;

import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;


public class Main {

	private long lastFrame;
	private long lastFps; //last fps time
	private int fpsCounter;
	private int fps;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;

	private boolean escapeReleased;
	private boolean onMenu;
	private Game game;
	private Menu menu;

	private PixelFormat renderSettings;

	public static void main(String[] args){
		try{
			new Main();
		}
		catch(Exception e){ // necessary to close the game on exception or the cursors is locked in the crashed game
			e.printStackTrace();
			quit();
		}
	}

	public Main(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Isomorphic");
			Display.setLocation(10, 10);
			renderSettings = new PixelFormat(8, 8, 0, 8); // alpha bits, depth bits, stencil bits, samples
			Display.create(renderSettings);
		}
		catch(LWJGLException e){
			e.printStackTrace();
		}

		initOpenGL();
		getDelta(); // call before loop to initialize lastFrame
		lastFps = getTime();

		//confine the mouse cursor and hide it
		Mouse.setGrabbed(true);
		this.initialize();

		while(!Display.isCloseRequested()){
			int delta = getDelta();

			this.update(delta);
			this.render();

			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	private void initOpenGL(){
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glViewport(0,0,WIDTH,HEIGHT);
	}

	private void initialize(){
		onMenu = true;
		boolean saveExists = saveExists();
		menu = new Menu(this, saveExists);
	}
	
	public void startNewGame(){
		game = new Game(this, false);
		game.setBackgroundColor();
		onMenu = false;
	}
	
	public void startExistingGame(){
		game = new Game(this, true);
		game.setBackgroundColor();
		onMenu = false;
	}
	
	public void resumeGame(){
		onMenu = false;
		game.setBackgroundColor();
	}
	
	private void pauseGame(){
		onMenu = true;
		menu.setBackgroundColor();
	}

	private void update(int delta){

		if(onMenu){
			if(Mouse.isGrabbed())
				Mouse.setGrabbed(false);
		}
		else{
			if(! Mouse.isGrabbed())
				Mouse.setGrabbed(true);
		}

		if(onMenu){
			menu.update();
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				if(escapeReleased)
					System.exit(0);
				escapeReleased = false;
			}
			else{
				escapeReleased = true;
			}
		}
		else{
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				if(escapeReleased){
					pauseGame();
				}
				escapeReleased = false;
			}
			else{
				escapeReleased = true;
			}
			game.update(delta);
		}

		updateFPS();
	}

	private void render(){
		// Clear the screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		if(onMenu){
			menu.setBackgroundColor();
			menu.render();
		}
		else{
			game.setBackgroundColor();
			game.render();
		}
	}

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	private void updateFPS(){
		if (getTime() - lastFps > 1000) {
			fps = fpsCounter;
			fpsCounter = 0;
			lastFps += 1000;
		}
		fpsCounter++;
	}

	public int getFPS(){
		return fps;
	}

	private boolean saveExists(){
		File workingDir = Util.getWorkingDirectory();
		if(! workingDir.exists())
			workingDir.mkdir();
		File saveDataFile = new File(Util.getWorkingDirectory().getAbsolutePath() + "/savedata.dat");
		if(! saveDataFile.exists()){
			System.out.println("No existing save data");
			return false;
		}
		else
			return true;
	}
	
	public static void quit(){
		//Display.destroy();
		System.exit(0);
	}
}
