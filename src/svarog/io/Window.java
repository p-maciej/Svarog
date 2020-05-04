package svarog.io;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HAND_CURSOR;
import static org.lwjgl.glfw.GLFW.glfwCreateStandardCursor;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

public class Window {
	private long window;
	
	private int width;
	private int height;
	
	private int minimumWidth;
	private int minimumHeight;
	
	private boolean fullscreen;
	private boolean hasResized;
	private GLFWWindowSizeCallback windowSize;
	
	private Input input;
	
	long cursor;
	
	private DoubleBuffer cursorPositionX;
	private DoubleBuffer cursorPositionY;
	
	public enum Cursor {
		Arrow,
		Pointer
	}
	
	private List<Cursor> cursorRequest;
	
	public Window() {
		cursorRequest = new ArrayList<Window.Cursor>();
		
		setSize(640, 480);
		setFullscreen(false);
		
		cursorPositionX = BufferUtils.createDoubleBuffer(1);
		cursorPositionY = BufferUtils.createDoubleBuffer(1);
	}
	
	public Window(int width, int height) {
		cursorRequest = new ArrayList<Window.Cursor>();
		
		setSize(width, height);
		setFullscreen(false);
		cursorPositionX = BufferUtils.createDoubleBuffer(1);
		cursorPositionY = BufferUtils.createDoubleBuffer(1);
	}
	
	public Window(int width, int height, boolean fullscreen) {
		cursorRequest = new ArrayList<Window.Cursor>();
		
		setSize(width, height);
		setFullscreen(fullscreen);
		cursorPositionX = BufferUtils.createDoubleBuffer(1);
		cursorPositionY = BufferUtils.createDoubleBuffer(1);
		hasResized = false;
	}
	
	private void setCursor(Cursor cursor) {
		if(cursor == Cursor.Pointer)
			this.cursor = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
		else 
			this.cursor = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		
		glfwSetCursor(window, this.cursor);
	}
	
	public void createWindow(String title) {
		this.window = glfwCreateWindow(this.getWidth(), this.getHeight(), title, this.getFullscreen() ? glfwGetPrimaryMonitor() : 0, 0);
		
		if(window == 0)
			throw new IllegalStateException("Failed to create window");
		
		
		if(!fullscreen) {
			GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vid.width()-width)/2, (vid.height()-height)/2);
		}
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		input = new Input(window);
		
		setLoacalCallbacks();
	}
	
	public void glInit() {
		GL.createCapabilities();
		glEnable(GL_BLEND);													// Allows transparency in opengl
		glEnable(GL_ALPHA);													//
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);					// 
		glEnable(GL_TEXTURE_2D);											// Allows load textures
	}
	
	public void cleanUp() {
		glfwFreeCallbacks(window);
	}
	
	public boolean processProgram() {
		return !glfwWindowShouldClose(window);
	}
	
	public void closeProgram() {
		glfwSetWindowShouldClose(window, true);
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public void requestCursor(Cursor cursor) {
		cursorRequest.add(cursor);
	}
	
	public void update() {
		hasResized = false;
		input.update();
		glfwGetCursorPos(window, cursorPositionX, cursorPositionY);
		
		boolean cursorSet = false;
		for(Cursor cursor : cursorRequest) {
			if(cursor == Cursor.Pointer) {
				setCursor(cursor);
				cursorSet = true;
				break;
			}
		}
		
		if(cursorSet == false)
			setCursor(Cursor.Arrow);
		
		cursorRequest.clear();
	}
	
	public void checkSize() {
		int tempWidth = width;
		int tempHeight = height;
		boolean changed = false;
		
		if(width < minimumWidth) {
			tempWidth = minimumWidth;
			changed = true;
		}		
		if(height < minimumHeight) {
			tempHeight = minimumHeight;
			changed = true;
		}
		
		if(changed == true)
			glfwSetWindowSize(window, tempWidth, tempHeight);
	}
	
	public void setSize(int width, int height) {
		this.minimumWidth = this.width = width;
		this.minimumHeight = this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public boolean getFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
	
	public boolean hasResized() {
		return hasResized;
	}
	
	public Input getInput() {
		return input;
	}
	
	public double getCursorPositionX() {
		return cursorPositionX.get(0);
	}
	
	public double getCursorPositionY() {
		return cursorPositionY.get(0);
	}
	
	public double getRelativePositionCursorX() {
		return -this.getWidth()/2 + this.getCursorPositionX();
	}
	
	public double getRelativePositionCursorY() {
		return this.getHeight()/2 - this.getCursorPositionY();
	}


	public static void setCallbacks() {
		glfwSetErrorCallback(new GLFWErrorCallback() {

			@Override
			public void invoke(int error, long description) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
			}
			
		});
	}
	
	private void setLoacalCallbacks() {
		windowSize = new GLFWWindowSizeCallback() {
			
			@Override
			public void invoke(long argWindow, int argWidth, int argHeight) {
				width = argWidth;
				height = argHeight;
				hasResized = true;
			}
		};
		
		glfwSetWindowSizeCallback(window, windowSize);
	}
}
