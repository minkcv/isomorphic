package game;
import java.util.ArrayList;

import gui.Message;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Player {
	private Game game;
	private ArrayList<Item> collectedItems;
	private float x, y, z;
	private float width, depth, height;
	private TopPlayer topPlayer;
	private SidePlayer sidePlayer;
	private boolean eReleased;
	private boolean onGround;
	private Message currentMessage;
	private boolean readingMessage;
	public Player(Game game, float x, float y, float z){
		this.game = game;
		this.x = x;
		this.y = y;
		this.z = z;
		width = World.CUBE_SIZE;
		height = World.CUBE_SIZE * 2;
		depth = World.CUBE_SIZE;
		collectedItems = new ArrayList<Item>();
		topPlayer = new TopPlayer((int)x, (int)z, (int)width, (int)depth);
		sidePlayer = new SidePlayer((int)z, (int)y, (int)width, (int)height);
	}

	public void update(Camera.Direction direction, World world){
		if(! readingMessage){
			if(direction == Camera.Direction.X){
				sidePlayer.update(world.getYZWalls(), world.getYZBoxes(), (int)z, (int)y, false);
				z = sidePlayer.getX();
				y = sidePlayer.getY();
				onGround = sidePlayer.onGround();
				for(Switch s : world.getSwitchesInPlane()){
					s.setPressed(false);
					if(sidePlayer.intersects(s.getSideRectangle())){
						s.setPressed(true);
					}
				}
				for(Item i : world.getItemsInPlane()){
					if(sidePlayer.intersects(i.getSideRectangle())){
						collectedItems.add(i);
						world.collectItem(i);
					}
				}
			}
			else if(direction == Camera.Direction.Y){
				topPlayer.update(world.getXZWalls(), world.getXZBoxes(), (int)x, (int)z);
				x = topPlayer.getX();
				z = topPlayer.getY();
				for(Switch s : world.getSwitchesInPlane()){
					s.setPressed(false);
					if(topPlayer.intersects(s.getTopRectangle())){
						s.setPressed(true);
					}
				}
				for(Item i : world.getItemsInPlane()){
					if(topPlayer.intersects(i.getXZRectangle())){
						collectedItems.add(i);
						world.collectItem(i);
					}
				}
			}
			else if(direction == Camera.Direction.Z){
				sidePlayer.update(world.getXYWalls(), world.getXYBoxes(), (int)x, (int)y, true);
				x = sidePlayer.getX();
				y = sidePlayer.getY();
				onGround = sidePlayer.onGround();
				for(Switch s : world.getSwitchesInPlane()){
					s.setPressed(false);
					if(sidePlayer.intersects(s.getSideRectangle())){
						s.setPressed(true);
					}
				}
				for(Item i : world.getItemsInPlane()){
					if(sidePlayer.intersects(i.getSideRectangle())){
						collectedItems.add(i);
						world.collectItem(i);
					}
				}
			}
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			if(eReleased){
				game.usePortalOrSave((int)x, (int)y, (int)z);
				Message newMessage = onSign(world.getSignsInPlane(), direction);
				if(!readingMessage && newMessage != null){
					currentMessage = newMessage;
					currentMessage.reset();
					game.readMessage(currentMessage);
					readingMessage = true;
				}
				else if(currentMessage != null && currentMessage.isFinished()){
					currentMessage.dismiss();
					readingMessage = false;
				}
			}
			eReleased = false;
		}
		else{
			eReleased = true;
		}

		// keep player in world bounds
		if(x < 0)
			x = 0;
		if(y < 0)
			y = 0;
		if(z < 0)
			z = 0;
		if(x > World.getSizeX() * World.CUBE_SIZE - World.CUBE_SIZE)
			x = World.getSizeX() * World.CUBE_SIZE - World.CUBE_SIZE;
		if(y > World.getSizeY() * World.CUBE_SIZE - World.CUBE_SIZE)
			y = World.getSizeY() * World.CUBE_SIZE - World.CUBE_SIZE;
		if(z > World.getSizeZ() * World.CUBE_SIZE - World.CUBE_SIZE)
			z = World.getSizeZ() * World.CUBE_SIZE - World.CUBE_SIZE;

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
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			// light faces
			// front z face
			GL11.glColor3f(1, 1, 1);
			GL11.glNormal3f(0, 0, -1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(width / 2, 0, 0);
			GL11.glVertex3f(width / 2, height, 0);
			
			GL11.glVertex3f(width / 2, height, 0);
			GL11.glVertex3f(0, height, 0);
			GL11.glVertex3f(0, 0, 0);
			
			// front x face
			GL11.glNormal3f(-1, 0, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, 0, depth / 2);
			GL11.glVertex3f(0, height, depth / 2);
			
			GL11.glVertex3f(0, height, depth / 2);
			GL11.glVertex3f(0, height, 0);
			GL11.glVertex3f(0, 0, 0);
			
			// top face
			GL11.glNormal3f(0, 1, 0);
			GL11.glVertex3f(0, height, 0);
			GL11.glVertex3f(width / 2, height, 0);
			GL11.glVertex3f(0, height, depth / 2);
			
			// dark faces
			// front z faces
			GL11.glColor3f(0, 0, 0);
			GL11.glNormal3f(0, 0, -1);
			GL11.glVertex3f(width / 2, 0, 0);
			GL11.glVertex3f(width, 0, 0);
			GL11.glVertex3f(width, height, 0);
			
			GL11.glVertex3f(width, height, 0);
			GL11.glVertex3f(width / 2, height, 0);
			GL11.glVertex3f(width / 2, 0, 0);
			
			// front x faces
			GL11.glNormal3f(-1, 0, 0);
			GL11.glVertex3f(0, 0, depth / 2);
			GL11.glVertex3f(0, 0, depth);
			GL11.glVertex3f(0, height, depth);
			
			GL11.glVertex3f(0, height, depth);
			GL11.glVertex3f(0, height, depth / 2);
			GL11.glVertex3f(0, 0, depth / 2);
			
			// back z face
			GL11.glNormal3f(0, 0, 1);
			GL11.glVertex3f(0, 0, depth);
			GL11.glVertex3f(width, 0, depth);
			GL11.glVertex3f(width, height, depth);
			
			GL11.glVertex3f(0, 0, depth);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(0, height, depth);
			
			// back x face
			GL11.glNormal3f(1, 0, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, 0, 0);
			GL11.glVertex3f(width, 0, depth);
			
			GL11.glVertex3f(width, 0, 0);
			GL11.glVertex3f(width, height, 0);
			GL11.glVertex3f(width, height, depth);
			
			// top face
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(width, height, 0);
			GL11.glVertex3f(width / 2, height, 0);
			
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(0, height, depth);
			GL11.glVertex3f(0, height, depth / 2);
			
			GL11.glVertex3f(width, height, depth);
			GL11.glVertex3f(0, height, depth / 2);
			GL11.glVertex3f(width / 2, height, 0);
			
		}
		GL11.glEnd();
		GL11.glTranslatef(-x, -y, -z);
	}
	
	private void oldRender(){
//		GL11.glBegin(GL11.GL_QUADS);
//		{
//			GL11.glNormal3f(0, 0, -1);
//			GL11.glColor3f(0, 0, 0);
//			GL11.glVertex3f(0, 0, 0);
//			GL11.glColor3f(1, 1, 1);
//			GL11.glVertex3f(0, height, 0);
//			GL11.glVertex3f(width, height, 0);
//			GL11.glColor3f(0, 0, 0);
//			GL11.glVertex3f(width, 0, 0);
//
//			//bottom
//			GL11.glNormal3f(0, -1, 0);
//			GL11.glColor3f(0, 0, 0);
//			GL11.glVertex3f(0, 0, 0);
//			GL11.glVertex3f(width, 0, 0);
//			GL11.glVertex3f(width, 0, depth);
//			GL11.glVertex3f(0, 0, depth);
//
//			GL11.glNormal3f(-1, 0, 0);
//			GL11.glColor3f(0, 0, 0);
//			GL11.glVertex3f(0, 0, 0);
//			GL11.glVertex3f(0, 0, depth);
//			GL11.glColor3f(1, 1, 1);
//			GL11.glVertex3f(0, height, depth);
//			GL11.glVertex3f(0, height, 0);
//
//			GL11.glNormal3f(0, 0, 1);
//			GL11.glColor3f(1, 1, 1);
//			GL11.glVertex3f(width, height, depth);
//			GL11.glVertex3f(0, height, depth);
//			GL11.glColor3f(0, 0, 0);
//			GL11.glVertex3f(0, 0, depth);
//			GL11.glVertex3f(width, 0, depth);
//
//			//top
//			GL11.glNormal3f(0, 1, 0);
//			GL11.glColor3f(1, 1, 1);
//			GL11.glVertex3f(width, height, depth);
//			GL11.glVertex3f(width, height, 0);
//			GL11.glVertex3f(0, height, 0);
//			GL11.glVertex3f(0, height, depth);
//
//			GL11.glNormal3f(1, 0, 0);
//			GL11.glColor3f(1, 1, 1);
//			GL11.glVertex3f(width, height, depth);
//			GL11.glColor3f(0, 0, 0);
//			GL11.glVertex3f(width, 0, depth);
//			GL11.glVertex3f(width, 0, 0);
//			GL11.glColor3f(1, 1, 1);
//			GL11.glVertex3f(width, height, 0);
//		}
//		GL11.glEnd();
	}

	private Message onSign(ArrayList<Sign> signs, Camera.Direction direction){
		if(direction == Camera.Direction.Y){ // top
			for(Sign s : signs){
				if(topPlayer.intersects(s.getTopSign().getLeftRectangle()) || 
						topPlayer.intersects(s.getTopSign().getRightRectangle()) ||
						topPlayer.intersects(s.getTopSign().getTopRectangle()) ||
						topPlayer.intersects(s.getTopSign().getBottomRectangle())){
					return s.getMessage();
				}
			}
		}
		else if(direction == Camera.Direction.X || direction == Camera.Direction.Z){ // side
			for(Sign s : signs){
				if(sidePlayer.intersects(s.getSideSign().getLeftRectangle()) || 
						sidePlayer.intersects(s.getSideSign().getRightRectangle())){
					return s.getMessage();
				}
			}
		}
		return null;
	}

	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getZ(){ return z; }

	public int getGridX(){ return (int)x / World.CUBE_SIZE; }
	public int getGridY(){ return (int)y / World.CUBE_SIZE; }
	public int getGridZ(){ return (int)z / World.CUBE_SIZE; }
	
	public ArrayList<Item> getCollectedItems(){ return collectedItems; }
	
	/** disable the player while a message gets read.
	 	you will need to send the message to the messenger and activate it too! */
	public void readMessage(Message m){
		readingMessage = true;
		currentMessage = m;
	}
	public boolean isReadingMessage(){ return readingMessage; }
	public boolean isOnGround(){ return onGround; }

	public void setPosition(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
