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


import org.lwjgl.opengl.GL;

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
	
	public static void main(String[] args) {
		Window.setCallbacks();
	
		if(!glfwInit()) { // Library init
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		
		Window window = new Window(); 
		window.setSize(1100, 800);
		window.createWindow("Svarog"); 										// Creating window "Svarog"
		
		
		GL.createCapabilities();
		
		glEnable(GL_BLEND);													// Allows transparency in opengl
		glEnable(GL_ALPHA);													//
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);					// 
		
		glEnable(GL_TEXTURE_2D);											// Allows load textures
		Camera camera = new Camera(window.getWidth(), window.getHeight());	// Creating camera width size of window

		


		
		Shader shader = new Shader("shader");								// This loads files named shader.vs and shader.fs
		
		
		World world = new World();											// World initialization
		
		world.fillWorld(new Texture("grass.png"));
		
		Tile test2 = new Tile(new Texture("wall.png")).setSolid();
		
		world.setTile(test2, 5, 0);
		world.setTile(test2, 6, 0);
		world.setTile(test2, 7, 0);
		world.setTile(test2, 7, 1);
		world.setTile(test2, 7, 2);
		
		
		////////// Test ///////
		
	
		world.getTile(7, 5).setSolid().setTexture(new Texture("house.png", 0, 0, 32), (byte)1);
		
		Tile h2 = new Tile(new Texture("house.png", 0, 1, 32)).setSolid();
		Tile h3 = new Tile(new Texture("house.png", 1, 0, 32)).setSolid();
		Tile h4 = new Tile(new Texture("house.png")).setSolid();
		
		world.setTile(h2, 8, 5);
		world.setTile(h3, 7, 6);
		world.setTile(h4, 9, 6);
		
		//////////////////////////////////////////////
		
		
		world.setBoundingBoxes();
		

		Player player = new Player(new Transform());
		
		
		double frame_cap = 1.0/60.0; 										// 60fps - frame limit
		
		/*// For fps counter //
		double frame_time = 0;
		int frames = 0;
		//////////////////////*/
		
		double time = Timer.getTime();										// Returns time in seconds
		double unprocessed = 0;
		
		while(window.processProgram()) {									// This works while program is running
			boolean render = false;
			
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			
			unprocessed += passed;
			
			//frame_time += passed;
			
			time = time_2;
			
			while(unprocessed >= frame_cap) {
				unprocessed -= frame_cap;
				render = true;
				
				if(window.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {		// If esc pressed then...
					System.out.println("TRUE");
				}
				
				player.update((float)0.3, window, camera, world);
				
				world.correctCamera(camera, window);						// This sets correct camera position on world
				
				window.update();										
				
				/*//// For fps counter ////
				if(frame_time >= 1.0) {
					frame_time = 0;
					//System.out.println("FPS:" + frames);
					frames = 0;
				}
				//////////////////////////*/
			}

			if(render) {
				glClear(GL_COLOR_BUFFER_BIT);

				
				world.render(shader, camera, window);						// world rendering
				player.render(shader, camera, world);
				window.swapBuffers(); 
				//frames++;
				
				
			}
			
		}
		
		glfwTerminate();
	}
}
