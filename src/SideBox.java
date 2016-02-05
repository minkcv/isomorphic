import java.awt.Rectangle;
import java.util.ArrayList;


public class SideBox extends Wall{
	private Rectangle topRectangle;
	private Rectangle leftRectangle;
	private Rectangle rightRectangle;
	private Rectangle bottomRectangle;
	private Rectangle belowPlayersFeetRectangle;

	private Rectangle leftPushRect;
	private Rectangle rightPushRect;

	private int xMoveSpeed = 1;
	private int xVelocity, yVelocity;
	private int fallFactor; // lower value falls faster
	private double fallingTime; // how long the box has been falling
	private int maxFallSpeed;
	private boolean touchingGround;
	private final int normalFallFactor = 60;

	private boolean pushLeft;
	private boolean pushRight;

	private boolean zSide;

	public SideBox(int x, int y, int width, int height){
		super(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		maxFallSpeed = -4;
		fallFactor = normalFallFactor;
		updateVerticalRectangles();
		updateHorizontalRectangles();
	}

	/*
	 * sideboxes horizontal movement is event driven from the player using the push methods
	 * verical movement is actively updated
	 */
	public void pushLeft(ArrayList<Wall> walls, boolean zSide){
		pushLeft = true;
		move(walls, zSide);
	}
	public void pushRight(ArrayList<Wall> walls, boolean zSide){
		pushRight = true;
		move(walls, zSide);
	}

	// only updates vertical aspect of box
	public void update(ArrayList<Wall> walls, int boxY, boolean zSide){
		y = boxY;
		this.zSide = zSide;
		bounding = new Rectangle(x, y, width, height);
		belowPlayersFeetRectangle = new Rectangle(x, y - 1, width, 1);

		touchingGround = false;
		for(Wall w : walls){
			if(belowPlayersFeetRectangle.intersects(w.getBounding())){
				touchingGround = true;
				yVelocity = 0;
			}
		}

		if(!touchingGround){
			fallingTime += 15;
			yVelocity -= (fallingTime / fallFactor);

			if(yVelocity < maxFallSpeed){
				yVelocity = maxFallSpeed;
			}
		}

		updateVerticalRectangles();
		verticalCollisionDetection(walls);

		y += yVelocity;
	}

	//to be called by push methods
	private void move(ArrayList<Wall> walls, boolean zSide){
		if(pushLeft){
			if(zSide)
				xVelocity = -xMoveSpeed;
			else
				xVelocity = xMoveSpeed;
		}
		else if(pushRight){
			if(zSide)
				xVelocity = xMoveSpeed;
			else
				xVelocity = -xMoveSpeed;
		}
		else{
			xVelocity = 0;
		}

		pushLeft = false;
		pushRight = false;

		boolean rightCollides = false;
		boolean leftCollides = false;

		for(Wall w : walls){
			//horizontal
			if(leftRectangle.intersects(w.getBounding())){
				leftCollides = true;
			}
			if(rightRectangle.intersects(w.getBounding())){
				rightCollides = true;
			}
		}

		//horizontal stopping
		if(leftCollides && xVelocity > 0){
			xVelocity = 0;
		}

		if(rightCollides && xVelocity < 0){
			xVelocity = 0;
		}

		x += xVelocity;
		updateHorizontalRectangles();
	}
	
	private void updateHorizontalRectangles(){
		bounding = new Rectangle(x, y, width, height);
		leftPushRect = new Rectangle(x + width - xMoveSpeed, y, xMoveSpeed, height);
		rightPushRect = new Rectangle(x, y, xMoveSpeed, height);		
		leftRectangle = new Rectangle(x + width, y - Math.abs(yVelocity), xMoveSpeed, height + Math.abs(yVelocity));
		rightRectangle = new Rectangle(x - xMoveSpeed, y - Math.abs(yVelocity), xMoveSpeed, height + Math.abs(yVelocity));
	}

	private void updateVerticalRectangles(){
		// AWT Rectangles have different origin specification than opengl
		topRectangle = new Rectangle(x, y + (Math.abs(yVelocity) + height), width, Math.abs(yVelocity));
		bottomRectangle = new Rectangle(x, y - Math.abs(yVelocity), width, Math.abs(yVelocity));
	}
	
	public void updateRectangles(){
		updateHorizontalRectangles();
		updateVerticalRectangles();
	}

	private void verticalCollisionDetection(ArrayList<Wall> walls){
		int partialFallDistance = Integer.MAX_VALUE;
		int partialJumpDistance = Integer.MAX_VALUE;
		
		for(Wall w : walls){

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

	public int getX(){ return x; }
	public int getY(){ return y; }

	public Rectangle getLeftRect(){ return leftPushRect; }
	public Rectangle getRightRect(){ return rightPushRect; }

	public boolean isZSide(){ return zSide; }
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
}
