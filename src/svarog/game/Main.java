package svarog.game;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import svarog.entity.Enemy;
import svarog.entity.Entity;
import svarog.entity.Player;
import svarog.gui.Button;
import svarog.gui.Group;
import svarog.gui.GuiPanels;
import svarog.gui.GuiRenderer;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.TextureObject;
import svarog.gui.font.Color;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.io.Timer;
import svarog.io.Window;
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

		Player player = new Player("player/mavak/", "mavak", new Transform().setPosition(40, 25), false);
		player.setHpXpAttack(100, 0, 50, 60);

		World currentWorld = new World(1, 0, 0);
		//= StartWorld.getWorld(player, camera, window);
		/////////////////////////////////////////////////////////////////////////////////////

		/////// GUI  ////////////////////////////////////////////////////////////////////////
		Shader guiShader = new Shader("shader");
		GuiRenderer guiRenderer = new GuiRenderer(window);
		
		GuiPanels panels = new GuiPanels();
		panels.addBottomPanel(Texture.getImageBuffer("images/bottom_panel.png"));
		panels.addRightPanel(Texture.getImageBuffer("images/background_right_panel.png"));
		panels.updateDynamicGuiElements(guiRenderer, window);
		
		Group group1 = new Group();
		Font verdana = new Font("verdana_20", new Color((byte)255, (byte)255, (byte)255));
		Font pressStart = new Font("PressStart", new Color((byte)255, (byte)255, (byte)255));
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
		
		Button button1 = new Button(new Texture("images/button.png"), stickTo.TopRight);
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
		
		guiRenderer.updatePositions();
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
				guiRenderer.deleteDynamicElements();
				
				currentWorld.update((float)0.2, window, camera);
				currentWorld.correctCamera(camera, window);							// This sets correct camera position on world

				
				glClear(GL_COLOR_BUFFER_BIT);
					
				currentWorld.render(shader, camera, window);							// world rendering
				
				if(currentWorld.getMouseOverEntityId() >= 0) {
					Line name = new Line(0, 0);
					Entity ent = currentWorld.getEntityById(currentWorld.getMouseOverEntityId());
					if(ent != null) {
						name.setString(ent.getName(), verdana);
					
						guiRenderer.showBubble(name, window.getRelativePositionCursorX(), window.getRelativePositionCursorY());
					}
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
				}
				
				guiRenderer.renderGuiObjects(guiShader, window);
				
				if(button1.isClicked())
					System.out.println("CLICK!");
				
				if(healBtn.isClicked()) {
					player.FullyRecoverHP();
					System.out.println("Health of player was fully recovered: " + player.getHP() + "hp.");
				}
				
				window.update();
				
				
				for(int i = 0; i < currentWorld.numberOfDoors(); i++) {
					if(currentWorld.getPlayer().getPositionX() == currentWorld.getDoor(i).getPositionX() && currentWorld.getPlayer().getPositionY() == currentWorld.getDoor(i).getPositionY()) {
						player.setPosition(currentWorld.getDoor(i).getDestinationX(), currentWorld.getDoor(i).getDestinationY());
						player.setSetCamWithoutAnimation(true);
						nextFrameLoadWorld = currentWorld.getDoor(i).getWorldIdDestination();
						break;
					} else 
						nextFrameLoadWorld = 0;
				}
				
				window.swapBuffers();
            }
		}
		
		glfwTerminate();
	}
}
