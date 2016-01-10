

import org.lwjgl.opengl.GL11;

public class Axes3D {
	private float lineLength;
	public Axes3D(){
		lineLength = 1;
	}
	public Axes3D(float lineLength){
		this.lineLength = lineLength;
	}
	
	public void render(){
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(1f, 0f, 0f);
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(lineLength, 0, 0);
		}
		GL11.glEnd();
		
		GL11.glColor3f(0f, 1f, 0f);
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, lineLength, 0);
		}
		GL11.glEnd();
		
		GL11.glColor3f(0f, 0f, 1f);
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, 0, lineLength);
		}
		GL11.glEnd();
	}
}
