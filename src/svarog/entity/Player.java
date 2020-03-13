package svarog.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Vector2f;
import org.joml.Vector3f;

import svarog.collision.AABB;
import svarog.collision.Collision;
import svarog.io.Window;
import svarog.render.Animation;
import svarog.render.Camera;
import svarog.render.Model;
import svarog.render.Shader;
import svarog.world.World;

public class Player {
	private Model model;
	private Animation texture;
	private Transform transform;
	private AABB bounding_box;
	
	
	public Player() {
		float[] vertices = new float[] {
				-1f, 1f, 0,
				1f, 1f, 0,			
				1f, -1f, 0,
				-1f, -1f, 0,
		};
		
		float[] texture = new float[] {
				0, 0,
				1, 0,	
				1, 1,
				0, 1,
		};
		
		int[] indices = new int[] {
				0,1,2,
				2,3,0
		};
		
		model = new Model(vertices, texture, indices);
		this.texture = new Animation(4, 3, "player/idle/player");
		
		transform = new Transform();
		transform.scale = new Vector3f(16,16, 1); // This must be changed when world scale is changed
		
		bounding_box = new AABB(new Vector2f(transform.position.x, transform.position.y), new Vector2f(1,1));
	}
	
	public void update(float delta, Window window, Camera camera, World world) {
		///////////// WASD Player movement ////////////////////
		if(window.getInput().isKeyDown(GLFW_KEY_A)) {
			transform.position.add(new Vector3f(-1*delta, 0,0));
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_D)) {
			transform.position.add(new Vector3f(1*delta, 0,0));
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_W)) {
			transform.position.add(new Vector3f(0, 1*delta,0));
		}
		
		if(window.getInput().isKeyDown(GLFW_KEY_S)) {
			transform.position.add(new Vector3f(0, -1*delta,0));
		}
		
		/////////////////////////////////////////////////////////////////
		
		
		////////// Blocking player to go outside of the map /////////////////////////
		if(transform.position.x < 0)
			transform.position.add(new Vector3f(1*delta, 0,0));
		
		if(transform.position.x > world.getWidth()*2-2)
			transform.position.add(new Vector3f(-1*delta, 0,0));
		
		if(transform.position.y > 0)
			transform.position.add(new Vector3f(0, -1*delta,0));
		
		if(transform.position.y < -(world.getHeight()*2-2))
			transform.position.add(new Vector3f(0, 1*delta,0));
		///////////////////////////////////////////////////////////////
		
		bounding_box.getCenter().set(transform.position.x, transform.position.y); // sets position of player

		/////////// Blocking player to enter solid tiles //////////////////////////////
		AABB[] boxes = new AABB[25];
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				boxes[i + j * 5] = world.getTileBoundingBox(
							(int)(((transform.position.x / 2) + 0.5f) - (5/2)) + i,
							(int)(((-transform.position.y / 2) + 0.5f) - (5/2)) + j
						);
			}
		}

		AABB box = null;
		for(int i = 0; i < boxes.length; i++) {
			if(boxes[i] != null) {
				if(box == null) box = boxes[i];

				Vector2f length1 = box.getCenter().sub(transform.position.x, transform.position.y, new Vector2f());
				Vector2f length2 = boxes[i].getCenter().sub(transform.position.x, transform.position.y, new Vector2f());

				if(length1.lengthSquared() > length2.lengthSquared()) {
					box = boxes[i];
				}
			}
		}
		if(box != null) {
			Collision data = bounding_box.getCollision(box);
			if(data.isIntersecting) {
				bounding_box.correctPosition(box, data);
				transform.position.set(bounding_box.getCenter(), 0);
			}
			
			for(int i = 0; i < boxes.length; i++) {
				if(boxes[i] != null) {
					if(box == null) box = boxes[i];
					
					Vector2f length1 = box.getCenter().sub(transform.position.x, transform.position.y, new Vector2f());
					Vector2f length2 = boxes[i].getCenter().sub(transform.position.x, transform.position.y, new Vector2f());
					
					if(length1.lengthSquared() > length2.lengthSquared()) {
						box = boxes[i];
					}
				}
			}
			
			data = bounding_box.getCollision(box);
			if(data.isIntersecting) {
				bounding_box.correctPosition(box, data);
				transform.position.set(bounding_box.getCenter(), 0);
			}
		}
		////////////////////////////////////////////////////////////////////////////////
		
		camera.getPosition().lerp(transform.position.mul(-world.getScale(), new Vector3f()), 0.05f); // Ustawienie poruszania siê kamery za graczem
		
		window = null;
		camera = null;
		world = null;
	}
	
	public void render(Shader shader, Camera camera) {
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", transform.getProjection(camera.getProjection()));
		texture.bind(0);
		model.render();
		
		shader = null;
		camera = null;
	}
}
