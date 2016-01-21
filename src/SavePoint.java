import org.lwjgl.opengl.GL11;


public class SavePoint implements ActiveObject{
	private float x, y, z;
	private float width, depth, height;
	private Camera.Direction direction;
	public SavePoint(float x, float y, float z, float width, float height, float depth){
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.depth = height;
		this.height = depth;
	}
	
	@Override
	public void update(World world, Camera.Direction direction) {
		this.direction = direction;
	}
	@Override
	public void alignPosition() {
		
	}
	@Override
	public void render(float shade) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslatef(x, y, z);
		if(direction == Camera.Direction.ISO || direction == Camera.Direction.FREE){
			renderISO();
		}
		else if(direction == Camera.Direction.X || direction == Camera.Direction.Z){
			renderSide(shade);
		}
		else if(direction == Camera.Direction.Y){
			renderTop();
		}
		GL11.glTranslatef(-x, -y, -z);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	private void renderSide(float shade){
		
		GL11.glBegin(GL11.GL_QUADS);
		{
//			GL11.glNormal3f(0, 0, -1);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(0, 0, -1);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(0, height, -1);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(width, height, -1);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(width, 0, -1);

//			GL11.glNormal3f(-1, 0, 0);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(0, 0, depth);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(-1, height, depth);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(-1, height, 0);

//			GL11.glNormal3f(0, 0, 1);
//			GL11.glColor4f(1, 1, 1, 0);
//			GL11.glVertex3f(width, height, depth);
//			GL11.glColor4f(1, 1, 1, 0);
//			GL11.glVertex3f(0, height, depth);
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glVertex3f(0, 0, depth);
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glVertex3f(width, 0, depth);

//			GL11.glNormal3f(1, 0, 0);
//			GL11.glColor4f(1, 1, 1, 0);
//			GL11.glVertex3f(width, height, depth);
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glVertex3f(width, 0, depth);
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glVertex3f(width, 0, 0);
//			GL11.glColor4f(1, 1, 1, 0);
//			GL11.glVertex3f(width, height, 0);
		}
		GL11.glEnd();
		
	}
	
	private void renderTop(){
		
	}
	
	private void renderISO(){
		GL11.glBegin(GL11.GL_QUADS);
		{
//			GL11.glNormal3f(0, 0, -1);

			// special floor that is 1 pixel above base y
//			GL11.glNormal3f(0, -1, 0);
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glVertex3f(0, 1, 0);
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glVertex3f(width, 1, 0);
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glVertex3f(width, 1, depth);
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glVertex3f(0, 1, depth);

//			GL11.glNormal3f(-1, 0, 0);

//			GL11.glNormal3f(0, 0, 1);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(0, height, depth);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(0, 0, depth);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(width, 0, depth);

//			GL11.glNormal3f(1, 0, 0);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(width, height, depth);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(width, 0, depth);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(width, 0, 0);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(width, height, 0);
			
			// front faces (to isometric view) drawn last so transparency works better
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(0, 0, depth);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(0, height, depth);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(0, height, 0);
			
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(0, height, 0);
			GL11.glColor4f(1, 1, 1, 0);
			GL11.glVertex3f(width, height, 0);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glVertex3f(width, 0, 0);
		}
		GL11.glEnd();
	}

	@Override
	public float getX(){ return x; }
	@Override
	public float getY(){ return y; }
	@Override
	public float getZ(){ return z; }
}
