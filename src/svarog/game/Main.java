package svarog.game;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.entity.Enemy;
import svarog.entity.Entity;
import svarog.entity.Player;
import svarog.gui.Arena;
import svarog.gui.ArenaContainer;
import svarog.gui.BubbleContainer;
import svarog.gui.Button;
import svarog.gui.Dialog;
import svarog.gui.DialogContainer;
import svarog.gui.Group;
import svarog.gui.GuiPanels;
import svarog.gui.GuiRenderer;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.PagedGuiWindow;
import svarog.gui.PagedGuiWindow.Type;
import svarog.gui.TextureObject;
import svarog.gui.Tile;
import svarog.gui.TileSheet;
import svarog.gui.font.Color;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.interactions.Interactions;
import svarog.io.Timer;
import svarog.io.Window;
import svarog.objects.Item;
import svarog.objects.ItemInfo;
import svarog.render.Animation;
import svarog.render.Camera;
import svarog.render.Shader;
import svarog.render.Texture;
import svarog.render.Transform;
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
	private static Button button1;
	private static Button button2;
	private static Button healBtn;
	private static PagedGuiWindow quests;
	private static WorldRenderer worldRenderer;
	private static TextureObject loading_text;
	
	//JG GLOBLA VARIABLES
	public static int ans1 = 0;
	public static Dialog dialog = null;
	public static Dialog dialog1 = null;
	
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

		

		
		player = new Player(0, "player/mavak/", "mavak", new Transform().setPosition(40, 25), false);
		player.setName("Ty");
		player.setHpXpAttack(100, 0, 50, 60);
		
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
		ArenaContainer.setBackgtroundColor(new Color((byte)141, (byte)88, (byte)50));
		
		panels = new GuiPanels();
		panels.addBottomPanel(Texture.getImageBuffer("images/bottom_panel.png"));
		panels.addRightPanel(Texture.getImageBuffer("images/background_right_panel.png"));
		panels.updateDynamicGuiElements(guiRenderer, window);
		
		Group group1 = new Group();
		Line test1 = new Line(GuiRenderer.stickTo.BottomLeft);
		test1.setString("Tekst w innym miejscu", verdana);
		test1.move(95, -25);

		TextBlock test = new TextBlock(400, stickTo.TopLeft);
		test.setString(verdana, "12 asê jsajhdkjs sdsadsa sad asdsadhjs dksfjlskdjflksdj flkjlkjdflsdjfljdslkj jjkdj lfjsldfjldksjj fklkdsjfl ksjdlfk");
		test.move(15, 15);
		group1.addTextBlock(test);
		group1.addTextureObject(test1);
		
		TextureObject bottomCorner1 = new TextureObject(new Texture("images/corner.png"), GuiRenderer.stickTo.BottomLeft);	
		TextureObject bottomCorner2 = new TextureObject(new Texture("images/corner.png"), GuiRenderer.stickTo.BottomRight);	
		TextureObject bottomBorderRightPanel = new TextureObject(new Texture("images/border_right_panel.png"), GuiRenderer.stickTo.BottomRight);
		bottomBorderRightPanel.move(0, -70);
		TextureObject topBorderRightPanel = new TextureObject(new Texture("images/border_right_panel.png"), GuiRenderer.stickTo.TopRight);
		
		button1 = new Button(new Texture("images/button.png"), new Texture("images/button_hover.png"), stickTo.TopRight);
		button1.move(-100, 100);
		
		button2 = new Button(new Texture("images/button.png"), new Texture("images/button_hover.png"), stickTo.TopRight);
		button2.move(-100, 180);
		
		healBtn = new Button(new Texture("images/button.png"), stickTo.TopRight);
		healBtn.move(-100, 250);
		
		guiRenderer.addGuiObject(bottomCorner1);
		guiRenderer.addGuiObject(bottomCorner2);
		guiRenderer.addGuiObject(bottomBorderRightPanel);
		guiRenderer.addGuiObject(topBorderRightPanel);
		guiRenderer.addGuiObject(button1);
		guiRenderer.addGuiObject(button2);
		guiRenderer.addGuiObject(healBtn);
		guiRenderer.addGroup(group1);
		
		
		/// Windows on GUI /////////////////////////
		quests = new PagedGuiWindow("Questy", pressStart, new TextureObject(new Texture("images/window1.png")));
		quests.setStickTo(stickTo.TopRight);
		quests.move(-520, -275);
		
		
		TextBlock topic = new TextBlock(280, new Vector2f());
		topic.setString(pressStart, "Nag³ówek");
		
		quests.addTextBlock(topic, Type.headline);
		
		TextBlock blck1 = new TextBlock(280, new Vector2f());
		blck1.setString(pressStart, "test test 12 asê jsajhdkjs sdsadsa sad asdsadhjs dksfjlskdjflksdj flkjlkjdflsdjfljdslkj jjkdj lfjsldfjldksjj ");
		
		quests.addTextBlock(blck1, Type.content);
		
		TextBlock blck2 = new TextBlock(280, new Vector2f());
		blck2.setString(pressStart, "1111 test test 12 asê jsajhdkjs sdsadsa sad asdsadhjs dksfjlskdjflksdj flkjlkjdflsdjfljdslkj jjkdj ");
		
		quests.addTextBlock(blck2, Type.normal);
		
		TextBlock blck3 = new TextBlock(280, new Vector2f());
		blck3.setString(pressStart, "1111 test test 12 asê jsajhdkjs sdsadsa sad asdsadhjs dksfjlskdjflksdj flkjlkjdflsdjfljdslkj jjkdj ");
		
		quests.addTextBlock(blck3, Type.normal);
		
		TextBlock topic2 = new TextBlock(280, new Vector2f());
		topic2.setString(pressStart, "Nag³ówek1");
		quests.addTextBlock(topic2, Type.headline);
		
		TextBlock blck4 = new TextBlock(280, new Vector2f());
		blck4.setString(pressStart, "11113 test test 12 asê jsajhdkjs sdsadsa sad asdsadhjs dksfjlskdjflksdj flkjlkjdflsdjfljdslkj jjkdj ");
		
		quests.addTextBlock(blck4, Type.content);
		
		TextBlock blck5 = new TextBlock(280, new Vector2f());
		blck5.setString(pressStart, "1234 test test 12 asê jsajhdkjs sdsadsa sad asdsadhjs dksfjlskdjflksdj flkjlkjdflsdjfljdslkj jjkdj ");
		
		quests.addTextBlock(blck5, Type.normal);
		
		quests.setPageContent();
		
		////////////////////////////////////////////
		
		/// Tiles on GUI ///////////////////////////
		tileSheet = new TileSheet();
		Texture tileTexture = new Texture("images/guiTile.png");
		Texture tileTexture_hover = new Texture("images/guiTile_hover.png");
		
		Group tileGroup = new Group();
		tileGroup.move(-25, 150);
		tileGroup.setStickTo(stickTo.BottomRight);
		List<Integer> puttables = new ArrayList<Integer>();
		puttables.add(0);
		int tileId = 0;
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 5; j++) {
				Tile tile = new Tile(tileId++, tileTexture, tileTexture_hover, (byte)0, j*50, -i*50);
				if(tileId!=1) // first tile not allowed for type 0
					tile.setPuttableItemTypes(puttables);
				tileGroup.addTextureObject(tile);
			}
		}

		tileSheet.addTileGroup(tileGroup);
		
		
		Group tileGroup2 = new Group();
		tileGroup2.setStickTo(stickTo.Bottom);
		tileGroup2.move(0, 10);
		for(int i = 0; i < 6; i++) {
				Tile tile = new Tile(tileId++, tileTexture, tileTexture_hover, (byte)0, i*50, 0);
				tile.setPuttableItemTypes(puttables);
				tileGroup2.addTextureObject(tile);
		}
		
		tileSheet.addTileGroup(tileGroup2);
		////////////////////////////////////////////
		
		guiRenderer.setTileSheet(tileSheet);
		
		guiRenderer.updatePositions();
		
		
		Item fancyItem = new Item(new Texture("textures/item.png"), new ItemInfo());
		fancyItem.setItemType(0);
		
		Item fancyItem2 = new Item(new Texture("textures/item.png"), new ItemInfo());
		fancyItem2.setItemType(0);
		
		try {
			guiRenderer.getTileSheet().getTile(2).putItem(fancyItem);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			guiRenderer.getTileSheet().getTile(3).putItem(fancyItem2);

		} catch (Exception e) {
			e.printStackTrace();
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
	
	public static void main(String[] args) {
		///////////////// INIT ///////////////////////////////////////////////////////////////
		Window.setCallbacks();
		if(!glfwInit()) { 													// Library init
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		//////////////////////////////////////////////////////////////////////////////////////
		
		windowInit();
		
		////////////TESTING INTERACTIONSMASTER ////////
		Interactions interactions = new Interactions("quest01.quest");
		
		/////////////////////// LOCAL VARIABLES ////////////////////////////////////////////
		long lastNanos = Timer.getNanoTime();
		int nextFrameLoadWorld = 1;
		int currentEntityId = -1;
		long startNanos = 0;
		boolean programInit = true;
		long start = -1;
		boolean worldLoaded = false;
		boolean joinThread = false;
		////////////////////////////////////////////////////////////////////////////////////
		
		while(window.processProgram()) {										// This works while program is running
			Timer.syncFrameRate(60, lastNanos);									// Fps limiter
            lastNanos = Timer.getNanoTime();
            
            if(nextFrameLoadWorld != 0) {
            	glClear(GL_COLOR_BUFFER_BIT);
            	glClearColor(0f, 0f, 0f, 1f);
            	
            	
                if(start == -1) {
                	start = Timer.getNanoTime();
                	
                	if(loading_text != null)
                		((Animation)loading_text.getTexture()).resetLastTime();
                }
                
            	if(programInit == true) {		
            		loadingScreen();
            		
                	loadingScreen.update(window);
                	loadingScreen.renderGuiObjects(guiShader, window);
                	
                	worldInit();
                	
                	programInit = false;
            	} else {
                	loadingScreen.update(window);
                	loadingScreen.renderGuiObjects(guiShader, window);
            	}
            	window.update();
            	window.swapBuffers(); 
            	
            	
            	if(worldLoaded == false) {            	
	            	currentWorld = WorldLoader.getWorld(nextFrameLoadWorld, player, camera, window);
	            	currentWorld.start();
	            	worldRenderer.setWorld(currentWorld);
	            	worldRenderer.calculateView(window);
	            	camera.setProjection(window.getWidth(), window.getHeight(), window, WorldRenderer.getScale(), currentWorld.getWidth(), currentWorld.getHeight(), worldRenderer.getWorldOffset());
	            	worldLoaded = true;
	            	joinThread = true;
            	}
            	
            	long stop = Timer.getNanoTime();
            	
            	if(Timer.getDelay(start, stop, 1)) {
	            	nextFrameLoadWorld = 0;
	            	start = -1;
            	}
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
				
				worldRenderer.update((float)0.2, window, camera);
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
				
				
				//Quick check if attack system is working properly, please don't remove, just comment, thanks
				for(int i=0; i < currentWorld.numberOfEntities() - 1 ; i++) { // this is nicer implementation. I've added methods to world to remove entity.
					if(currentWorld.getEntity(i).isClicked()) {
						if(currentWorld.getEntity(i) instanceof Enemy) {

							player.fightShow(guiRenderer, player, (Enemy)currentWorld.getEntity(i), currentWorld, pressStart);

						}
					}
					
					/*if(currentWorld.isOverEntity(currentWorld.getEntity(i), camera, window) && window.getInput().isMouseButtonPressed(0)) {
						if(currentWorld.getEntity(i).getId() == 4 && !guiRenderer.isDialogOpen()) {
							dialog = new Dialog(1);
							dialog.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec pretium sem sem, ac pellentesque dolor dignissim ac. In hendrerit, nulla ut vulputate maximus, tortor arcu varius diam, ac molestie arcu nisi id odio. ");
							List<Answer> ans = new ArrayList<Answer>();
							ans.add(new Answer(0, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec pretium sem sem, ac pellentesque dolor dignissim ac.", 1));
							ans.add(new Answer(1, "Test2", 1));
							dialog.setAnswers(ans);
							guiRenderer.showDialog(dialog);
						}
					}*/
				}
				
				interactions.ChceckInteractions(worldRenderer, camera, window, guiRenderer);
				
				guiRenderer.renderGuiObjects(guiShader, window);
				
				if(button1.isClicked())
					guiRenderer.addWindow(quests);
				
				if(button2.isClicked()) {
					Arena arena = new Arena(player, currentWorld.getEntity(0));
					List<TextBlock> log = new ArrayList<TextBlock>();
					TextBlock tbx = new TextBlock(250, new Vector2f());
					tbx.setString(pressStart, "Killed yourself");
					
					TextBlock tbx2 = new TextBlock(250, new Vector2f());
					tbx2.setString(pressStart, "Suicide");
					
					log.add(tbx);
					log.add(tbx2);
					arena.setLog(log);
					guiRenderer.showArena(arena);
				}
				
				if(healBtn.isClicked()) {
					player.FullyRecoverHP();
					System.out.println("Health of player was fully recovered: " + player.getHP() + "hp.");
				}
				
				/*if(dialog != null) {
					if(dialog.clickedAnswer() != null) {
						if(dialog.clickedAnswer().getId() == 0) {
							guiRenderer.closeDialog();
							Dialog dialog1 = new Dialog(2);
							dialog1.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
							List<Answer> ans1 = new ArrayList<Answer>();
							ans1.add(new Answer(0, "Ala ma kota", 1));
							ans1.add(new Answer(1, "Tekst saukdhskajhdksajhdkjsahkjd 1", 1));
							dialog1.setAnswers(ans1);
							guiRenderer.showDialog(dialog1);
						}
					}
				}*/
				
				for(int i = 0; i < currentWorld.numberOfDoors(); i++) {
					if(currentWorld.getPlayer().getPositionX() == currentWorld.getDoor(i).getPositionX() && currentWorld.getPlayer().getPositionY() == currentWorld.getDoor(i).getPositionY()) {
						player.setPosition(currentWorld.getDoor(i).getDestinationX(), currentWorld.getDoor(i).getDestinationY());
						player.setSetCamWithoutAnimation(true);
						nextFrameLoadWorld = currentWorld.getDoor(i).getWorldIdDestination();
						worldLoaded = false;
						break;
					} else 
						nextFrameLoadWorld = 0;
				}
				
				window.update();
				window.swapBuffers();
            }
		}
		
		glfwTerminate();
	}
}
