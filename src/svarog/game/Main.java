package svarog.game;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.opengl.GL;
import svarog.entity.Player;
import svarog.entity.Transform;
import svarog.gui.GuiRenderer;
import svarog.io.Timer;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Shader;
import svarog.render.Texture;
import svarog.world.World;

public class Main {
	public static void main(String[] args) {
		Window.setCallbacks();
	
		if(!glfwInit()) { 													// Library init
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		
		Window window = new Window(); 
		window.setSize(1000, 800);
		window.createWindow("Svarog"); 										// Creating window "Svarog"
		
		
		GL.createCapabilities();
		
		glEnable(GL_BLEND);													// Allows transparency in opengl
		glEnable(GL_ALPHA);													//
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);					// 
		
		glEnable(GL_TEXTURE_2D);											// Allows load textures

		Shader shader = new Shader("pixelart");
		
		Camera camera = new Camera();	// Creating camera width size of window

		Player player = new Player("player/mavak/", "mavak", new Transform().setPosition(40, 30), false);
				
		World currentWorld = StartWorld.getWorld(player, camera, window);
		
		/////// GUI test //////////
		Shader guiShader = new Shader("shader");
		Camera guiCamera = new Camera();
		GuiRenderer guiRenderer = new GuiRenderer();
		guiCamera.setProjection(window.getWidth(), window.getHeight());
		Texture gui = new Texture("gui.png");
		///////////////////////////////////
		long lastNanos = Timer.getNanoTime();
		int nextFrameLoadWorld = 0;
		while(window.processProgram()) {										// This works while program is running
			Timer.syncFrameRate(60, lastNanos);									// Fps limiter
            lastNanos = Timer.getNanoTime();
            
            if(nextFrameLoadWorld != 0) {
            	glClearColor(0f, 0f, 0f, 1f);
            	window.update();
            	glClear(GL_COLOR_BUFFER_BIT);
            	window.swapBuffers(); 
            	

            	
            	currentWorld = WorldLoader.getWorld(nextFrameLoadWorld, player, camera, window);
            	nextFrameLoadWorld = 0;
            } else {
				glClearColor(0.2f, 0.2f, 0.2f, 1f);
				if(window.hasResized()) {
					camera.setProjection(window.getWidth(), window.getHeight(), window, currentWorld.getScale(), currentWorld.getWidth(), currentWorld.getHeight());
					currentWorld.calculateView(window);
					glViewport(0, 0, window.getWidth(), window.getHeight());
				}
				
				
				currentWorld.update((float)0.2, window, camera);
				currentWorld.correctCamera(camera, window);							// This sets correct camera position on world
										
				window.update();	
	
				glClear(GL_COLOR_BUFFER_BIT);
	
					
				currentWorld.render(shader, camera, window);							// world rendering
				
				guiRenderer.renderGuiObject(guiShader, gui, guiCamera);
				
				window.swapBuffers(); 
				
				for(int i = 0; i < currentWorld.numberOfDoors(); i++) {
					if(currentWorld.getPlayer().getPositionX() == currentWorld.getDoor(i).getPositionX() && currentWorld.getPlayer().getPositionY() == currentWorld.getDoor(i).getPositionY()) {
						player.setPosition(currentWorld.getDoor(i).getDestinationX(), currentWorld.getDoor(i).getDestinationY());
						player.setSetCamWithoutAnimation(true);
						nextFrameLoadWorld = currentWorld.getDoor(i).getWorldIdDestination();
						break;
					} else 
						nextFrameLoadWorld = 0;
				}
            }
		}
		
		glfwTerminate();
	}
}
