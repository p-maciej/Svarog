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
	
	private Group backgroundElements;
	private Group content;
	
	private Vector2f position;
	private stickTo stickTo;
	
	private Button closeButton;
	
	private Font windowFont;
	
	public GuiWindow(String title, Font font, int width, int height, stickTo stickTo) {
		this.id = auto_increment++;
		this.setWidth(width);
		this.setHeight(height);
		this.setTitle(title);
		this.setWindowFont(font);
		
		position = new Vector2f();
		backgroundElements = new Group();
		content = new Group();
		this.setStickTo(stickTo);
		
		addStaticElements();
	}
	
	public GuiWindow(String title, Font font, int width, int height, Vector2f position) {
		this.id = auto_increment++;
		this.setWidth(width);
		this.setHeight(height);
		this.setTitle(title);
		this.setWindowFont(font);
		
		setPosition(position);
		backgroundElements = new Group();
		content = new Group();
		
		addStaticElements();
	}
	
	public GuiWindow(String title, Font font, TextureObject backgroundTexture) {
		this.id = auto_increment++;
		this.setWidth(backgroundTexture.getWidth());
		this.setHeight(backgroundTexture.getHeight());
		this.setTitle(title);
		this.setWindowFont(font);
		
		position = new Vector2f();
		backgroundElements = new Group();
		content = new Group();
		
		backgroundElements.addTextureObject(backgroundTexture);
		
		addStaticElements();
	}
	
	private void addStaticElements() {
		Button closeDialog = new Button(new Texture("images/dialog/close_dialog.png"), new Vector2f(getWidth()/2-15, getHeight()/2-15));
		closeButton = closeDialog;
		backgroundElements.addTextureObject(closeDialog);
		
		Line title = new Line(0, getHeight()/2-15);
		title.setString(this.title, windowFont);
		title.setOverable(true);
		title.setMovable(true);
		title.setClickable(true);
		
		backgroundElements.addTextureObject(title);
	}
	
	public void addBackgroundTextureObject(TextureObject object) {
		backgroundElements.addTextureObject(object);
	}
	
	public void addBackgroundTextBlock(TextBlock object) {
		backgroundElements.addTextBlock(object);
	}
	
	public void addContentTextureObject(TextureObject object) {
		content.addTextureObject(object);
	}
	
	public void addContentTextBlock(TextBlock object) {
		content.addTextBlock(object);
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
	
	Group getBackgroundElements() {
		return backgroundElements;
	}
	
	Group getContentElements() {
		return content;
	}
	
	private void setRelativePositions() {
		content.setStickTo(stickTo);
		backgroundElements.setStickTo(stickTo);
	
		
		content.setPosition(position);
		backgroundElements.setPosition(position);
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
}
