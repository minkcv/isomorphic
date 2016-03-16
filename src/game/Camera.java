package game;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Camera {
	private Game game;
	private float rotateSpeed = 5;
	private float cameraX, cameraY, cameraZ;
	private float xRotation, yRotation;
	private float newXRotation, newYRotation;
	private boolean downReleased, leftReleased, upReleased, rightReleased;
	private boolean rotating;
	public static enum Direction {
		X, Y, Z, ISO, FREE
	}
	private Direction direction;
	private Direction previousDirection;
	public Camera(Game game){
		this.game = game;
		direction = Direction.Z;
		newXRotation = 0;
		newYRotation = 0;
		xRotation = 0;
		yRotation = 0;
	}

	public void update(){
		if(game.playerOnGround() && ! game.playerReadingMessage()){
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				if(downReleased && direction != Direction.ISO){
					direction = Direction.ISO;
					newXRotation = 45;
					newYRotation = 45;
				}
				downReleased = false;
			}
			else{
				downReleased = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				if(leftReleased && direction != Direction.X){
					direction = Direction.X;
					newXRotation = 90;
					newYRotation = 0;
				}
				leftReleased = false;
			}
			else{
				leftReleased = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
				if(upReleased && direction != Direction.Y){
					direction = Direction.Y;
					newXRotation = 0;
					newYRotation = 90;
				}
				upReleased = false;
			}
			else{
				upReleased = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				if(rightReleased && direction != Direction.Z){
					direction = Direction.Z;
					newXRotation = 0;
					newYRotation = 0;
				}
				rightReleased = false;
			}
			else{
				rightReleased = true;
			}

			if(Mouse.isButtonDown(0)){
				direction = Direction.FREE;
			}
			else if(direction == Direction.FREE){
				direction = Direction.ISO;
				newXRotation = 45;
				newYRotation = 45;
				xRotation = 45; // non smooth rotation
				yRotation = 45;
			}
		}

		if(direction == Direction.FREE){
			xRotation -= Mouse.getDX();
			yRotation -= Mouse.getDY();
		}
		else{
			Mouse.getDX(); // return value not used, intentional side effect
			Mouse.getDY();
			if(xRotation > newXRotation)
				xRotation -= rotateSpeed;
			if(xRotation < newXRotation)
				xRotation += rotateSpeed;
			if(yRotation > newYRotation)
				yRotation -= rotateSpeed;
			if(yRotation < newYRotation)
				yRotation += rotateSpeed;
		}

		if(direction != previousDirection){
			game.alignPlayer();
			game.alignBoxes();
			game.computeObjectsInPlane(direction);
		}
		rotating = (xRotation != newXRotation) || (yRotation != newYRotation);
		previousDirection = direction;
	}

	public void transformToCamera(){
		GL11.glRotatef(-yRotation, 1, 0, 0);
		GL11.glRotatef(-xRotation, 0, 1, 0);
		GL11.glTranslatef(-cameraX, -cameraY, -cameraZ);
	}

	public void undoTransform(){
		GL11.glTranslatef(cameraX, cameraY, cameraZ);
		GL11.glRotatef(xRotation, 0, 1, 0);
		GL11.glRotatef(yRotation, 1, 0, 0);
	}

	public void rotateToCamera(){
		GL11.glRotatef(-yRotation, 1, 0, 0);
		GL11.glRotatef(-xRotation, 0, 1, 0);
	}
	
	public void undoRotate(){
		GL11.glRotatef(xRotation, 0, 1, 0);
		GL11.glRotatef(yRotation, 1, 0, 0);
	}

	public void setPosition(float x, float y, float z){
		cameraX = x;
		cameraY = y;
		cameraZ = z;
	}
	public void hardRotate(char r){
		if(r == 'x'){
			direction = Direction.X;
			newXRotation = 90;
			newYRotation = 0;
			xRotation = 90;
			yRotation = 0;
		}
		else if(r == 'y'){
			direction = Direction.Y;
			newXRotation = 0;
			newYRotation = 90;
			xRotation = 0;
			yRotation = 90;
		}
		else if(r == 'z'){
			direction = Direction.Z;
			newXRotation = 0;
			newYRotation = 0;
			xRotation = 0;
			yRotation = 0;
		}
	}

	public Direction getDirection(){
		return direction;
	}
	public boolean isRotating(){ return rotating; }

}
