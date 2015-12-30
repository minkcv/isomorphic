import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;


public class World {
	private Game game;
	private Cube[][][] cubes;
	private static Random rand = new Random();
	private Light light;
	public static final int dimension = 10;
	public World(Game game){
		this.game = game;
		loadWorld(2);
		light = new Light(-60, 50, -30);
	}
	
	private void loadWorld(int worldNumber){
		cubes = new Cube[dimension][dimension][dimension];
		for(int y = 0; y < dimension; y++){
			try {
				BufferedImage worldMap = ImageIO.read( getClass().getResource("/resources/worlds/" + worldNumber + "/" + y + ".png"));
				for (int x = 0; x < worldMap.getWidth(); x++) {
					for (int z = 0; z < worldMap.getHeight(); z++) {
						Color c = new Color(worldMap.getRGB(x, z));
						if(c.getBlue() == 255 && c.getRed() == 0 && c.getGreen() == 0){
							cubes[x][y][z] = new Cube(x * dimension, y * dimension, z * dimension, dimension, dimension, dimension);
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
						new Cube(i * dimension, j * dimension, j2 * dimension, 
								dimension, dimension, dimension);
				}
			}
		}
	}

	public void render(){
		float playerX = game.getPlayerPosition()[0];
		float playerY = game.getPlayerPosition()[1];
		float playerZ = game.getPlayerPosition()[2];
		light.render();
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[i].length; j++) {
				for (int j2 = 0; j2 < cubes[i][j].length; j2++) {
					if(cubes[i][j][j2] != null){
						if(game.getCameraDirection() == Camera.Direction.X){
							if(i * World.dimension == playerX)
								cubes[i][j][j2].render();
						}
						else if(game.getCameraDirection() == Camera.Direction.Z){
							if(j2 * World.dimension == playerZ)
								cubes[i][j][j2].render();
						}
						else if(game.getCameraDirection() == Camera.Direction.ISO || game.getCameraDirection() == Camera.Direction.Y){
							cubes[i][j][j2].render();
						}
					}
				}
			}
		}
	}
}
