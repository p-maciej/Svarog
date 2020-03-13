package svarog.entity;

import org.joml.Matrix4f;
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

public class Entity {
	private static final float[] verticesArray = new float[] {
			-1f, 1f, 0,
			1f, 1f, 0,			
			1f, -1f, 0,
			-1f, -1f, 0,
	};
	
	private static final float[] textureArray = new float[] {
			0, 0,
			1, 0,	
			1, 1,
			0, 1,
	};
	
	private static final int[] indicesArray = new int[] {
			0,1,2,
			2,3,0
	};
	
	private Model model;
	private Animation texture;
	private Transform transform;
	private AABB bounding_box;
	
	
	public Entity(Animation animation, Transform transform) {		
		model = new Model(verticesArray, textureArray, indicesArray);
		this.texture = animation;
		
		this.transform = transform;
		transform.scale = new Vector3f(1,1,0);
		
		bounding_box = new AABB(new Vector2f(transform.position.x, transform.position.y), new Vector2f(1,1));
	}
	
	public void move(Vector2f direction) {
		transform.position.add(new Vector3f(direction, 0));
		bounding_box.getCenter().set(transform.position.x, transform.position.y);
	}
	
	public void update(float delta, Window window, Camera camera, World world) {	
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
	
	public void render(Shader shader, Camera camera, World world) {
		Matrix4f target = camera.getProjection();
		target.mul(world.getWorld());
		
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", transform.getProjection(target));
		this.texture.bind(0);
		this.model.render();
		
		shader = null;
		camera = null;
	}
}