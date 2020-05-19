package svarog.world;

import java.util.Collections;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import svarog.audio.Audio;
import svarog.entity.Enemy;
import svarog.entity.Entity;
import svarog.io.Timer;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Model;
import svarog.render.RenderProperties;
import svarog.render.Shader;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.World.EntityRespawn;

public class WorldRenderer implements RenderProperties {
	static final float scale = 16f;
	
	private Model model;
	
	private World world;
	
	private Vector2f worldOffset;
	
	private int mouseOverX;
	private int mouseOverY;
	
	private int viewX;
	private int viewY;
	
	private static int mouseOverEntityId;
	private static int clickedEntityId;
	
	private static boolean mouseInteractionLock = false;
	
	public WorldRenderer() {
		this.world = null;
		model = new Model(verticesArray, textureArray, indicesArray);
		
		worldOffset = new Vector2f();
	}
	
	public WorldRenderer(World world) {
		this.setWorld(world);
		model = new Model(verticesArray, textureArray, indicesArray);
		
		worldOffset = new Vector2f();
	}
	
	public void render(Shader shader, Camera camera, Window window) {	
		mouseOverEntityId = -1;
		clickedEntityId = -1;
		
		int posX = (int)(camera.getPosition().x / (scale*2));
		int posY = (int)(camera.getPosition().y / (scale*2));

		//position of coursor on the World
		int x = (-(int)((camera.getPosition().x)) - (int)window.getWidth()/2 + (int)(worldOffset.x/2) + (int)scale + (int)window.getCursorPositionX());
		int y = ((int)((camera.getPosition().y)) - (int)window.getHeight()/2 + (int)(worldOffset.y/2) + (int)scale + (int)window.getCursorPositionY());

		for(int i = 0; i < viewX; i++) {
			for(int j = 0; j < viewY; j++) {
				if(mouseInteractionLock == false) {
					if((i-posX-(viewX/2)+1)*scale*2 < x && (i-posX-(viewX/2)+1+1)*scale*2 > x && (j+posY-(viewY/2))*scale*2 < y && (j+posY-(viewY/2)+1)*scale*2 > y) {
						mouseOverX = i-posX-(viewX/2)+1;
						mouseOverY = j+posY-(viewY/2);
					}
				}
				Tile t = world.getTile(i-posX-(viewX/2)+1, j+posY-(viewY/2));
				if(t != null)
					renderTile(t, i-posX-(viewX/2)+1, -j-posY+(viewY/2), shader, world.getWorld(), camera, false); // Rendering first 2 layers of map
			}
		}	
		
		for(Entity entity : world.getEntities()) {
			renderEntity(entity, shader, camera, world); // Entities rendering
			if(entity.isOverable()) {
				if(isOverEntity(entity, camera, window)) {
					if(mouseInteractionLock == false)
						mouseOverEntityId = entity.getId();
					
					if(entity.isClickable())
						if(window.getInput().isMouseButtonPressed(0))
							clickedEntityId = mouseOverEntityId;
				}
			}
		}
		
		for(int i = 0; i < viewX; i++) {
			for(int j = 0; j < viewY; j++) {
				Tile t = world.getTile(i-posX-(viewX/2)+1, j+posY-(viewY/2));
				if(t != null)
					renderTile(t, i-posX-(viewX/2)+1, -j-posY+(viewY/2), shader, world.getWorld(), camera, true); // Rendering last layer(3)
			}
		}
		
		if(mouseOverEntityId >= 0)
			window.requestCursor(Window.Cursor.Pointer);
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
	}
	
	public void renderEntity(Entity entity, Shader shader, Camera camera, World world) {
		if(entity.getTexture() != null) {
			Matrix4f target = camera.getProjection();
			target.mul(world.getWorld());
			
			Transform temp = new Transform().set(entity.getTransform());
			
			if(entity.getFullBoundingBox() == false)
				temp.getPosition().y += 1f; // This sets offset in texture rendering when entity should walk like on foots
				
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", temp.getProjection(target));
			shader.setUniform("sharpness", 1.0f);
			
			entity.getTexture().bind(0);
			this.model.render();
		}
	}
	
	public void setBuffers() {
		for(int x = 0; x < world.getWidth(); x++) {
			for(int y = 0; y < world.getHeight(); y++) {
				for(byte layer = 0; layer < 3; layer++) {
					Texture ly = world.getTiles()[x][y].getTexture(layer);
					
					if(ly != null) {
						ly.prepare();
					}
				}
			}
		}
	}
	
	public void update(float delta, Window window, Camera camera, Audio audioPlayer) { // World update
		for(int i = 0; i < world.getEntitiesToRespawn().size(); i++) {
			EntityRespawn temp =  world.getEntitiesToRespawn().get(i);
			if(temp.getEntity().getRespownInSec() != -1) {
				if(Timer.getDelay(temp.getTimerStart(), Timer.getNanoTime(), temp.getEntity().getRespownInSec())) {
					if(temp.getEntity() instanceof Enemy)
						((Enemy)temp.getEntity()).resetEnemy();
					
					world.getEntities().add(temp.getEntity());
					Collections.swap(world.getEntities(), world.numberOfEntities()-1, world.numberOfEntities()-2);
					world.getEntitiesToRespawn().remove(i);
				}
			}
		}
		
		for(Entity entity : world.getEntities()) {
			entity.update(delta, window, camera, this, audioPlayer);
			entity.collideWithTiles(world);
			entity.collideWithEntities(world);
		}
	}
	
	public void calculateView(Window window) {
		viewX = (int)(((window.getWidth()-worldOffset.x) / (scale*2)) + 4);
		viewY = (int)(((window.getHeight()-worldOffset.y) / (scale*2)) + 4);
	}
	
	public void correctCamera(Camera camera, Window window) {
		Vector3f position = camera.getPosition();
		int w = (int)(-world.getWidth() * scale * 2);
		int h = (int)(world.getHeight() * scale * 2);
		
		if(position.x > -(window.getWidth()/2)+scale+worldOffset.x/2) // Left border - add higher value, more offset
			position.x = -(window.getWidth()/2)+scale+worldOffset.x/2;
		if(position.x < w + (window.getWidth()/2)+scale-worldOffset.x/2) // Right border - subtract
			position.x = w + (window.getWidth()/2)+scale-worldOffset.x/2;
		
		if(position.y < (window.getHeight()/2)-scale-worldOffset.y/2) // Top border - subtract
			position.y = (window.getHeight()/2)-scale-worldOffset.y/2;
		if(position.y > h-(window.getHeight()/2)-scale+worldOffset.y/2) // Bottom border - add
			position.y = h-(window.getHeight()/2)-scale+worldOffset.y/2;
	}
	
	public boolean isOverEntity(Entity entity, Camera camera, Window window) {
		int posX = (int)(entity.getTransform().getPosition().x*scale);
		int posY = (int)(-entity.getTransform().getPosition().y*scale);
		
		int x = (-(int)((camera.getPosition().x)) - (int)(window.getWidth()/2) + (int)(worldOffset.x/2) + (int)scale + (int)window.getCursorPositionX());
		int y = ((int)((camera.getPosition().y)) - (int)(window.getHeight()/2) + (int)(worldOffset.y/2) + (int)scale + (int)window.getCursorPositionY());
		

		if(posX < x && (posX+entity.getTransform().getScale().x*scale*2) > x && (posY-entity.getTransform().getScale().y*scale+entity.getTransform().getOffset()) < y && posY+scale*2+entity.getTransform().getOffset() > y) {
			return true;
		}
		
		return false;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
	
	public int getMouseOverX() {
		return mouseOverX;
	}
	
	public int getMouseOverY() {
		return mouseOverY;
	}

	public static int getMouseOverEntityId() {
		return mouseOverEntityId;
	}

	public static int getClickedEntityId() {
		return clickedEntityId;
	}
	
	public Vector2f getWorldOffset() {
		return worldOffset;
	}

	public void setWorldOffset(Vector2f worldOffset) {
		this.worldOffset.set(worldOffset);
	}
	
	public static float getScale() {
		return scale;
	}

	public static boolean isMouseInteractionLocked() {
		return mouseInteractionLock;
	}

	public static void setMouseInteractionLock(boolean mouseInteractionLock) {
		WorldRenderer.mouseInteractionLock = mouseInteractionLock;
	}
}
