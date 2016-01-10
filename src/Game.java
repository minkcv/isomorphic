import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


public class Game {
	private Main main;
	private Camera camera;
	private World world;
	private Player player;
	private float zoom = 10;
	private float minZoom = 5;
	private float maxZoom = 20;
	private Axes3D axes;
	public Game(Main main){
		this.main = main;
		camera = new Camera(this);
		world = new World(this);
		player = new Player(this, 0, 10, 0);
		axes = new Axes3D(10);
	}
	
	public void update(int delta){
		player.update(camera.getDirection(), world);
		camera.update();
		camera.setPosition(player.getX(), player.getY(), player.getZ());
		
		zoom -= Mouse.getDWheel() / 120;
		if(zoom < minZoom)
			zoom = minZoom;
		else if(zoom > maxZoom)
			zoom = maxZoom;
	}
	
	public void render(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		double d = World.CUBE_SIZE * zoom;
		GL11.glOrtho(-d, d, -d, d, d * 2, -d * 2);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		camera.transformToCamera();
		
		world.render(camera.getDirection());
		player.render();
		
		camera.undoTransform();
		GL11.glTranslatef(-80, -80, 0);
		camera.rotateToCamera();
		axes.render();
	}
	
	public float[] getPlayerPosition(){
		float[] pos = {player.getX(), player.getY(), player.getZ()};
		return pos;
	}
	
	public void alignPlayer(){
		player.alignPosition();
	}
	
	public void updateWallsInPlane(Camera.Direction direction){
		world.updateWallsInPlane(direction, player.getGridX(), player.getGridY(), player.getGridZ());
	}
	
	public void createWalls(Camera.Direction direction){
		
	}
}
