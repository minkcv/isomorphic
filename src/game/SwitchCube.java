package game;

import game.Camera.Direction;

// probably didn't need to extend cube here
public class SwitchCube extends Cube implements ActiveObject {
	private int gridX, gridY, gridZ;
	private int switchID; // id of the switch that causes this cube to move
	private TopBox topBox;
	private SideBox sideBox;
	private Camera.Direction previousDirection;
	private boolean active = true;
	private boolean mode; // determines whether this cube turns on or off when the switch is triggered
	public SwitchCube(float x, float y, float z, float width, float height, float depth, float r, float g, float b, int switchID, boolean mode){
		super(x, y, z, width, height, depth, r, g, b, false);
		gridX = (int)(x / World.CUBE_SIZE);
		gridY = (int)(y / World.CUBE_SIZE);
		gridZ = (int)(z / World.CUBE_SIZE);
		this.switchID = switchID;
		topBox = new TopBox((int)x, (int)z, (int)width, (int)depth);
		sideBox = new SideBox((int)x, (int)y, (int)width, (int)height);
	}
	@Override
	public void update(World world, Direction direction) {
		if(previousDirection != direction){
			if(direction == Camera.Direction.Y){
				topBox.setPosition((int)x, (int)z);
				topBox.updateRectangles();
			}
			else if(direction == Camera.Direction.X){
				sideBox.setPosition((int)z, (int)y);
				sideBox.updateRectangles();
			}
			else if(direction == Camera.Direction.Z){
				sideBox.setPosition((int)x, (int)y);
				sideBox.updateRectangles();
			}

			previousDirection = direction;
		}
		else{
			if(direction == Camera.Direction.Y){
				x = topBox.getX();
				z = topBox.getY();
			}
			else if(direction == Camera.Direction.X){
				sideBox.update(world.getYZWalls(), (int)y, false);
				z = sideBox.getX();
				y = sideBox.getY();
			}
			else if(direction == Camera.Direction.Z){
				sideBox.update(world.getXYWalls(), (int)y, true);
				x = sideBox.getX();
				y = sideBox.getY();
			}
		}
	}
	
	@Override
	public void alignPosition() {
		// TODO Auto-generated method stub
		
	}
	public boolean isActive(){ return active; }
	public void setActive(boolean a){
		active = a;
	}
	@Override
	public void render(float shade) {
		if(active)
			super.render(shade, true);
	}
	public SideBox getSideBox(){
		return sideBox;
	}
	public TopBox getTopBox(){
		return topBox;
	}
	public boolean getMode(){ return mode; }
	public int getSwitchID(){ return switchID; }
	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	@Override
	public float getZ() { return z; }
	@Override
	public int getGridX() { return gridX; }
	@Override
	public int getGridY() { return gridY; }
	@Override
	public int getGridZ() { return gridZ; }
}
