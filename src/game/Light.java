package game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;


public class Light {
	private FloatBuffer position;
	private int lightId;
	private static int lightCount = GL11.GL_LIGHT0; // 16384
	private float[] lightAmbient  = {0.0f, 0.0f, 0.0f, 1.0f};
	private float[] lightDiffuse  = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] lightSpecular = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] matSpecular   = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] matDiffuse    = {1.0f, 0.0f, 0.0f, 1.0f};
	private float[] matAmbient    = {1.0f, 0.0f, 0.0f, 1.0f};
	private float[] matShininess  = {64.0f, 0.0f, 0.0f, 0.0f};
	private FloatBuffer lightAmbientFB = toFloatBuffer(lightAmbient);
	private FloatBuffer lightDiffuseFB = toFloatBuffer(lightDiffuse);
	private FloatBuffer lightSpecularFB = toFloatBuffer(lightSpecular);
	private FloatBuffer matSpecularFB = toFloatBuffer(matSpecular);
	private FloatBuffer matDiffuseFB = toFloatBuffer(matDiffuse);
	private FloatBuffer matAmbientFB = toFloatBuffer(matAmbient);
	private FloatBuffer matShininessFB = toFloatBuffer(matShininess);
	
	public Light(float x, float y, float z){
		float[] pos = {x, y, z};
		position = toFloatBuffer(pos);
		lightId = lightCount;
		lightCount++;
	}
	
	public void render(){
		GL11.glLight(lightId, GL11.GL_AMBIENT, lightAmbientFB);
		GL11.glLight(lightId, GL11.GL_DIFFUSE, lightDiffuseFB);
		GL11.glLight(lightId, GL11.GL_SPECULAR, lightSpecularFB);

		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, matSpecularFB);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, matDiffuseFB);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, matAmbientFB);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SHININESS, matShininessFB);
		GL11.glLight(lightId, GL11.GL_POSITION, position);

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(lightId);
	}
	
	public static void disableAllLights(){
		GL11.glDisable(GL11.GL_LIGHT0);
		GL11.glDisable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_LIGHT2);
		GL11.glDisable(GL11.GL_LIGHT3);
		GL11.glDisable(GL11.GL_LIGHT4);
		GL11.glDisable(GL11.GL_LIGHT5);
		GL11.glDisable(GL11.GL_LIGHT6);
		GL11.glDisable(GL11.GL_LIGHT7);
	}
	
	private static FloatBuffer toFloatBuffer(float[] array){
		ByteBuffer temp = ByteBuffer.allocateDirect(array.length * 8);
		temp.order(ByteOrder.nativeOrder());
		FloatBuffer fb = temp.asFloatBuffer();
		fb.put(array);
		fb.rewind();
		return fb;
	}
}
