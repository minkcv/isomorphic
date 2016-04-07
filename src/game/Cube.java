package game;
import org.lwjgl.opengl.GL11;


public class Cube {

	protected float x, y, z;
	protected float r, g, b, a;
	protected float width, height, depth;
	protected boolean xyHidden, xzHidden, yzHidden; // front faces
	protected boolean xyrHidden, xzrHidden, yzrHidden; // rear faces
	protected boolean culled; // not rendered because not currently visible
	private boolean invisible; // permanently not rendered

	public Cube(float x, float y, float z, 
			float width, float height, float depth,
			float r, float g, float b, boolean invisible){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.invisible = invisible;
		this.width = width;
		this.depth = depth;
		this.height = height;
	}
	
	public void update(int delta){
		
	}

	public void render(float shade, boolean slice){
		if(!culled && !invisible){
			GL11.glTranslatef(x, y, z);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glFrontFace(GL11.GL_CW);
			GL11.glColor3f(r - shade, g - shade, b - shade);
			GL11.glBegin(GL11.GL_QUADS);
			{

				if(slice || !xyHidden){
					GL11.glNormal3f(0, 0, -1);
					GL11.glVertex3f(0, 0, 0);
					GL11.glVertex3f(0, height, 0);
					GL11.glVertex3f(width, height, 0);
					GL11.glVertex3f(width, 0, 0);
				}

				if(slice || !xzHidden){
					//GL11.glColor3f(0, 1, 0);
					GL11.glNormal3f(0, 1, 0);
					GL11.glVertex3f(width, height, depth);
					GL11.glVertex3f(width, height, 0);
					GL11.glVertex3f(0, height, 0);
					GL11.glVertex3f(0, height, depth);
				}

				if(slice || !yzHidden){
					//GL11.glColor3f(0, 0, 1);
					GL11.glNormal3f(-1, 0, 0);
					GL11.glVertex3f(0, 0, 0);
					GL11.glVertex3f(0, 0, depth);
					GL11.glVertex3f(0, height, depth);
					GL11.glVertex3f(0, height, 0);
				}

				if(slice || !xyrHidden){
					//GL11.glColor3f(1, 0, 0);
					GL11.glNormal3f(0, 0, 1);
					GL11.glVertex3f(width, height, depth);
					GL11.glVertex3f(0, height, depth);
					GL11.glVertex3f(0, 0, depth);
					GL11.glVertex3f(width, 0, depth);
				}

				if(slice || !xzrHidden){
					//GL11.glColor3f(0, 1, 0);
					GL11.glNormal3f(0, -1, 0);
					GL11.glVertex3f(0, 0, 0);
					GL11.glVertex3f(width, 0, 0);
					GL11.glVertex3f(width, 0, depth);
					GL11.glVertex3f(0, 0, depth);
				}

				if(slice || !yzrHidden){
					//GL11.glColor3f(0, 0, 1);
					GL11.glNormal3f(1, 0, 0);
					GL11.glVertex3f(width, height, depth);
					GL11.glVertex3f(width, 0, depth);
					GL11.glVertex3f(width, 0, 0);
					GL11.glVertex3f(width, height, 0);
				}
			}
			GL11.glEnd();
			GL11.glTranslatef(-x, -y, -z);
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
	}

	public void setCulled(boolean c){ culled = c; }
	public void setHiddenFaces(boolean xy, boolean xz, boolean yz, boolean xyr, boolean xzr, boolean yzr){
		xyHidden = xy;
		xzHidden = xz;
		yzHidden = yz;
		xyrHidden = xyr;
		xzrHidden = xzr;
		yzrHidden = yzr;
	}
	public boolean isInvisible(){ return invisible; }
}
