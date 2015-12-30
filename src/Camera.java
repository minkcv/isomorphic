

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Camera {
	private Game game;
	private float rotateSpeed = 5;
	private float cameraX, cameraY, cameraZ;
	private float xRotation, yRotation;
	private float newXRotation, newYRotation;
	public static enum Direction {
		X, Y, Z, ISO
	}
	private Direction direction;
	public Camera(Game game){
		this.game = game;
		direction = Direction.ISO;
		newXRotation = 45;
		newYRotation = 45;
	}
	
	public void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			game.alignPlayer();
			direction = Direction.ISO;
			newXRotation = 45;
			newYRotation = 45;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			game.alignPlayer();
			direction = Direction.X;
			newXRotation = 90;
			newYRotation = 0;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			game.alignPlayer();
			direction = Direction.Y;
			newXRotation = 0;
			newYRotation = 90;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			game.alignPlayer();
			direction = Direction.Z;
			newXRotation = 0;
			newYRotation = 0;
		}
		if(xRotation > newXRotation)
			xRotation -= rotateSpeed;
		if(xRotation < newXRotation)
			xRotation += rotateSpeed;
		if(yRotation > newYRotation)
			yRotation -= rotateSpeed;
		if(yRotation < newYRotation)
			yRotation += rotateSpeed;
	}
	
	public void transformToCamera(){
		GL11.glRotatef(-yRotation, 1, 0, 0);
		GL11.glRotatef(-xRotation, 0, 1, 0);
		GL11.glTranslatef(-cameraX, -cameraY, -cameraZ);
	}
	
	public void setPosition(float x, float y, float z){
		cameraX = x;
		cameraY = y;
		cameraZ = z;
	}
	
	public Direction getDirection(){
		return direction;
	}
}
