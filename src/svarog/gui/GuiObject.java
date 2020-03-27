package svarog.gui;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.State;
import svarog.gui.GuiRenderer.stickTo;
import svarog.objects.MouseInteraction;
import svarog.render.Texture;
import svarog.render.Transform;

public class GuiObject implements MouseInteraction {
	private static int auto_increment = 0; // for the moment
	
	private int id;
	private float width;
	private float height;
	private Transform transform;
	private Vector2f relativeTransform;
	private stickTo stickTo;
	private State state;
	
	private boolean isClickable = false;
	private boolean isMovable = false;
	private boolean isOverAllowed = false;
	private String overDescription = "";

	private static final float scale = 16f;
	
	protected GuiObject() {
		transform = new Transform();
		relativeTransform = new Vector2f();
		
		this.id = auto_increment++;
		this.setSize(10, 10);
		this.configureTransform();
		this.setPosition(new Vector2f());
	}
	
	protected GuiObject(int width, int height, stickTo stickTo) {
		transform = new Transform();
		relativeTransform = new Vector2f();
		
		this.id = auto_increment++;
		this.setSize(width, height);
		this.setStickTo(stickTo);
		this.configureTransform();
		this.setPosition(new Vector2f());
	}

	protected GuiObject(int width, int height, Vector2f position) {
		transform = new Transform();
		relativeTransform = new Vector2f();
		
		this.id = auto_increment++;
		this.setSize(width, height);
		this.configureTransform();
		this.setPosition(position);
	}

	public float getHeight() {
		return height*(scale/16);
	}


	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		configureTransform();
	}


	public float getWidth() {
		return width*(scale/16);
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
	
	private void configureTransform() {
		transform.setScale(scale*2, scale*2);
		this.transform.getScale().x = getWidth()/2 * (scale/16);
		this.transform.getScale().y = getHeight()/2 * (scale/16);
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

	@Override
	public boolean isClickable() {
		return isClickable;
	}

	@Override
	public boolean isMovable() {
		return isMovable;
	}

	@Override
	public boolean isOverAllowed() {
		return isOverAllowed;
	}

	@Override
	public String mouseOverDescription() {
		return overDescription;
	}

	public void setOverDescription(String overDescription) {
		this.overDescription = overDescription;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}

	public void setMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}

	public void setOverAllowed(boolean isOverAllowed) {
		this.isOverAllowed = isOverAllowed;
	}

}
