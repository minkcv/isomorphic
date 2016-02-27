package game;
import engine.Main;
import engine.Util;
import gui.Message;
import gui.Messenger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


public class Game {
	private Main main;
	private Camera camera;
	private World world;
	private Player player;
	private float zoom = 10;
	private float scale; // zoom * cube size
	private float minZoom = 5;
	private float maxZoom = 20;
	private Axes3D axes;
	private Messenger messenger;
	public Game(Main main, boolean loadSave){
		this.main = main;
		camera = new Camera(this);
		axes = new Axes3D(10);
		world = new World(this);
		messenger = new Messenger();
		if(loadSave){
			loadGame();
		}
		else{
			player = new Player(this, 0, 10, 0);
			world.loadWorld(4);			
		}
	}

	public void update(int delta){
		scale = World.CUBE_SIZE * zoom;
		camera.update();
		if(! camera.isRotating()){
			player.update(camera.getDirection(), world);
			world.update(camera.getDirection());
		}
		else{
			world.update(camera.getDirection());
		}
		messenger.update();
		
		camera.setPosition(player.getX(), player.getY(), player.getZ());
		if(camera.getDirection() != Camera.Direction.ISO && camera.getDirection() != Camera.Direction.FREE)
			world.cullCubes(player.getX(), player.getY(), player.getZ(), scale + World.CUBE_SIZE);
		else
			world.cullCubes(player.getX(), player.getY(), player.getZ(), 2 * (scale + World.CUBE_SIZE));

		zoom -= Mouse.getDWheel() / 120;
		if(zoom < minZoom)
			zoom = minZoom;
		else if(zoom > maxZoom)
			zoom = maxZoom;
	}

	public void render(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-scale, scale, -scale, scale, scale * 2, -scale * 2);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		camera.transformToCamera();

		player.render();
		world.render(camera.getDirection());

		camera.undoTransform();
		
		GL11.glTranslatef(15 - scale, 15 - scale, -scale);
		camera.rotateToCamera();
		axes.render();
		camera.undoRotate();
		GL11.glTranslatef(-(15 - scale), -(15 - scale), scale);
		
		if(messenger.isActive())
			messenger.render();
	}

	public void usePortalOrSave(int playerX, int playerY, int playerZ){
		int saveID = world.playerOnSave(playerX, playerY, playerZ);
		if(saveID > 0){
			saveGame(saveID, world.getWorldID());
			return;
		}
		int[] destination = world.playerOnPortal(playerX, playerY, playerZ);
		if(destination[0] > 0){
			int destID = destination[0];
			int destWorld = destination[1];
			world.loadWorld(destWorld);
			int[] portalPosition = world.getPositionOfPortal(destID);
			player.setPosition(portalPosition[0] * World.CUBE_SIZE, portalPosition[1] * World.CUBE_SIZE, portalPosition[2] * World.CUBE_SIZE);
			return;
		}
	}

	private void saveGame(int saveID, int worldID){
		File workingDir = Util.getWorkingDirectory();
		try{
			if(! workingDir.exists())
				workingDir.mkdir();
			File saveDataFile = new File(Util.getWorkingDirectory().getAbsolutePath() + "/savedata.dat");
			FileOutputStream saveDataOut = new FileOutputStream(saveDataFile);
			if(! saveDataFile.exists()){
				saveDataFile.createNewFile();
			}
			String saveData = saveID + " " + worldID;
			System.out.println("Saved game, world: " + worldID + " save: " + saveID);
			saveDataOut.write(saveData.getBytes());
			saveDataOut.flush();
			saveDataOut.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void loadGame(){
		File workingDir = Util.getWorkingDirectory();
		try{
			if(! workingDir.exists())
				workingDir.mkdir();
			File saveDataFile = new File(Util.getWorkingDirectory().getAbsolutePath() + "/savedata.dat");
			if(! saveDataFile.exists()){
				System.out.println("No existing save data");
			}
			else {
				Scanner saveDataScanner = new Scanner(saveDataFile);
				try{
					int saveID = Integer.parseInt(saveDataScanner.next());
					int worldID = Integer.parseInt(saveDataScanner.next());
					world.loadWorld(worldID);
					int[] position = world.getPositionOfSave(saveID);
					player = new Player(this, position[0] * World.CUBE_SIZE, position[1] * World.CUBE_SIZE, position[2] * World.CUBE_SIZE);
					System.out.println("Loaded save data, world: " + worldID + " save: " + saveID);
				}catch(NumberFormatException e){
					saveDataScanner.close();
					saveDataFile.delete();
				}
				saveDataScanner.close();
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void readMessage(Message msg){
		messenger.setActiveMessage(msg);
		messenger.activate();
	}

	public float[] getPlayerPosition(){
		float[] pos = {player.getX(), player.getY(), player.getZ()};
		return pos;
	}

	public void alignPlayer(){
		player.alignPosition();
	}

	public void alignBoxes(){
		world.alignActiveObjects();
	}

	public void computeObjectsInPlane(Camera.Direction direction){
		world.computeObjectsInPlane(direction, player.getGridX(), player.getGridY(), player.getGridZ());
	}
	
	public boolean playerOnGround(){ return player.isOnGround(); }
	public boolean playerReadingMessage(){ return player.isReadingMessage(); }
	public void setBackgroundColor(){ world.setBackgroundColor(); }
}
