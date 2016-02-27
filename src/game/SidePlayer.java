package game;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;


public class SidePlayer {
	private int x, y, width, height;
	private int xVelocity, yVelocity;
	private Rectangle bounding;
	private Rectangle topRectangle;
	private Rectangle topLeftRectangle;
	private Rectangle topRightRectangle;
	private Rectangle leftTallRectangle; // height and yVelocity tall (past feet)
	private Rectangle rightTallRectangle;
	private Rectangle leftPushRect; // height tall
	private Rectangle rightPushRect;
	private Rectangle bottomRectangle;
	private Rectangle bottomLeftRectangle;
	private Rectangle bottomRightRectangle;
	private Rectangle belowPlayersFeetRectangle;

	private int xMoveSpeed = 1;
	private int jumpSpeed;
	private int fallFactor; // lower value falls faster
	private double fallingTime; // how long the player has been falling
	private int maxFallSpeed;
	private boolean onGround;
	private boolean wReleased;
	private final int normalFallFactor = 60; // can jump 3 cubes high
	private final int highJumpFallFactor = 80; // can jump 5 cubes high

	public SidePlayer(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		jumpSpeed = 2;
		maxFallSpeed = -4;
		fallFactor = normalFallFactor;
	}

	public void update(ArrayList<Wall> walls, ArrayList<SideBox> boxes, int playerX, int playerY, boolean zSide){
		x = playerX;
		y = playerY;
		bounding = new Rectangle(x, y, width, height);
		belowPlayersFeetRectangle = new Rectangle(x, y - 1, width, 1);

		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			if(zSide)
				xVelocity = -xMoveSpeed;
			else
				xVelocity = xMoveSpeed;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			if(zSide)
				xVelocity = xMoveSpeed;
			else
				xVelocity = -xMoveSpeed;
		}
		else{
			xVelocity = 0;
		}

		onGround = false;
		for(Wall w : walls){
			if(belowPlayersFeetRectangle.intersects(w.getBounding())){
				onGround = true;
				yVelocity = 0;
			}
		}

		//jumping
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			if(wReleased && onGround){
				onGround = false;
				fallingTime = 0;
			}
			wReleased = false;
		}
		else{
			wReleased = true;
		}

		if(!wReleased && !onGround){
			yVelocity += jumpSpeed;		
		}

		if(!onGround){
			fallingTime += 15;
			yVelocity -= (fallingTime / fallFactor);

			if(yVelocity < maxFallSpeed){
				yVelocity = maxFallSpeed;
			}
		}

		updateRectangles();
		boxCollisions(boxes, walls, zSide);
		collisionDetection(walls);

		x += xVelocity;
		y += yVelocity;
	}

	private void updateRectangles(){
		// AWT Rectangles have different origin specification than opengl
		topRectangle = new Rectangle(x, y + (Math.abs(yVelocity) + height), width, Math.abs(yVelocity));
		topLeftRectangle = new Rectangle(x - Math.abs(xVelocity), y + (Math.abs(yVelocity) - height), Math.abs(xVelocity), Math.abs(yVelocity));
		topRightRectangle = new Rectangle(x + width, y + (Math.abs(yVelocity) + height), Math.abs(xVelocity), Math.abs(yVelocity));
		leftTallRectangle = new Rectangle(x + width, y - Math.abs(yVelocity), Math.abs(xVelocity), height + Math.abs(yVelocity));
		rightTallRectangle = new Rectangle(x - Math.abs(xVelocity), y - Math.abs(yVelocity), Math.abs(xVelocity), height + Math.abs(yVelocity));
		bottomLeftRectangle = new Rectangle(x + width, y - Math.abs(yVelocity), Math.abs(xVelocity), Math.abs(yVelocity));
		bottomRightRectangle = new Rectangle(x - Math.abs(xVelocity), y - Math.abs(yVelocity), Math.abs(xVelocity), Math.abs(yVelocity));
		bottomRectangle = new Rectangle(x, y - Math.abs(yVelocity), width, Math.abs(yVelocity));
		
		leftPushRect = new Rectangle(x + width, y, xMoveSpeed, height);
		rightPushRect = new Rectangle(x - xMoveSpeed, y, xMoveSpeed, height);
	}
	
	private void boxCollisions(ArrayList<SideBox> boxes, ArrayList<Wall> walls, boolean zSide){
		for(SideBox b : boxes){
			if(leftPushRect.intersects(b.getRightRect())){// && xVelocity > 0){
				if(b.isZSide())
					b.pushRight(walls, zSide);
				else
					b.pushLeft(walls, zSide);
			}
			else if(rightPushRect.intersects(b.getLeftRect())){// && xVelocity < 0){
				if(b.isZSide())
					b.pushLeft(walls, zSide);
				else
					b.pushRight(walls, zSide);
			}
		}
	}

	private void collisionDetection(ArrayList<Wall> walls){
		boolean rightCollides = false;
		boolean leftCollides = false;
		int partialFallDistance = Integer.MAX_VALUE;
		int partialJumpDistance = Integer.MAX_VALUE;

		Rectangle gapRect = new Rectangle(0, 0);
		boolean wallBelowGap = false;
		boolean wallAboveGap = false;
		for(Wall w : walls){

			//horizontal
			if(leftTallRectangle.intersects(w.getBounding())){
				rightCollides = true;
			}
			if(rightTallRectangle.intersects(w.getBounding())){
				leftCollides = true;
			}

			//vertical

			//top
			if(topRectangle.intersects(w.getBounding())){
				//int distanceToBottomOfWall = y - (w.getBounding().y + w.getBounding().height);
				int distanceToBottomOfWall = w.getBounding().y - (y + height);
				if(distanceToBottomOfWall < partialJumpDistance){
					partialJumpDistance = distanceToBottomOfWall;
				}
			}

			//bottom
			if(bottomRectangle.intersects(w.getBounding())){
				int distanceToTopOfWall = y - (w.getBounding().y + w.getBounding().height);
				if(distanceToTopOfWall < partialFallDistance){
					partialFallDistance = distanceToTopOfWall;
				}
			}

			// 2 block tall gap between walls
			if((xVelocity < 0 && bottomLeftRectangle.intersects(w.getBounding())) || (xVelocity > 0 && bottomRightRectangle.intersects(w.getBounding()))){
				gapRect = new Rectangle(w.getBounding().x, w.getBounding().y, w.getBounding().width, 2 * w.getBounding().height);
				wallBelowGap = true;
			}
			if((xVelocity < 0 && topLeftRectangle.intersects(w.getBounding())) || (xVelocity > 0 && topRightRectangle.intersects(w.getBounding()))){
				wallAboveGap = true;
			}
		}

		boolean gapOpen = true;
		for(Wall w : walls){
			if(gapRect.intersects(w.getBounding())){
				gapOpen = false;
			}
		}

		if(gapOpen && wallBelowGap && wallAboveGap){
			partialFallDistance = gapRect.y - this.bounding.y;
		}

		//horizontal stopping
		if(leftCollides && xVelocity < 0 && !(gapOpen && wallBelowGap && wallAboveGap)){
			xVelocity = 0;
		}

		if(rightCollides && xVelocity > 0 && !(gapOpen && wallBelowGap && wallAboveGap)){
			xVelocity = 0;			
		}

		//vertical stopping
		if(onGround){
			if(yVelocity < 0){
				yVelocity = 0;
			}
		}
		else{ // bottom
			if(yVelocity < 0 && partialFallDistance != Integer.MAX_VALUE){
				yVelocity = -partialFallDistance;
			}
		}

		// top
		if(yVelocity > 0 && partialJumpDistance != Integer.MAX_VALUE){
			yVelocity = partialJumpDistance;
		}
	}
	
	public boolean intersects(Rectangle r){
		return bounding.intersects(r);
	}

	public int getX(){ return x; }
	public int getY(){ return y; }
	public boolean onGround(){ return onGround; }
}
