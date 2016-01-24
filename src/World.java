import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;

//import org.newdawn.slick.Color;


public class World {
	private Game game;
	private Cube[][][] cubes;
	private Light light;
	public static final int CUBE_SIZE = 10;
	private static int worldSizeX;
	private static int worldSizeY;
	private static int worldSizeZ;
	private ArrayList<ActiveObject> activeObjects;
	private ArrayList<Wall> xzWalls;
	private ArrayList<Wall> xyWalls;
	private ArrayList<Wall> yzWalls;
	private ArrayList<SideBox> xyBoxes;
	private ArrayList<SideBox> yzBoxes;
	private ArrayList<TopBox>  xzBoxes;
	private int worldID;
	
	// does not load a world
	public World(Game game){
		this.game = game;
		light = new Light(-60, 50, -30);
		yzWalls = new ArrayList<Wall>();
		xzWalls = new ArrayList<Wall>();
		xyWalls = new ArrayList<Wall>();
		xyBoxes = new ArrayList<SideBox>();
		yzBoxes = new ArrayList<SideBox>();
		xzBoxes = new ArrayList<TopBox>();
		activeObjects = new ArrayList<ActiveObject>();
	}

	public void loadWorld(int worldNumber){
		worldID = worldNumber;
		yzWalls = new ArrayList<Wall>();
		xzWalls = new ArrayList<Wall>();
		xyWalls = new ArrayList<Wall>();
		xyBoxes = new ArrayList<SideBox>();
		yzBoxes = new ArrayList<SideBox>();
		xzBoxes = new ArrayList<TopBox>();
		activeObjects = new ArrayList<ActiveObject>();
		activeObjects.add(new Box(2 * CUBE_SIZE, CUBE_SIZE, 2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE, 0, 0, 1));
		activeObjects.add(new SavePoint(3 * CUBE_SIZE, CUBE_SIZE, 3 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
		activeObjects.add(new Portal(CUBE_SIZE, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE, 1, 0, 0, 1, 4, 1));
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
						Color c = new Color(worldMap.getRGB(x, z), true);
						if(c.getAlpha() == 255){
							cubes[x][y][z] = new Cube(x * CUBE_SIZE, y * CUBE_SIZE, z * CUBE_SIZE, 
									CUBE_SIZE, CUBE_SIZE, CUBE_SIZE,
									c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(Camera.Direction direction){
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[i].length; j++) {
				for (int j2 = 0; j2 < cubes[i][j].length; j2++) {
					//if(cubes[i][j][j2] != null)
					//cubes[i][j][j2].update(this, direction);
				}
			}
		}
		for(ActiveObject b : activeObjects){
			b.update(this, direction);
		}
	}

	public void alignActiveObjects(){
		for(ActiveObject a : activeObjects){
			a.alignPosition();
		}
	}

	//xyz: player grid position
	public void computeObjectsInPlane(Camera.Direction direction, int x, int y, int z){
		if(direction == Camera.Direction.X){ // side
			yzWalls = new ArrayList<Wall>();
			for (int y2 = 0; y2 < cubes[x].length; y2++) {
				for (int z2 = 0; z2 < cubes[x][y2].length; z2++) {
					if(cubes[x][y2][z2] != null){
						yzWalls.add(new Wall(z2 * CUBE_SIZE, y2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
					}
				}
			}
			for(ActiveObject a : activeObjects){
				if(a instanceof Box){
					Box b = (Box)a;
					if(b.getX() / CUBE_SIZE == x){
						yzWalls.add(b.getSideBox());
					}
				}
			}
			yzBoxes = new ArrayList<SideBox>();
			for(ActiveObject a : activeObjects){
				if(a instanceof Box){
					Box b = (Box)a;
					if(b.getX() / CUBE_SIZE == x){
						yzBoxes.add(b.getSideBox());
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

					if(y - 1 >= 0 && cubes[x2][y - 1][z2] == null){ // no floor
						xzWalls.add(new Wall(x2 * CUBE_SIZE, z2 * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE));
					}
				}
			}
			for(ActiveObject a : activeObjects){
				if(a instanceof Box){
					Box b = (Box)a;
					if(b.getY() / CUBE_SIZE == y){
						xzWalls.add(b.getTopBox());
					}
				}
			}
			xzBoxes = new ArrayList<TopBox>();
			for(ActiveObject a : activeObjects){
				if(a instanceof Box){
					Box b = (Box)a;
					if(b.getY() / CUBE_SIZE == y){
						xzBoxes.add(b.getTopBox());
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
			for(ActiveObject a : activeObjects){
				if(a instanceof Box){
					Box b = (Box)a;
					if(b.getZ() / CUBE_SIZE == z){
						xyWalls.add(b.getSideBox());
					}
				}
			}
			xyBoxes = new ArrayList<SideBox>();
			for(ActiveObject a : activeObjects){
				if(a instanceof Box){
					Box b = (Box)a;
					if(b.getZ() / CUBE_SIZE == z){
						xyBoxes.add(b.getSideBox());
					}
				}
			}
		}
	}

	public void cullCubes(float centerX, float centerY, float centerZ, float distance){
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[i].length; j++) {
				for (int j2 = 0; j2 < cubes[i][j2].length; j2++) {
					if(cubes[i][j][j2] != null){
						float cubeX = i * CUBE_SIZE;
						float cubeY = j * CUBE_SIZE;
						float cubeZ = j2 * CUBE_SIZE;
						if(Math.abs(centerX - cubeX) > distance || Math.abs(centerY - cubeY) > distance || Math.abs(centerZ - cubeZ) > distance){
							cubes[i][j][j2].setCulled(true);
						}
						else{
							cubes[i][j][j2].setCulled(false);
						}
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
							float distanceFromPlayer = i * CUBE_SIZE - playerX;
							if(distanceFromPlayer == 0)
								cubes[i][j][j2].render(0);
							else if(distanceFromPlayer > 0)
								cubes[i][j][j2].render(distanceFromPlayer / 255);
						}
						else if(direction == Camera.Direction.Z){
							float distanceFromPlayer = j2 * CUBE_SIZE - playerZ;
							if(distanceFromPlayer == 0)
								cubes[i][j][j2].render(0);
							else if(distanceFromPlayer > 0)
								cubes[i][j][j2].render(distanceFromPlayer / 255);
						}
						else if(direction == Camera.Direction.Y){
							float distanceFromPlayer = j * CUBE_SIZE - playerY;
							if(distanceFromPlayer == 0 || distanceFromPlayer == CUBE_SIZE)
								cubes[i][j][j2].render(0);
							else if(distanceFromPlayer < 0)
								cubes[i][j][j2].render(-distanceFromPlayer / 255);
						}
						else if(direction == Camera.Direction.ISO){
							cubes[i][j][j2].render(0);
						}
						else if(direction == Camera.Direction.FREE){
							cubes[i][j][j2].render(0);
						}
					}
				}
			}
		}
		renderActiveObjects(direction);
	}

	private void renderActiveObjects(Camera.Direction direction){
		float playerX = game.getPlayerPosition()[0];
		float playerY = game.getPlayerPosition()[1];
		float playerZ = game.getPlayerPosition()[2];
		for(ActiveObject a : activeObjects){
			if(direction == Camera.Direction.X){
				float distanceFromPlayer = a.getX() - playerX;
				if(distanceFromPlayer == 0)
					a.render(0);
				else if(distanceFromPlayer > 0)
					a.render(distanceFromPlayer / 255);
			}
			else if(direction == Camera.Direction.Z){
				float distanceFromPlayer = a.getZ() - playerZ;
				if(distanceFromPlayer == 0)
					a.render(0);
				else if(distanceFromPlayer > 0)
					a.render(distanceFromPlayer / 255);
			}
			else if(direction == Camera.Direction.Y){
				float distanceFromPlayer = a.getY() - playerY;
				if(distanceFromPlayer == 0 || distanceFromPlayer == CUBE_SIZE)
					a.render(0);
				else if(distanceFromPlayer < 0)
					a.render(-distanceFromPlayer / 255);
			}
			else if(direction == Camera.Direction.ISO){
				a.render(0);
			}
			else if(direction == Camera.Direction.FREE){
				a.render(0);
			}
		}
	}
	
	/**
	 * tests if a player is touching a portal and returns the portal info
	 * @param playerX player actual position, not grid
	 * @param playerY
	 * @param playerZ
	 * @return portal information:<br>
	 *  destination[0] = portal destination id<br>
	 *  destination[1] = portal destination world
	 *  returns {0, 0} if not on a portal
	 */
	public int[] playerOnPortal(int playerX, int playerY, int playerZ){
		int[] destination = {0, 0};
		for(ActiveObject a : activeObjects){
			if(a instanceof Portal){
				Portal p = (Portal)a;
				if(Math.abs(p.getX() - playerX) < CUBE_SIZE && Math.abs(p.getY() - playerY) < CUBE_SIZE && Math.abs(p.getZ() - playerZ) < CUBE_SIZE){
					destination[0] = p.getDestinationID();
					destination[1] = p.getDestinationWorld();
					return destination;
				}
			}
		}
		return destination;
	}
	
	/**
	 * tests if a player is touching a save and returns the save id
	 * @param playerX player actual position, not grid
	 * @param playerY
	 * @param playerZ
	 * @return id of the save point
	 */
	public int playerOnSave(int playerX, int playerY, int playerZ){
		for(ActiveObject a : activeObjects){
			if(a instanceof SavePoint){
				SavePoint s = (SavePoint)a;
				if(Math.abs(s.getX() - playerX) < CUBE_SIZE && Math.abs(s.getY() - playerY) < CUBE_SIZE && Math.abs(s.getZ() - playerZ) < CUBE_SIZE)
					return s.getSaveID();
			}
		}
		return 0;
	}

	// returns grid position of save if one matches save id
	public int[] getPositionOfSave(int saveID){
		for(ActiveObject a : activeObjects){
			if(a instanceof SavePoint){
				SavePoint s = (SavePoint)a;
				if(s.getSaveID() == saveID){
					int[] position = new int[3];
					position[0] = s.getGridX();
					position[1] = s.getGridY();
					position[2] = s.getGridZ();
					return position;
				}
			}
		}
		System.out.println("SavePoint not found, maybe save file is corrupt");
		return null;
	}
	
	//returns grid position of portal if one matches portal id
	public int[] getPositionOfPortal(int portalID){
		for(ActiveObject a : activeObjects){
			if(a instanceof Portal){
				Portal p = (Portal)a;
				if(p.getCurrentID() == portalID){
					int[] position = new int[3];
					position[0] = p.getGridX();
					position[1] = p.getGridY();
					position[2] = p.getGridZ();
					return position;
				}
			}
		}
		System.out.println("Portal not found, check destinations of portals");
		return null;
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

	public ArrayList<TopBox> getXZBoxes(){
		return xzBoxes;
	}
	public ArrayList<SideBox> getYZBoxes(){
		return yzBoxes;
	}
	public ArrayList<SideBox> getXYBoxes(){
		return xyBoxes;
	}

	public static int getSizeX(){ return worldSizeX; }
	public static int getSizeY(){ return worldSizeY; }
	public static int getSizeZ(){ return worldSizeZ; }
	public int getWorldID(){ return worldID; }
}
