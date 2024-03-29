package svarog.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joml.Matrix4f;

import svarog.entity.Player;
import svarog.gui.GroupProperties.groupType;
import svarog.gui.PagedGuiWindow.WindowTextType;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.io.Window;
import svarog.io.Timer;
import svarog.io.Window.Cursor;
import svarog.language.LanguageLoader;
import svarog.objects.Item;
import svarog.objects.ItemProperties.ItemType;
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
	private List<Group> groups;
	private List<GuiWindow> windows; 
	private BubbleContainer bubbleContainer;
	private DialogContainer dialogContainer;
	private ArenaContainer arenaContainer;
	private StatsContainer statsContainer;
	private TileSheet tileSheet;
	
	
	private static int clickedObjectId;
	private static int mouseOverObjectId;
	private static int mouseOverTileId;
	private static int clickedTileId;
	private static int draggingFromObjectId;
	private static int draggingItemId;
	private static boolean objectDraggedOut;
	private static int draggingWindowId;
	private static GuiWindow draggingWindow;
	private static boolean setPointer;
	private static int pressedObjectId;
	private static int pressedObjId;
	private static int setObjectId;
	private static long clickedTime;
	private static int clickCount;

	private static int bubbleXoffest = 35; // I thing we can make setter for this to be customizable
	private static int bubbleYoffset = 20;
	private int worldXOffset;
	private int worldYOffset;
	
	boolean update = false;

	private int marginRight;
	private int marginBottom;
	private int borderRight;
	private int borderBottom;
	
	public GuiRenderer(Window window) {
		this.objects = new ArrayList<GuiObject>();
		this.groups = new ArrayList<Group>();
		this.tileSheet = new TileSheet();
		this.windows = new ArrayList<GuiWindow>();
		this.bubbleContainer = new BubbleContainer(bubbleXoffest, bubbleYoffset);
		this.dialogContainer = new DialogContainer(worldXOffset, worldYOffset);
		this.arenaContainer = new ArenaContainer();
		this.statsContainer = new StatsContainer();
		
		this.setMarginBottom(0);
		this.setMarginRight(0);
		this.setWorldXOffset(0);
		this.setWorldYOffset(0);
		
		this.borderRight = window.getWidth()-this.marginRight;
		this.borderBottom = window.getHeight()-this.marginBottom;
		
		this.model = new Model(verticesArray, textureArray, indicesArray);
		this.camera = new Camera();
		
		camera.setProjection(window.getWidth(), window.getHeight());
		
		this.windowWidth = window.getWidth();
		this.windowHeight = window.getHeight();
		
		draggingFromObjectId = -1;
		mouseOverTileId = -1;
		clickedTileId = -1;
		objectDraggedOut = false;
		draggingWindowId = -1;
		pressedObjectId = -1;
		pressedObjId = -1;
		setObjectId = -1;
		draggingWindow = null;
		draggingItemId = -1;
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
				if(!(object instanceof TextBlock)) {
					if(object.getStickTo() != null) {
						setObjectStickTo(object);
						object.getTransform().getPosition().add(object.getPosition().x+group.getTransform().x, object.getPosition().y+group.getTransform().y, 0);
					} else {
						object.getTransform().getPosition().set(object.getPosition().x+group.getTransform().x, object.getPosition().y+group.getTransform().y, 0);
					}
				} else {
					if(object.getStickTo() != null) {
						setTextBlockStickTo(object);
						object.getTransform().getPosition().add(object.getPosition().x+group.getTransform().x, object.getPosition().y+group.getTransform().y, 0);
					} else {
						object.getTransform().getPosition().set(object.getPosition().x+group.getTransform().x, object.getPosition().y+group.getTransform().y, 0);
					}
				}
			}
		}
		////////////////////
		
		for(GuiWindow item : windows) {
			if(item.getElements().getStickTo() != null) {
				setGroupStickTo(item.getElements());
				item.getElements().getTransform().add(item.getElements().getPosition().x, item.getElements().getPosition().y);
			} else {
				item.getElements().getTransform().set(item.getElements().getPosition().x, item.getElements().getPosition().y);
			}
			
			for(GuiObject object : item.getElements().getTextureObjectList()) {
				if(!(object instanceof TextBlock)) {
					if(object.getStickTo() != null) {
						setObjectStickTo(object);
						object.getTransform().getPosition().add(object.getPosition().x+item.getElements().getTransform().x, object.getPosition().y+item.getElements().getTransform().y, 0);
					} else {
						object.getTransform().getPosition().set(object.getPosition().x+item.getElements().getTransform().x, object.getPosition().y+item.getElements().getTransform().y, 0);
					}
				} else {
					if(object.getStickTo() != null) {
						setTextBlockStickTo(object);
						object.getTransform().getPosition().add(object.getPosition().x+item.getElements().getTransform().x, object.getPosition().y+item.getElements().getTransform().y, 0);
					} else {
						object.getTransform().getPosition().set(object.getPosition().x+item.getElements().getTransform().x, object.getPosition().y+item.getElements().getTransform().y, 0);
					}
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
			
			if(item instanceof TradeWindow) {
				for(TileSheet sheet : ((TradeWindow)item).getProducts())  {
					for(Group group : sheet.getTileGroupsList()) {		
						group.getTransform().set(group.getPosition().x+item.getPosition().x, group.getPosition().y-item.getPosition().y);
						
						
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
				}
			}
			
			if(item instanceof ItemWindow) {
				for(Group group : ((ItemWindow) item).getTileSheet().getTileGroupsList()) {
					if(group.getType() == groupType.swap) {
						group.getTransform().set(group.getPosition().x+item.getPosition().x, group.getPosition().y-item.getPosition().y);
					
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
				}
			}
		}
		
		////// GROUPS FROM TILESHEET /////////////
		for(Group group : tileSheet.getTileGroupsList()) {		
			if(group.getType() != groupType.swap) {
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
		}
		/////////////////////////////////////////
		
		//// ORDINARY OBJECTS///////////////////
		for(GuiObject object : objects) {
			if(object.getStickTo() != null) {
				if(!(object instanceof TextBlock))
					setObjectStickTo(object);
				else
					setTextBlockStickTo((TextBlock)object);
				object.getTransform().getPosition().add(object.getPosition().x, object.getPosition().y, 0);
			} else {
				object.getTransform().getPosition().set(object.getPosition().x, object.getPosition().y, 0);
			}	
		}
		//////////////////////////////////////
	}
	
	public void renderGuiObjects(Shader shader, Window window, Player player) {
		mouseOverTileId = -1;
		clickedObjectId = -1;
		clickedTileId = -1;
		mouseOverObjectId = -1;
		setPointer = false;
		int windowToRemove = -1;
		
		statsContainer.update(this);
		
		if(DialogContainer.isDialogClosing())
			DialogContainer.setDialogClosing(false);
		
		if(ArenaContainer.isArenaClosing())
			ArenaContainer.setArenaClosing(false);
		
		// Interaction
		// Dynamic images render first (they are deleted every window resize)
		for(GuiObject object : objects) {
			mouseInteraction(object, window);
		}
		
		// Then render groups, they usually will be moving or not windows inside the game
		for(Group group : groups) {		
			for(GuiObject object : group.getTextureObjectList()) {
				mouseInteraction(object, window);
			}
		}

		
		for(GuiWindow item : windows) {	
			for(GuiObject object : item.getElements().getTextureObjectList()) {
				mouseInteraction(object, window);
				

				if(item instanceof PagedGuiWindow) {
					for(WindowTextType block : ((PagedGuiWindow) item).getTextBlocks()) {
						mouseInteraction(block.getBlock(), window);
					}
				}
				
				if(item instanceof TradeWindow) {
					for(TileSheet sheet : ((TradeWindow)item).getProducts())  {
						for(Group group : sheet.getTileGroupsList()) {
							for(GuiObject obj : group.getTextureObjectList()) {
								mouseInteraction(obj, window);
								Item temp = ((Tile)obj).getPuttedItem();
								if(temp != null) 
									mouseInteraction(temp, window);
							}
						}
					}
				}
				
				if(item instanceof ItemWindow) {
					for(Group group : ((ItemWindow)item).getTileSheet().getTileGroupsList()) {
						if(group.getType() == groupType.swap) { 
							for(GuiObject obj : group.getTextureObjectList()) {
								mouseInteraction(obj, window);
								Item temp = ((Tile)obj).getPuttedItem();
								if(temp != null) 
									mouseInteraction(temp, window);
							}
						}
					}
				}
			}
		}
		
		// And groups of tiles
		for(Group group : tileSheet.getTileGroupsList()) {
			if(group.getType() != groupType.swap) { 
				for(GuiObject object : group.getTextureObjectList()) {
					mouseInteraction(object, window);
					Item temp = ((Tile)object).getPuttedItem();
					if(temp != null) 
						mouseInteraction(temp, window);
				}
			}
		}
		////////////////////////////////////////
		
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
		
		// Then render groups, they usually will be moving or not windows inside the game
		for(Group group : groups) {		
			for(GuiObject object : group.getTextureObjectList()) {
				renderGuiObject(object, shader, window);
			}
		}
		
		// And groups of tiles
		for(Group group : tileSheet.getTileGroupsList()) {
			if(group.getType() != groupType.swap) {
				for(GuiObject object : group.getTextureObjectList()) {
					renderGuiObject(object, shader, window);
				}
			}
		}
		
		int outerIndex = 0;
		int draggingIndex = -1;
		
		update = false;
		
		for(int i = 0; i < windows.size(); i++) {
			GuiWindow item = windows.get(i);
			for(GuiObject object : item.getElements().getTextureObjectList()) {
				renderGuiObject(object, shader, window);
				
				boolean updateT = windowInteraction(item, object, window);
				
				if(!update && updateT)
					update = true;

				
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
			
			if(item instanceof PagedGuiWindow) {
				for(WindowTextType block : ((PagedGuiWindow) item).getTextBlocks()) {
					renderGuiObject(block.getBlock(), shader, window);
				}
			}
			
			if(item instanceof TradeWindow) {
				for(TileSheet sheet : ((TradeWindow)item).getProducts())  {
					for(Group group : sheet.getTileGroupsList()) {
						for(GuiObject object : group.getTextureObjectList()) {
							renderGuiObject(object, shader, window);
							Item temp = ((Tile)object).getPuttedItem();
							if(temp != null)  {
								renderGuiObject(temp, shader, window);
							
							
								if(((Tile)object).getTileId() < 200) {
									addToTileSheet((TradeWindow)item, (Tile)object, window, ((TradeWindow)item).getProducts()[1]);
								} else {					
									if(window.getInput().isMouseButtonReleased(0))
										if(mouseOverTileId == ((Tile)object).getId()) {
											((Tile)object).removePuttedItem();
											((TradeWindow) item).update();
											update = true;
										}
								}
							}
						}
					}
				}
			}
			
			if(item instanceof ItemWindow) {
				for(Group group : ((ItemWindow) item).getTileSheet().getTileGroupsList()) {
					if(group.getType() == groupType.swap) {
						for(GuiObject object : group.getTextureObjectList()) {
							renderGuiObject(object, shader, window);
							Item temp = ((Tile)object).getPuttedItem();
							if(temp != null && temp.getId() != draggingItemId)  {
								renderGuiObject(temp, shader, window);
							}
						}
					}
				}
			}
			
			if(item.getCloseButton() != null) {
				if(item.getCloseButton().isClicked()) {
					windowToRemove = item.getId();
				}
			}
			

		}
		
		if(WorldRenderer.isMouseInteractionLocked() && mouseOverObjectId < 0)
			WorldRenderer.setMouseInteractionLock(false);
		else if(!WorldRenderer.isMouseInteractionLocked() && mouseOverObjectId >= 0)
			WorldRenderer.setMouseInteractionLock(true);
		
		if(update)
			updatePositions();
		
		
		
		if(draggingWindowId >= 0) {
			if(windows.get(windows.size()-1).getId() != draggingWindowId) {
				if(windows.size() > 1) {
					removeWindow(draggingWindowId);
					addWindow(draggingWindow);
				}
			}
		}
		
		
		
		// Render items
		for(Group group : tileSheet.getTileGroupsList()) {
			if(group.getType() != groupType.swap) {
				for(GuiObject object : group.getTextureObjectList()) {
					Item temp = ((Tile)object).getPuttedItem();
					if(temp != null)  {
						renderGuiObject(temp, shader, window);
					}
				}
			}
		}
		
		for(GuiWindow item : windows) {
			if(item instanceof ItemWindow) {
				for(Group group : ((ItemWindow) item).getTileSheet().getTileGroupsList()) {
					if(group.getType() == groupType.swap) {
						for(GuiObject object : group.getTextureObjectList()) {
							Item temp = ((Tile)object).getPuttedItem();
							if(temp != null && temp.getId() == draggingItemId)  {
								renderGuiObject(temp, shader, window);
							}
						}
					}
				}
			}
		}
		
		// Drag and drop for groups
		for(Group group : tileSheet.getTileGroupsList()) {
			for(GuiObject object : group.getTextureObjectList()) {
				dragAndDrop((Tile)object, window, player);
			}
		}
		

		
		for(Group group : tileSheet.getTileGroupsList()) {
			int index = 0;
			for(GuiObject object : group.getTextureObjectList()) {
				dragAndDrop((Tile)object, window, player);
				
				if(draggingFromObjectId >= 0 && object.getId() == draggingFromObjectId) {
					if(group.getTextureObjectList().size()-1 != index) {
						Collections.swap(group.getTextureObjectList(), index, group.getTextureObjectList().size()-1);
					}
					draggingIndex = outerIndex;
				}
				index++;
			}
			outerIndex++;
		}
		
		if(draggingIndex >= 0) {
			if(tileSheet.getTileGroupsList().size() > 1 && draggingIndex != tileSheet.getTileGroupsList().size()-1) {
				Collections.swap(tileSheet.getTileGroupsList(), draggingIndex, tileSheet.getTileGroupsList().size()-1);
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
				closeArena(player);
			}
		}

		if(setPointer == true)
			window.requestCursor(Cursor.Pointer);
		
		if(windowToRemove != -1)
			this.removeWindow(windowToRemove);
	}
	
	private boolean windowInteraction(GuiWindow item, GuiObject object, Window window) {
		boolean update = false;
		if(object.isOverable() && object.isMovable() && draggingFromObjectId == -1) {
			if(draggingWindowId == item.getId()) {
				if(item.getPosition().x < -(window.getWidth()/2)) {
					item.setPosition(-(window.getWidth()/2)+item.getWidth()/2, item.getPosition().y);
					update = true;
				}
				
				if(window.getCursorPositionX() < item.getWidth()/2) {
					item.setPosition(-(window.getWidth()/2)+item.getWidth()/2, item.getPosition().y);
					update = true;
				} else if(window.getCursorPositionX()+item.getWidth()/2 > borderRight) {
					item.setPosition(borderRight/2-item.getWidth()/2+worldXOffset/2, item.getPosition().y);
					update = true;
				}

				if(item.getPosition().y-item.getHeight()/2 < -(window.getHeight()/2)) {
					item.setPosition(item.getPosition().x, -(window.getHeight()/2)+item.getHeight()/2);
					update = true;
				}
				
				if(window.getCursorPositionY() < 15) {
					item.setPosition(item.getPosition().x, -(window.getHeight()/2)+item.getHeight()/2);
					update = true;
				} else if(window.getCursorPositionY()+item.getHeight()-15 > borderBottom) {
					item.setPosition(item.getPosition().x, borderBottom/2-item.getHeight()/2-worldYOffset/2);
					update = true;
				}
			}
			
			if((mouseOverObjectId == object.getId() && draggingWindowId == -1) || (draggingWindowId >= 0 && draggingWindowId == item.getId())) {
				if(window.getInput().isMouseButtonDown(0)) {
					draggingWindowId = item.getId();
					draggingWindow = item;
					if(item.getStickTo() != null && item.getId() == draggingWindowId) {
						item.setStickTo(null);
						item.setPosition((float)window.getRelativePositionCursorX(), -((float)window.getRelativePositionCursorY()-item.getHeight()/2+15));
					}

					if(window.getCursorPositionX()+item.getWidth()/2 < borderRight && window.getCursorPositionY()+item.getHeight() - 15 < borderBottom  && window.getCursorPositionY()-15 > 0 && window.getCursorPositionX()-item.getWidth()/2 > 0 && item.getId() == draggingWindowId) {
						item.setPosition((float)window.getRelativePositionCursorX(), -((float)window.getRelativePositionCursorY()-item.getHeight()/2+15));
					}
					
					update = true;
				}
			}
			if(draggingWindowId >= 0 && window.getInput().isMouseButtonReleased(0)) {
				draggingWindowId = -1;
				draggingWindow = null;
			}
		} else if(!window.getInput().isMouseButtonDown(0) && draggingWindowId >= 0) {
			draggingWindowId = -1;
			draggingWindow = null;
		}
		
		return update;
	}
	
	private void mouseInteraction(GuiObject object, Window window) {
		if(object.isMouseOver(window, window.getCursorPositionX(), window.getCursorPositionY())) {
			setPointer = false;

			mouseOverObjectId = object.getId();
			
			if(object instanceof Tile)
				mouseOverTileId = object.getId();
				
			if(object.isClickable() && window.getInput().isMouseButtonReleased(0)) {
				clickedObjectId = object.getId();
			}
			
			if(object.isClickable())
				setPointer = true;
		}
	}
	
	private void renderGuiObject(GuiObject object, Shader shader, Window window) {
		if(!(object instanceof TextBlock)) {
			Matrix4f projection = camera.getProjection();
			
			object.update();
			
			if(object.getTexture() != null)
				object.getTexture().bind(0);
			
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", object.getTransform().getProjection(projection));
			model.render();	
		} else {
			TextBlock block = (TextBlock)object;
			block.update();
			
			for(int i = 0; i < block.getLines().size(); i++) {
				Matrix4f projection = camera.getProjection();
				Line line = block.getLines().get(i);
						
				line.getTransform().getPosition().x = block.getTransform().getPosition().x + (float)(line.getWidth())/2;
				line.getTransform().getPosition().y = block.getTransform().getPosition().y + -i*line.getHeight();
						
				line.getTexture().bind(0);
				shader.bind();
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", line.getTransform().getProjection(projection));
				model.render();
			}
		}
	}
	
	private void addToTileSheet(TradeWindow current, Tile object, Window window, TileSheet sheet) {
		if(object.getId() == mouseOverTileId && setObjectId == -1 || setObjectId == object.getId()) {
			if(window.getInput().isMouseButtonPressed(0)) {
				pressedObjId = mouseOverTileId;
			}
			if(window.getInput().isMouseButtonReleased(0)) {
				if(mouseOverTileId == pressedObjId) {
					clickCount++;
					
					if(clickCount >= 2) {
						sheet.putItemFirstEmpty(new Item(object.getPuttedItem()));
						current.update();
						update = true;
						clickCount = 0;
						setObjectId = -1;
					}
				}
			}	
			if(window.getInput().isMouseButtonDown(0)) {
				if(((Tile) object).getPuttedItem() != null && setObjectId == -1) {
					setObjectId = object.getId();
					clickedTime = Timer.getNanoTime();
				}
			}
			
			if(Timer.getDelay(clickedTime, Timer.getNanoTime(), 0.3f) && mouseOverTileId == pressedObjId) {
				if(clickCount == 1) {
					if(StatsContainer.getItemInfo() != null)
						this.removeWindow(StatsContainer.getItemInfo().getId());
					
					
					StatsContainer.setItemInfo(this.getStatsContainer().createItemWindow(((Tile) object).getPuttedItem()));
					
					StatsContainer.getItemInfo().setStickTo(stickTo.BottomRight);
					StatsContainer.getItemInfo().move(-530, 300);
					this.addWindow(StatsContainer.getItemInfo());
					clickCount = 0;
					setObjectId = -1;
				}
			}
		}
		
		if(mouseOverTileId != pressedObjId) {
			clickCount = 0;
		}
		
		if(window.getInput().isMouseButtonReleased(0) && setObjectId != -1 && clickCount == 0)
			setObjectId = -1;
	}
	
	private void dragAndDrop(Tile object, Window window, Player player) {
		if(object != null) {
			if((object.getId() == mouseOverTileId && draggingFromObjectId == -1) || draggingFromObjectId == object.getId() && draggingWindowId == -1) {
				if(object.getId() == mouseOverTileId) {
					if(window.getInput().isMouseButtonPressed(0)) {
						pressedObjectId = mouseOverTileId;
					}
					if(window.getInput().isMouseButtonReleased(0)) {
						if(mouseOverTileId == pressedObjectId) {
							if(!Timer.getDelay(clickedTime, Timer.getNanoTime(), 0.3f))
								clickedTileId = mouseOverTileId;
						}
					}
				}
				if(window.getInput().isMouseButtonDown(0)) {
					if(((Tile) object).getPuttedItem() != null) {
						if(draggingFromObjectId == -1) {
							draggingFromObjectId = object.getId();
							if(object.getPuttedItem() != null)
								draggingItemId = object.getPuttedItem().getId();
							
							clickedTime = Timer.getNanoTime();
						}
						
						if((draggingFromObjectId != mouseOverTileId) || objectDraggedOut == true) {
							object.getPuttedItem().setPosition((float)window.getRelativePositionCursorX(), (float)window.getRelativePositionCursorY());
							objectDraggedOut = true;
						}
					}
				} else if(window.getInput().isMouseButtonReleased(0) && draggingFromObjectId != -1) {
					if(mouseOverTileId != draggingFromObjectId) {
						Tile tile = tileSheet.getTileByObjectId(mouseOverTileId);
						if(tile != null && !tile.isSwap()) {
							try {
								tile.putItem(object.getPuttedItem(), player);
								tile.getPuttedItem().setPosition(tile.getTransform().getPosition().x, tile.getTransform().getPosition().y);
								
								if(object.isSwap() && !tile.isSwap())
									player.getInventory().getItems().add(tile.getPuttedItem());
								
								object.removePuttedItem();
								
								if(tile.getPuttableItemTypes().size() == 1 && (tile.getPuttableItemTypes().get(0) == ItemType.armor || tile.getPuttableItemTypes().get(0) == ItemType.gloves || tile.getPuttableItemTypes().get(0) == ItemType.helm || tile.getPuttableItemTypes().get(0) == ItemType.weapon || tile.getPuttableItemTypes().get(0) == ItemType.shoes)) {
									statsContainer.updatePlayerProperties(this, player);
								} else if(object.getPuttableItemTypes().size() == 1 && (object.getPuttableItemTypes().get(0) == ItemType.armor || object.getPuttableItemTypes().get(0) == ItemType.gloves || object.getPuttableItemTypes().get(0) == ItemType.helm || object.getPuttableItemTypes().get(0) == ItemType.weapon || object.getPuttableItemTypes().get(0) == ItemType.shoes)) {
									statsContainer.updatePlayerProperties(this, player);
								}
							} catch (Exception e) {
								if(e.getMessage() == "consume") {
									player.AddPlayerHP(((Item)object.getPuttedItem()).getItemInfo().getHpRegeneration());
									player.getInventory().removeItemById(object.getPuttedItem().getId());
										
									object.removePuttedItem();
									statsContainer.updatePlayerStats(this, player);
										
								} else if(e.getMessage() == "delete") {
									this.tileSheet.requestDeleteItem(object.getTileId());
									object.getPuttedItem().setPosition(object.getTransform().getPosition().x, object.getTransform().getPosition().y);
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
					draggingItemId = -1;
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

		this.borderRight = window.getWidth()-this.marginRight;
		this.borderBottom = window.getHeight()-this.marginBottom;
		
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
	
	private void setTextBlockStickTo(GuiObject object) {
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
		if(mouseOverObjectId == clickedObjectId)
			return clickedObjectId;
		else return -1;
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
		if(state == State.guiPanel)
			objects.add(0, object);
		else
			objects.add(object.setState(state));
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
		
		if(window instanceof TradeWindow) {
			add = !checkTradeWindows();
		}
		
		if(add) {
			windows.add(window);
			updatePositions();
		}
	}
	
	private boolean checkTradeWindows() {
		for(GuiWindow wind : windows) {
			if(wind instanceof TradeWindow)
				return true;
		}
		 return false;
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
		for(int i = 0; i < windows.size(); i++) {
			if(windows.get(i).getId() == windowId) {
				windows.get(i).setClosed(true);
				windows.remove(i);
				
				if(WorldRenderer.isMouseInteractionLocked())
					WorldRenderer.setMouseInteractionLock(false);
			}
		}
	}
	
	public void showBubble(Line line, double posX, double posY) {
		this.groups.add(bubbleContainer.getBubble(line, posX, posY));
	}
	
	public void showDialog(Dialog dialog, LanguageLoader language) {
		if(dialogContainer.getDialogId() == -1) {
			groups.add(dialogContainer.createDialog(dialog, language));
			updatePositions();
		}
	}
	
	public void closeDialog() {	
		dialogContainer.closeDialog(this);
	}
	
	public boolean isDialogOpen() {
		return dialogContainer.isDialogOpen();
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
	
	public StatsContainer getStatsContainer() {
		return statsContainer;
	}
	
	public void closeArena(Player player) {
		if(player.getIsFightWin()==2) {
			player.getHP().SetMaxHP(player.getHP().getMaxHP());
			statsContainer.updatePlayerStats(this, player);
		}
		arenaContainer.closeArena(this);
		WorldRenderer.setMouseInteractionLock(false);
		
	}
	
	static int getMouseOverTileId() {
		return mouseOverTileId;
	}

	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
		this.borderRight = borderRight-this.marginRight;
	}

	public void setMarginBottom(int marginBottom) {
		this.marginBottom = marginBottom;
		this.borderBottom = borderBottom-this.marginBottom;
	}

	public static int getClickedTileId() {
		return clickedTileId;
	}
	
	public int getWorldXOffset() {
		return worldXOffset;
	}

	public void setWorldXOffset(int worldXOffset) {
		dialogContainer.setDialogXOffset(worldXOffset);
		this.worldXOffset = worldXOffset;
	}

	public int getWorldYOffset() {
		return worldYOffset;
	}

	public void setWorldYOffset(int worldYOffset) {
		dialogContainer.setDialogYOffset(worldYOffset);
		this.worldYOffset = worldYOffset;
	}
}
