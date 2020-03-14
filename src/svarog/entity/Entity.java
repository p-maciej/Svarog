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
import svarog.render.Texture;
import svarog.world.World;

public class Entity {
	private static int auto_increment = 0;
	
	private static final float[] verticesArray = new float[] {
			-1f, 1f, 0,
			1f, 1f, 0,			
			1f, -1f, 0,
			-1f, -1f, 0,
	};
	
	private static final float[] textureArray = new float[] {
			0, 0,
			0, 1,	
			1, 1,
			1, 0,
	};
	
	private static final int[] indicesArray = new int[] {
			0,1,2,
			2,3,0
	};
	
	/// Model ////
	private Model model;
	private Animation animation;
	private Texture texture;
	protected Transform transform;
	private AABB bounding_box;
	//////////////
	
	//// Properties ////
	private int id;
	private boolean isStatic = true;
	
	
	public Entity(Animation animation, Transform transform) {	
		id = auto_increment;
		auto_increment++;
		
		model = new Model(verticesArray, textureArray, indicesArray);
		this.animation = animation;
		
		this.transform = transform;
		
		if(transform.scale.x < 1)
			transform.scale.x = 1;
		if(transform.scale.y < 1)
			transform.scale.y = 1;
			
		
		bounding_box = new AABB(new Vector2f(transform.position.x, transform.position.y), new Vector2f(transform.scale.x,transform.scale.y));
	}
	
	public Entity(Texture texture, Transform transform) {		
		id = auto_increment;
		auto_increment++;
		
		model = new Model(verticesArray, textureArray, indicesArray);
		this.texture = texture;
		
		this.transform = transform;
		
		this.transform.position.x *= 2;
		this.transform.position.y *= 2;
		
		if(transform.scale.x < 1)
			transform.scale.x = 1;
		if(transform.scale.y < 1)
			transform.scale.y = 1;
			
		
		float diff = (float)texture.height / (float)texture.width;
		if(texture.height > texture.width)
			transform.scale.y = diff;
		else
			transform.scale.x = diff;
		
		bounding_box = new AABB(new Vector2f(transform.position.x, transform.position.y), new Vector2f(transform.scale.x,transform.scale.y));		
	}
	
	
	
	
	public void move(Vector2f direction) {
		transform.position.add(new Vector3f(direction, 0));
		bounding_box.getCenter().set(transform.position.x, transform.position.y);
		direction = null;
	}
	
	public void collideWithTiles(World world) {	
		for(int x = 0; x < world.getWidth(); x++) {
			for(int y = 0; y < world.getHeight(); y++) {
				AABB box = world.getTileBoundingBox(x, y);
				
				if(box != null) {
					Collision collision = box.getCollision(bounding_box);
					
					if(collision.isIntersecting) {
						bounding_box.correctPosition(box, collision);
						transform.position.set(bounding_box.getCenter(), 0);
					}	
				}
			}
		}
		
		

		world = null;
	}
	
	public void collideWithEntities(World world) {
		for(int i = 0; i < world.numberOfEntities(); i++) {
			if(world.getEntity(i).id != this.id) {
				
				Collision collision = bounding_box.getCollision(world.getEntity(i).getBoduningBox());
				
				if(collision.isIntersecting) {
					if(world.getEntity(i).isStatic == false) {
						collision.distance.x /= 2;
						collision.distance.y /= 2;
					}
					
					bounding_box.correctPosition(world.getEntity(i).getBoduningBox(), collision);
					transform.position.set(bounding_box.getCenter(), 0);
					
					if(world.getEntity(i).isStatic == false) {
						world.getEntity(i).bounding_box.correctPosition(bounding_box, collision);
						world.getEntity(i).transform.position.set(world.getEntity(i).bounding_box.getCenter().x, world.getEntity(i).bounding_box.getCenter().y, 0);
					}
				}
			}
		}
		
		world = null;
	}
	
	public void update(float delta, Window window, Camera camera, World world) {	
		////////// Blocking player to go outside of the map /////////////////////////
		if(transform.position.x < 1)
			transform.position.add(new Vector3f(1*delta, 0,0));
		
		if(transform.position.x > world.getWidth()*2-3)
			transform.position.add(new Vector3f(-1*delta, 0,0));
		
		if(transform.position.y > -1)
			transform.position.add(new Vector3f(0, -1*delta,0));
		
		if(transform.position.y < -(world.getHeight()*2-3))
			transform.position.add(new Vector3f(0, 1*delta,0));
		///////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////////////
		
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
		
		if(animation == null)
			this.texture.bind(0);
		else
			this.animation.bind(0);
		
		this.model.render();
		
		shader = null;
		camera = null;
		world = null;
	}
	
	public Entity setIsStatic(boolean state) {
		this.isStatic = state;
		
		return this;
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public AABB getBoduningBox() {
		return bounding_box;
	}
}