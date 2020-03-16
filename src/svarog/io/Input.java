package svarog.io;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

public class Input {
	private long window;
	private boolean keys[];
	
	
	Input(long window) {
		this.window = window;
		this.keys = new boolean[GLFW_KEY_LAST];

		for(int i = 0; i < GLFW_KEY_LAST; i++)
			this.keys[i] = false;
	}

	public boolean isKeyDown(int key) {
		return (glfwGetKey(window, key) == 1);
	}
	
	public boolean isMouseButtonDown(int button) {
		return (glfwGetMouseButton(window, button) == 1);
	}
	
	public boolean isKeyPressed(int key) {
		return (isKeyDown(key) && !keys[key]);
	}
	
	public boolean isKeyReleased(int key) {
		return (!isKeyDown(key) && keys[key]);
	}
	
	public void update() {
		for(int i = 32; i < GLFW_KEY_LAST; i++)
			keys[i] = isKeyDown(i);
	}
}
