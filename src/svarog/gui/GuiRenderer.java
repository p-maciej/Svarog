package svarog.gui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import svarog.gui.font.Line;
import svarog.gui.font.Font;
import svarog.gui.font.TextBlock;
import svarog.io.Window;
import svarog.io.Window.Cursor;
import svarog.objects.Item;
import svarog.render.Camera;
import svarog.render.Model;
import svarog.render.RenderProperties;
import svarog.render.Shader;
import svarog.render.Texture;

public class GuiRenderer implements RenderProperties {
	
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
	private static int draggingFromObjectId;
	private static boolean objectDraggedOut;
	private static boolean setPointer;

	private TextureObject bubbleLeft;
	private TextureObject bubbleRight;
	private BufferedImage bubbleCenter;

	private static int bubbleXoffest = 35; // I thing we can make setter for this to be customizable
	private static int bubbleYoffset = 20;
	private static int worldXOffset = -350;
	private static int worldYOffset = 70;
	
	private TileSheet tileSheet;
	
	private int dialogId;
	private Button dialogButton;
	
	private Font dialogFont;
	private Font answerFont;
	private Font answerHoverFont;
	private TextureObject dialogTop;
	private BufferedImage dialogCenter;
	
	public GuiRenderer(Window window) {
		this.objects = new ArrayList<GuiObject>();
		this.textBlocks = new ArrayList<TextBlock>();
		this.groups = new ArrayList<Group>();
		this.tileSheet = new TileSheet();
		
		this.model = new Model(verticesArray, textureArray, indicesArray);
		this.camera = new Camera();
		
		camera.setProjection(window.getWidth(), window.getHeight());
		
		this.windowWidth = window.getWidth();
		this.windowHeight = window.getHeight();
		
		draggingFromObjectId = -1;
		objectDraggedOut = false;
		
		dialogId = -1;
	}
	
	public void updatePositions() {
		
		/// GROUPS /////////
		for(Group group : groups) {
			if(group.getStickTo() != null) {
				setGroupStickTo(group);
				group.getTransform().add(group.getPosition().x, group.getPosition().y);
			} else {
				group.getTransform().set(group.getPosition().x, group.getPosition().y);
			}
			
			for(GuiObject object : group.getTextureObjectList()) {
				if(object.getStickTo() != null) {
					setObjectStickTo(object);
					object.getTransform().getPosition().add(object.getPosition().x+group.getTransform().x, object.getPosition().y+group.getTransform().y, 0);
				} else {
					object.getTransform().getPosition().set(object.getPosition().x+group.getTransform().x, object.getPosition().y+group.getTransform().y, 0);
				}
			}
			
			for(TextBlock textBlock : group.getTextBlockList()) {
				if(textBlock.getStickTo() != null) {
					setTextBlockStickTo(textBlock);
					textBlock.getTransform().getPosition().add(textBlock.getPosition().x+group.getTransform().x, textBlock.getPosition().y+group.getTransform().y, 0);
				} else {
					textBlock.getTransform().getPosition().set(textBlock.getPosition().x+group.getTransform().x, textBlock.getPosition().y+group.getTransform().y, 0);
				}
			}
		}
		////////////////////
		
		////// GROUPS FROM TILESHEET /////////////
		for(Group group : tileSheet.getTileGroupsList()) {			
			if(group.getStickTo() != null) {
				setGroupStickTo(group);
				group.getTransform().add(group.getPosition().x, group.getPosition().y);
			} else {
				group.getTransform().set(group.getPosition().x, group.getPosition().y);
			}
			
			for(GuiObject object : group.getTextureObjectList()) {
				if(object.getStickTo() != null) {
					setObjectStickTo(object);
					object.getTransform().getPosition().add(object.getPosition().x +group.getTransform().x, object.getPosition().y +group.getTransform().y, 0);
				} else {
					object.getTransform().getPosition().set(object.getPosition().x +group.getTransform().x, object.getPosition().y +group.getTransform().y, 0);
				}
				
				if(((Tile)object).getPuttedItem() != null) {
					((Tile)object).getPuttedItem().setPosition(object.getTransform().getPosition().x, object.getTransform().getPosition().y);
				}
			}
		}
		/////////////////////////////////////////
		
		//// ORDINARY OBJECTS///////////////////
		for(GuiObject object : objects) {
			if(object.getStickTo() != null) {
				setObjectStickTo(object);
				object.getTransform().getPosition().add(object.getPosition().x, object.getPosition().y, 0);
			} else {
				object.getTransform().getPosition().set(object.getPosition().x, object.getPosition().y, 0);
			}	
		}
		//////////////////////////////////////
		
		//////// TEXTBLOCKS ////////////////////////
		for(TextBlock textBlock : textBlocks) {
			if(textBlock.getStickTo() != null) {
				setTextBlockStickTo(textBlock);
				textBlock.getTransform().getPosition().add(textBlock.getPosition().x, textBlock.getPosition().y, 0);
			} else {
				textBlock.getTransform().getPosition().set(textBlock.getPosition().y, textBlock.getPosition().y, 0);
			}	
		}
		///////////////////////////////////////////
	}
	
	public void renderGuiObjects(Shader shader, Window window) {
		clickedObjectId = -1;
		mouseOverObjectId = -1;
		setPointer = false;
		
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
			for(TextureObject object : group.getTextureObjectList()) {
				renderGuiObject(object, shader, window);
			}
			
			for(TextBlock block : group.getTextBlockList()) {
				renderTextBlock(block, shader, window);
			}
		}
		
		// And groups of tiles
		for(Group group : tileSheet.getTileGroupsList()) {
			for(TextureObject object : group.getTextureObjectList()) {
				renderGuiObject(object, shader, window);
				Item temp = ((Tile)object).getPuttedItem();
				if(temp != null) 
					renderGuiObject(temp, shader, window);
			}
		}
		
		// Render items
		for(Group group : tileSheet.getTileGroupsList()) {
			for(TextureObject object : group.getTextureObjectList()) {
				Item temp = ((Tile)object).getPuttedItem();
				if(temp != null)  {
					renderGuiObject(temp, shader, window);
				}
			}
		}
		
		// Drag and drop for groups
		for(Group group : tileSheet.getTileGroupsList()) {
			for(TextureObject object : group.getTextureObjectList()) {
				dragAndDrop((Tile)object, window);
			}
		}

		if(dialogButton != null) {
			if(dialogButton.isClicked()) {
				closeDialog();
			}
		}

		if(setPointer == true)
			window.requestCursor(Cursor.Pointer);
	}
	
	private void renderTextBlock(TextBlock block, Shader shader, Window window) {
		// Mouse interaction //
		if(block.isOverable()) {
			if(block.isMouseOver(window, window.getCursorPositionX(), window.getCursorPositionY())) {
				if(block.isOverable())
					mouseOverObjectId = block.getId();
					
				if(block.isClickable() && window.getInput().isMouseButtonPressed(0)) {
					clickedObjectId = block.getId();
				}
				
				if(block.isClickable())
					setPointer = true;
			}
		}
		/////////////////////
		
		block.update();
		
		for(int i = 0; i < block.getLines().size(); i++) {
			Matrix4f projection = camera.getProjection();
			Line line = block.getLines().get(i);
					
			line.getTransform().getPosition().x = block.getTransform().getPosition().x + line.getWidth()/2;
			line.getTransform().getPosition().y = block.getTransform().getPosition().y + -i*line.getHeight();
					
			line.getTexture().bind(0);
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", line.getTransform().getProjection(projection));
			model.render();
		}
	}
	
	private void renderGuiObject(GuiObject object, Shader shader, Window window) {
			Matrix4f projection = camera.getProjection();
			
			// Mouse interaction //
			if(object.isOverable()) {
				if(object.isMouseOver(window, window.getCursorPositionX(), window.getCursorPositionY())) {
					if(object.isOverable())
						mouseOverObjectId = object.getId();
						
					if(object.isClickable() && window.getInput().isMouseButtonPressed(0)) {
						clickedObjectId = object.getId();
					}
					
					if(object.isClickable())
						setPointer = true;
				}
			}
			/////////////////////
			
			object.update();
			
			object.getTexture().bind(0);
			
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", object.getTransform().getProjection(projection));
			model.render();	
	}
	
	private void dragAndDrop(Tile object, Window window) {
		if(object != null) {
			if((object.getId() == mouseOverObjectId && draggingFromObjectId == -1) || draggingFromObjectId == object.getId()) {
				if(window.getInput().isMouseButtonDown(0)) {
					if(((Tile) object).getPuttedItem() != null) {
						if(draggingFromObjectId == -1)
							draggingFromObjectId = object.getId();
						
						if((draggingFromObjectId != mouseOverObjectId) || objectDraggedOut == true) {
							object.getPuttedItem().setPosition((float)window.getRelativePositionCursorX(), (float)window.getRelativePositionCursorY());
							objectDraggedOut = true;
						}
					}
				} else if(window.getInput().isMouseButtonReleased(0) && draggingFromObjectId != -1) {
					if(mouseOverObjectId != draggingFromObjectId) {
						Tile tile = tileSheet.getTileByObjectId(mouseOverObjectId);
						if(tile != null) {
							if(tile.getPuttedItem() == null) {
								try {
									tile.putItem(object.getPuttedItem());
									tile.getPuttedItem().setPosition(tile.getTransform().getPosition().x, tile.getTransform().getPosition().y);
									object.removePuttedItem();
								} catch (Exception e) {
									object.getPuttedItem().setPosition(object.getTransform().getPosition().x, object.getTransform().getPosition().y);
								}
							} else {
								object.getPuttedItem().setPosition(object.getTransform().getPosition().x, object.getTransform().getPosition().y);
							}
						} else {
							object.getPuttedItem().setPosition(object.getTransform().getPosition().x, object.getTransform().getPosition().y);
						}
					} else {
						object.getPuttedItem().setPosition(object.getTransform().getPosition().x, object.getTransform().getPosition().y);
					}
					draggingFromObjectId = -1;
					objectDraggedOut = false;
				}
			}
		}
	}
	
	public void showBubble(Line line, double posX, double posY) {
		if(bubbleLeft != null && bubbleRight != null && bubbleCenter != null) {
			Group group = new Group(State.dynamicImage);
			bubbleLeft.setPosition((float)posX+bubbleXoffest, (float)posY+bubbleYoffset);
			group.addTextureObject(bubbleLeft);
			
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
			
			TextureObject center = new TextureObject(new Texture(contentBackground, line.getWidth(), bubbleCenter.getHeight()), (float)(posX+line.getWidth()/2+bubbleLeft.getWidth()/2+bubbleXoffest), (float)(posY+bubbleYoffset));
			group.addTextureObject(center);
			
			line.setPosition((float)(posX+line.getWidth()/2+bubbleLeft.getWidth()/2+bubbleXoffest), (float)(posY+bubbleYoffset-bubbleCenter.getHeight()/2 +line.getHeight()/2));
			group.addTextureObject(line);
			
			bubbleRight.setPosition((float)posX+bubbleXoffest+line.getWidth()+bubbleLeft.getWidth()-3, (float)posY+bubbleYoffset);
			group.addTextureObject(bubbleRight);
			
			this.addGroup(group);
		} else 
			throw new IllegalStateException("Bubble textures isn't declared!");
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
	
	public void deleteDynamicGroups() {
		for(int i = 0; i < groups.size(); i++)
			if(groups.get(i).getState() != null)
				if(groups.get(i).getState() == State.dynamicImage)
					groups.remove(i);
	}
	
	private void createDialog(Dialog dialog) {
		if(dialogTop != null && dialogCenter != null && answerFont != null && answerHoverFont != null && dialogFont != null) {
			Group group = new Group();
			
			int yOffset = 36;
			int interspace = 8;
			TextBlock content = new TextBlock(550, new Vector2f());
			content.setString(dialogFont, dialog.getContent());
			
			int height = content.getHeight()-dialogTop.getHeight()+yOffset;
			if(height < 0)
				height = 0;
			
			int top = 0;
			int left = -dialogTop.getWidth()/2+15;
			
			
			for(int i = dialog.getAnswers().size()-1; i >= 0; i--) {
				Answer answer = dialog.getAnswers().get(i);
				
				TextBlockButton ans = new TextBlockButton(535, new Vector2f());
				ans.setString(answerFont, answerHoverFont, answer.getContent());
				top -= ans.getHeight()+interspace;
				height += ans.getHeight()+interspace;
				ans.move(left+15, top);
				answer.setObjectId(ans.getId());
				group.addTextBlock(ans);
			}
	
			top -= content.getHeight()+interspace;
			
			if(height*dialogTop.getWidth()*4 > 0) {
				ByteBuffer center = BufferUtils.createByteBuffer(height*dialogTop.getWidth()*4);
			
				for(int j = 0; j < dialogTop.getWidth(); j++) {
					for(int i = 0; i < height; i++) {
						int pixel = dialogCenter.getRGB(j, 0);
						center.put(((byte)((pixel >> 16) & 0xFF)));
						center.put(((byte)((pixel >> 8) & 0xFF)));
						center.put((byte)(pixel & 0xFF));
						center.put(((byte)((pixel >> 24) & 0xFF)));
					}
				}
				center.flip();
				
				TextureObject centerTexture = new TextureObject(new Texture(center, dialogTop.getWidth(), height));	
				centerTexture.move(0, -height/2);
				group.addTextureObject(centerTexture);
			}
	
			content.move(left, top);
			
			dialogTop.setPosition(0, height+dialogTop.getHeight()/2);
			Button closeDialog = new Button(new Texture("images/dialog/close_dialog.png"), new Vector2f(-left, -top+10));
			dialogButton = closeDialog;
			
			
			group.addTextureObject(dialogTop);	
			
			group.addTextBlock(content);
			group.addTextureObject(closeDialog);
			
			dialogId = group.getId();
			group.setStickTo(stickTo.Bottom);
			group.move(worldXOffset/2, worldYOffset);
			groups.add(group);
		} else
			throw new IllegalStateException("Set all of required textures and fonts for dialog in renderer!");
	}
	
	public void setTopDialog(BufferedImage topDialog) {
		TextureObject tempDialogTop = new TextureObject(new Texture(topDialog));
		this.dialogTop = tempDialogTop;
	}
	
	public void setCenterDialog(BufferedImage centerImage) {
		this.dialogCenter = centerImage;
	}
	
	public void showDialog(Dialog dialog) {
		if(dialogId == -1) {
			createDialog(dialog);
			updatePositions();
		}
	}
	
	public void closeDialog() {	
		if(dialogId >= 0) {
			removeGroup(dialogId);
			dialogId = -1;
		}
	}
	
	public boolean isDialogOpen() {
		if(dialogId >= 0)
			return true;
		else 
			return false;
	}
	
	private void setObjectStickTo(GuiObject object) {
		switch(object.getStickTo()) {
			case Top:
				object.setTranformPosition(0, getTop() - object.getHeight()/2);
				break;
			case Bottom:
				object.setTranformPosition(0, getBottom() + object.getHeight()/2);
				break;
			case BottomLeft:
				object.setTranformPosition(getLeft() + object.getWidth()/2, getBottom() + object.getHeight()/2);
				break;
			case BottomRight:
				object.setTranformPosition(getRight() - object.getWidth()/2, getBottom() + object.getHeight()/2);
				break;
			case Left:
				object.setTranformPosition(getLeft() + object.getWidth()/2, 0);
				break;
			case Right:
				object.setTranformPosition(getRight() - object.getWidth()/2, 0);
				break;
			case TopLeft:
				object.setTranformPosition(getLeft() + object.getWidth()/2, getTop() - object.getHeight()/2);
				break;
			case TopRight:
				object.setTranformPosition(getRight() - object.getWidth()/2, getTop() - object.getHeight()/2);
				break;
		}
	}
	
	private void setGroupStickTo(Group object) {
		switch(object.getStickTo()) {
			case Top:
				object.setTransformPosition(0, getBottom());
				break;
			case Bottom:
				object.setTransformPosition(0, getTop() + object.getGroupHeight());
				break;
			case BottomLeft:
				object.setTransformPosition(getLeft(), getTop() - object.getGroupHeight());
				break;
			case BottomRight:
				object.setTransformPosition(getRight() - object.getGroupWidth(), getTop() - object.getGroupHeight());
				break;
			case Left:
				object.setTransformPosition(getLeft(), 0);
				break;
			case Right:
				object.setTransformPosition(getRight() - object.getGroupWidth(), 0);
				break;
			case TopLeft:
				object.setTransformPosition(getLeft(), getBottom());
				break;
			case TopRight:
				object.setTransformPosition(getRight()-object.getGroupWidth(), getBottom());
				break;
		}
	}
	
	private void setTextBlockStickTo(TextBlock object) {
		switch(object.getStickTo()) {
			case Top:
				object.setTranformPosition(0, getTop() - 10);
				break; 
			case Bottom:
				object.setTranformPosition(0, getBottom() + object.getHeight());
				break;
			case BottomLeft:
				object.setTranformPosition(getLeft(), getBottom() + object.getHeight());
				break;
			case BottomRight:
				object.setTranformPosition(getRight() - object.getWidth(), getBottom() + object.getHeight());
				break;
			case Left:
				object.setTranformPosition(getLeft(), 0);
				break;
			case Right:
				object.setTranformPosition(getRight() - object.getWidth(), 0);
				break;
			case TopLeft:
				object.setTranformPosition(getLeft(), getTop() - 10);
				break;
			case TopRight:
				object.setTranformPosition(getRight() - object.getWidth(), getTop() - 10);
				break;
		}
	}

	public static int getClickedObjectId() {
		return clickedObjectId;
	}
	
	public static int getMouseOverObjectId() {
		return mouseOverObjectId;
	}
	
	public static int getDraggingFromObjectId() {
		return draggingFromObjectId;
	}
	
	public void setBubbleLeft(BufferedImage bubbleLeft) {
		TextureObject tempBubbleLeft = new TextureObject(new Texture(bubbleLeft));
		this.bubbleLeft = tempBubbleLeft;
	}

	public void setBubbleRight(BufferedImage bubbleRight) {
		TextureObject tempBubbleRight = new TextureObject(new Texture(bubbleRight));
		this.bubbleRight = tempBubbleRight;
	}

	public void setBubbleCenter(BufferedImage bubbleCenter) {
		this.bubbleCenter = bubbleCenter;
	}

	public void setTileSheet(TileSheet tileSheet) {
		this.tileSheet = tileSheet;
	}
	
	public TileSheet getTileSheet() {
		return tileSheet;
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
	
	public void removeGroup(int groupId) {
		for(int i = 0; i < groups.size(); i++)
			if(groups.get(i).getId() == groupId)
				groups.remove(i);
	}

	public void setDialogFont(Font dialogFont) {
		this.dialogFont = dialogFont;
	}

	public void setAnswerFont(Font answerFont) {
		this.answerFont = answerFont;
	}

	public void setAnswerHoverFont(Font answerHoverFont) {
		this.answerHoverFont = answerHoverFont;
	}
}
