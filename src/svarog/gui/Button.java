package svarog.gui;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.stickTo;
import svarog.io.Window;
import svarog.objects.MouseInteraction;
import svarog.render.Texture;

public class Button extends TextureObject implements MouseInteraction {
	
	public Button(Texture texture, Vector2f position) {
		super(texture, position);
		super.setClickable(true);
	}
	
	public Button(Texture texture, stickTo stickTo) {
		super(texture, stickTo);
	}
	
	public Button(Texture texture, float X, float Y) {
		super(texture, X, Y);
	}
	
	public boolean isMouseOver(Window window, double x, double y) {
		float posX = window.getWidth()/2 + super.getTransform().getPosition().x - super.getWidth()/2;
		float posY = window.getHeight()/2 - super.getTransform().getPosition().y - super.getHeight()/2;
		
		
		if(x > posX && y > posY && y < posY+super.getHeight() && x < posX+super.getWidth())
			return true;
		else
			return false;
	}
}
