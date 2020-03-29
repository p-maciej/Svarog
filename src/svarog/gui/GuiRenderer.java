package svarog.gui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.io.Window;
import svarog.io.Window.Cursor;
import svarog.render.Camera;
import svarog.render.Model;
import svarog.render.Shader;
import svarog.render.Texture;

public class GuiRenderer {
	private static final float[] verticesArray = new float[] {
			-1f, 1f, 0,
			1f, 1f, 0,			
			1f, -1f, 0,
			-1f, -1f, 0,
	};
	
	private static final float[] textureArray = new float[] {
			0, 0,
			0, 1,	
			1, 1,
			1, 0,
	};
	
	private static final int[] indicesArray = new int[] {
			0,1,2,
			2,3,0
	};
	
	public static enum stickTo {
		Top,
		Bottom,
		Left,
		Right,
		TopLeft,
		TopRight,
		BottomLeft,
		BottomRight
	}
	
	public static enum State {
		staticImage,
		dynamicImage,
		guiPanel
	}
	
	private Model model;
	private Camera camera;
	
	private int windowWidth;
	private int windowHeight;
	
	private List<GuiObject> objects;
	private List<TextBlock> textBlocks;
	private List<Group> groups;

	private static int clickedObjectId;
	private static int mouseOverObjectId;
	
	
	private TextureObject bubbleLeft;
	private TextureObject bubbleRight;
	private BufferedImage bubbleCenter;
	
	public GuiRenderer(Window window) {
		this.objects = new ArrayList<GuiObject>();
		this.textBlocks = new ArrayList<TextBlock>();
		this.groups = new ArrayList<Group>();
		
		this.model = new Model(verticesArray, textureArray, indicesArray);
		this.camera = new Camera();
		
		camera.setProjection(window.getWidth(), window.getHeight());
		
		this.windowWidth = window.getWidth();
		this.windowHeight = window.getHeight();
		
		bubbleLeft = new TextureObject(new Texture("images/bubble/left.png"));
		bubbleRight = new TextureObject(new Texture("images/bubble/right.png"));
		bubbleCenter = Texture.getImageBuffer("images/bubble/center.png");
	}
	
	public void updatePositions() {
		for(Group group : groups) {
			for(GuiObject object : group.getTextureObjectList()) {
				if(object.getStickTo() != null) {
					setObjectStickTo(object);
				}
				
				object.getTransform().getPosition().add(object.getMove().x+group.getPosition().x, object.getMove().y+group.getPosition().y, 0);
			}
			
			for(TextBlock textBlock : group.getTextBlockList()) {
				if(textBlock.getStickTo() != null) {
					setTextBlockStickTo(textBlock);
				}
				
				textBlock.getPosition().add(textBlock.getMove().x+group.getPosition().x, textBlock.getMove().y+group.getPosition().y);
			}
		}
		
		for(GuiObject object : objects) {
			if(object.getStickTo() != null) {
				setObjectStickTo(object);
			}
			
			object.getTransform().getPosition().add(object.getMove().x, object.getMove().y, 0);
		}
		
		for(TextBlock textBlock : textBlocks) {
			if(textBlock.getStickTo() != null) {
				setTextBlockStickTo(textBlock);
			}
			
			textBlock.getPosition().add(textBlock.getMove().x, textBlock.getMove().y);
		}
	}
	
	private void setObjectStickTo(GuiObject object) {
		switch(object.getStickTo()) {
			case Top:
				object.setPosition(0, getTop() - object.getHeight()/2);
				break;
			case Bottom:
				object.setPosition(0, getBottom() + object.getHeight()/2);
				break;
			case BottomLeft:
				object.setPosition(getLeft() + object.getWidth()/2, getBottom() + object.getHeight()/2);
				break;
			case BottomRight:
				object.setPosition(getRight() - object.getWidth()/2, getBottom() + object.getHeight()/2);
				break;
			case Left:
				object.setPosition(getLeft() + object.getWidth()/2, 0);
				break;
			case Right:
				object.setPosition(getRight() - object.getWidth()/2, 0);
				break;
			case TopLeft:
				object.setPosition(getLeft() + object.getWidth()/2, getTop() - object.getHeight()/2);
				break;
			case TopRight:
				object.setPosition(getRight() - object.getWidth()/2, getTop() - object.getHeight()/2);
				break;
		}
	}
	
	private void setTextBlockStickTo(TextBlock object) {
		switch(object.getStickTo()) {
			case Top:
				object.setPosition(0, getTop() - 10);
				break;
			case Bottom:
				object.setPosition(0, getBottom() + object.getHeight());
				break;
			case BottomLeft:
				object.setPosition(getLeft(), getBottom() + object.getHeight());
				break;
			case BottomRight:
				object.setPosition(getRight() - object.getMaxWidth(), getBottom() + object.getHeight());
				break;
			case Left:
				object.setPosition(getLeft(), 0);
				break;
			case Right:
				object.setPosition(getRight() - object.getMaxWidth(), 0);
				break;
			case TopLeft:
				object.setPosition(getLeft(), getTop() - 10);
				break;
			case TopRight:
				object.setPosition(getRight() - object.getMaxWidth(), getTop() - 10);
				break;
		}
	}
	
	public void renderGuiObjects(Shader shader, Window window) {
		clickedObjectId = -1;
		mouseOverObjectId = -1;
		
		// Dynamic images render first (they are deleted every window resize)
		for(GuiObject object : objects) {
			if(object.getState() == State.guiPanel) {
				renderGuiObject(object, shader, window);
			}
		}
		
		for(GuiObject object : objects) {
			if(object.getState() == State.dynamicImage) {
				renderGuiObject(object, shader, window);
			}
		}
		
		// Then render everything else in adding order
		for(GuiObject object : objects) {
			if(object.getState() == State.staticImage || object.getState() == null) {
				renderGuiObject(object, shader, window);
			}
		}
		
		// After that textBlocks, they supposed to cover textures
		for(TextBlock block : textBlocks) {
			renderTextBlock(block, shader, window);
		}
		
		// Then render groups, they usually will be moving or not windows inside the game
		for(Group group : groups) {
			for(TextureObject object : group.getTextureObjectList()) { // i'm not sure that we need this <----
				if(object.getState() == State.dynamicImage) {
					renderGuiObject(object, shader, window);
				}
			}
			
			for(TextureObject object : group.getTextureObjectList()) {
				if(object.getState() != State.dynamicImage) {
					renderGuiObject(object, shader, window);
				}
			}
			
			for(TextBlock block : group.getTextBlockList()) {
				renderTextBlock(block, shader, window);
			}
		}
		
		if(mouseOverObjectId >= 0)
			window.requestCursor(Cursor.Pointer);
	}
	
	private void renderTextBlock(TextBlock block, Shader shader, Window window) {
		for(int i = 0; i < block.getLines().size(); i++) {
			Matrix4f projection = camera.getProjection();
			Line line = block.getLines().get(i);
					
			line.getTransform().getPosition().x = block.getPosition().x + line.getWidth()/2;
			line.getTransform().getPosition().y = block.getPosition().y + -i*line.getHeight();
					
			line.getTexture().bind(0);
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", line.getTransform().getProjection(projection));
			model.render();
		}
	}
	
	private void renderGuiObject(GuiObject object, Shader shader, Window window) {
			Matrix4f projection = camera.getProjection();
			object.getTexture().bind(0);
			
			if(object.isClickable()) {
				if(object.isMouseOver(window, window.getCursorPositionX(), window.getCursorPositionY())) {
					mouseOverObjectId = object.getId();
					if(window.getInput().isMouseButtonPressed(0)) {
						clickedObjectId = object.getId();
					}
				}
			}
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", object.getTransform().getProjection(projection));
			model.render();	
	}
	
	public void addGuiObject(GuiObject object) {
		objects.add(object);
	}
	
	public void addGuiObject(GuiObject object, State state) {
		objects.add(object.setState(state));
	}
	
	public void addTextBlock(TextBlock textBlock) {
		textBlocks.add(textBlock);
	}
	
	public void addGroup(Group group) {
		groups.add(group);
	}
	
	public float getRight() {
		return windowWidth/2;
	}
	
	public float getLeft() {
		return -windowWidth/2;
	}
	
	public float getTop() {
		return windowHeight/2;
	}
	
	public float getBottom() {
		return -windowHeight/2;
	}
	
	public void showBubble(Line line, double posX, double posY) {
		int Xoffest = 35;
		int Yoffset = 20;
		bubbleLeft.setPosition((float)posX+Xoffest, (float)posY+Yoffset);
		this.addGuiObject(bubbleLeft, State.dynamicImage);
		
		ByteBuffer contentBackground = BufferUtils.createByteBuffer((int)(bubbleCenter.getHeight()*line.getWidth()*4));
		
		for(int i = 0; i < line.getWidth(); i++) {
			for(int j = 0; j < bubbleCenter.getHeight(); j++) {
				int pixel = bubbleCenter.getRGB(0, j);
				contentBackground.put(((byte)((pixel >> 16) & 0xFF)));
				contentBackground.put(((byte)((pixel >> 8) & 0xFF)));
				contentBackground.put((byte)(pixel & 0xFF));
				contentBackground.put(((byte)((pixel >> 24) & 0xFF)));
			}
		}
		contentBackground.flip();
		
		TextureObject center = new TextureObject(new Texture(contentBackground, line.getWidth(), bubbleCenter.getHeight()), (float)(posX+line.getWidth()/2+bubbleLeft.getWidth()/2+Xoffest), (float)(posY+Yoffset));
		this.addGuiObject(center, State.dynamicImage);
		
		line.setPosition((float)(posX+line.getWidth()/2+bubbleLeft.getWidth()/2+Xoffest), (float)(posY+Yoffset));
		this.addGuiObject(line, State.dynamicImage);
		
		bubbleRight.setPosition((float)posX+Xoffest+line.getWidth()+bubbleLeft.getWidth()-3, (float)posY+Yoffset);
		this.addGuiObject(bubbleRight, State.dynamicImage);
	}
	
	public void update(Window window) {
		camera.setProjection(window.getWidth(), window.getHeight());
		this.windowHeight = window.getHeight();
		this.windowWidth = window.getWidth();
		updatePositions();
	}
	
	public void deleteGuiPanels() {
		for(int i = 0; i < objects.size(); i++)
			if(objects.get(i).getState() != null)
				if(objects.get(i).getState() == State.guiPanel)
					objects.remove(i);
	}
	
	public void deleteDynamicElements() {
		for(int i = 0; i < objects.size(); i++)
			if(objects.get(i).getState() != null)
				if(objects.get(i).getState() == State.dynamicImage)
					objects.remove(i);
	}

	protected static int getClickedObjectId() {
		return clickedObjectId;
	}
}
