package svarog.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import svarog.entity.Player;
import svarog.gui.PagedGuiWindow.WindowTextType;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.io.Window;
import svarog.io.Window.Cursor;
import svarog.objects.Item;
import svarog.render.Camera;
import svarog.render.Model;
import svarog.render.RenderProperties;
import svarog.render.Shader;
import svarog.world.WorldRenderer;

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
	private List<GuiWindow> windows; 
	private BubbleContainer bubbleContainer;
	private DialogContainer dialogContainer;
	private ArenaContainer arenaContainer;
	private StatsContainer statsContainer;
	private TileSheet tileSheet;
	
	private static int clickedObjectId;
	private static int mouseOverObjectId;
	private static int draggingFromObjectId;
	private static boolean objectDraggedOut;
	private static int draggingWindowId;
	private static boolean setPointer;

	private static int bubbleXoffest = 35; // I thing we can make setter for this to be customizable
	private static int bubbleYoffset = 20;
	private static int worldXOffset = -350;
	private static int worldYOffset = 70;
	

	
	public GuiRenderer(Window window) {
		this.objects = new ArrayList<GuiObject>();
		this.textBlocks = new ArrayList<TextBlock>();
		this.groups = new ArrayList<Group>();
		this.tileSheet = new TileSheet();
		this.windows = new ArrayList<GuiWindow>();
		this.bubbleContainer = new BubbleContainer(bubbleXoffest, bubbleYoffset);
		this.dialogContainer = new DialogContainer(worldXOffset, worldYOffset);
		this.arenaContainer = new ArenaContainer();
		this.statsContainer = new StatsContainer();
		
		this.model = new Model(verticesArray, textureArray, indicesArray);
		this.camera = new Camera();
		
		camera.setProjection(window.getWidth(), window.getHeight());
		
		this.windowWidth = window.getWidth();
		this.windowHeight = window.getHeight();
		
		draggingFromObjectId = -1;
		objectDraggedOut = false;
		draggingWindowId = -1;
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
		
		for(GuiWindow item : windows) {
			if(item.getElements().getStickTo() != null) {
				setGroupStickTo(item.getElements());
				item.getElements().getTransform().add(item.getElements().getPosition().x, item.getElements().getPosition().y);
			} else {
				item.getElements().getTransform().set(item.getElements().getPosition().x, item.getElements().getPosition().y);
			}
			
			for(GuiObject object : item.getElements().getTextureObjectList()) {
				if(object.getStickTo() != null) {
					setObjectStickTo(object);
					object.getTransform().getPosition().add(object.getPosition().x+item.getElements().getTransform().x, object.getPosition().y+item.getElements().getTransform().y, 0);
				} else {
					object.getTransform().getPosition().set(object.getPosition().x+item.getElements().getTransform().x, object.getPosition().y+item.getElements().getTransform().y, 0);
				}
			}
			
			for(TextBlock textBlock : item.getElements().getTextBlockList()) {
				if(textBlock.getStickTo() != null) {
					setTextBlockStickTo(textBlock);
					textBlock.getTransform().getPosition().add(textBlock.getPosition().x+item.getElements().getTransform().x, textBlock.getPosition().y+item.getElements().getTransform().y, 0);
				} else {
					textBlock.getTransform().getPosition().set(textBlock.getPosition().x+item.getElements().getTransform().x, textBlock.getPosition().y+item.getElements().getTransform().y, 0);
				}
			}
			
			if(item instanceof PagedGuiWindow) {
				for(WindowTextType textBlock : ((PagedGuiWindow) item).getTextBlocks()) {
					if(textBlock.getBlock().getStickTo() != null) {
						setTextBlockStickTo(textBlock.getBlock());
						textBlock.getBlock().getTransform().getPosition().add(textBlock.getBlock().getPosition().x+item.getElements().getTransform().x, textBlock.getBlock().getPosition().y+item.getElements().getTransform().y, 0);
					} else {
						textBlock.getBlock().getTransform().getPosition().set(textBlock.getBlock().getPosition().x+item.getElements().getTransform().x, textBlock.getBlock().getPosition().y+item.getElements().getTransform().y, 0);
					}
				}
			}
		}
		
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
	
	public void renderGuiObjects(Shader shader, Window window, Player player, Font font) {
		clickedObjectId = -1;
		mouseOverObjectId = -1;
		setPointer = false;
		int windowToRemove = -1;
		
		if(DialogContainer.isDialogClosing())
			DialogContainer.setDialogClosing(false);
		
		if(ArenaContainer.isArenaClosing())
			ArenaContainer.setArenaClosing(false);
		
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
		
		boolean worldLock = false;
		for(GuiWindow item : windows) {	
			worldLock = false;
			for(TextureObject object : item.getElements().getTextureObjectList()) {
				renderGuiObject(object, shader, window);
				
				
				if(mouseOverObjectId == item.getBackgroundWindowId())
					worldLock = true;
				
				if(object.isOverable() && object.isMovable()) {
					boolean update = false;
					
					if(item.getPosition().x < -(window.getWidth()/2)) {
						item.setPosition(-(window.getWidth()/2)+item.getWidth()/2, item.getPosition().y);
						update = true;
					}

					if(item.getPosition().y-item.getHeight()/2 < -(window.getHeight()/2)) {
						item.setPosition(item.getPosition().x, -(window.getHeight()/2)+item.getHeight()/2);
						update = true;
					}

					if(mouseOverObjectId == object.getId() || draggingWindowId >= 0) {
						worldLock = true;
						if(window.getInput().isMouseButtonDown(0)) {
							draggingWindowId = object.getId();
							if(item.getStickTo() != null)
								item.setStickTo(null);
							
							if(window.getCursorPositionX()+item.getWidth()/2 < window.getWidth()-350 && window.getCursorPositionY()+item.getHeight() - 15 < window.getHeight()-70 && window.getCursorPositionY()-15 > 0 && window.getCursorPositionX()-item.getWidth()/2 > 0) {
								item.setPosition((float)window.getRelativePositionCursorX(), -((float)window.getRelativePositionCursorY()-item.getHeight()/2+15));
							}
							
							update = true;
						}
					}
					if(draggingWindowId >= 0 && window.getInput().isMouseButtonReleased(0)) {
						draggingWindowId = -1;
					}
					
					if(update)
						updatePositions();
				}
				
				if(object instanceof Button && item instanceof PagedGuiWindow) {
					if(((Button) object).isClicked() && clickedObjectId == ((PagedGuiWindow)item).getPageLeft().getId()) {
						((PagedGuiWindow)item).previousPage();
						updatePositions();
					}
					if(((Button) object).isClicked() && clickedObjectId == ((PagedGuiWindow)item).getPageRight().getId()) {
						((PagedGuiWindow)item).nextPage();
						updatePositions();
					}
				}
			}		

			
			for(TextBlock block : item.getElements().getTextBlockList()) {
				renderTextBlock(block, shader, window);
			}
			
			if(item instanceof PagedGuiWindow) {
				for(WindowTextType block : ((PagedGuiWindow) item).getTextBlocks()) {
					renderTextBlock(block.getBlock(), shader, window);
				}
			}
			
			if(item.getCloseButton().isClicked()) {
				windowToRemove = item.getId();
				worldLock = false;
			}
			
			if(WorldRenderer.isMouseInteractionLocked() && !worldLock)
				WorldRenderer.setMouseInteractionLock(false);
			else if(!WorldRenderer.isMouseInteractionLocked() && worldLock)
				WorldRenderer.setMouseInteractionLock(true);
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
				dragAndDrop((Tile)object, window, player, font);
			}
		}

		if(dialogContainer.getDialogButton() != null) {
			if(dialogContainer.getDialogButton().isClicked()) {
				DialogContainer.setDialogClosing(true);
				closeDialog();
			}
		}
		
		if(this.arenaContainer.getCloseArenaButton() != null) {
			if(this.arenaContainer.getCloseArenaButton().isClicked()) {
				ArenaContainer.setArenaClosing(true);
				closeArena();
			}
		}

		if(setPointer == true)
			window.requestCursor(Cursor.Pointer);
		
		if(windowToRemove != -1)
			this.removeWindow(windowToRemove);
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
			
			if(object.getTexture() != null)
				object.getTexture().bind(0);
			
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", object.getTransform().getProjection(projection));
			model.render();	
	}
	
	private void dragAndDrop(Tile object, Window window, Player player, Font font) {
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
							try {
								tile.putItem(object.getPuttedItem());
								tile.getPuttedItem().setPosition(tile.getTransform().getPosition().x, tile.getTransform().getPosition().y);
								object.removePuttedItem();
							} catch (Exception e) {
								if(e.getMessage() == "consume") {
									player.AddPlayerHP(((Item)object.getPuttedItem()).getItemInfo().getHpRegeneration());
									player.getInventory().removeItemById(object.getId());
										
									object.removePuttedItem();
									statsContainer.playerStatsDynamic(this, player);
										
								} else if(e.getMessage() == "delete") {
									player.getInventory().removeItemById(object.getId());
									object.removePuttedItem();
								} else {
									object.getPuttedItem().setPosition(object.getTransform().getPosition().x, object.getTransform().getPosition().y);
								}
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
	
	public void update(Window window) {
		camera.setProjection(window.getWidth(), window.getHeight());
		this.windowHeight = window.getHeight();
		this.windowWidth = window.getWidth();
		
		updatePositions();
	}
	
	public void updateAfterResize(Window window) {
		this.windowHeight = window.getHeight();
		this.windowWidth = window.getWidth();

		arenaContainer.reload(this);
		Group container = arenaContainer.getArenaGroup(windowWidth, windowHeight);
		if(container != null) {
			groups.add(container);
			updatePositions();
		}
		
		update(window);
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
	
	public void deleteGuiPanelGroups() {
		for(int i = 0; i < groups.size(); i++)
			if(groups.get(i).getState() != null)
				if(groups.get(i).getState() == State.guiPanel)
					groups.remove(i);
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
	
	public void addWindow(GuiWindow window) {
		boolean add = true;
		for(GuiWindow wind : windows) {
			if(wind.getId() == window.getId())
				add = false;
		}
		
		if(add) {
			windows.add(window);
			updatePositions();
		}
	}
	
	public void removeGuiObject(GuiObject object) {
		objects.remove(object);
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
	
	public void removeGroup(Group group) {
		groups.remove(group);
	}
	
	public void removeWindow(int windowId) {
		for(int i = 0; i < windows.size(); i++)
			if(windows.get(i).getId() == windowId)
				windows.remove(i);
	}
	
	public void showBubble(Line line, double posX, double posY) {
		this.groups.add(bubbleContainer.getBubble(line, posX, posY));
	}
	
	public void showDialog(Dialog dialog) {
		if(dialogContainer.getDialogId() == -1) {
			groups.add(dialogContainer.createDialog(dialog));
			updatePositions();
		}
	}
	
	public void closeDialog() {	
		dialogContainer.closeDialog(this);
	}
	
	public boolean isDialogOpen() {
		return dialogContainer.isDialogOpen();
	}
	
	public void updatePlayerStats(Player player) {
		this.statsContainer.playerStatsDynamic(this, player);
	}
	
	public void showArena(Arena arena) {
		this.arenaContainer.setArena(arena);
		Group container = arenaContainer.getArenaGroup(windowWidth, windowHeight);
		if(container != null) {
			WorldRenderer.setMouseInteractionLock(true);
			groups.add(container);
			updatePositions();
		}
	}
	
	public void closeArena() {
		arenaContainer.closeArena(this);
		WorldRenderer.setMouseInteractionLock(false);
	}
}
