package svarog.gui;

import org.joml.Vector2f;

import svarog.render.Texture;

public class Tile {
	private static int auto_increment = 0;
	
	private int id;
	private byte tileType;
	private Texture texture;
	private Vector2f position;
	
	public Tile(byte tileType, Vector2f position) {
		this.position = new Vector2f();
		
		this.id = auto_increment++;
		this.setPosition(position);
		this.setTileType(tileType);
	}
	
	public Tile(Texture texture, byte tileType, Vector2f position) {
		this.position = new Vector2f();
		
		this.id = auto_increment++;
		this.setTexture(texture);
		this.setPosition(position);
		this.setTileType(tileType);
	}
	
	public Tile(Texture texture, byte tileType, int X, int Y) {
		this.position = new Vector2f();
		
		this.id = auto_increment++;
		this.setTexture(texture);
		this.setPosition(new Vector2f(X, Y));
		this.setTileType(tileType);
	}
	
	public int getId() {
		return id;
	}
	
	public int getTileType() {
		return tileType;
	}
	
	public void setTileType(byte tileType) {
		this.tileType = tileType;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public void setPosition(int X, int Y) {
		this.position = new Vector2f(X, Y);
	}
}
