package svarog.gui;

import org.joml.Vector2f;

import svarog.entity.Transform;
import svarog.gui.GuiRenderer.State;
import svarog.gui.GuiRenderer.stickTo;
import svarog.render.Texture;

public class GuiObject {
	private static int auto_increment = 0; // for the moment
	
	private int id;
	private float width;
	private float height;
	private Transform transform;
	private Vector2f relativeTransform;
	private stickTo stickTo;
	private State state;

	private static final float scale = 16f;
	
	protected GuiObject() {
		transform = new Transform();
		relativeTransform = new Vector2f();
		
		this.id = auto_increment++;
		this.setWidth(10);
		this.setHeight(10);
		this.configureTransform(new Vector2f());
	}
	
	protected GuiObject(int width, int height, stickTo stickTo) {
		transform = new Transform();
		relativeTransform = new Vector2f();
		
		this.id = auto_increment++;
		this.setWidth(width);
		this.setHeight(height);
		this.setStickTo(stickTo);
		this.configureTransform(new Vector2f());
	}

	protected GuiObject(int width, int height, Vector2f position) {
		transform = new Transform();
		relativeTransform = new Vector2f();
		
		this.id = auto_increment++;
		this.setWidth(width);
		this.setHeight(height);
		this.configureTransform(position);
	}

	public float getHeight() {
		return height*(scale/16);
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public float getWidth() {
		return width*(scale/16);
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getId() {
		return id;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setPosition(Vector2f position) {
		this.transform.getPosition().x = position.x;
		this.transform.getPosition().y = position.y;
	}
	
	public void setPosition(float X, float Y) {
		this.transform.getPosition().x = X;
		this.transform.getPosition().y = Y;
	}
	
	public void move(Vector2f direction) {
		this.relativeTransform.add(direction.x, -direction.y);
	}
	
	public void move(float X, float Y) {
		this.relativeTransform.add(X, -Y);
	}

	public static float getScale() {
		return scale;
	}
	
	private void configureTransform(Vector2f position) {
		transform.setScale(scale*2, scale*2);
		this.transform.getScale().x = getWidth()/2 * (scale/16);
		this.transform.getScale().y = getHeight()/2 * (scale/16);
		
		setPosition(position);
	}

	public stickTo getStickTo() {
		return stickTo;
	}

	public void setStickTo(stickTo stickTo) {
		this.stickTo = stickTo;
	}

	public Vector2f getMove() {
		return relativeTransform;
	}

	public State getState() {
		return state;
	}
	
	Texture getTexture() {
		return null;
	}

	public GuiObject setState(State state) {
		this.state = state;
		return this;
	}
}
