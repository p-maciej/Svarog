package svarog.game;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import java.util.List;

import svarog.entity.Enemy;
import svarog.entity.Entity;
import svarog.entity.Player;
import svarog.gui.Answer;
import svarog.gui.Button;
import svarog.gui.Dialog;
import svarog.gui.Group;
import svarog.gui.GuiPanels;
import svarog.gui.GuiRenderer;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.TextureObject;
import svarog.gui.Tile;
import svarog.gui.TileSheet;
import svarog.gui.font.Color;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.io.Timer;
import svarog.io.Window;
import svarog.objects.Item;
import svarog.objects.ItemInfo;
import svarog.render.Camera;
import svarog.render.Shader;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.World;

public class Main {
	public static void main(String[] args) {
		///////////////// INIT ///////////////////////////////////////////////////////////////
		Window.setCallbacks();
		if(!glfwInit()) { 													// Library init
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		
		Window window = new Window(); 
		window.setSize(1200, 800);
		window.createWindow("Svarog"); 										// Creating window "Svarog"
		window.glInit();
		//////////////////////////////////////////////////////////////////////////////////////
		
		//////////////// WORLD ///////////////////////////////////////////////////////////////
		Shader shader = new Shader("shader");
		Camera camera = new Camera();

		Player player = new Player(0, "player/mavak/", "mavak", new Transform().setPosition(40, 25), false);
		player.setName("Ty");
		player.setHpXpAttack(100, 0, 50, 60);

		World currentWorld = new World(1, 0, 0);
		/////////////////////////////////////////////////////////////////////////////////////

		/////// GUI  ////////////////////////////////////////////////////////////////////////
		Font verdana = new Font("verdana_20", new Color((byte)255, (byte)255, (byte)0));
		Font pressStart = new Font("font", new Color((byte)255, (byte)255, (byte)255));
		Font pressStartY = new Font("font", new Color((byte)255, (byte)255, (byte)0));
		Font pressStartR = new Font("font", new Color((byte)255, (byte)0, (byte)0));
		
		Shader guiShader = new Shader("shader");
		GuiRenderer guiRenderer = new GuiRenderer(window);
		
		guiRenderer.setBubbleLeft(Texture.getImageBuffer("images/bubble/left.png"));
		guiRenderer.setBubbleRight(Texture.getImageBuffer("images/bubble/right.png"));
		guiRenderer.setBubbleCenter(Texture.getImageBuffer("images/bubble/center.png"));
		
		guiRenderer.setTopDialog(Texture.getImageBuffer("images/dialog/dialog_top.png"));
		guiRenderer.setCenterDialog(Texture.getImageBuffer("images/dialog/dialog_center.png"));
		guiRenderer.setDialogFont(pressStart);
		guiRenderer.setAnswerFont(pressStartY);
		guiRenderer.setAnswerHoverFont(pressStartR);
		
		GuiPanels panels = new GuiPanels();
		panels.addBottomPanel(Texture.getImageBuffer("images/bottom_panel.png"));
		panels.addRightPanel(Texture.getImageBuffer("images/background_right_panel.png"));
		panels.updateDynamicGuiElements(guiRenderer, window);
		
		Group group1 = new Group();
		Line test1 = new Line(GuiRenderer.stickTo.BottomLeft);
		test1.setString("Tekst w innym miejscu", verdana);
		test1.move(95, -25);

		TextBlock test = new TextBlock(400, stickTo.TopLeft);
		test.setString(verdana, "12 as� jsajhdkjs sdsadsa sad asdsadhjs dksfjlskdjflksdj flkjlkjdflsdjfljdslkj jjkdj lfjsldfjldksjj fklkdsjfl ksjdlfk");
		test.move(15, 15);
		group1.addTextBlock(test);
		group1.addTextureObject(test1);
		
		TextureObject bottomCorner1 = new TextureObject(new Texture("images/corner.png"), GuiRenderer.stickTo.BottomLeft);	
		TextureObject bottomCorner2 = new TextureObject(new Texture("images/corner.png"), GuiRenderer.stickTo.BottomRight);	
		TextureObject bottomBorderRightPanel = new TextureObject(new Texture("images/border_right_panel.png"), GuiRenderer.stickTo.BottomRight);
		bottomBorderRightPanel.move(0, -70);
		TextureObject topBorderRightPanel = new TextureObject(new Texture("images/border_right_panel.png"), GuiRenderer.stickTo.TopRight);
		
		Button button1 = new Button(new Texture("images/button.png"), new Texture("images/button_hover.png"), stickTo.TopRight);
		button1.move(-100, 100);
		
		Button healBtn = new Button(new Texture("images/button.png"), stickTo.TopRight);
		healBtn.move(-100, 200);
		
		guiRenderer.addGuiObject(bottomCorner1);
		guiRenderer.addGuiObject(bottomCorner2);
		guiRenderer.addGuiObject(bottomBorderRightPanel);
		guiRenderer.addGuiObject(topBorderRightPanel);
		guiRenderer.addGuiObject(button1);
		guiRenderer.addGuiObject(healBtn);
		guiRenderer.addGroup(group1);
		
		/// Tiles on GUI ///////////////////////////
		TileSheet tileSheet = new TileSheet();
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
		
		////////// LOADING SCREEN //////////////////////////////////////////////////////////
		GuiRenderer loadingScreen = new GuiRenderer(window);
		
		TextureObject background = new TextureObject(new Texture("textures/loading_screen.png"));	
		TextureObject loading_text = new TextureObject(new Texture("textures/animations/loading/loading_3.png"));
		loadingScreen.addGuiObject(background);
		loadingScreen.addGuiObject(loading_text);
		////////////////////////////////////////////////////////////////////////////////////
		
		/////////////////////// LOCAL VARIABLES ////////////////////////////////////////////
		long lastNanos = Timer.getNanoTime();
		int nextFrameLoadWorld = 1;
		int currentEntityId = -1;
		long startNanos = 0;
		Dialog dialog = null;
		////////////////////////////////////////////////////////////////////////////////////
		
		while(window.processProgram()) {										// This works while program is running
			Timer.syncFrameRate(60, lastNanos);									// Fps limiter
            lastNanos = Timer.getNanoTime();
            
            if(nextFrameLoadWorld != 0) {
            	glClear(GL_COLOR_BUFFER_BIT);
            	glClearColor(0f, 0f, 0f, 1f);
            	long start = Timer.getNanoTime();
            	loadingScreen.update(window);
            	loadingScreen.renderGuiObjects(guiShader, window);
            	
            	window.update();
            	
            	window.swapBuffers();  	
            	camera.setProjection(window.getWidth(), window.getHeight(), window, currentWorld.getScale(), currentWorld.getWidth(), currentWorld.getHeight(), currentWorld.getWorldOffset());
            	currentWorld = WorldLoader.getWorld(nextFrameLoadWorld, player, camera, window);
            	long stop = Timer.getNanoTime();
            	
            	Timer.sleep(stop - start, 1000000000);
            	nextFrameLoadWorld = 0;
            } else {
				glClearColor(0.2f, 0.2f, 0.2f, 1f);
				if(window.hasResized()) {
					camera.setProjection(window.getWidth(), window.getHeight(), window, currentWorld.getScale(), currentWorld.getWidth(), currentWorld.getHeight(), currentWorld.getWorldOffset());
					guiRenderer.deleteGuiPanels();
					panels.updateDynamicGuiElements(guiRenderer, window);
					guiRenderer.update(window);
					
					currentWorld.calculateView(window);
					glViewport(0, 0, window.getWidth(), window.getHeight());
				}
				glClear(GL_COLOR_BUFFER_BIT);
				
				guiRenderer.deleteDynamicGroups();
				
				currentWorld.update((float)0.2, window, camera);
				currentWorld.correctCamera(camera, window);							// This sets correct camera position on world

				

					
				currentWorld.render(shader, camera, window);							// world rendering
				
				
				
				if(currentWorld.getMouseOverEntityId() >= 0) {
					if(currentWorld.getMouseOverEntityId() != currentEntityId) {
						startNanos = Timer.getNanoTime();
						currentEntityId = currentWorld.getMouseOverEntityId();
					}
					
					if(Timer.getDelay(startNanos, Timer.getNanoTime(), 0.4)) {
						Line name = new Line(0, 0);
						Entity ent = currentWorld.getEntityById(currentWorld.getMouseOverEntityId());
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
				for(int i=0; i < currentWorld.numberOfEntities() - 1 ; i++) {
					if(currentWorld.getEntity(i) instanceof Enemy) {
						if(currentWorld.isOverEntity(currentWorld.getEntity(i), camera, window) && window.getInput().isMouseButtonPressed(0)) {
							while(((Enemy) (currentWorld.getEntity(i))).GetEnemyHP()>0) {
								System.out.println("Enemy HP (before attack): " + ((Enemy) (currentWorld.getEntity(i))).GetEnemyHP());
								((Enemy) (currentWorld.getEntity(i))).DecreaseEnemyHP(player.getRandomAttack());
								System.out.println("Enemy HP:  (after attack): " + ((Enemy) (currentWorld.getEntity(i))).GetEnemyHP());
								if(((Enemy) (currentWorld.getEntity(i))).GetEnemyHP()<0) {
									System.out.println("Enemy "+ (currentWorld.getEntity(i)).getName() + " died, you WON!!!");
									(currentWorld.getEntity(i)).setPosition(1,1);
								}else {
									System.out.println("Player HP (before attack): " + player.getHP());
									player.DecreasePlayerHP(((Enemy) (currentWorld.getEntity(i))).GetRandomAttack());
									System.out.println("Player HP (after attack): " + player.getHP());
									if(player.getHP()<0) {
										System.out.println("Player died, " + (currentWorld.getEntity(i)).getName() + " was killing more people than ever.");
										break;
									}
								}
							}
						}
					}
					
					if(currentWorld.isOverEntity(currentWorld.getEntity(i), camera, window) && window.getInput().isMouseButtonPressed(0)) {
						if(currentWorld.getEntity(i).getId() == 4 && !guiRenderer.isDialogOpen()) {
							dialog = new Dialog(1);
							dialog.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec pretium sem sem, ac pellentesque dolor dignissim ac. In hendrerit, nulla ut vulputate maximus, tortor arcu varius diam, ac molestie arcu nisi id odio. ");
							List<Answer> ans = new ArrayList<Answer>();
							ans.add(new Answer(0, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec pretium sem sem, ac pellentesque dolor dignissim ac.", 1));
							ans.add(new Answer(1, "Test2", 1));
							dialog.setAnswers(ans);
							guiRenderer.showDialog(dialog);
						}
					}
				}
				
				guiRenderer.renderGuiObjects(guiShader, window);
				
				if(button1.isClicked())
					System.out.println("CLICK!");
				
				if(healBtn.isClicked()) {
					player.FullyRecoverHP();
					System.out.println("Health of player was fully recovered: " + player.getHP() + "hp.");
				}
				
				if(dialog != null) {
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
				}
				
				for(int i = 0; i < currentWorld.numberOfDoors(); i++) {
					if(currentWorld.getPlayer().getPositionX() == currentWorld.getDoor(i).getPositionX() && currentWorld.getPlayer().getPositionY() == currentWorld.getDoor(i).getPositionY()) {
						player.setPosition(currentWorld.getDoor(i).getDestinationX(), currentWorld.getDoor(i).getDestinationY());
						player.setSetCamWithoutAnimation(true);
						nextFrameLoadWorld = currentWorld.getDoor(i).getWorldIdDestination();
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
