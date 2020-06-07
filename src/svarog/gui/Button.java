package svarog.gui;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.stickTo;
import svarog.objects.MouseInteraction;
import svarog.render.Texture;

public class Button extends TextureObject implements MouseInteraction {
	
	Texture copy;
	Texture hoverTexture;
	boolean hover;
	
	public Button(Texture texture) {
		super(texture, new Vector2f());
		super.setClickable(true);
		super.setOverable(true);
		this.hoverTexture = this.copy = texture;
		this.hover = false;
	}
	
	public Button(Texture texture, Vector2f position) {
		super(texture, position);
		super.setClickable(true);
		super.setOverable(true);
		this.hoverTexture = this.copy = texture;
		this.hover = false;
	}
	
	public Button(Texture texture, stickTo stickTo) {
		super(texture, stickTo);
		super.setClickable(true);
		super.setOverable(true);
		this.hoverTexture = this.copy = texture;
		this.hover = false;
	}
	
	public Button(Texture texture, float X, float Y) {
		super(texture, X, Y);
		super.setClickable(true);
		super.setOverable(true);
		this.hoverTexture = this.copy = texture;
		this.hover = false;
	}
	
	public Button(Texture texture, Texture hoverTexture) {
		super(texture, new Vector2f());
		super.setClickable(true);
		super.setOverable(true);
		this.copy = texture;
		this.hoverTexture = hoverTexture;
		this.hover = false;
	}
	
	public Button(Texture texture, Texture hoverTexture, Vector2f position) {
		super(texture, position);
		super.setClickable(true);
		super.setOverable(true);
		this.copy = texture;
		this.hoverTexture = hoverTexture;
		this.hover = false;
	}
	
	public Button(Texture texture, Texture hoverTexture, stickTo stickTo) {
		super(texture, stickTo);
		super.setClickable(true);
		super.setOverable(true);
		this.copy = texture;
		this.hoverTexture = hoverTexture;
		this.hover = false;
	}
	
	public Button(Texture texture, Texture hoverTexture, float X, float Y) {
		super(texture, X, Y);
		super.setClickable(true);
		super.setOverable(true);
		this.copy = texture;
		this.hoverTexture = hoverTexture;
		this.hover = false;
	}
	
	Button(Button button) {
		super(button.texture);
		super.setPosition(button.getPosition());
		
		if(button.getStickTo() != null)
			super.setStickTo(button.getStickTo());
		
		super.setClickable(true);
		super.setOverable(true);
		
		if(button.hoverTexture != null) {
			this.copy = button.texture;
			this.hoverTexture = button.hoverTexture;
		} else {
			this.hoverTexture = this.copy = button.texture;
		}
		
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
