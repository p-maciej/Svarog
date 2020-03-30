package svarog.gui;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.stickTo;
import svarog.render.Texture;

public class TextureObject extends GuiObject {
	Texture texture;

	public TextureObject(Texture texture) {
		super(texture.getWidth(), texture.getHeight(), new Vector2f());
		this.texture = texture;
	}
	
	public TextureObject(Texture texture, stickTo stickTo) {
		super(texture.getWidth(), texture.getHeight(), stickTo);
		this.texture = texture;
	}
	
	public TextureObject(Texture texture, Vector2f position) {
		super(texture.getWidth(), texture.getHeight(), position);
		this.texture = texture;
	}
	
	public TextureObject(Texture texture, float X, float Y) {
		super(texture.getWidth(), texture.getHeight(), new Vector2f(X, Y));
		this.texture = texture;
	}
	
	public TextureObject(Vector2f position) {
		super(0, 0, position);
	}
	
	public TextureObject(stickTo stickTo) {
		super(0, 0, stickTo);
	}
	
	@Override
	public Texture getTexture() {
		return texture;
	}
	
	protected void setTexture(Texture texture) {
		this.texture = texture;
	}

	@Override
	void update() {}
}
