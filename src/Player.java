import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


public class Player {
	private Game game;
	private float x, y, z;
	private float width, depth, height;
	private float moveSpeed = 1;
	public Player(Game game, float x, float y, float z){
		this.game = game;
		this.x = x;
		this.y = y;
		this.z = z;
		width = World.dimension;
		height = World.dimension * 2;
		depth = World.dimension;
	}
	
	public void update(){
		Camera.Direction direction = game.getCameraDirection();
		if(direction == Camera.Direction.X){
			if(Keyboard.isKeyDown(Keyboard.KEY_W)){
				y += moveSpeed;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
				y -= moveSpeed;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				z += moveSpeed;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				z -= moveSpeed;
			}
		}
		else if(direction == Camera.Direction.Y){
			if(Keyboard.isKeyDown(Keyboard.KEY_W)){
				z += moveSpeed;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
				z -= moveSpeed;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				x -= moveSpeed;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				x += moveSpeed;
			}
			
		}
		else if(direction == Camera.Direction.Z){
			if(Keyboard.isKeyDown(Keyboard.KEY_W)){
				y += moveSpeed;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
				y -= moveSpeed;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				x -= moveSpeed;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				x += moveSpeed;
			}
		}
		
	}
	
	public void alignPosition(){
		if(x % World.dimension > World.dimension / 2){
			x += x % World.dimension;
		}
		else{
			x -= x % World.dimension;
		}
		
		if(y % World.dimension > World.dimension / 2){
			y += y % World.dimension;
		}
		else{
			y -= y % World.dimension;
		}
		
		if(z % World.dimension > World.dimension / 2){
			z += z % World.dimension;
		}
		else{
			z -= z % World.dimension;
		}
	}
	
	public void render(){
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(1, 0, 0);
			GL11.glNormal3f(0, 0, -1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, height, 0);
			GL11.glVertex3f(width, height, 0);
			GL11.glVertex3f(width, 0, 0);

//			GL11.glColor3f(0, 1, 0);
			GL11.glNormal3f(0, -1, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(width, 0, 0);
			GL11.glVertex3f(width, 0, depth);
			GL11.glVertex3f(0, 0, depth);

//			GL11.glColor3f(0, 0, 1);
			GL11.glNormal3f(-1, 0, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, 0, depth);
			GL11.glVertex3f(0, height, depth);
			GL11.glVertex3f(0, height, 0);

//			GL11.glColor3f(1, 0, 0);
			GL11.glNormal3f(0, 0, 1);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(0, height, depth);
			GL11.glVertex3f(0, 0, depth);
			GL11.glVertex3f(width, 0, depth);

//			GL11.glColor3f(0, 1, 0);
			GL11.glNormal3f(0, 1, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, height, 0);
			GL11.glVertex3f(0, height, 0);
			GL11.glVertex3f(0, height, depth);

//			GL11.glColor3f(0, 0, 1);
			GL11.glNormal3f(1, 0, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, 0, depth);
			GL11.glVertex3f(width, 0, 0);
			GL11.glVertex3f(width, height, 0);
		}
		GL11.glEnd();
		GL11.glTranslatef(-x, -y, -z);
	}
	
	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getZ(){ return z; }
}
