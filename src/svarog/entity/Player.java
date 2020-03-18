package svarog.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;

import org.joml.Vector2f;
import org.joml.Vector3f;

import svarog.io.Window;
import svarog.render.Animation;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.World;

public class Player extends Entity {
	private boolean setCamWithoutAnimation;
	
	private int[] lastKeysPressed = new int[4];
	private int lastPressedKey;
	private String texturesPath;
	private String fileName;
	
	public Player(String texturePath, String filename, Transform transform, boolean fullBoundingBox) {
		super(new Texture("animations/" + texturePath + "idle/down/" + filename + ".png"), transform, fullBoundingBox);
		
		this.texturesPath = texturePath;
		this.fileName = filename;
		
		setCamWithoutAnimation = true;
		lastPressedKey = GLFW_KEY_LAST;
		
		super.setIsStatic(false); // Non-static - default setting for player 
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
			
			if(super.currentDirection != Direction.left) {
				super.setAnimation(Direction.left, new Animation(4, 8, this.texturesPath + "walking/left/" + this.fileName));
			}
		} else if(direction == 68) {
			movement.add(1*delta, 0);
			
			if(super.currentDirection != Direction.right) {
				super.setAnimation(Direction.right, new Animation(4, 8, this.texturesPath + "walking/right/" + this.fileName));
			}
		} else if(direction == 87) {
			movement.add(0, 1*delta);
			
			if(super.currentDirection != Direction.up) {
				super.setAnimation(Direction.up, new Animation(4, 8, this.texturesPath + "walking/up/" + this.fileName));
			}
		} else if(direction == 83) {
			movement.add(0, -1*delta);
			
			if(super.currentDirection != Direction.down) {
				super.setAnimation(Direction.down, new Animation(4, 8, this.texturesPath + "walking/down/" + this.fileName));
			}
		} else if(direction == 0) {
			if(super.currentDirection == Direction.left && super.texture == null) {
				super.setTexture(new Texture("animations/" + this.texturesPath + "idle/left/" + this.fileName + ".png"));
			}
			if(super.currentDirection == Direction.right && super.texture == null) {
				super.setTexture(new Texture("animations/" + this.texturesPath + "idle/right/" + this.fileName + ".png"));
			}
			if(super.currentDirection == Direction.up && super.texture == null) {
				super.setTexture(new Texture("animations/" + this.texturesPath + "idle/up/" + this.fileName + ".png"));
			}
			if(super.currentDirection == Direction.down && super.texture == null) {
				super.setTexture(new Texture("animations/" + this.texturesPath + "idle/down/" + this.fileName + ".png"));
			}
		}
		
		lastPressedKey = direction;
		
		move(movement);
		
		if(setCamWithoutAnimation == true) {
			camera.setPosition(transform.getPosition().mul(-world.getScale(), new Vector3f()));
			setCamWithoutAnimation = false;
		}
		else {
			camera.getPosition().lerp(transform.getPosition().mul(-world.getScale(), new Vector3f()), 0.25f); // Camera movement
		}
		
		super.update(delta, window, camera, world);
		/////////////////////////////////////////////////////////
		
		window = null;
		camera = null;
		world = null;
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
