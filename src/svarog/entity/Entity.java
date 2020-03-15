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
	protected Transform textureTransform;
	private AABB bounding_box;
	//////////////
	
	//// Properties ////
	private int id;
	private boolean isStatic = true;
	private boolean fullBoundingBox;
	
	
	// Animation constructor
	public Entity(Animation animation, Transform transform, boolean fullBoundingBox) {	
		id = auto_increment;
		auto_increment++;
		
		model = new Model(verticesArray, textureArray, indicesArray);
		this.animation = animation;
		this.transform = transform;
		this.setFullBoundingBox(fullBoundingBox);
		
		setEntityProperties();
	}
	
	// Texture constructor
	public Entity(Texture texture, Transform transform, boolean fullBoundingBox) {		
		id = auto_increment;
		auto_increment++;
		
		model = new Model(verticesArray, textureArray, indicesArray);
		this.texture = texture;
		this.transform = transform;
		this.setFullBoundingBox(fullBoundingBox);
		
		float diff = (float)texture.height / (float)texture.width;
		if(texture.height > texture.width)
			transform.getScale().y = diff;
		else
			transform.getScale().x = diff;
		
		setEntityProperties();	
	}
	
	// This sets correct scale of entity
	private void setEntityProperties() {
		this.transform.getPosition().x *= 2;
		this.transform.getPosition().y *= 2;
		
		if(transform.getScale().x < 1)
			transform.getScale().x = 1;
		if(transform.getScale().y < 1)
			transform.getScale().y = 1;	
		
		float offset = 0;
		if(fullBoundingBox == false)
			offset = -1;
			
		bounding_box = new AABB(new Vector2f(transform.getPosition().x, transform.getPosition().y), new Vector2f(transform.getScale().x,transform.getScale().y+offset));
	}
	
	
	
	// Moves character trough the map
	public void move(Vector2f direction) {
		transform.getPosition().add(new Vector3f(direction, 0));
		bounding_box.getCenter().add(direction);
		direction = null;
	}
	
	// Enables collision with world (map)
	public void collideWithTiles(World world) {	
		for(int x = 0; x < world.getWidth(); x++) {
			for(int y = 0; y < world.getHeight(); y++) {
				AABB box = world.getTileBoundingBox(x, y);
				
				if(box != null) {
					Collision collision = box.getCollision(bounding_box);
					
					if(collision.isIntersecting()) {
						bounding_box.correctPosition(box, collision);
						transform.getPosition().set(bounding_box.getCenter(), 0);
					}	
				}
			}
		}

		world = null;
	}
	
	// Enables collision with another entities
	public void collideWithEntities(World world) {
		for(int i = 0; i < world.numberOfEntities(); i++) {
			if(world.getEntity(i).id != this.id) {
				
				Collision collision = bounding_box.getCollision(world.getEntity(i).getBoduningBox());
				
				if(collision.isIntersecting()) {
					if(world.getEntity(i).isStatic == false) {
						collision.getDistance().x /= 2;
						collision.getDistance().y /= 2;
					}
					
					bounding_box.correctPosition(world.getEntity(i).getBoduningBox(), collision);
					transform.getPosition().set(bounding_box.getCenter(), 0);
					
					if(world.getEntity(i).isStatic == false) {
						world.getEntity(i).bounding_box.correctPosition(bounding_box, collision);
						world.getEntity(i).transform.getPosition().set(world.getEntity(i).bounding_box.getCenter().x, world.getEntity(i).bounding_box.getCenter().y, 0);
					}
				}
			}
		}
		
		world = null;
	}
	
	public void update(float delta, Window window, Camera camera, World world) {	
		////////// Blocking player to go outside of the map ///////////
		if(transform.getPosition().x < 1)
			transform.getPosition().add(new Vector3f(1*delta, 0,0));
		
		if(transform.getPosition().x > world.getWidth()*2-3)
			transform.getPosition().add(new Vector3f(-1*delta, 0,0));
		
		if(transform.getPosition().y > -1)
			transform.getPosition().add(new Vector3f(0, -1*delta,0));
		
		if(transform.getPosition().y < -(world.getHeight()*2-3))
			transform.getPosition().add(new Vector3f(0, 1*delta,0));
		///////////////////////////////////////////////////////////////

		
		window = null;
		camera = null;
		world = null;
	}
	
	// Character rendering
	public void render(Shader shader, Camera camera, World world) {
		Matrix4f target = camera.getProjection();
		target.mul(world.getWorld());
		
		Transform temp = new Transform().set(transform);
		
		if(fullBoundingBox == false)
			temp.getPosition().y += 1f; // This sets offset in texture rendering when entity should walk like on foots
			
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", temp.getProjection(target));
		
		
		
		if(animation == null)
			this.texture.bind(0);
		else
			this.animation.bind(0);
		
		this.model.render();
		
		shader = null;
		camera = null;
		world = null;
		target = null;
	}
	
	public Entity setIsStatic(boolean state) {
		this.isStatic = state;
		return this;
	}
	
	public Entity setFullBoundingBox(boolean state) {
		this.fullBoundingBox = state;
		return this;
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public AABB getBoduningBox() {
		return bounding_box;
	}
}