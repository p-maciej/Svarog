package svarog.world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import svarog.collision.AABB;
import svarog.entity.Entity;
import svarog.entity.NPC;
import svarog.entity.Player;
import svarog.io.Timer;
import svarog.render.Texture;

import svarog.world.Tile;

public class World implements Runnable {
	private int id;
	private Tile[][] tiles;
	private int width;
	private int height;
	private AABB[][] bounding_boxes;
	
	private List<Entity> entities;
	private List<Door> doors;
	
	private Matrix4f world;
	
	private List<EntityRespawn> entitiesToRespawn;

	// Multithreading Variables //
	private Texture fillWorldTexture;
	private String mapFileName;
	private int tileSize;
	private String mapBoundingFileName;
	
	private Thread worldThread;
	
	
	private List<QueuedTile> tileQueue; 
	/////////////////////////////
	
	
	public World(int id, int width, int height) {
		entities = new ArrayList<Entity>();
		doors = new ArrayList<Door>();
		tileQueue = new ArrayList<QueuedTile>();
		entitiesToRespawn = new ArrayList<EntityRespawn>();
		
		this.setId(id);
		this.width = width;
		this.height = height;
		
		tiles = new Tile[width][height];
		bounding_boxes = new AABB[width][height];
		
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(WorldRenderer.scale);
	}
	
	public void loadMap(String filename, int tileSize) {
		this.mapFileName = filename;
		this.tileSize = tileSize;
	}
	
	private void loadMapInt(String filename, int tileSize) {
		BufferedImage image;
		
		try {
			image = ImageIO.read(new File("./resources/textures/maps/" + filename));
			
			if(image.getWidth()%tileSize == 0 && image.getHeight()%tileSize == 0) {	
				if(image.getWidth()/tileSize == this.getWidth() && image.getHeight()/tileSize == this.getHeight()) {
					for(int x = 0; x < this.getWidth(); x++) {
						for(int y = 0; y < this.getHeight(); y++) {
							ByteBuffer pixels = BufferUtils.createByteBuffer(tileSize*tileSize*4);
							for(int i = x*tileSize; i < x*tileSize + tileSize; i++) {
								for(int j = y*tileSize; j < y*tileSize + tileSize; j++) {
									int pixel = image.getRGB(i, j);
									pixels.put(((byte)((pixel >> 16) & 0xFF))); // red
									pixels.put(((byte)((pixel >> 8) & 0xFF)));  // green
									pixels.put((byte)(pixel & 0xFF)); 			// blue
									pixels.put(((byte)((pixel >> 24) & 0xFF))); // alpha
								}
							}
							pixels.flip();
	
							tiles[x][y] = new Tile(new Texture(pixels, tileSize));
							
							pixels = null;
						}
					}
				} else
					throw new IllegalStateException("Wrong map size!");			
			} else
				throw new IllegalStateException("Wrong tile size!");
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		filename = null;
		image = null;
	}
	
	public void setSolidTilesFromMap(String filename) {
		this.mapBoundingFileName = filename;
	}
	
	private void setSolidTilesFromMapInt(String filename) {
		BufferedImage image;

		try {
			image = ImageIO.read(new File("./resources/textures/maps/masks/" + filename));
			int width = image.getWidth();
			int height = image.getHeight();

			if(width == this.getWidth() && height == this.getHeight()) {				
				for(int i = 0; i < width; i++) {
					for(int j = 0; j < height; j++) {
						int pixel = image.getRGB(i, j);
						
						if(((pixel >> 16) & 0xFF) == 0 && ((pixel >> 8) & 0xFF) == 0 && (pixel & 0xFF) == 0) {
							tiles[i][j].setSolid();
						}
					}
				}
			} else {
				throw new IllegalStateException("Wrong map size!");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		image = null;
	}
	
	public Entity getEntity(int i) {
		if(entities.size() > i)
			return entities.get(i);
		else
			return null;
	}
	
	public Entity getEntityById(int id) {
		for(Entity entity : entities) {
			if(entity.getId() == id) // There should be id, instead of ObjectId if we want search by id from xml
				return entity;
		}
		
		return null;
	}
	
	public Entity getEntityByObjectId(int id) {
		for(Entity entity : entities) {
			if(entity.getObjectId() == id) // There should be id, instead of ObjectId if we want search by id from xml
				return entity;
		}
		
		return null;
	}
	
	public void addEntity(Entity entity) {
		if(entity.getTexture() != null)
			entity.getTexture().prepare();
		
		if(!isEntityWaitingToRespawn(entity.getId())) {
			this.entities.add(entity);	
		}
	}
	
	public int numberOfEntities() {
		return entities.size();
	}
	
	public Door getDoor(int i) {
		if(doors.size() > i)
			return doors.get(i);
		else
			return null;
	}
	
	public int numberOfDoors() {
		return doors.size();
	}
	
	public void addDoor(Door door) {
		this.doors.add(door);
	}
	
	public NPC getNpcByNpcId(int npcId) {
		for(Entity i: entities) {
			if(i instanceof NPC) {
				if(((NPC) i).getGlobalNpcID()==npcId) {
					return ((NPC)i);
				}
			}
		}
		return null;
	}
	
	public ArrayList<NPC> getNPCs(){
		ArrayList<NPC> temp = new ArrayList<>();
		for(Entity i: entities) {
			if(i instanceof NPC) {
				temp.add(((NPC)(i)));
			}
		}
		return temp;
	}
	
	public Player getPlayer() {
		for(Entity i: entities) {
			if(i instanceof Player) {
				return (Player)(i);
			}
		}
		return null;
	}
	
	public void fillWorld(Texture texture) {
		fillWorldTexture = texture;
	}
	
	private void fillWorldInt(Texture texture) {
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				tiles[i][j] = new Tile(texture);
	}
	
	public void setTile(Tile tile, int x, int y) {
		if(x >= 0 && x < width && y >= 0 && y <= height) {
			tiles[x][y] = tile;
			
			if(tile.isSolid())
				bounding_boxes[x][y] = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1,1));
			else
				bounding_boxes[x][y] = null;
		}
	}
	
	Tile getTile(int x, int y) {
		try {
			return tiles[x][y];
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public AABB getTileBoundingBox(int x, int y) {
		try {
			return bounding_boxes[x][y];
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private void setBoundingBoxes() {
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				if(tiles[x][y].isSolid())
					bounding_boxes[x][y] = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1,1));
				else
					bounding_boxes[x][y] = null;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Matrix4f getWorld() {
		return world;
	}
	
	List<Entity> getEntities() {
		return entities;
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
	public void removeAndRespawn(Entity entity) {
		entitiesToRespawn.add(new EntityRespawn(entity, Timer.getNanoTime()));
		entities.remove(entity);
	}
	
	public void removeEntity(int entityId) {
		for(int i = 0; i < this.numberOfEntities(); i++) {
			if(entities.get(i).getId() == entityId)
				entities.remove(i);
		}
	}
	
	public void addTile(Tile tile, int x, int y) {
		tileQueue.add(new QueuedTile(tile, x, y));
	}
	
	
	public List<EntityRespawn> getEntitiesToRespawn() {
		return entitiesToRespawn;
	}
	

	public void addEntitiesToRespawn(EntityRespawn entityRespawn) {
		this.entitiesToRespawn.add(entityRespawn);
	}
	
	public boolean isEntityWaitingToRespawn(int id) {
		for(EntityRespawn entity : entitiesToRespawn)
			if(entity.getEntity().getId() == id)
				return true;

		return false;
	}

	@Override
	public void run() {
		if(fillWorldTexture != null)
			this.fillWorldInt(fillWorldTexture);
		
		if(mapFileName != null && tileSize > 0)
			this.loadMapInt(mapFileName, tileSize);
		
		if(mapBoundingFileName != null) {
			this.setSolidTilesFromMapInt(mapBoundingFileName);
			this.setBoundingBoxes();
		}
	}
	
	public void start() {
		  if (worldThread == null) {
			   worldThread = new Thread(this, "world");
			   worldThread.start();
		  }
	}
	
	public void join(WorldRenderer renderer) throws InterruptedException {
		  if (worldThread != null) {
			   worldThread.join();
			   setQueuedTiles();
			   renderer.setBuffers();
		  }
	}
	
	private void setQueuedTiles() {
		for(QueuedTile tile : tileQueue) {
			for(byte i = 0; i < 3; i++) {
				if(tile.getTile().getTexture(i) != null) {
					tiles[tile.getX()][tile.getY()].setTexture(tile.getTile().getTexture(i), i);
				}
			}	
		}
	}
	
	private class QueuedTile {
		private Tile tile;
		private int x;
		private int y;
		
		public QueuedTile(Tile tile, int x, int y) {
			this.tile = tile;
			this.x = x;
			this.y = y;
		}

		public Tile getTile() {
			return tile;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
	
	public class EntityRespawn {
		private Entity entity;
		private long timerStart;
		
		public EntityRespawn(Entity entity, long timerStart) {
			this.setEntity(entity);
			this.setTimerStart(timerStart);
		}
		
		public Entity getEntity() {
			return entity;
		}
		
		public void setEntity(Entity entity) {
			this.entity = entity;
		}
		
		public long getTimerStart() {
			return timerStart;
		}
		
		public void setTimerStart(long timerStart) {
			this.timerStart = timerStart;
		}
	}
}
