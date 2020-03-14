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
		
		world.fillWorld(new Texture("grass_map_1.png"));
		
		Tile test2 = new Tile(new Texture("wall.png")).setSolid();
		
		world.setTile(test2, 5, 0);
		world.setTile(test2, 6, 0);
		world.setTile(test2, 7, 0);
		world.setTile(test2, 7, 1);
		world.setTile(test2, 7, 2);
		
		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 4; j++)
				world.getTile(12+i, 12+j).setSolid().setTexture(new Texture("home1_map_1.png", i, j, 32), (byte)1);

		
		//////////////////////////////////////////////
		
		
		world.addEntity(new Entity(new Texture("player.png"), new Transform().setPosition(10, 10)).setIsStatic(false));
		world.addEntity(new Entity(new Texture("player.png"), new Transform().setPosition(20, 20)));
		world.addEntity(new Player(new Transform().setPosition(15,  5)));
		
		
		world.setBoundingBoxes();
		

		while(window.processProgram()) {									// This works while program is running
			if(window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {		// If esc pressed then...
				System.exit(0);
			}
				
			world.update((float)0.2, window, camera);
			world.correctCamera(camera, window);						// This sets correct camera position on world
				
			window.update();										
			

			glClear(GL_COLOR_BUFFER_BIT);

				
			world.render(shader, camera, window);						// world rendering
			window.swapBuffers(); 
			
		}
		
		glfwTerminate();
	}
}
