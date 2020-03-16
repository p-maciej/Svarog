package svarog.game;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
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

import svarog.entity.Entity;
import svarog.entity.Player;
import svarog.entity.Transform;
import svarog.io.Timer;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Shader;
import svarog.render.Texture;
import svarog.world.Tile;
import svarog.world.World;

public class Main {
	private static int currentWorldId = 1;
	
	private static World testWorld1(Camera camera, Window window) {
		World world = new World(42, 30);											// World initialization
		world.calculateView(window);
		
		world.fillWorld(new Texture("grass_map_1.png"));
		
		Tile test2 = new Tile(new Texture("wall.png")).setSolid();
		
		world.setTile(test2, 5, 0);
		world.setTile(test2, 6, 0);
		world.setTile(test2, 7, 0);
		world.setTile(test2, 7, 1);
		world.setTile(test2, 7, 2);
		
		camera.setProjection(window.getWidth(), window.getHeight(), window, world.getScale(), world.getWidth(), world.getHeight());

		
		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 4; j++)
				world.getTile(12+i, 12+j).setTexture(new Texture("home1_map_1.png", i, j, 32), (byte)1);
		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 4; j++)
				world.getTile(20+i, 7+j).setTexture(new Texture("home1_map_1.png", i, j, 32), (byte)(j < 3 ? 2 : 1));
		
		world.addEntity(new Entity(new Texture("player.png"), new Transform().setPosition(10, 10), true).setIsStatic(false));
		world.addEntity(new Entity(new Texture("player.png"), new Transform().setPosition(18, 17), true));
		world.addEntity(new Player(new Transform().setPosition(15,  5), false));
		
		world.setSolidTilesFromMap("map1.png");
		
		world.setBoundingBoxes();
		
		return world;
	}
	
	private static World testWorld2(Camera camera, Window window) {
		World world = new World(42, 30);											// World initialization
		world.calculateView(window);
		
		world.fillWorld(new Texture("grass_map_1.png"));
		

		camera.setProjection(window.getWidth(), window.getHeight(), window, world.getScale(), world.getWidth(), world.getHeight());

		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 4; j++)
				world.getTile(7+i, 15+j).setTexture(new Texture("home1_map_1.png", i, j, 32), (byte)(j < 3 ? 2 : 1));
		
		world.addEntity(new Entity(new Texture("player.png"), new Transform().setPosition(10, 10), true).setIsStatic(false));
		world.addEntity(new Entity(new Texture("player.png"), new Transform().setPosition(18, 17), true));
		world.addEntity(new Player(new Transform().setPosition(10,  2), false));
		
		//world.setSolidTilesFromMap("map1.png");
		
		world.setBoundingBoxes();
		
		return world;
	}
	
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

		Shader shader = new Shader("shader");								// This loads files named shader.vs and shader.fs
		Camera camera = new Camera(window.getWidth(), window.getHeight());	// Creating camera width size of window

		World currentWorld = testWorld1(camera, window);
		
		

		
		long lastNanos = Timer.getNanoTime();
		while(window.processProgram()) {									// This works while program is running
			Timer.syncFrameRate(60, lastNanos);									// Fps limiter
            lastNanos = Timer.getNanoTime();
            
			glClearColor(0.2f, 0.2f, 0.2f, 1f);
			if(window.hasResized()) {
				camera.setProjection(window.getWidth(), window.getHeight(), window, currentWorld.getScale(), currentWorld.getWidth(), currentWorld.getHeight());
				currentWorld.calculateView(window);
				glViewport(0, 0, window.getWidth(), window.getHeight());
			}
			
			
			if(window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {			// If esc pressed then...
				System.exit(0);
			}
			
			if(window.getInput().isKeyPressed(GLFW_KEY_1)) {
				if(currentWorldId != 1) {
					currentWorld = testWorld1(camera, window);
					currentWorldId = 1;
					continue;
				}
			}
			
			if(window.getInput().isKeyPressed(GLFW_KEY_2)) {
				if(currentWorldId != 2) {
					currentWorld = testWorld2(camera, window);
					currentWorldId = 2;
					continue;
				}
			}
				
			currentWorld.update((float)0.2, window, camera);
			currentWorld.correctCamera(camera, window);							// This sets correct camera position on world
				
			window.update();										
			

			glClear(GL_COLOR_BUFFER_BIT);

				
			currentWorld.render(shader, camera, window);							// world rendering
			window.swapBuffers(); 
			
		}
		
		glfwTerminate();
	}
}
