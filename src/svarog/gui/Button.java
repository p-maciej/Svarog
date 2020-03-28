package svarog.gui;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.stickTo;
import svarog.objects.MouseInteraction;
import svarog.render.Texture;

public class Button extends TextureObject implements MouseInteraction {
	
	public Button(Texture texture, Vector2f position) {
		super(texture, position);
		super.setClickable(true);
	}
	
	public Button(Texture texture, stickTo stickTo) {
		super(texture, stickTo);
		super.setClickable(true);
	}
	
	public Button(Texture texture, float X, float Y) {
		super(texture, X, Y);
		super.setClickable(true);
	}
	
	public boolean isClicked() {
		return GuiRenderer.getClickedObjectId() == super.getId() ? true : false;
	}
}
