package svarog.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;

import org.joml.Vector2f;
import org.joml.Vector3f;

import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.World;

public class Player extends Entity {
	private boolean setCamWithoutAnimation;
	
	private int[] lastKeysPressed = new int[4];
	private int lastPressedKey;
	
	public Player(Transform transform, boolean fullBoundingBox) {
		super(new Texture("avatar.png"), transform, fullBoundingBox);
		setCamWithoutAnimation = true;
		lastPressedKey = GLFW_KEY_LAST;
	}
	
	private int getNewPressedKey(int[] lastPressedKeys, int[] pressedKeys) {
		int pressed = 0;
		for(int i = 0; i < 4; i++) {
			if(pressedKeys[i] != -1)
				pressed++;
			
			if(lastKeysPressed[i] != pressedKeys[i] && pressedKeys[i] != -1) {
				return pressedKeys[i];
			}
		}
		
		if(pressed == 1) {
			for(int i = 0; i < 4; i++) {
				if(pressedKeys[i] != -1)
					return pressedKeys[i];
			}
		}
		
		if(pressed == 0) {
			return 0;
		}
		
		return lastPressedKey;
	}
	
	private void setLastKeysPressed(int[] keysPressed) {
		for(int i = 0; i < 4; i++) {
			lastKeysPressed[i] = keysPressed[i];
		}
	}
	
	@Override
	public void update(float delta, Window window, Camera camera, World world) {
		Vector2f movement = new Vector2f();
		
		///////////// WASD Player movement ////////////////////
		
		int direction = 0;
		int keysPressed[] = new int[4];
		
		if(window.getInput().isKeyDown(GLFW_KEY_A)) {
			keysPressed[0] = GLFW_KEY_A;
		}
		else {
			keysPressed[0] = -1;
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_D)) {
			keysPressed[1] = GLFW_KEY_D;
		}
		else {
			keysPressed[1] = -1;
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_W)) {
			keysPressed[2] = GLFW_KEY_W;
		}
		else {
			keysPressed[2] = -1;
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_S)) {
			keysPressed[3] = GLFW_KEY_S;
		}
		else {
			keysPressed[3] = -1;
		}
		
		direction = getNewPressedKey(lastKeysPressed, keysPressed);
		setLastKeysPressed(keysPressed);
		
		
		if(direction == 65) {
			movement.add(-1*delta, 0);
		} else if(direction == 68) {
			movement.add(1*delta, 0);
		} else if(direction == 87) {
			movement.add(0, 1*delta);
		} else if(direction == 83) {
			movement.add(0, -1*delta);
		}
		
		lastPressedKey = direction;
		
		/*
		if(window.getInput().isKeyDown(GLFW_KEY_A)) {
			movement.add(-1*delta, 0);
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_D)) {
			movement.add(1*delta, 0);
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_W)) {
			movement.add(0, 1*delta);
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_S)) {
			movement.add(0, -1*delta);
		}
		*/
		
		move(movement);
		
		if(setCamWithoutAnimation == true) {
			camera.setPosition(transform.getPosition().mul(-world.getScale(), new Vector3f()));
			setCamWithoutAnimation = false;
		}
		else {
			camera.getPosition().lerp(transform.getPosition().mul(-world.getScale(), new Vector3f()), 0.05f); // Camera movement
		}
		
		super.update(delta, window, camera, world);
		/////////////////////////////////////////////////////////
		
		window = null;
		camera = null;
		world = null;
	}
	
	public int getPositionX() {
		return (int)(transform.getPosition().x/2);
	}
	
	public int getPositionY() {
		return (int)(transform.getPosition().y/2*(-1));
	}

	public void setSetCamWithoutAnimation(boolean setCamWithoutAnimation) {
		this.setCamWithoutAnimation = setCamWithoutAnimation;
	}
}
