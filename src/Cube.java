import org.lwjgl.opengl.GL11;


public class Cube {

	private float x, y, z;
	private float r, g, b;
	private float width, height, depth;
	private boolean culled;

	public Cube(float x, float y, float z, 
			float width, float height, float depth,
			float r, float g, float b){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.width = width;
		this.depth = height;
		this.height = depth;
		culled = false;
	}

	public void update(){

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

	public void setCulled(boolean c){ culled = c; }

	public void setPosition(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
