package game;
import java.awt.Rectangle;
import java.util.ArrayList;


public class TopBox extends Wall{
	private int xVelocity, yVelocity;
	private int moveSpeed = 1;
	private Rectangle rightCollisionRect;
	private Rectangle leftCollisionRect;
	private Rectangle topCollisionRect;
	private Rectangle bottomCollisionRect;
	private Rectangle topLeftCR;
	private Rectangle topRightCR;
	private Rectangle bottomLeftCR;
	private Rectangle bottomRightCR;
	
	private Rectangle bottomPushRect;
	private Rectangle topPushRect;
	private Rectangle leftPushRect;
	private Rectangle rightPushRect;
	
	private boolean pushUp; // this box is being pushed upward from the bottom
	private boolean pushDown;
	private boolean pushLeft;
	private boolean pushRight;
	
	public TopBox(int x, int y, int width, int height){
		super(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		updateRectangles();
	}
	
	/*
	 * topbox movement is event driven from the player using the push methods
	 */
	public void pushLeft(ArrayList<Wall> walls){ 
		pushLeft = true;
		move(walls);
	}
	public void pushRight(ArrayList<Wall> walls){
		pushRight = true;
		move(walls);
	}
	public void pushDown(ArrayList<Wall> walls){
		pushDown = true;
		move(walls);
	}
	public void pushUp(ArrayList<Wall> walls){
		pushUp = true;
		move(walls);
	}
	
	private void move(ArrayList<Wall> walls){
		xVelocity = 0;
		yVelocity = 0;
		if(pushUp){
			yVelocity = moveSpeed;
		}
		else if(pushDown){
			yVelocity = -moveSpeed;
		}
		if(pushLeft){
			xVelocity = -moveSpeed;
		}
		else if(pushRight){
			xVelocity = moveSpeed;
		}
		
		pushUp = false;
		pushDown = false;
		pushLeft = false;
		pushRight = false;
		
		collisionDetection(walls);
		
		x += xVelocity;
		y += yVelocity;
		updateRectangles();
	}

	public void updateRectangles(){
		bounding = new Rectangle(x, y, width, height);

		//top is sort of bottom here because awt and opengl coordinate systems / rectangle origins
		rightCollisionRect = new Rectangle(x + width, y, moveSpeed, height);
		leftCollisionRect = new Rectangle(x - moveSpeed, y, moveSpeed, height);
		topCollisionRect = new Rectangle(x, y - moveSpeed, width, moveSpeed);
		bottomCollisionRect = new Rectangle(x, y + height, width, moveSpeed);
		topLeftCR = new Rectangle(x - moveSpeed, y - moveSpeed, moveSpeed, moveSpeed);
		topRightCR = new Rectangle(x + width, y - moveSpeed, moveSpeed, moveSpeed);
		bottomLeftCR = new Rectangle(x - moveSpeed, y + height, moveSpeed, moveSpeed);
		bottomRightCR = new Rectangle(x + width, y + height, moveSpeed, moveSpeed);
		
		topPushRect = new Rectangle(x, y, width, moveSpeed);
		bottomPushRect = new Rectangle(x, y + height - moveSpeed, width, moveSpeed);
		leftPushRect = new Rectangle(x, y , moveSpeed, height);
		rightPushRect = new Rectangle(x + width - moveSpeed, y, moveSpeed, height);
	}

	private void collisionDetection(ArrayList<Wall> walls){
		//set true if that box intersects any wall
		boolean topLeft = false;
		boolean top = false;
		boolean topRight = false;
		boolean right = false;
		boolean bottomRight = false;
		boolean bottom = false;
		boolean bottomLeft = false;
		boolean left = false;

		for(Wall w : walls){
			if(this.topLeftCR.intersects(w.getBounding())){
				topLeft = true;
			}				
			if(this.topRightCR.intersects(w.getBounding())){
				topRight = true;
			}				
			if(this.bottomLeftCR.intersects(w.getBounding())){
				bottomLeft = true;
			}				
			if(this.bottomRightCR.intersects(w.getBounding())){
				bottomRight = true;
			}				
			if(this.rightCollisionRect.intersects(w.getBounding())){
				right = true;
			}				
			if(this.leftCollisionRect.intersects(w.getBounding())){
				left = true;
			}				
			if(this.topCollisionRect.intersects(w.getBounding())){
				top = true;
			}				
			if(this.bottomCollisionRect.intersects(w.getBounding())){
				bottom  = true;
			}
		}

		//moving against a wall
		if(top && yVelocity < 0){
			yVelocity = 0;
		}

		if(bottom && yVelocity > 0){
			yVelocity = 0;
		}

		if(left && xVelocity < 0){
			xVelocity = 0;
		}

		if(right && xVelocity > 0){
			xVelocity = 0;
		}

		//gap in wall and holding 2 directions

		//gap above
		if(topLeft && yVelocity < 0 && xVelocity < 0 && topRight){ //moving left and up
			xVelocity = 0;
		}
		else if(topRight && yVelocity < 0 && xVelocity > 0 && topLeft){ //moving right and up
			xVelocity = 0;
		}

		//gap on left
		if(topLeft && yVelocity < 0 && xVelocity < 0 && bottomLeft){ //moving left and up
			yVelocity = 0;
		}		
		else if(bottomLeft && yVelocity > 0 && xVelocity < 0 && topLeft){ //moving left and down
			yVelocity = 0;
		}

		//gap on right
		if(topRight && yVelocity < 0 && xVelocity > 0 && bottomRight){
			yVelocity = 0;
		}
		else if (topRight && yVelocity > 0 && xVelocity > 0 && bottomRight){
			yVelocity = 0;
		}

		//gap below
		if(bottomRight && yVelocity > 0 && xVelocity > 0 && bottomLeft){
			xVelocity = 0;
		}
		else if (bottomRight && yVelocity > 0 && xVelocity < 0 && bottomLeft){
			xVelocity = 0;
		}

		//top left corner only colliding
		if(topLeft && ( ! top && ! left)){
			if(xVelocity < 0 && yVelocity < 0){
				xVelocity = 0;
			}
		}
		//topRight only
		if(topRight && ( ! top && ! right)){
			if(xVelocity > 0 && yVelocity < 0){
				xVelocity = 0;
			}
		}
		//bottom right corner only
		if(bottomRight && ( ! right && ! bottom)){
			if(xVelocity > 0 && yVelocity > 0){
				xVelocity = 0;
			}
		}
		//bottom left corner only
		if(bottomLeft && ( ! bottom && ! left)){
			if(xVelocity < 0 && yVelocity > 0){
				xVelocity = 0;
			}
		}
	}
	
	public int getX(){ return x; }
	public int getY(){ return y; }

	
	public Rectangle getLeftRect(){ return leftPushRect; }
	public Rectangle getRightRect(){ return rightPushRect; }
	public Rectangle getTopRect(){ return topPushRect; }
	public Rectangle getBottomRect(){ return bottomPushRect; }
	
	public void setPosition(int boxX, int boxY){
		x = boxX;
		y = boxY;
	}
}
