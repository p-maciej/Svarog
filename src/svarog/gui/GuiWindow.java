package svarog.gui;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.render.Texture;

public class GuiWindow {
	private static int auto_increment = 0;
	private int id;
	private int width;
	private int height;
	private String title;
	
	private Group elements;
	
	private Vector2f position;
	private stickTo stickTo;
	
	private Button closeButton;
	
	protected Font windowFont;
	
	private int backgroundWindowId;
	
	private boolean isClosed;

	public GuiWindow(String title, Font font, int width, int height, stickTo stickTo) {
		this.id = auto_increment++;
		this.setWidth(width);
		this.setHeight(height);
		this.setTitle(title);
		this.setWindowFont(font);
		this.setClosed(false);
		
		position = new Vector2f();
		elements = new Group();
		this.setStickTo(stickTo);
		
		addStaticElements();
	}
	
	public GuiWindow(String title, Font font, int width, int height, Vector2f position) {
		this.id = auto_increment++;
		this.setWidth(width);
		this.setHeight(height);
		this.setTitle(title);
		this.setWindowFont(font);
		this.setClosed(false);
		
		setPosition(position);
		elements = new Group();
		
		addStaticElements();
	}
	
	public GuiWindow(String title, Font font, TextureObject backgroundTexture) {
		this.id = auto_increment++;
		this.setWidth(backgroundTexture.getWidth());
		this.setHeight(backgroundTexture.getHeight());
		this.setTitle(title);
		this.setWindowFont(font);
		this.setClosed(false);
		
		position = new Vector2f();
		elements = new Group();
		
		backgroundTexture.setOverable(true);
		backgroundWindowId = backgroundTexture.getId();
		elements.addTextureObject(backgroundTexture);
		
		addStaticElements();
	}
	
	private void addStaticElements() {
		Button closeDialog = new Button(new Texture("images/dialog/close_dialog.png"), new Vector2f(getWidth()/2-15, getHeight()/2-15));
		closeButton = closeDialog;
		elements.addTextureObject(closeDialog);
		
		Line title = new Line(0, getHeight()/2-15);
		title.setString(this.title, windowFont);
		title.setOverable(true);
		title.setMovable(true);
		title.setClickable(true);
		
		elements.addTextureObject(title);
	}
	
	public void addTextureObject(TextureObject object) {
		elements.addTextureObject(object);
	}
	
	public void addTextBlock(TextBlock object) {
		elements.addTextBlock(object);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	Group getElements() {
		return elements;
	}
	
	private void setRelativePositions() {
		elements.setStickTo(stickTo);
	
		elements.setPosition(position);
	}

	public stickTo getStickTo() {
		return stickTo;
	}

	public void setStickTo(stickTo stickTo) {
		this.stickTo = stickTo;
		setRelativePositions();
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
		setRelativePositions();
	}
	
	public void setPosition(float X, float Y) {
		this.position = new Vector2f(X, Y);
		setRelativePositions();
	}
	
	public void move(float X, float Y) {
		this.position.add(X, -Y);
		setRelativePositions();
	}
	
	public void move(Vector2f rel) {
		this.position.add(rel.x, -rel.y);
		setRelativePositions();
	}

	public int getId() {
		return id;
	}

	public Button getCloseButton() {
		return closeButton;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setWindowFont(Font windowFont) {
		this.windowFont = windowFont;
	}

	int getBackgroundWindowId() {
		return backgroundWindowId;
	}
	
	
	public boolean isClosed() {
		return isClosed;
	}

	void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
}
