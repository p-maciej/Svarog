package svarog.gui;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.State;
import svarog.gui.GuiRenderer.stickTo;
import svarog.io.Window;
import svarog.objects.MouseInteraction;
import svarog.render.Texture;
import svarog.render.Transform;

public abstract class GuiObject implements MouseInteraction, Comparable<GuiObject> {
	private static int auto_increment = 0; // for the moment
	
	private int id;
	private int width;
	private int height;
	private Transform transform;
	private Vector2f position;
	private stickTo stickTo;
	private State state;
	
	private boolean isClickable;
	private boolean isMovable;
	private boolean isOverAllowed;

	private static final float scale = 16f;
	
	protected GuiObject() {
		transform = new Transform();
		position = new Vector2f();
		
		this.id = auto_increment++;
		this.setSize(10, 10);
		this.configureTransform();
		this.setPosition(new Vector2f());
	}
	
	protected GuiObject(int width, int height, stickTo stickTo) {
		transform = new Transform();
		position = new Vector2f();
		
		this.id = auto_increment++;
		this.setSize(width, height);
		this.setStickTo(stickTo);
		this.configureTransform();
		this.setPosition(new Vector2f());
	}

	protected GuiObject(int width, int height, Vector2f position) {
		transform = new Transform();
		this.position = new Vector2f();
		
		this.id = auto_increment++;
		this.setSize(width, height);
		this.configureTransform();
		this.setPosition(position);
	}

	public int getHeight() {
		return (int)(height*(scale/16));
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		configureTransform();
	}


	public int getWidth() {
		return (int)(width*(scale/16));
	}


	public int getId() {
		return id;
	}

	public Transform getTransform() {
		return transform;
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position.x = position.x;
		this.position.y = position.y;
		setTransformPosition(position);
	}
	
	public void setTransformPosition(Vector2f position) {
		this.transform.getPosition().x = position.x;
		this.transform.getPosition().y = position.y;
	}
	
	public void setPosition(float X, float Y) {
		this.position.x = X;
		this.position.y = Y;
		setTranformPosition(X, Y);
	}
	
	public void setTranformPosition(float X, float Y) {
		this.transform.getPosition().x = X;
		this.transform.getPosition().y = Y;
	}
	
	public void move(Vector2f direction) {
		this.position.add(direction.x, -direction.y);
	}
	
	public void move(float X, float Y) {
		this.position.add(X, -Y);
	}

	public static float getScale() {
		return scale;
	}
	
	private void configureTransform() {
		transform.setScale(scale*2, scale*2);
		this.transform.getScale().x = (float)Math.floor((float)(getWidth())/2 * (scale/16) * 2f) / 2f;
		this.transform.getScale().y = (float)Math.floor((float)(getHeight())/2 * (scale/16) * 2f) / 2f;
	}

	public stickTo getStickTo() {
		return stickTo;
	}

	public void setStickTo(stickTo stickTo) {
		this.stickTo = stickTo;
	}

	public State getState() {
		return state;
	}
	
	protected abstract Texture getTexture();
	
	protected abstract void update();

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
	public boolean isOverable() {
		return isOverAllowed;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}

	public void setMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}

	public void setOverable(boolean isOverAllowed) {
		this.isOverAllowed = isOverAllowed;
	}
	
	public boolean isMouseOver(Window window, double x, double y) {
		float posX = window.getWidth()/2 + getTransform().getPosition().x - getWidth()/2;
		float posY = window.getHeight()/2 - getTransform().getPosition().y - getHeight()/2;
		
		if(x > posX && y > posY && y < posY+getHeight() && x < posX+getWidth())
			return true;
		else
			return false;
	}
	
	@Override
	public int compareTo(GuiObject o) {
		return this.id - o.id;
	}
}
