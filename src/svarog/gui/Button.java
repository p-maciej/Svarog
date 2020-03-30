package svarog.gui;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.stickTo;
import svarog.objects.MouseInteraction;
import svarog.render.Texture;

public class Button extends TextureObject implements MouseInteraction {
	
	private Texture copy;
	private Texture hoverTexture;
	private boolean hover;
	
	public Button(Texture texture, Vector2f position) {
		super(texture, position);
		super.setClickable(true);
		this.hoverTexture = this.copy = texture;
		this.hover = false;
	}
	
	public Button(Texture texture, stickTo stickTo) {
		super(texture, stickTo);
		super.setClickable(true);
		this.hoverTexture = this.copy = texture;
		this.hover = false;
	}
	
	public Button(Texture texture, float X, float Y) {
		super(texture, X, Y);
		super.setClickable(true);
		this.hoverTexture = this.copy = texture;
		this.hover = false;
	}
	
	public Button(Texture texture, Texture hoverTexture, Vector2f position) {
		super(texture, position);
		super.setClickable(true);
		this.copy = texture;
		this.hoverTexture = hoverTexture;
		this.hover = false;
	}
	
	public Button(Texture texture, Texture hoverTexture, stickTo stickTo) {
		super(texture, stickTo);
		super.setClickable(true);
		this.copy = texture;
		this.hoverTexture = hoverTexture;
		this.hover = false;
	}
	
	public Button(Texture texture, Texture hoverTexture, float X, float Y) {
		super(texture, X, Y);
		super.setClickable(true);
		this.copy = texture;
		this.hoverTexture = hoverTexture;
		this.hover = false;
	}
	
	public boolean isClicked() {
		return GuiRenderer.getClickedObjectId() == super.getId() ? true : false;
	}
	
	@Override
	public void update() {
		if(GuiRenderer.getMouseOverObjectId() == super.getId() && hover == false) {
			hover = true;
			super.setTexture(this.hoverTexture);
		} else if(GuiRenderer.getMouseOverObjectId() != super.getId() && hover == true) {
			hover = false;
			super.setTexture(this.copy);
		}
	}
}
