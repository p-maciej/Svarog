package svarog.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.State;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.font.TextBlock;

public class Group implements Comparable<Group> {
	private static int auto_increment = 0;
	private int id;
	private Vector2f transform;
	private Vector2f position;
	
	private List<TextureObject> textureObjects;
	private List<TextBlock> textBlocks;
	
	private State state;
	
	private int groupWidth;
	private int groupHeight;
	
	private stickTo stickTo;
	
	public Group() {
		id = auto_increment++;
		
		textureObjects = new ArrayList<TextureObject>();
		textBlocks = new ArrayList<TextBlock>();
		transform = new Vector2f();
		position = new Vector2f();
	}
	
	public Group(State state) {
		id = auto_increment++;
		
		this.state = state;
		textureObjects = new ArrayList<TextureObject>();
		textBlocks = new ArrayList<TextBlock>();
		transform = new Vector2f();
		position = new Vector2f();
	}
	
	public void addTextureObject(TextureObject textureObject) {
		textureObjects.add(textureObject);
	}
	
	public void removeTextureObject(int id) {
		for(TextureObject object : textureObjects) {
			if(object.getId() == id)
				textureObjects.remove(object);
		}
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
	
	public void setTransformPosition(Vector2f position) {
		this.position.set(position.x, -position.y);
	}
	
	public void setTransformPosition(float X, float Y) {
		this.transform.set(X, -Y);
	}
	
	public void setPosition(Vector2f direction) {
		this.position.set(direction.x, -direction.y);
		setTransformPosition(direction);
	}
	
	public void setPosition(float X, float Y) {
		this.position.set(X, -Y);
		setTransformPosition(X, Y);
	}
	
	public void move(Vector2f direction) {
		this.position.add(direction.x, direction.y);
	}
	
	public void move(float X, float Y) {
		this.position.add(X, Y);
	}
	
	public Vector2f getTransform() {
		return transform;
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public State getState() {
		return state;
	}
	
	void getGroupSize() {
		int minXValue = 10000000;
		int maxXValue = 0;
		
		int minYValue = 10000000;
		int maxYValue = 0;
		for(TextureObject object : textureObjects) {
			int width = object.getWidth();
			int height = object.getHeight();
			float posX = object.getPosition().x;
			float posY = object.getPosition().y;
			
			if(posX-width/2 < minXValue)
				minXValue = (int)posX-width/2;
			if(posX+width/2 > maxXValue)
				maxXValue = (int)posX+width/2;
			if(posY-height/2 > minYValue)
				minYValue = (int)posY+height/2;
			if(posY+height/2 < maxYValue)
				maxYValue = (int)posY-height/2;
		}
		
		
		this.groupWidth = maxXValue - minXValue;
		this.groupHeight = -(maxYValue - minXValue);
	}

	public int getGroupWidth() {
		return groupWidth;
	}

	public int getGroupHeight() {
		return groupHeight;
	}

	public stickTo getStickTo() {
		return stickTo;
	}

	public void setStickTo(stickTo stickTo) {
		this.stickTo = stickTo;
	}
	
	List<TextureObject> getObjects() {
		return textureObjects;
	}

	@Override
	public int compareTo(Group o) {
		return id - o.id;
	}
}
