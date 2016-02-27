package gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import engine.GameFonts;
import engine.Main;

// manager class for on screen dialogue messages
// uses xy left top coordinate system
public class Messenger {
	private float leftMargin = 5;
	private float rightMargin = 5;
	private float bottomMargin = 20;
	private float topMargin = 600;
	private float textPadding = 16;
	private long printSpeed = 75; // milliseconds between chars
	private int fontSize = 32;
	private long lastPrintTime;
	private Message activeMessage;
	private boolean active;
	private boolean eReleased;
	public Messenger(){

	}

	public void update(){
		if(active){
			if(activeMessage.isWaitingForAdvance()){
				if(Keyboard.isKeyDown(Keyboard.KEY_E)){
					if(eReleased){
						activeMessage.advanceLine();
					}
					eReleased = false;
				}
				else{
					eReleased = true;
				}
			}
			if(! activeMessage.isFinished() && System.currentTimeMillis() - lastPrintTime > printSpeed){
				lastPrintTime = System.currentTimeMillis();
				activeMessage.advanceText();
			}
			if(activeMessage.isDismissed()){
				active = false;
			}
		}
	}

	public void render(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Main.WIDTH, Main.HEIGHT, 0, -5, 5);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT); // draw on a new "layer"
		GL11.glColor3f(0, 0, 0);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2f(leftMargin, topMargin);
			GL11.glVertex2f(Main.WIDTH - rightMargin, topMargin);
			GL11.glVertex2f(Main.WIDTH- rightMargin, Main.HEIGHT - bottomMargin);
			GL11.glVertex2f(leftMargin, Main.HEIGHT - bottomMargin);
			//GL11.glVertex3f(leftMargin, topMargin, -1);
			//GL11.glVertex3f(Main.WIDTH - rightMargin, topMargin, -1);
			//GL11.glVertex3f(Main.WIDTH- rightMargin, Main.HEIGHT - bottomMargin, -1);
			//GL11.glVertex3f(leftMargin, Main.HEIGHT - bottomMargin, -1);
		}
		GL11.glEnd();

		GL11.glTranslatef(0, 0, 1); // put text in front of background

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);

		GameFonts.courierFont32pt.drawString(leftMargin + textPadding, topMargin + textPadding, activeMessage.getCurrentText()[0], Color.white);
		GameFonts.courierFont32pt.drawString(leftMargin + textPadding, topMargin + textPadding + fontSize, activeMessage.getCurrentText()[1], Color.white);
		GameFonts.courierFont32pt.drawString(leftMargin + textPadding, topMargin + textPadding + fontSize * 2, activeMessage.getCurrentText()[2], Color.white);
	}

	public void setActiveMessage(Message msg){
		activeMessage = msg;
	}

	public void activate(){
		active = true;
		lastPrintTime = System.currentTimeMillis();
	}
	public void deactivate(){ active = false; }
	public boolean isActive(){ return active; }
}
