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
import svarog.entity.Player;
import svarog.render.Texture;

import svarog.world.Tile;

public class World{
	private int id;
	private int playerId;
	private Tile[][] tiles;
	private int width;
	private int height;
	private AABB[][] bounding_boxes;
	
	private List<Entity> entities;
	private List<Door> doors;
	
	private Matrix4f world;
	
	public World(int id, int width, int height) {
		entities = new ArrayList<Entity>();
		doors = new ArrayList<Door>();
		
		this.setId(id);
		this.width = width;
		this.height = height;
		
		tiles = new Tile[width][height];
		bounding_boxes = new AABB[width][height];
		
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(WorldRenderer.scale);
	}
	
	public void loadMap(String filename, int tileSize) {
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
	
							setTile(new Tile(new Texture(pixels, tileSize)), x, y);
							
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
			if(entity.getObjectId() == id)
				return entity;
		}
		
		return null;
	}
	
	public void addEntity(Entity entity) {
		entity.prepare();
		this.entities.add(entity);
		
		if(entity instanceof Player)
			playerId = entities.size() - 1;
			
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
	
	public Player getPlayer() {
		return (Player)this.entities.get(playerId);
	}
	
	public void fillWorld(Texture texture) {
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
	
	public Tile getTile(int x, int y) {
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
	
	public void setBoundingBoxes() {
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
}
