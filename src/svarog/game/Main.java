package svarog.game;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
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
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.opengl.GL;

import svarog.entity.Entity;
import svarog.entity.Player;
import svarog.entity.Transform;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Shader;
import svarog.render.Texture;
import svarog.world.Tile;
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
		Camera camera = new Camera(600, 400);	// Creating camera width size of window

		


		
		Shader shader = new Shader("shader");								// This loads files named shader.vs and shader.fs
		
		
		World world = new World();											// World initialization
		world.calculateView(window);
		camera.setProjection(window.getWidth(), window.getHeight(), window, world.getScale(), world.getWidth(), world.getHeight());
		world.fillWorld(new Texture("grass_map_1.png"));
		
		Tile test2 = new Tile(new Texture("wall.png")).setSolid();
		
		world.setTile(test2, 5, 0);
		world.setTile(test2, 6, 0);
		world.setTile(test2, 7, 0);
		world.setTile(test2, 7, 1);
		world.setTile(test2, 7, 2);
		
		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 4; j++)
				world.getTile(12+i, 12+j).setTexture(new Texture("home1_map_1.png", i, j, 32), (byte)1);
		
		
		world.addEntity(new Entity(new Texture("player.png"), new Transform().setPosition(10, 10), true).setIsStatic(false));
		world.addEntity(new Entity(new Texture("player.png"), new Transform().setPosition(18, 17), true));
		world.addEntity(new Player(new Transform().setPosition(15,  5), false));
		
		world.setSolidTilesFromMap("map1.png");
		
		world.setBoundingBoxes();
		
		long lastNanos = System.nanoTime();
		while(window.processProgram()) {									// This works while program is running
			syncFrameRate(60, lastNanos);									// Fps limiter
            lastNanos = System.nanoTime();
			glClearColor(0.2f, 0.2f, 0.2f, 1f);
			if(window.hasResized()) {
				camera.setProjection(window.getWidth(), window.getHeight(), window, world.getScale(), world.getWidth(), world.getHeight());
				world.calculateView(window);
				glViewport(0, 0, window.getWidth(), window.getHeight());
			}
			
			
			if(window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {			// If esc pressed then...
				System.exit(0);
			}
				
			world.update((float)0.2, window, camera);
			world.correctCamera(camera, window);							// This sets correct camera position on world
				
			window.update();										
			

			glClear(GL_COLOR_BUFFER_BIT);

				
			world.render(shader, camera, window);							// world rendering
			window.swapBuffers(); 
			
		}
		
		glfwTerminate();
	}
	
	private static void syncFrameRate(float fps, long lastNanos) {
        long targetNanos = lastNanos + (long) (1_000_000_000.0f / fps) - 1_000_000L;  // subtract 1 ms to skip the last sleep call
        try {
        	while (System.nanoTime() < targetNanos) {
        		Thread.sleep(1);
        	}
        }
        catch (InterruptedException ignore) {}
    }
}
