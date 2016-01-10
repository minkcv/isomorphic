import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


public class Player {
	private Game game;
	private float x, y, z;
	private float xVelocity, yVelocity, zVelocity;
	private float width, depth, height;
	private float moveSpeed = 1;
	private TopPlayer topPlayer;
	private SidePlayer sidePlayer;
	public Player(Game game, float x, float y, float z){
		this.game = game;
		this.x = x;
		this.y = y;
		this.z = z;
		width = World.CUBE_SIZE;
		height = World.CUBE_SIZE * 2;
		depth = World.CUBE_SIZE;
		
		topPlayer = new TopPlayer((int)x, (int)z, (int)width, (int)depth);
		sidePlayer = new SidePlayer((int)z, (int)y, (int)width, (int)height);
	}
	
	public void update(Camera.Direction direction, ArrayList<Wall> walls){
		xVelocity = 0;
		yVelocity = 0;
		zVelocity = 0;
		if(direction == Camera.Direction.X){
			sidePlayer.update(walls, (int)z, (int)y, false);
			z = sidePlayer.getX();
			y = sidePlayer.getY();
		}
		else if(direction == Camera.Direction.Y){
			topPlayer.update(walls, (int)x, (int)z);
			x = topPlayer.getX();
			z = topPlayer.getY();
		}
		else if(direction == Camera.Direction.Z){
			sidePlayer.update(walls, (int)x, (int)y, true);
			x = sidePlayer.getX();
			y = sidePlayer.getY();
		}
		
		x += xVelocity;
		y += yVelocity;
		z += zVelocity;
	}
	
	public void alignPosition(){
		if(x % World.CUBE_SIZE >= World.CUBE_SIZE / 2){
			x += World.CUBE_SIZE - (x % World.CUBE_SIZE);
		}
		else if(x % World.CUBE_SIZE < World.CUBE_SIZE / 2){
			x -= x % World.CUBE_SIZE;
		}
		
		if(y % World.CUBE_SIZE >= World.CUBE_SIZE / 2){
			y += World.CUBE_SIZE - (y % World.CUBE_SIZE);
		}
		else if(y % World.CUBE_SIZE < World.CUBE_SIZE / 2){
			y -= y % World.CUBE_SIZE;
		}
		
		if(z % World.CUBE_SIZE >= World.CUBE_SIZE / 2){
			z += World.CUBE_SIZE - (z % World.CUBE_SIZE);
		}
		else if(z % World.CUBE_SIZE < World.CUBE_SIZE / 2){
			z -= z % World.CUBE_SIZE;
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
	
	public int getGridX(){ return (int)x / World.CUBE_SIZE; }
	public int getGridY(){ return (int)y / World.CUBE_SIZE; }
	public int getGridZ(){ return (int)z / World.CUBE_SIZE; }
}