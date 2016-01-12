import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;


public class World {
	private Game game;
	private Cube[][][] cubes;
	private Light light;
	public static final int CUBE_SIZE = 10;
	private static int worldSizeX;
	private static int worldSizeY;
	private static int worldSizeZ;
	private ArrayList<Wall> xzWalls;
	private ArrayList<Wall> xyWalls;
	private ArrayList<Wall> yzWalls;
	public World(Game game){
		this.game = game;
		loadWorld(1);
		light = new Light(-60, 50, -30);
		yzWalls = new ArrayList<Wall>();
		xzWalls = new ArrayList<Wall>();
		xyWalls = new ArrayList<Wall>();
	}
	
	private void loadWorld(int worldNumber){
		Scanner sizeFile = new Scanner(getClass().getResourceAsStream("/resources/worlds/" + worldNumber + "/size.txt"));
		worldSizeX = sizeFile.nextInt();
		worldSizeY = sizeFile.nextInt();
		worldSizeZ = sizeFile.nextInt();
		sizeFile.close();
		cubes = new Cube[worldSizeX][worldSizeY][worldSizeZ];
		for(int y = 0; y < worldSizeY; y++){
			try {
				BufferedImage worldMap = ImageIO.read( getClass().getResource("/resources/worlds/" + worldNumber + "/" + y + ".png"));
				for (int x = 0; x < worldSizeX; x++) {
					for (int z = 0; z < worldSizeZ; z++) {
						Color c = new Color(worldMap.getRGB(x, z));
						if(c.getBlue() == 255 && c.getRed() == 0 && c.getGreen() == 0 && c.getAlpha() == 255){
							cubes[x][y][z] = new Cube(x * CUBE_SIZE, y * CUBE_SIZE, z * CUBE_SIZE, 
									CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//xyz: player grid position
	public void updateWallsInPlane(Camera.Direction direction, int x, int y, int z){
		if(direction == Camera.Direction.X){ // side
			yzWalls = new ArrayList<Wall>();
			for (int y2 = 0; y2 < cubes[x].length; y2++) {
				for (int z2 = 0; z2 < cubes[x][y2].length; z2++) {
					if(cubes[x][y2][z2] != null){
						yzWalls.add(new Wall(z2 * CUBE_SIZE, y2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
					}
				}
			}
		}
		else if(direction == Camera.Direction.Y){ // top
			xzWalls = new ArrayList<Wall>();
			for (int x2 = 0; x2 < cubes.length; x2++) {
				for (int z2 = 0; z2 < cubes[x2][y].length; z2++) { // walls where there are cubes in the same plane
					if(cubes[x2][y][z2] != null){
						xzWalls.add(new Wall(x2 * CUBE_SIZE, z2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
					}
					for(int y2 = y - 1; y2 >= 0; y2--){ // walls where there is no floor in the same plane
						if(cubes[x2][y2][z2] == null){
							xzWalls.add(new Wall(x2 * CUBE_SIZE, z2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
						}
					}
				}
			}
		}
		else if(direction == Camera.Direction.Z){ // side
			xyWalls = new ArrayList<Wall>();
			for (int x2 = 0; x2 < cubes.length; x2++) {
				for (int y2 = 0; y2 < cubes[x2].length; y2++) {
					if(cubes[x2][y2][z] != null){
						xyWalls.add(new Wall(x2 * CUBE_SIZE, y2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
					}
				}
			}
		}
	}

	public void render(Camera.Direction direction){
		float playerX = game.getPlayerPosition()[0];
		float playerY = game.getPlayerPosition()[1];
		float playerZ = game.getPlayerPosition()[2];
		light.render();
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[i].length; j++) {
				for (int j2 = 0; j2 < cubes[i][j].length; j2++) {
					if(cubes[i][j][j2] != null){
						if(direction == Camera.Direction.X){
							if(i * CUBE_SIZE > playerX)
								cubes[i][j][j2].render(false);
							else if(i * CUBE_SIZE == playerX)
								cubes[i][j][j2].render(true);
						}
						else if(direction == Camera.Direction.Z){
							if(j2 * CUBE_SIZE > playerZ)
								cubes[i][j][j2].render(false);
							else if(j2 * CUBE_SIZE == playerZ)
								cubes[i][j][j2].render(true);
						}
						else if(direction == Camera.Direction.Y){
							if(j * CUBE_SIZE < playerY)
								cubes[i][j][j2].render(false);
							else if(j * CUBE_SIZE == playerY)
								cubes[i][j][j2].render(true);
						}
						else if(direction == Camera.Direction.ISO){
							cubes[i][j][j2].render(true);
						}
						else if(direction == Camera.Direction.FREE){
							cubes[i][j][j2].render(true);
						}
					}
				}
			}
		}
	}
	
	public ArrayList<Wall> getXZWalls(){
		return xzWalls;
	}
	public ArrayList<Wall> getXYWalls(){
		return xyWalls;
	}
	public ArrayList<Wall> getYZWalls(){
		return yzWalls;
	}
	
	public static int getSizeX(){ return worldSizeX; }
	public static int getSizeY(){ return worldSizeY; }
	public static int getSizeZ(){ return worldSizeZ; }
}
