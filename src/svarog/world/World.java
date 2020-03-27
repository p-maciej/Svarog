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
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Model;
import svarog.render.Shader;
import svarog.render.Texture;

public class World {
	private Model model;
	
	private static final float[] vertices = new float[] {
			-1f, 1f, 0,
			1f, 1f, 0,			
			1f, -1f, 0,
			-1f, -1f, 0,
	};
	
	private static final float[] texture = new float[] {
			0, 0,
			0, 1,	
			1, 1,
			1, 0,
	};
	
	private static final int[] indices = new int[] {
			0,1,2,
			2,3,0
	};
	
	private static final float scale = 16f;
	
	private int viewX;
	private int viewY;
	
	private int id;
	private int playerId;
	private Tile[][] tiles;
	private int width;
	private int height;
	private AABB[][] bounding_boxes;
	private Vector2f worldOffset;
	
	private List<Entity> entities;
	private List<Door> doors;
	
	private Matrix4f world;
	
	private int mouseOverX;
	private int mouseOverY;
	
	public World(int id, int width, int height) {
		entities = new ArrayList<Entity>();
		doors = new ArrayList<Door>();
		worldOffset = new Vector2f();
		model = new Model(vertices, texture, indices);
		
		this.setId(id);
		this.width = width;
		this.height = height;
		
		tiles = new Tile[width][height];
		bounding_boxes = new AABB[width][height];
		
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(scale);
	}
	
	public boolean IsOverEntity(Entity entity, Camera camera, Window window) {
		int posX = (int)(entity.getTransform().getPosition().x*scale);
		int posY = (int)(-entity.getTransform().getPosition().y*scale);
		
		int x = (-(int)((camera.getPosition().x)) - (int)(window.getWidth()/2) + (int)(worldOffset.x/2) + (int)scale + (int)window.getCursorPositionX());
		int y = ((int)((camera.getPosition().y)) - (int)(window.getHeight()/2) + (int)(worldOffset.y/2) + (int)scale + (int)window.getCursorPositionY());
		
		if(posX < x && (posX + scale*2) > x  && (posY -scale*2) < y && (posY + scale*2) > y) {
			return true;
		}
		return false;
	}
	
	public void render(Shader shader, Camera camera, Window window) {		
		int posX = (int)(camera.getPosition().x / (scale*2));
		int posY = (int)(camera.getPosition().y / (scale*2));

		//position of coursor on the World
		int x = (-(int)((camera.getPosition().x)) - (int)window.getWidth()/2 + (int)(worldOffset.x/2) + (int)scale + (int)window.getCursorPositionX());
		int y = ((int)((camera.getPosition().y)) - (int)window.getHeight()/2 + (int)(worldOffset.y/2) + (int)scale + (int)window.getCursorPositionY());

		for(int i = 0; i < viewX; i++) {
			for(int j = 0; j < viewY; j++) {
				if((i-posX-(viewX/2)+1)*scale*2 < x && (i-posX-(viewX/2)+1+1)*scale*2 > x && (j+posY-(viewY/2))*scale*2 < y && (j+posY-(viewY/2)+1)*scale*2 > y) {
					mouseOverX = i-posX-(viewX/2)+1;
					mouseOverY = j+posY-(viewY/2);
				}
				Tile t = getTile(i-posX-(viewX/2)+1, j+posY-(viewY/2));
				if(t != null)
					renderTile(t, i-posX-(viewX/2)+1, -j-posY+(viewY/2), shader, world, camera, false); // Rendering first 2 layers of map
			}
		}	
		
		for(Entity entity : entities) {
			entity.render(shader, camera, this); // Entities rendering
		}
		
		for(int i = 0; i < viewX; i++) {
			for(int j = 0; j < viewY; j++) {
				Tile t = getTile(i-posX-(viewX/2)+1, j+posY-(viewY/2));
				if(t != null)
					renderTile(t, i-posX-(viewX/2)+1, -j-posY+(viewY/2), shader, world, camera, true); // Rendering last layer(3)
			}
		}

		shader = null;
		camera = null;
	}

	
	public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera, boolean topLayer) {
		shader.bind();
		
		if(!topLayer) {
			for(byte i = 0; i < 2; i++) { // Rendering every layer of tile
				if(tile.getTexture(i) != null) {
					tile.getTexture(i).bind(0);
				
					Matrix4f tile_position = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));
					Matrix4f target = new Matrix4f();
					
					camera.getProjection().mul(world, target);
					target.mul(tile_position);				
					
					shader.setUniform("sampler", 0);
					shader.setUniform("projection", target);
					shader.setUniform("sharpness", 1f);
					
					this.model.render();
				}
			}
		} else {
			if(tile.getTexture((byte)2) != null) {
				tile.getTexture((byte)2).bind(0);
			
				Matrix4f tile_position = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));
				Matrix4f target = new Matrix4f();
				
				camera.getProjection().mul(world, target);
				target.mul(tile_position);
				
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", target);
				shader.setUniform("sharpness", 1.0f);
				
				this.model.render();
			}
		}
		
		tile = null;
		shader = null;
		world = null;
		camera = null;
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
	
	public void update(float delta, Window window, Camera camera) { // World update
		for(Entity entity : entities) {
			entity.update(delta, window, camera, this);
			entity.collideWithTiles(this);
			entity.collideWithEntities(this);
		}
	}
	
	public Entity getEntity(int i) {
		if(entities.size() > i)
			return entities.get(i);
		else
			return null;
	}
	
	public void addEntity(Entity entity) {
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
	
	public void calculateView(Window window) {
		viewX = (int)(((window.getWidth()-worldOffset.x) / (scale*2)) + 4);
		viewY = (int)(((window.getHeight()-worldOffset.y) / (scale*2)) + 4);
	}
	
	public void correctCamera(Camera camera, Window window) {
		Vector3f position = camera.getPosition();
		int w = (int)(-width * scale * 2);
		int h = (int)(height * scale * 2);
		
		if(position.x > -(window.getWidth()/2)+scale+worldOffset.x/2) // Left border - add higher value, more offset
			position.x = -(window.getWidth()/2)+scale+worldOffset.x/2;
		if(position.x < w + (window.getWidth()/2)+scale-worldOffset.x/2) // Right border - subtract
			position.x = w + (window.getWidth()/2)+scale-worldOffset.x/2;
		
		if(position.y < (window.getHeight()/2)-scale-worldOffset.y/2) // Top border - subtract
			position.y = (window.getHeight()/2)-scale-worldOffset.y/2;
		if(position.y > h-(window.getHeight()/2)-scale+worldOffset.y/2) // Bottom border - add
			position.y = h-(window.getHeight()/2)-scale+worldOffset.y/2;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public float getScale() {
		return scale;
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

	public Vector2f getWorldOffset() {
		return worldOffset;
	}

	public void setWorldOffset(Vector2f worldOffset) {
		this.worldOffset.set(worldOffset);
	}
	
	public int getMouseOverX() {
		return mouseOverX;
	}
	
	public int getMouseOverY() {
		return mouseOverY;
	}
}
