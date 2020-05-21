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
import svarog.entity.EntityItem;
import svarog.entity.NPC;
import svarog.entity.Player;
import svarog.gui.ArenaContainer;
import svarog.gui.BubbleContainer;
import svarog.gui.Button;

import svarog.gui.DialogContainer;
import svarog.gui.Group;
import svarog.gui.GuiPanels;
import svarog.gui.GuiRenderer;
import svarog.gui.GuiWindow;
import svarog.gui.StatsContainer;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.TextureObject;
import svarog.gui.Tile;
import svarog.gui.TileSheet;
import svarog.gui.font.Color;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.interactions.Interactions;
import svarog.interactions.PathFinder;
import svarog.io.Timer;
import svarog.io.Window;
import svarog.language.InterfaceTranslations.languages;
import svarog.language.LanguageLoader;
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
	private static GuiPanels panels;
	private static TileSheet tileSheet;
	private static GuiRenderer loadingScreen;
	private static Button questsButton;
	private static Button healBtn;
	private static WorldRenderer worldRenderer;
	private static TextureObject loading_text;
	private static Audio audioPlayer;
	private static GuiWindow questsWindow;
	private static LanguageLoader language;
	private static TextureObject itemWindowBackground;
	
	// Fonts
	private static Font roboto_15;
	private static Font roboto_15_G;
	private static Font roboto_15_Y;
	private static Font roboto_18;
	private static Font roboto_18_R;
	private static Font roboto_18_Y;
	
	// Menu
	private static GuiRenderer menu;
	private static Button menuStartButton;
	private static Button menuExitButton;
	private static Button menuResumeButton;
	private static Button menuLoadButton;
	private static Button menuSaveButton;
	private static Button menuAuthorsButton;
	private static GuiWindow authorsWindow;
	
	
	// Confirm window
	private static GuiWindow confirmWindow;
	private static Button applyButton;
	private static Button cancelButton;
	
	//Path Finder
	private static PathFinder pathFinder;
	
	private static void windowInit() {
		window = new Window();
		window.setSize(1200, 700);
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
		
		if(walk != null) {
			player = new Player(walk, Save.getPlayerParam());
			player.setSpeed(0.2f);
		}
		
		currentWorld = new World(1, 0, 0);
		
		worldRenderer = new WorldRenderer(currentWorld);
		
		Vector2f offset = new Vector2f(350, 70);
		worldRenderer.setWorldOffset(offset);
		/////////////////////////////////////////////////////////////////////////////////////
		
		pathFinder = new PathFinder();
		
		itemWindowBackground = new TextureObject(new Texture("images/window2.png"));
		
		/////// GUI  ////////////////////////////////////////////////////////////////////////
		roboto_18 = new Font("roboto_18", new Color((byte)255, (byte)255, (byte)255));
		roboto_18_Y = new Font("roboto_18", new Color((byte)255, (byte)255, (byte)0));
		roboto_18_R = new Font("roboto_18", new Color((byte)255, (byte)0, (byte)0));
		
		StatsContainer.setLargeFont(roboto_18);
		StatsContainer.setSmallFont(roboto_15);
		
		guiRenderer = new GuiRenderer(window);
		
		guiRenderer.setWorldXOffset(-350);
		guiRenderer.setWorldYOffset(70);
		
		BubbleContainer.setBubbleLeft(Texture.getImageBuffer("images/bubble/left.png"));
		BubbleContainer.setBubbleRight(Texture.getImageBuffer("images/bubble/right.png"));
		BubbleContainer.setBubbleCenter(Texture.getImageBuffer("images/bubble/center.png"));
		
		DialogContainer.setTopDialog(Texture.getImageBuffer("images/dialog/dialog_top.png"));
		DialogContainer.setCenterDialog(Texture.getImageBuffer("images/dialog/dialog_center.png"));
		DialogContainer.setDialogFont(roboto_18);
		DialogContainer.setAnswerFont(roboto_18_Y);
		DialogContainer.setAnswerHoverFont(roboto_18_R);
		
		
		ArenaContainer.setArenaLogBackground(Texture.getImageBuffer("images/arena/log_background.png"));
		ArenaContainer.setArenaImage(Texture.getImageBuffer("images/arena/arena.png"));
		ArenaContainer.setBackgtroundColor(new Color((byte)64, (byte)52, (byte)32));
		
		panels = new GuiPanels();
		panels.addBottomPanel(Texture.getImageBuffer("images/bottom_panel.png"));
		panels.addRightPanel(Texture.getImageBuffer("images/background_right_panel.png"));
		panels.updateDynamicGuiElements(guiRenderer, window);
		
		Group statsStatic = new Group();
		Line HPtext = new Line(GuiRenderer.stickTo.BottomLeft);
		HPtext.setString("HP:", roboto_18);
		HPtext.move(75, -35);

		Line XPtext = new Line(GuiRenderer.stickTo.BottomLeft);
		XPtext.setString("XP:", roboto_18);
		XPtext.move(75, -5);
		
		statsStatic.addTextureObject(XPtext);
		statsStatic.addTextureObject(HPtext);
		
		Group playerStats = new Group();
		playerStats.setStickTo(stickTo.TopRight);
		playerStats.move(-330, -20);
		
		
		TextureObject statBackground = new TextureObject(new Texture("images/stat_background.png"));
		statBackground.move(100, 60);
		Line levelText = new Line(0, 0);
		levelText.setString(language.getValue("levelItem"), roboto_15);
		levelText.move(levelText.getWidth()/2+11, 10);
		
		Line attackText = new Line(0, -25);
		attackText.setString(language.getValue("attackItem"), roboto_15);
		attackText.move(attackText.getWidth()/2+11, 19);
		
		Line defenseText = new Line(0, -50);
		defenseText.setString(language.getValue("defenseItem"), roboto_15);
		defenseText.move(defenseText.getWidth()/2+11, 29);
		
		
		playerStats.addTextureObject(statBackground);
		playerStats.addTextureObject(attackText);
		playerStats.addTextureObject(defenseText);
		playerStats.addTextureObject(levelText);
		
		TextureObject bottomCorner1 = new TextureObject(new Texture("images/corner.png"), GuiRenderer.stickTo.BottomLeft);	
		TextureObject bottomCorner2 = new TextureObject(new Texture("images/corner.png"), GuiRenderer.stickTo.BottomRight);	
		TextureObject bottomBorderRightPanel = new TextureObject(new Texture("images/border_right_panel.png"), GuiRenderer.stickTo.BottomRight);
		bottomBorderRightPanel.move(0, -70);
		TextureObject topBorderRightPanel = new TextureObject(new Texture("images/border_right_panel.png"), GuiRenderer.stickTo.TopRight);
		
		questsButton = new Button(new Texture("images/gui/button_quest.png"), new Texture("images/gui/button_quest_hover.png"), stickTo.TopRight);
		questsButton.move(-25, 10);
		
		healBtn =  new Button(new Texture("images/gui/button_heal.png"), new Texture("images/gui/button_heal_hover.png"), stickTo.TopRight);
		healBtn.move(-25, 70);
		
		
		guiRenderer.setMarginRight(bottomBorderRightPanel.getWidth());
		guiRenderer.setMarginBottom(70);
		
		guiRenderer.addGuiObject(bottomCorner1);
		guiRenderer.addGuiObject(bottomCorner2);
		guiRenderer.addGuiObject(bottomBorderRightPanel);
		guiRenderer.addGuiObject(topBorderRightPanel);
		guiRenderer.addGuiObject(questsButton);
		guiRenderer.addGuiObject(healBtn);
		guiRenderer.addGroup(statsStatic);
		guiRenderer.addGroup(playerStats);
		
	
		guiRenderer.getStatsContainer().updatePlayerStats(guiRenderer, player);
		guiRenderer.getStatsContainer().updatePlayerProperties(guiRenderer, player);
		guiRenderer.getStatsContainer().updatePlayerInventory(guiRenderer, player);
		
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
		tileGroup.move(-25, 110);
		tileGroup.setStickTo(stickTo.BottomRight);		
		
		// Character EQ //
		Group tileGroup3 = new Group();
		tileGroup3.setStickTo(stickTo.BottomRight);
		tileGroup3.move(-25, 470);
		
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
		
		

		for(Item itemT: player.getInventory().getItems()) {
			if(itemT.getItemInfo().getTileID() !=-1 && guiRenderer.getTileSheet().getTile(itemT.getItemInfo().getTileID()).getPuttedItem() == null ) {
				try {
					guiRenderer.getTileSheet().getTile(itemT.getItemInfo().getTileID()).putItem(itemT, player);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				guiRenderer.getTileSheet().putItemFirstEmpty(itemT, player);
			}
		}
		
		confirmWindow = new GuiWindow(language.getValue("deleteItemRequestTitle"), roboto_15, new TextureObject(new Texture("images/window3.png")), false);
		applyButton = new Button(new Texture("images/buttonYes.png"),new Texture("images/buttonYes_hover.png"), new Vector2f(-50, -15));
		cancelButton = new Button(new Texture("images/buttonNo.png"),new Texture("images/buttonNo_hover.png"), new Vector2f(50, -15));
		confirmWindow.addTextureObject(applyButton);
		confirmWindow.addTextureObject(cancelButton);

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
		language = LanguageLoader.getInstance(languages.PL_pl);
		roboto_15 = new Font("roboto_15", new Color((byte)255, (byte)255, (byte)255));
		roboto_15_G  = new Font("roboto_15", new Color((byte)200, (byte)200, (byte)200));
		roboto_15_Y  = new Font("roboto_15", new Color((byte)255, (byte)255, (byte)150));
		
		guiShader = new Shader("shader");
		
		menu = new GuiRenderer(window);
		
		
		TextureObject menuBackground = new TextureObject(new Texture("images/menu/menuBackground.png"));	
		
		menuStartButton = new Button(new Texture("images/menu/start.png"),new Texture("images/menu/start_hover.png"), new Vector2f(0, 190));
		menuResumeButton = new Button(new Texture("images/menu/resume.png"),new Texture("images/menu/resume_hover.png"), new Vector2f(0, 190));
		menuSaveButton = new Button(new Texture("images/menu/save.png"),new Texture("images/menu/save_hover.png"), new Vector2f(0, 65));
		menuLoadButton = new Button(new Texture("images/menu/load.png"),new Texture("images/menu/load_hover.png"), new Vector2f(0, 65));
		menuExitButton = new Button(new Texture("images/menu/exit.png"),new Texture("images/menu/exit_hover.png"), new Vector2f(0, -60));
		menuAuthorsButton = new Button(new Texture("images/menu/authors.png"),new Texture("images/menu/authors_hover.png"), new Vector2f(0, -185));
		
		
		authorsWindow = new GuiWindow("Autorzy", roboto_15, new TextureObject(new Texture("images/window4.png")));
		
		int yPos = authorsWindow.getHeight()/2 - 50;
		Line line1 = new Line(-authorsWindow.getWidth()/2, yPos);
		line1.setString(language.getValue("authorsWindowHeader"), roboto_15);
		line1.move(line1.getWidth()/2 + 10, 0);
		
		yPos -= 20;
		Line line2 = new Line(-authorsWindow.getWidth()/2, yPos);
		line2.setString("- Maciej Pawlak", roboto_15);
		line2.move(line2.getWidth()/2 + 20, 0);
		
		yPos -= 20;
		Line line3 = new Line(-authorsWindow.getWidth()/2, yPos);
		line3.setString("- Dawid Garnczarek", roboto_15);
		line3.move(line3.getWidth()/2 + 20, 0);
		
		yPos -= 20;
		Line line4 = new Line(-authorsWindow.getWidth()/2, yPos);
		line4.setString("- Jakub Go³êbiowski", roboto_15);
		line4.move(line4.getWidth()/2 + 20, 0);
		
		yPos -= 20;
		Line line5 = new Line(-authorsWindow.getWidth()/2, yPos);
		line5.setString("- Patryk Góra", roboto_15);
		line5.move(line5.getWidth()/2 + 20, 0);
		
		Line line6 = new Line(authorsWindow.getWidth()/2, -authorsWindow.getHeight()/2+20);
		line6.setString("Copyright 2020", roboto_15);
		line6.move(-line6.getWidth()/2-10,  0);
		
		authorsWindow.addTextureObject(line1);
		authorsWindow.addTextureObject(line2);
		authorsWindow.addTextureObject(line3);
		authorsWindow.addTextureObject(line4);
		authorsWindow.addTextureObject(line5);
		authorsWindow.addTextureObject(line6);
		
		menu.addGuiObject(menuBackground);
		menu.addGuiObject(menuStartButton);
		menu.addGuiObject(menuExitButton);
		menu.addGuiObject(menuLoadButton);
		menu.addGuiObject(menuAuthorsButton);
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
		WorldLoader.setWorldLoaded(false);
		boolean joinThread = false;
		GuiWindow itemInfo = null;
		
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
            	menu.renderGuiObjects(guiShader, window, player);
            	
            	
            	if(menuStartButton.isClicked() || menuResumeButton.isClicked()) {
            		Save.ReadFrom("MainSave.save");
            		showMenu = false;
            	}
            	
            	if(menuLoadButton.isClicked()) {
            		Save.ReadFrom("Save");
            		showMenu = false;
            	}
            	
        		if(menuSaveButton.isClicked() && player != null) {
        			Save.SaveAs("Save", player, currentWorld);
        			showMenu = false;
        		}
            	
            	if(menuExitButton.isClicked()) {
            		window.closeProgram();
            	}
            	
            	if(menuAuthorsButton.isClicked()) {
            		menu.addWindow(authorsWindow);
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
	                	loadingScreen.renderGuiObjects(guiShader, window, player);

	                	worldInit();
	                	
	                	programInit = false;
	            	} else {
	                	loadingScreen.update(window);
	                	loadingScreen.renderGuiObjects(guiShader, window, player);
	            	}
	            	window.update();
	            	window.swapBuffers(); 
	            	
	            	
	            	if(WorldLoader.isWorldLoaded() == false) {            	
		            	currentWorld = WorldLoader.getWorld(WorldLoader.getNextFrameLoadWorld(), player, camera, window);
		            	currentWorld.start();
		            	worldRenderer.setWorld(currentWorld);
		            	worldRenderer.calculateView(window);
		            	camera.setProjection(window.getWidth(), window.getHeight(), window, WorldRenderer.getScale(), currentWorld.getWidth(), currentWorld.getHeight(), worldRenderer.getWorldOffset());
		            	WorldLoader.setWorldLoaded(true);
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
								name.setString(ent.getName(), roboto_15);
							
								guiRenderer.showBubble(name, window.getRelativePositionCursorX(), window.getRelativePositionCursorY());
							}
						}
					} else {
						if(currentEntityId != -1)
							currentEntityId = -1;
					}
					

					for(int i=0; i < currentWorld.numberOfEntities() ; i++) { // this is nicer implementation. I've added methods to world to remove entity.
						Entity entity = currentWorld.getEntity(i);
						if(entity.isClicked()) {
							if(entity instanceof Enemy) {
								player.fightShow(guiRenderer, player, (Enemy)entity, currentWorld, roboto_18);
								guiRenderer.getStatsContainer().updatePlayerStats(guiRenderer, player);
								guiRenderer.getStatsContainer().updatePlayerInventory(guiRenderer, player);
	
							} else if(entity instanceof NPC && ((NPC)entity).getInteractions() != null) {
								((NPC)entity).getInteractions().ChceckInteractions(worldRenderer, camera, window, guiRenderer, player, entity.getId(), language);
								Interactions.setTalkingNPCid(i);
							} else if(entity instanceof EntityItem) {
								currentWorld.removeAndRespawn(entity);
								for(Item item : ((EntityItem)entity).getLoot()) {
									player.addItemToInventoryWithGUIupdate(new Item(item), guiRenderer);
								}
							}
						}
					}

					if(Interactions.getTalkingNPCid() != -1){
						
						((NPC)currentWorld.getEntity(Interactions.getTalkingNPCid())).getInteractions().ChceckInteractions(worldRenderer, camera, window, guiRenderer, player, currentWorld.getEntity(Interactions.getTalkingNPCid()).getId(), language);
					}
					if(DialogContainer.isDialogClosing()) {
						((NPC)currentWorld.getEntity(Interactions.getTalkingNPCid())).getInteractions().setEnded(true);
						Interactions.setTalkingNPCid(-1);
					}
	
					guiRenderer.renderGuiObjects(guiShader, window, player);

					Tile tempTile = tileSheet.clickedTile();
					if(tempTile != null) {
						Item tempItem = tempTile.getPuttedItem();
						if(tempItem != null) {
							if(itemInfo != null)
								guiRenderer.removeWindow(itemInfo.getId());
							
							
							itemInfo = guiRenderer.getStatsContainer().createItemWindow(tempItem, language, itemWindowBackground);
							
							itemInfo.setStickTo(stickTo.BottomRight);
							itemInfo.move(-530, 300);
							guiRenderer.addWindow(itemInfo);
						}
					}
					
					
					if(questsButton.isClicked()) {
						if(questsWindow != null) {
							if(questsWindow.isClosed()) {
								questsWindow = player.getQuestsPagedOnGUI(roboto_15, roboto_15_G, roboto_15_Y, language);
								guiRenderer.addWindow(questsWindow);
							}
						} else {
							questsWindow = player.getQuestsPagedOnGUI(roboto_15, roboto_15_G, roboto_15_Y, language);
							guiRenderer.addWindow(questsWindow);
						}
					}
					
					if(healBtn.isClicked()) {
						player.FullyRecoverHP();
						//player.setMovement(player.movePlayer(65, true));
						pathFinder.reset();
						pathFinder.movePlayer(player);
						pathFinder.setIsWorking(1);
						System.out.println("Health of player was fully recovered: " + player.getHP().GetHP() + "hp.");
						guiRenderer.getStatsContainer().updatePlayerStats(guiRenderer, player);
						player.addItemToInventoryWithGUIupdate(new Item(Save.getItemById(20)), guiRenderer);
						//guiRenderer.addWindow(confirmWindow);
					}
					if(window.getInput().isMouseButtonReleased(0) && GuiRenderer.getMouseOverObjectId() == -1 ) {
						System.out.println(worldRenderer.getMouseOverX()+" "+worldRenderer.getMouseOverY());
					}
					if(window.getInput().isMouseButtonReleased(0) && GuiRenderer.getMouseOverObjectId() == -1 && WorldRenderer.getMouseOverEntityId()==-1) {
						//pathFinder.stupidMover(worldRenderer, player);
					}
					//System.out.println(window.getCursorPositionX() + "  " + window.getCursorPositionY());
					//System.out.println(worldRenderer.getMouseOverX() + "  " + worldRenderer.getMouseOverY());
					if(pathFinder.getIsWorking()==1) {
						pathFinder.movePlayer(player);
					}
					// confirm window
					if(cancelButton.isClicked()) {
						guiRenderer.getTileSheet().cancelRemoveItem();
						guiRenderer.removeWindow(confirmWindow.getId());
					}
					
					if(applyButton.isClicked()) {
						if(tileSheet.itemToDelete() >= 0) {
							Tile temp = guiRenderer.getTileSheet().getTile(guiRenderer.getTileSheet().itemToDelete());

							player.getInventory().removeItemById(temp.getPuttedItem().getId());
							guiRenderer.getTileSheet().removeItem(temp);
							guiRenderer.removeWindow(confirmWindow.getId());
						}
					}
					
					if(tileSheet.itemToDelete() >= 0) {
						guiRenderer.addWindow(confirmWindow);
					}
					

					
					
					for(int i = 0; i < currentWorld.numberOfDoors(); i++) {
						if(currentWorld.getPlayer().getPositionX() == currentWorld.getDoor(i).getPositionX() && currentWorld.getPlayer().getPositionY() == currentWorld.getDoor(i).getPositionY()) {
							player.setPosition(currentWorld.getDoor(i).getDestinationX(), currentWorld.getDoor(i).getDestinationY());
							player.setSetCamWithoutAnimation(true);
							WorldLoader.setNextFrameLoadWorld(currentWorld.getDoor(i).getWorldIdDestination());
							WorldLoader.setWorldLoaded(false);
							break;
						} else {
							WorldLoader.setNextFrameLoadWorld(0);
						}
					}
					
					if(ArenaContainer.isArenaClosing()) {
						player.setMovementLock(false);
						
						if(player.getIsFightWin() == 2) {
							player.setSetCamWithoutAnimation(true);
							player.setPosition(Save.getPlayerParam().getPositionX(), Save.getPlayerParam().getPositionY());
							if(WorldLoader.getLoadedWorldId() != 1) {
								WorldLoader.setNextFrameLoadWorld(1);
								WorldLoader.setWorldLoaded(false);
							}
						}
						player.setIsFightWin(0);
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
