import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;


public class World {
	private Game game;
	private Cube[][][] cubes;
	private static Random rand = new Random();
	private Light light;
	public static final int CUBE_SIZE = 10;
	public static final int WORLD_SIZE = 10;
	private ArrayList<Wall> xzWalls;
	private ArrayList<Wall> xyWalls;
	private ArrayList<Wall> yzWalls;
	public World(Game game){
		this.game = game;
		loadWorld(2);
		light = new Light(-60, 50, -30);
	}
	
	private void loadWorld(int worldNumber){
		cubes = new Cube[WORLD_SIZE][WORLD_SIZE][WORLD_SIZE];
		for(int y = 0; y < WORLD_SIZE; y++){
			try {
				BufferedImage worldMap = ImageIO.read( getClass().getResource("/resources/worlds/" + worldNumber + "/" + y + ".png"));
				for (int x = 0; x < worldMap.getWidth(); x++) {
					for (int z = 0; z < worldMap.getHeight(); z++) {
						Color c = new Color(worldMap.getRGB(x, z));
						if(c.getBlue() == 255 && c.getRed() == 0 && c.getGreen() == 0){
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

	private void createTestWorld(){
		cubes = new Cube[5][5][5];
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[i].length; j++) {
				for (int j2 = 0; j2 < cubes[i][j].length; j2++) {
					if(rand.nextInt(10) < 3)
						cubes[i][j][j2] = 
						new Cube(i * CUBE_SIZE, j * CUBE_SIZE, j2 * CUBE_SIZE, 
								CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
				}
			}
		}
	}
	
	//xyz: player grid position
	public ArrayList<Wall> getWallsInPlane(Camera.Direction direction, int x, int y, int z){
		ArrayList<Wall> walls = new ArrayList<Wall>();
		if(direction == Camera.Direction.X){ // side
			for (int y2 = 0; y2 < cubes[x].length; y2++) {
				for (int z2 = 0; z2 < cubes[x][y2].length; z2++) {
					if(cubes[x][y2][z2] != null){
						walls.add(new Wall(z2 * CUBE_SIZE, y2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
					}
				}
			}
		}
		else if(direction == Camera.Direction.Y){ // top
			for (int x2 = 0; x2 < cubes.length; x2++) {
				for (int z2 = 0; z2 < cubes[x2][y].length; z2++) {
					if(cubes[x2][y][z2] != null){
						walls.add(new Wall(x2 * CUBE_SIZE, z2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
					}
				}
			}
		}
		else if(direction == Camera.Direction.Z){ // side
			for (int x2 = 0; x2 < cubes.length; x2++) {
				for (int y2 = 0; y2 < cubes[x2].length; y2++) {
					if(cubes[x2][y2][z] != null){
						walls.add(new Wall(x2 * CUBE_SIZE, y2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
					}
				}
			}
		}
		return walls;
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
							if(i * World.WORLD_SIZE > playerX)
								cubes[i][j][j2].render(false);
							else if(i * World.WORLD_SIZE == playerX)
								cubes[i][j][j2].render(true);
						}
						else if(direction == Camera.Direction.Z){
							if(j2 * World.WORLD_SIZE > playerZ)
								cubes[i][j][j2].render(false);
							else if(j2 * World.WORLD_SIZE == playerZ)
								cubes[i][j][j2].render(true);
						}
						else if(direction == Camera.Direction.Y){
							if(j * World.WORLD_SIZE < playerY)
								cubes[i][j][j2].render(false);
							else if(j * World.WORLD_SIZE == playerY)
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
}
