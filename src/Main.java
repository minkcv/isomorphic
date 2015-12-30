
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


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

	public static void main(String[] args){
		new Main();
	}

	public Main(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Isomorphic");
			Display.setLocation(10, 10);
			Display.create();
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
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0,0,WIDTH,HEIGHT);
//		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void initialize(){
		onMenu = true;
		menu = new Menu(this);
		game = new Game(this);
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
				if(escapeReleased)
					onMenu = true;
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
			menu.render();
		}
		else{
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
	public void setOnMenu(boolean onMenu){
		this.onMenu = onMenu;
	}
}
