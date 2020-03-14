package svarog.world;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import svarog.collision.AABB;
import svarog.entity.Entity;
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
	
	
	private int viewX;
	private int viewY;
	
	private Tile[][] tiles;
	private int width;
	private int height;
	private int scale;
	private AABB[][] bounding_boxes;
	
	private List<Entity> entities;
	
	private Matrix4f world;
	
	public World() {
		entities = new ArrayList<Entity>();
		
		model = new Model(vertices, texture, indices);
		
		width = 32;
		height = 22;
		scale = 20;
		
		tiles = new Tile[width][height];
		bounding_boxes = new AABB[width][height];
		
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(scale);
	}
	
	public void render(Shader shader, Camera camera) {		
		int posX = (int)camera.getPosition().x / (scale*2);
		int posY = (int)camera.getPosition().y / (scale*2);
		
		for(int i = 0; i < viewX; i++) {
			for(int j = 0; j < viewY; j++) {
				Tile t = getTile(i-posX-(viewX/2)+1, j+posY-(viewY/2));
				if(t != null)
					renderTile(t, i-posX-(viewX/2)+1, -j-posY+(viewY/2), shader, world, camera);
			}
		}
		
		
		for(Entity entity : entities) {
			entity.render(shader, camera, this);
		}
		
		shader = null;
		camera = null;
	}
	
	public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera) {
		shader.bind();
		
		for(byte i = 0; i < 3; i++) {
			if(tile.getTexture(i) != null)
				tile.getTexture(i).bind(0);
			
				Matrix4f tile_position = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));
				Matrix4f target = new Matrix4f();
				
				camera.getProjection().mul(world, target);
				target.mul(tile_position);
				
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", target);
				
				this.model.render();
		}
		
		tile = null;
		shader = null;
		world = null;
		camera = null;
	}
	
	public void update(float delta, Window window, Camera camera) {
		for(Entity entity : entities) {
			entity.update(delta, window, camera, this);
			entity.collideWithTiles(this);
			entity.collideWithEntities(this);
		}
	}
	
	public Entity getEntity(int i) {
		return entities.get(i);
	}
	
	public int numberOfEntities() {
		return entities.size();
	}
	
	public void correctCamera(Camera camera, Window window) {
		Vector3f position = camera.getPosition();
		int w = -width * scale * 2;
		int h = height * scale * 2;
		
		if(position.x > -(window.getWidth()/2)+scale)
			position.x = -(window.getWidth()/2)+scale;
		if(position.x < w + (window.getWidth()/2)+scale)
			position.x = w + (window.getWidth()/2)+scale;
		
		if(position.y < (window.getHeight()/2)-scale)
			position.y = (window.getHeight()/2)-scale;
		if(position.y > h-(window.getHeight()/2)-scale)
			position.y = h-(window.getHeight()/2)-scale;
	}
	
	public void setTile(Tile tile, int x, int y) {
		tiles[x][y] = tile;
		
		if(tile.isSolid())
			bounding_boxes[x][y] = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1,1));
		else
			bounding_boxes[x][y] = null;
	}
	
	public void fillWorld(Texture texture) {
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				tiles[i][j] = new Tile(texture);
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
		viewX = (window.getWidth() / (scale*2)) + 4;
		viewY = (window.getHeight() / (scale*2)) + 4;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public int getScale() {
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
}
