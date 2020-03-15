package svarog.world;

import svarog.render.Texture;

public class Tile {
	private static int auto_increment = 0;
	
	private int id;
	private boolean solid;
	Texture[] texture = new Texture[3];
	
	
	public Tile(Texture texture) {
		this.id = auto_increment;
		auto_increment++;
		
		this.texture[0] = texture;
		this.solid = false;
	}
	
	public void setTexture(Texture texture, byte layer) {
		if(layer == 0 || layer == 1 || layer == 2)
			this.texture[layer] = texture;
		else
			throw new IllegalStateException("Layer number out of range");
	}
	
	public Texture getTexture(byte layer) {
		return texture[layer];
	}

	public Tile setSolid() {
		this.solid = true;
		return this;
	}
	
	public boolean isSolid() {
		return solid;
	}

	public int getId() {
		return id;
	}
}
