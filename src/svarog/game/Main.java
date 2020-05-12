package svarog.game;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;

import svarog.audio.Audio;
import svarog.audio.Sound;
import svarog.entity.Enemy;
import svarog.entity.Entity;
import svarog.entity.NPC;
import svarog.entity.Player;
import svarog.gui.ArenaContainer;
import svarog.gui.BubbleContainer;
import svarog.gui.Button;

import svarog.gui.DialogContainer;
import svarog.gui.Group;
import svarog.gui.GuiPanels;
import svarog.gui.GuiRenderer;
import svarog.gui.StatsContainer;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.TextureObject;
import svarog.gui.Tile;
import svarog.gui.TileSheet;
import svarog.gui.font.Color;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.interactions.Interactions;
import svarog.io.Timer;
import svarog.io.Window;
import svarog.objects.Item;
import static svarog.objects.ItemInfo.ItemType;
import svarog.render.Animation;
import svarog.render.Camera;
import svarog.render.Shader;
import svarog.render.Texture;
import svarog.save.Save;
import svarog.world.World;
import svarog.world.WorldRenderer;


public class Main {
	private static Window window;
	private static Shader shader;
	private static Camera camera;
	private static Player player;
	private static World currentWorld;
	private static Shader guiShader;
	private static GuiRenderer guiRenderer;
	private static Font verdana;
	private static Font pressStart;
	private static Font pressStartY;
	private static Font pressStartR;
	private static GuiPanels panels;
	private static TileSheet tileSheet;
	private static GuiRenderer loadingScreen;
	private static Button questsButton;
	private static Button healBtn;
	private static WorldRenderer worldRenderer;
	private static TextureObject loading_text;
	private static Audio audioPlayer;
	
	// Menu
	private static GuiRenderer menu;
	private static Button menuStartButton;
	private static Button menuExitButton;
	private static Button menuResumeButton;
	private static Button menuLoadButton;
	private static Button menuSaveButton;
	
	private static void windowInit() {
		window = new Window();
		window.setSize(1200, 800);
		window.createWindow("Svarog"); 										// Creating window "Svarog"
		window.glInit();
	}

	
	private static void worldInit() {	
		//////////////// WORLD ///////////////////////////////////////////////////////////////
		shader = new Shader("shader");
		camera = new Camera();

		audioPlayer = new Audio();
		
		Sound walk = null;
		try {
			walk = new Sound("walk01.ogg", true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(walk != null)
			player = new Player(walk, Save.getPlayerParam());

		
		currentWorld = new World(1, 0, 0);
		
		worldRenderer = new WorldRenderer(currentWorld);
		
		Vector2f offset = new Vector2f(350, 70);
		worldRenderer.setWorldOffset(offset);
		/////////////////////////////////////////////////////////////////////////////////////
		
		
		/////// GUI  ////////////////////////////////////////////////////////////////////////
		verdana = new Font("verdana_20", new Color((byte)255, (byte)255, (byte)0));
		pressStart = new Font("font", new Color((byte)255, (byte)255, (byte)255));
		pressStartY = new Font("font", new Color((byte)255, (byte)255, (byte)0));
		pressStartR = new Font("font", new Color((byte)255, (byte)0, (byte)0));
		
		StatsContainer.setFont(verdana);
		
		guiRenderer = new GuiRenderer(window);
		
		
		BubbleContainer.setBubbleLeft(Texture.getImageBuffer("images/bubble/left.png"));
		BubbleContainer.setBubbleRight(Texture.getImageBuffer("images/bubble/right.png"));
		BubbleContainer.setBubbleCenter(Texture.getImageBuffer("images/bubble/center.png"));
		
		DialogContainer.setTopDialog(Texture.getImageBuffer("images/dialog/dialog_top.png"));
		DialogContainer.setCenterDialog(Texture.getImageBuffer("images/dialog/dialog_center.png"));
		DialogContainer.setDialogFont(pressStart);
		DialogContainer.setAnswerFont(pressStartY);
		DialogContainer.setAnswerHoverFont(pressStartR);
		
		
		ArenaContainer.setArenaLogBackground(Texture.getImageBuffer("images/arena/log_background.png"));
		ArenaContainer.setArenaImage(Texture.getImageBuffer("images/arena/arena.png"));
		ArenaContainer.setBackgtroundColor(new Color((byte)64, (byte)52, (byte)32));
		
		panels = new GuiPanels();
		panels.addBottomPanel(Texture.getImageBuffer("images/bottom_panel.png"));
		panels.addRightPanel(Texture.getImageBuffer("images/background_right_panel.png"));
		panels.updateDynamicGuiElements(guiRenderer, window);
		
		Group statsStatic = new Group();
		Line HPtext = new Line(GuiRenderer.stickTo.BottomLeft);
		HPtext.setString("HP:", verdana);
		HPtext.move(75, -35);
		
		
		Line XPtext = new Line(GuiRenderer.stickTo.BottomLeft);
		XPtext.setString("XP:", verdana);
		XPtext.move(75, -5);
		
		statsStatic.addTextureObject(XPtext);
		statsStatic.addTextureObject(HPtext);
		
		guiRenderer.updatePlayerStats(player);
		
		TextureObject bottomCorner1 = new TextureObject(new Texture("images/corner.png"), GuiRenderer.stickTo.BottomLeft);	
		TextureObject bottomCorner2 = new TextureObject(new Texture("images/corner.png"), GuiRenderer.stickTo.BottomRight);	
		TextureObject bottomBorderRightPanel = new TextureObject(new Texture("images/border_right_panel.png"), GuiRenderer.stickTo.BottomRight);
		bottomBorderRightPanel.move(0, -70);
		TextureObject topBorderRightPanel = new TextureObject(new Texture("images/border_right_panel.png"), GuiRenderer.stickTo.TopRight);
		
		questsButton = new Button(new Texture("images/gui/button_quest.png"), new Texture("images/gui/button_quest_hover.png"), stickTo.TopRight);
		questsButton.move(-250, 10);
		
		healBtn =  new Button(new Texture("images/gui/button_heal.png"), new Texture("images/gui/button_heal_hover.png"), stickTo.TopRight);
		healBtn.move(-25, 10);
		
		guiRenderer.addGuiObject(bottomCorner1);
		guiRenderer.addGuiObject(bottomCorner2);
		guiRenderer.addGuiObject(bottomBorderRightPanel);
		guiRenderer.addGuiObject(topBorderRightPanel);
		guiRenderer.addGuiObject(questsButton);
		guiRenderer.addGuiObject(healBtn);
		guiRenderer.addGroup(statsStatic);
		
	
		
		/// Tiles on GUI ///////////////////////////
		tileSheet = new TileSheet();
		Texture tileTexture = new Texture("images/guiTile.png");
		Texture tileTexture_hover = new Texture("images/guiTile_hover.png");
		
		// Tiles to character EQ
		Texture tileHelmetTexture = new Texture("images/eqTile/helmetTile.png");
		Texture tileHelmetTexture_hover = new Texture("images/eqTile/helmetTile_hover.png");
		
		Texture tileArmorTexture = new Texture("images/eqTile/armorTile.png");
		Texture tileArmorTexture_hover = new Texture("images/eqTile/armorTile_hover.png");
		
		Texture tileBootsTexture = new Texture("images/eqTile/bootsTile.png");
		Texture tileBootsTexture_hover = new Texture("images/eqTile/bootsTile_hover.png");
		
		Texture tileSwordTexture = new Texture("images/eqTile/swordTile.png");
		Texture tileSwordTexture_hover = new Texture("images/eqTile/swordTile_hover.png");
		
		Texture tileGlovesTexture = new Texture("images/eqTile/glovesTile.png");
		Texture tileGlovesTexture_hover = new Texture("images/eqTile/glovesTile_hover.png");
		
		Texture tileTrashTexture = new Texture("images/trash.png"); 
		
		int tileId = 0;
		
		// Main EQ //
		Group tileGroup = new Group();
		tileGroup.move(-25, 150);
		tileGroup.setStickTo(stickTo.BottomRight);		
		
		// Character EQ //
		Group tileGroup3 = new Group();
		tileGroup3.setStickTo(stickTo.BottomRight);
		tileGroup3.move(-25, 545);
		
		Tile helmet = new Tile(tileId++, tileHelmetTexture, tileHelmetTexture_hover, (byte)0, 0, 50);
		helmet.setPuttableItemTypes(Arrays.asList(ItemType.helm)); // item type 1
		
		Tile armor = new Tile(tileId++, tileArmorTexture, tileArmorTexture_hover, (byte)0, 0, 0);
		armor.setPuttableItemTypes(Arrays.asList(ItemType.armor));
		
		Tile boots = new Tile(tileId++, tileBootsTexture, tileBootsTexture_hover, (byte)0, 0, -50);
		boots.setPuttableItemTypes(Arrays.asList(ItemType.shoes));
		
		Tile sword = new Tile(tileId++, tileSwordTexture, tileSwordTexture_hover, (byte)0, -50, 0);
		sword.setPuttableItemTypes(Arrays.asList(ItemType.weapon));
		
		Tile gloves = new Tile(tileId++, tileGlovesTexture, tileGlovesTexture_hover, (byte)0, 50, 0);
		gloves.setPuttableItemTypes(Arrays.asList(ItemType.gloves));
		
		tileGroup3.addTextureObject(helmet);
		tileGroup3.addTextureObject(armor);
		tileGroup3.addTextureObject(boots);
		tileGroup3.addTextureObject(gloves);
		tileGroup3.addTextureObject(sword);
		
		tileSheet.addTileGroup(tileGroup3);
		
		// Trash //
		Group trashG = new Group();
		trashG.setStickTo(stickTo.BottomRight);
		trashG.move(20, 120);
		
		Tile trash = new Tile(tileId++, tileTrashTexture, tileTrashTexture, (byte)0, 0, 0);
		trash.setPuttableItemTypes(Arrays.asList(ItemType.trash));
		
		trashG.addTextureObject(trash);
		
		tileSheet.addTileGroup(trashG);
		//////////////////
		
		List<ItemType> puttables = Arrays.asList(ItemType.values()); // main eq - there should be every item type

		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 5; j++) {
				Tile tile = new Tile(tileId++, tileTexture, tileTexture_hover, (byte)0, j*50, -i*50);
				tile.setPuttableItemTypes(puttables);
				
				tileGroup.addTextureObject(tile);
			}
		}

		tileSheet.addTileGroup(tileGroup);
		////////////
		
		
		// Bottom bar eq //
		Group tileGroup2 = new Group();
		tileGroup2.setStickTo(stickTo.Bottom);
		tileGroup2.move(0, 10);
		for(int i = 0; i < 6; i++) {
				Tile tile = new Tile(tileId++, tileTexture, tileTexture_hover, (byte)0, i*50, 0);
				tile.setPuttableItemTypes(puttables); // bottom bar tiles - idk what there should be
				tileGroup2.addTextureObject(tile);
		}
		
		tileSheet.addTileGroup(tileGroup2);
		///////////////////
	
		
		////////////////////////////////////////////
		
		guiRenderer.setTileSheet(tileSheet);
		
		guiRenderer.updatePositions();
		
		//Inventory inventory = new Inventory(itemsT);

		for(Item itemT: player.getInventory().getItems()) {
			if(itemT.getItemInfo().getTileID() !=-1 && guiRenderer.getTileSheet().getTile(itemT.getItemInfo().getTileID()).getPuttedItem() == null ) {
				try {
					guiRenderer.getTileSheet().getTile(itemT.getItemInfo().getTileID()).putItem(itemT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				guiRenderer.getTileSheet().putItemFirstEmpty(itemT);
			}
		}

		////////////////////////////////////////////////////////////////////////////////////
	}

	private static void loadingScreen() {
		guiShader = new Shader("shader");
	
		////////// LOADING SCREEN //////////////////////////////////////////////////////////
		loadingScreen = new GuiRenderer(window);
		
		TextureObject background = new TextureObject(new Texture("textures/loading_screen.png"));	
		loading_text = new TextureObject(new Animation(4, 5, "loading/loading"));
		loadingScreen.addGuiObject(background);
		loadingScreen.addGuiObject(loading_text);
		////////////////////////////////////////////////////////////////////////////////////
	}
	
	private static void menuInit() {
		guiShader = new Shader("shader");
		
		menu = new GuiRenderer(window);
		
		
		TextureObject menuBackground = new TextureObject(new Texture("images/menu/menuBackground.png"));	
		
		menuStartButton = new Button(new Texture("images/menu/start.png"),new Texture("images/menu/start_hover.png"), new Vector2f(0, 130));
		menuResumeButton = new Button(new Texture("images/menu/resume.png"),new Texture("images/menu/resume_hover.png"), new Vector2f(0, 130));
		menuSaveButton = new Button(new Texture("images/menu/save.png"),new Texture("images/menu/save_hover.png"), new Vector2f(0, 0));
		menuLoadButton = new Button(new Texture("images/menu/load.png"),new Texture("images/menu/load_hover.png"), new Vector2f(0, 0));
		menuExitButton = new Button(new Texture("images/menu/exit.png"),new Texture("images/menu/exit_hover.png"), new Vector2f(0, -130));
		
		menu.addGuiObject(menuBackground);
		menu.addGuiObject(menuStartButton);
		menu.addGuiObject(menuExitButton);
		menu.addGuiObject(menuLoadButton);
	}
	
	private static void pauseGame() {	
		if(menu != null) {
			menu.removeGuiObject(menuStartButton);
			menu.removeGuiObject(menuLoadButton);
			
			menu.addGuiObject(menuResumeButton);
			menu.addGuiObject(menuSaveButton);
		}
	}
	
	public static void main(String[] args) {
		///////////////// INIT ///////////////////////////////////////////////////////////////
		Window.setCallbacks();
		if(!glfwInit()) { 													// Library init
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		//////////////////////////////////////////////////////////////////////////////////////
		
		windowInit();
		
		/////////////////////// LOCAL VARIABLES ////////////////////////////////////////////
		long lastNanos = Timer.getNanoTime();
		int currentEntityId = -1;
		long startNanos = 0;
		boolean programInit = true;
		long start = -1;
		boolean worldLoaded = false;
		boolean joinThread = false;
		
		boolean showMenu = true;
		////////////////////////////////////////////////////////////////////////////////////
		
		while(window.processProgram()) {										// This works while program is running
			Timer.syncFrameRate(60, lastNanos);									// Fps limiter
            lastNanos = Timer.getNanoTime();
            
            if(showMenu == true) {
            	glClear(GL_COLOR_BUFFER_BIT);
            	glClearColor(0.2352f, 0.2078f, 0.1607f, 1f); // <= tutaj kolor tla
            	
            	if(menu == null) 
            		menuInit();
            	
            	if(player != null && audioPlayer != null)
            		if(audioPlayer.isPlaying(player.getWalkSound()))
            				audioPlayer.stop(player.getWalkSound());
            	 	
            	menu.update(window);
            	menu.renderGuiObjects(guiShader, window, player, verdana);
            	
            	
            	if(menuStartButton.isClicked() || menuResumeButton.isClicked()) {
            		Save.ReadFrom("MainSave.save", player);
            		showMenu = false;
            	}
            	
            	if(menuLoadButton.isClicked()) {
            		Save.ReadFrom("Save", player);
            		showMenu = false;
            	}
            	
        		if(menuSaveButton.isClicked() && player != null) {
        			Save.SaveAs("Save", player, currentWorld);
        			showMenu = false;
        		}
            	
            	if(menuExitButton.isClicked()) {
            		window.closeProgram();
            	}
            	
            	

            	window.update();
            	window.swapBuffers(); 
            	
            	if(window.hasResized())
            		glViewport(0, 0, window.getWidth(), window.getHeight());
            	
            } else if(showMenu == false) {
	            if(WorldLoader.getNextFrameLoadWorld() != 0) {
	            	glClear(GL_COLOR_BUFFER_BIT);
	            	glClearColor(0f, 0f, 0f, 1f);
	            	
	              	if(player != null && audioPlayer != null)
	            		if(audioPlayer.isPlaying(player.getWalkSound()))
	            				audioPlayer.stop(player.getWalkSound());
	              	
	                if(start == -1) {
	                	start = Timer.getNanoTime();
	                	
	                	if(loading_text != null)
	                		((Animation)loading_text.getTexture()).resetLastTime();
	                }
	                
	            	if(programInit == true) {		
	            		loadingScreen();
	            		
	                	loadingScreen.update(window);
	                	loadingScreen.renderGuiObjects(guiShader, window, player, verdana);

	                	worldInit();
	                	
	                	programInit = false;
	            	} else {
	                	loadingScreen.update(window);
	                	loadingScreen.renderGuiObjects(guiShader, window, player, verdana);
	            	}
	            	window.update();
	            	window.swapBuffers(); 
	            	
	            	
	            	if(worldLoaded == false) {            	
		            	currentWorld = WorldLoader.getWorld(WorldLoader.getNextFrameLoadWorld(), player, camera, window);
		            	currentWorld.start();
		            	worldRenderer.setWorld(currentWorld);
		            	worldRenderer.calculateView(window);
		            	camera.setProjection(window.getWidth(), window.getHeight(), window, WorldRenderer.getScale(), currentWorld.getWidth(), currentWorld.getHeight(), worldRenderer.getWorldOffset());
		            	worldLoaded = true;
		            	joinThread = true;
	            	}
	            	
	            	long stop = Timer.getNanoTime();
	            	
	            	if(Timer.getDelay(start, stop, 1)) {
		            	WorldLoader.setNextFrameLoadWorld(0);
		            	start = -1;
	            	}
	            	
	            	if(window.hasResized())
	            		glViewport(0, 0, window.getWidth(), window.getHeight());
	            } else {
	            	if(joinThread == true) {
		        		try {
		        			currentWorld.join(worldRenderer);
		        		} catch (InterruptedException e) {
		        			e.printStackTrace();
		        		}
	
		        		joinThread = false;
	            	}
	        		
	            	
					glClearColor(0.2f, 0.2f, 0.2f, 1f);
					if(window.hasResized()) {
						window.checkSize();
						camera.setProjection(window.getWidth(), window.getHeight(), window, WorldRenderer.getScale(), currentWorld.getWidth(), currentWorld.getHeight(), worldRenderer.getWorldOffset());
						guiRenderer.deleteGuiPanels();
						guiRenderer.deleteGuiPanelGroups();
						panels.updateDynamicGuiElements(guiRenderer, window);
						guiRenderer.updateAfterResize(window);
						
						worldRenderer.calculateView(window);
						glViewport(0, 0, window.getWidth(), window.getHeight());
					}
					glClear(GL_COLOR_BUFFER_BIT);
					
					
					guiRenderer.deleteDynamicGroups();
					
					worldRenderer.update((float)0.2, window, camera, audioPlayer);
					worldRenderer.correctCamera(camera, window);							// This sets correct camera position on world
	
					
	
						
					worldRenderer.render(shader, camera, window);							// world rendering
					
					
					
					if(WorldRenderer.getMouseOverEntityId() >= 0) {
						if(WorldRenderer.getMouseOverEntityId() != currentEntityId) {
							startNanos = Timer.getNanoTime();
							currentEntityId = WorldRenderer.getMouseOverEntityId();
						}
						
						if(Timer.getDelay(startNanos, Timer.getNanoTime(), 0.4)) {
							Line name = new Line(0, 0);
							Entity ent = currentWorld.getEntityById(WorldRenderer.getMouseOverEntityId());
							if(ent != null) {
								name.setString(ent.getName(), pressStart);
							
								guiRenderer.showBubble(name, window.getRelativePositionCursorX(), window.getRelativePositionCursorY());
							}
						}
					} else {
						if(currentEntityId != -1)
							currentEntityId = -1;
					}
					
					
					for(int i=0; i < currentWorld.numberOfEntities() - 1 ; i++) { // this is nicer implementation. I've added methods to world to remove entity.
						if(currentWorld.getEntity(i).isClicked()) {
							if(currentWorld.getEntity(i) instanceof Enemy) {
	
								player.fightShow(guiRenderer, player, (Enemy)currentWorld.getEntity(i), currentWorld, pressStart);
								guiRenderer.updatePlayerStats(player);
	
							}if(currentWorld.getEntity(i) instanceof NPC && ((NPC)currentWorld.getEntity(i)).getInteractions() != null) {
								((NPC)currentWorld.getEntity(i)).getInteractions().ChceckInteractions(worldRenderer, camera, window, guiRenderer, player, currentWorld.getEntity(i).getId());
								Interactions.setTalkingNPCid(i);
							}
						}
					}
					if(Interactions.getTalkingNPCid() != -1){
						
						((NPC)currentWorld.getEntity(Interactions.getTalkingNPCid())).getInteractions().ChceckInteractions(worldRenderer, camera, window, guiRenderer, player, currentWorld.getEntity(Interactions.getTalkingNPCid()).getId());
					}
					if(DialogContainer.isDialogClosing()) {
						((NPC)currentWorld.getEntity(Interactions.getTalkingNPCid())).getInteractions().setEnded(true);
						Interactions.setTalkingNPCid(-1);
					}
					
					guiRenderer.renderGuiObjects(guiShader, window, player, verdana);
					
					if(questsButton.isClicked())
						guiRenderer.addWindow(player.getQuestsPagedOnGUI(pressStart));
					
					if(healBtn.isClicked()) {
						player.FullyRecoverHP();
						System.out.println("Health of player was fully recovered: " + player.getHP().GetHP() + "hp.");
						guiRenderer.updatePlayerStats(player);
						player.addItemToInventoryWithGUIupdate(new Item(new Texture("textures/helmet.png"), Save.getItemById(1).getItemInfo(), ItemType.helm), guiRenderer);
					}
					
					
					for(int i = 0; i < currentWorld.numberOfDoors(); i++) {
						if(currentWorld.getPlayer().getPositionX() == currentWorld.getDoor(i).getPositionX() && currentWorld.getPlayer().getPositionY() == currentWorld.getDoor(i).getPositionY()) {
							player.setPosition(currentWorld.getDoor(i).getDestinationX(), currentWorld.getDoor(i).getDestinationY());
							player.setSetCamWithoutAnimation(true);
							WorldLoader.setNextFrameLoadWorld(currentWorld.getDoor(i).getWorldIdDestination());
							worldLoaded = false;
							break;
						} else 
							WorldLoader.setNextFrameLoadWorld(0);
					}
					
					window.update();
					
					if(window.getInput().isKeyDown(GLFW_KEY_ESCAPE)) {
						showMenu = true;
						pauseGame();
					}
					
					window.swapBuffers();
	            }
            }
		}
		
		try {
			if(audioPlayer != null)
				audioPlayer.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		glfwTerminate();
	}
}
