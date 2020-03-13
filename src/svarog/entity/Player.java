package svarog.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Vector2f;
import org.joml.Vector3f;

import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.World;

public class Player extends Entity {
	public Player(Transform transform) {
		super(new Texture("avatar.png"), transform);
	}
	
	@Override
	public void update(float delta, Window window, Camera camera, World world) {
		Vector2f movement = new Vector2f();
		///////////// WASD Player movement ////////////////////
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
		
		move(movement);
		
		camera.getPosition().lerp(transform.position.mul(-world.getScale(), new Vector3f()), 0.05f);
		
		super.update(delta, window, camera, world);
		/////////////////////////////////////////////////////////////////
		
		window = null;
		camera = null;
		world = null;
	}
}
