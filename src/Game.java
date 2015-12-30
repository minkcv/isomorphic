import org.lwjgl.opengl.GL11;


public class Game {
	private Main main;
	private Camera camera;
	private World world;
	private Player player;
	public Game(Main main){
		this.main = main;
		camera = new Camera(this);
		world = new World(this);
		float a = (World.dimension * World.dimension) / 2;
		camera.setPosition(a, a, a);
		player = new Player(this, 0, 0, 0);
	}
	
	public void update(int delta){
		camera.update();
		player.update();
	}
	
	public void render(){
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_COLOR_MATERIAL); // for lighting
//		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		double d = world.dimension * 10;
		GL11.glOrtho(-d, d, -d, d, d, -d);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		camera.transformToCamera();
		
		world.render();
		player.render();
	}
	
	public Camera.Direction getCameraDirection(){
		return camera.getDirection();
	}
	
	public float[] getPlayerPosition(){
		float[] pos = {player.getX(), player.getY(), player.getZ()};
		return pos;
	}
	
	public void alignPlayer(){
		player.alignPosition();
	}
}
