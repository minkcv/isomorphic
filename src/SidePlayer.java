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
	private Rectangle leftBoxRect; // height tall
	private Rectangle rightBoxRect;
	private Rectangle bottomRectangle;
	private Rectangle bottomLeftRectangle;
	private Rectangle bottomRightRectangle;
	private Rectangle belowPlayersFeetRectangle;

	private int xMoveSpeed = 1;
	private int jumpSpeed;
	private int fallFactor; // lower value falls faster
	private double fallingTime; // how long the player has been falling
	private int maxFallSpeed;
	private boolean touchingGround;
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

		touchingGround = false;
		for(Wall w : walls){
			if(belowPlayersFeetRectangle.intersects(w.getBounding())){
				touchingGround = true;
				yVelocity = 0;
			}
		}

		//jumping
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			if(wReleased && touchingGround){
				touchingGround = false;
				fallingTime = 0;
			}
			wReleased = false;
		}
		else{
			wReleased = true;
		}

		if(!wReleased && !touchingGround){
			yVelocity += jumpSpeed;		
		}

		if(!touchingGround){
			fallingTime += 15;
			yVelocity -= (fallingTime / fallFactor);

			if(yVelocity < maxFallSpeed){
				yVelocity = maxFallSpeed;
			}
		}

		updateRectangles();
		boxCollisions(boxes);
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
		leftBoxRect = new Rectangle(x + width, y - Math.abs(yVelocity), Math.abs(xVelocity), height);
		rightBoxRect = new Rectangle(x - Math.abs(xVelocity), y - Math.abs(yVelocity), Math.abs(xVelocity), height);
		bottomLeftRectangle = new Rectangle(x + width, y - Math.abs(yVelocity), Math.abs(xVelocity), Math.abs(yVelocity));
		bottomRightRectangle = new Rectangle(x - Math.abs(xVelocity), y - Math.abs(yVelocity), Math.abs(xVelocity), Math.abs(yVelocity));
		bottomRectangle = new Rectangle(x, y - Math.abs(yVelocity), width, Math.abs(yVelocity));
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
		if(touchingGround){
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
	
	private void boxCollisions(ArrayList<SideBox> boxes){
		for(SideBox b : boxes){
			if(leftBoxRect.intersects(b.getRightRect())){
				if(b.isZSide())
					b.pushRight();
				else
					b.pushLeft();
			}
			else if(rightBoxRect.intersects(b.getLeftRect())){
				if(b.isZSide())
					b.pushLeft();
				else
					b.pushRight();
			}
		}
	}
	public int getX(){ return x; }
	public int getY(){ return y; }
}
