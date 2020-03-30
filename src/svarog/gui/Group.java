package svarog.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.State;
import svarog.gui.font.TextBlock;

public class Group {
	private static int auto_increment = 0;
	private int id;
	private Vector2f relativePosition;
	
	private List<TextureObject> textureObjects;
	private List<TextBlock> textBlocks;
	
	
	private State state;
	
	public Group() {
		id = auto_increment++;
		
		textureObjects = new ArrayList<TextureObject>();
		textBlocks = new ArrayList<TextBlock>();
		relativePosition = new Vector2f();
	}
	
	public Group(State state) {
		id = auto_increment++;
		
		this.state = state;
		textureObjects = new ArrayList<TextureObject>();
		textBlocks = new ArrayList<TextBlock>();
		relativePosition = new Vector2f();
	}
	
	public void addTextureObject(TextureObject textureObject) {
		textureObjects.add(textureObject);
	}
	
	public void addTextBlock(TextBlock textBlock) {
		textBlocks.add(textBlock);
	}
	
	public void removeTextureObject(TextureObject textureObject) {
		textureObjects.remove(textureObject);
	}
	
	public void removeTextBlock(TextBlock textBlock) {
		textBlocks.remove(textBlock);
	}

	public int getId() {
		return id;
	}
	
	List<TextureObject> getTextureObjectList() {
		return textureObjects;
	}
	
	List<TextBlock> getTextBlockList() {
		return textBlocks;
	}
	
	public void setPosition(Vector2f direction) {
		this.relativePosition.add(direction.x, -direction.y);
	}
	
	public void setPosition(float X, float Y) {
		this.relativePosition.add(X, -Y);
	}
	
	public Vector2f getPosition() {
		return relativePosition;
	}

	public State getState() {
		return state;
	}
}
