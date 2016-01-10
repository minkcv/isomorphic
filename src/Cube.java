import org.lwjgl.opengl.GL11;


public class Cube {
	
	private float x, y, z;
	private float width, height, depth;

	public Cube(float x, float y, float z, 
				float width, float height, float depth){
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.depth = height;
		this.height = depth;
	}
	
	public void update(){
		
	}
	
	public void render(boolean inPlane){
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_QUADS);
		{
			if(inPlane)
				GL11.glColor3f(0, 0, 1f);
			else
				GL11.glColor3f(0, 0, 0.8f);
			
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
	
	public void setPosition(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
