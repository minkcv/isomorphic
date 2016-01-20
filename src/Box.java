import org.lwjgl.opengl.GL11;


public class Box extends Cube{
	private TopBox topBox;
	private SideBox sideBox;
	public Box(float x, float y, float z, 
			float width, float height, float depth,
			float r, float g, float b){
		super(x, y, z, width, height, depth, r, g, b);
		
		topBox = new TopBox((int)x, (int)z, (int)width, (int)height);
		sideBox = new SideBox((int)x, (int)y, (int)width, (int)height);
	}

	public void update(World world, Camera.Direction direction){
		if(direction == Camera.Direction.Y){
			topBox.update(world.getXZWalls(), (int)x, (int)z);
			x = topBox.getX();
			z = topBox.getY();
		}
		else if(direction == Camera.Direction.X){
			sideBox.update(world.getYZWalls(), (int)z, (int)y, false);
			z = sideBox.getX();
			y = sideBox.getY();
		}
		else if(direction == Camera.Direction.Z){
			sideBox.update(world.getXYWalls(), (int)x, (int)y, true);
			x = sideBox.getX();
			y = sideBox.getY();
		}
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

	public void render(float shade){
		if(!culled){
			GL11.glTranslatef(x, y, z);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glColor3f(r - shade, g - shade, b - shade);


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
	}
	
	public TopBox getTopBox(){ return topBox; }
	public SideBox getSideBox(){ return sideBox; }
	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getZ(){ return z; }
}
