package svarog.entity;
import org.joml.Vector2f;
import org.joml.Vector3f;

import svarog.audio.Audio;
import svarog.collision.AABB;
import svarog.collision.Collision;
import svarog.io.Window;
import svarog.objects.MouseInteraction;
import svarog.render.Animation;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.save.NpcParameters;
import svarog.world.World;
import svarog.world.WorldRenderer;

public abstract class Entity implements MouseInteraction {
	private static int auto_increment = 0;
	
	/// Model ////
	protected Texture texture;
	protected Transform transform;
	protected Transform textureTransform;
	private AABB bounding_box;
	//////////////
	
	//// Properties ////
	private int id;
	private int objectId;
	private boolean isStatic = true;
	private boolean fullBoundingBox;
	
	protected Direction currentDirection;
	protected boolean[] isColliding = new boolean[2];
	
	private String entityName;
	
	private boolean isClickable;
	private boolean isMovable;
	private boolean isOverable;
	
	//Data
	private int respownInSec;
	
	public static enum Direction {
		left,
		right,
		down,
		up
	}
	
	// Animation constructor
	public Entity(int id, Animation animation, Transform transform, boolean fullBoundingBox) {	
		this.objectId = auto_increment++;
		this.setId(id);
		this.setRespownInSec(0);
		
		this.entityName = new String();
		//this.animation = animation;
		this.transform = transform;
		this.setFullBoundingBox(fullBoundingBox);
		
		float diff = (float)animation.getHeight() / (float)animation.getWidth();
		if(animation.getHeight() > animation.getWidth())
			transform.getScale().y = diff;
		else
			transform.getScale().x = diff;
		
		setEntityProperties();
	}
	
	// Texture constructor
	public Entity(int id, Texture texture, Transform transform, boolean fullBoundingBox) {		
		this.objectId = auto_increment++;
		this.setId(id);
		this.setRespownInSec(0);
		
		this.entityName = new String();
		this.texture = texture;
		this.transform = transform;
		this.setFullBoundingBox(fullBoundingBox);
		
		float diff = (float)texture.getHeight() / (float)texture.getWidth();
		if(texture.getHeight() > texture.getWidth())
			transform.getScale().y = diff;
		else
			transform.getScale().x = diff;
		
		setEntityProperties();	
	}
	
	public Entity(int id, NpcParameters npcParameters) {
		if(npcParameters.getTexturePath().isEmpty()) {
			this.objectId = auto_increment++;
			this.setId(id);
			this.setRespownInSec(0);
			
			this.setName(npcParameters.getName());
			this.transform = new Transform().setPosition(npcParameters.getPosX(), npcParameters.getPosY());
			this.transform.getPosition().x *= 2;
			this.transform.getPosition().y *= 2;
		}else {
			this.objectId = auto_increment++;
			this.setId(id);
			this.setRespownInSec(0);
			
			this.entityName = npcParameters.getName();
			this.texture = new Texture(npcParameters.getTexturePath());
			this.transform = new Transform().setPosition(npcParameters.getPosX(), npcParameters.getPosY());
			this.setFullBoundingBox(npcParameters.isFullBoundingBox());
			
			float diff = (float)texture.getHeight() / (float)texture.getWidth();
			if(texture.getHeight() > texture.getWidth())
				transform.getScale().y = diff;
			else
				transform.getScale().x = diff;
			
			setEntityProperties();	
		}
	}
	
	public Entity(int id, NpcParameters npcParameters, Transform transform) {
		if(npcParameters.getTexturePath().isEmpty()) {
			this.objectId = auto_increment++;
			this.setId(id);
			this.setRespownInSec(0);
			
			this.setName(npcParameters.getName());
			this.transform = transform;
			this.transform.getPosition().x *= 2;
			this.transform.getPosition().y *= 2;
		}else {
			this.objectId = auto_increment++;
			this.setId(id);
			this.setRespownInSec(0);
			
			this.entityName = npcParameters.getName();
			this.texture = new Texture(npcParameters.getTexturePath());
			this.transform = transform;
			this.setFullBoundingBox(npcParameters.isFullBoundingBox());
			
			float diff = (float)texture.getHeight() / (float)texture.getWidth();
			if(texture.getHeight() > texture.getWidth())
				transform.getScale().y = diff;
			else
				transform.getScale().x = diff;
			
			setEntityProperties();	
		}
	}
	
	public Entity(int id, Transform transform, String name) {
		this.objectId = auto_increment++;
		this.setId(id);
		this.setRespownInSec(0);
		
		this.setName(name);
		this.transform = transform;
		this.transform.getPosition().x *= 2;
		this.transform.getPosition().y *= 2;
	}
	
	// This sets correct scale of entity
	private void setEntityProperties() {
		this.transform.getPosition().x *= 2;
		this.transform.getPosition().y *= 2;
		
		if(transform.getScale().x < 1)
			transform.getScale().x = 1;
		if(transform.getScale().y < 1)
			transform.getScale().y = 1;	
		
		transform.setOffset((int)WorldRenderer.getScale());
		
		float offset = 0;
		if(fullBoundingBox == false)
			offset = -1;
			
		bounding_box = new AABB(new Vector2f(transform.getPosition().x, transform.getPosition().y), new Vector2f(transform.getScale().x,transform.getScale().y+offset));
	}
	
	
	
	// Moves character trough the map
	public void move(Vector2f direction) {
		transform.getPosition().add(new Vector3f(direction, 0));
		bounding_box.getCenter().set(transform.getPosition().x, transform.getPosition().y);
		direction = null;
	}
	
	public void setPosition(int x, int y) {
		transform.setPosition(x*2, y*2);
		bounding_box.getCenter().set(transform.getPosition().x, transform.getPosition().y);
	}
	
	// Enables collision with world (map)
	public void collideWithTiles(World world) {	
		isColliding[0] = false;
		if(!this.isStatic()) {
			for(int x = 0; x < world.getWidth(); x++) {
				for(int y = 0; y < world.getHeight(); y++) {
					AABB box = world.getTileBoundingBox(x, y);
					
					if(box != null) {
						Collision collision = box.getCollision(bounding_box);
						
						if(collision.isIntersecting()) {
							bounding_box.correctPosition(box, collision);
							transform.getPosition().set(bounding_box.getCenter(), 0);
							isColliding[0] = true;
						}
					}
				}
			}
		}
		world = null;
	}
	
	// Enables collision with another entities
	public void collideWithEntities(World world) {
		isColliding[1] = false;
		if(!this.isStatic()) { 
			for(int i = 0; i < world.numberOfEntities(); i++) {
				if(world.getEntity(i).id != this.id) {		
					if(world.getEntity(i).getBoduningBox() != null) {
						Collision collision = bounding_box.getCollision(world.getEntity(i).getBoduningBox());
						
						if(collision.isIntersecting()) {
							if(world.getEntity(i).isStatic == false) {
								collision.getDistance().x /= 2;
								collision.getDistance().y /= 2;
							}
							
							bounding_box.correctPosition(world.getEntity(i).getBoduningBox(), collision);
							transform.getPosition().set(bounding_box.getCenter(), 0);
							
							
							
							if(world.getEntity(i).isStatic == false) {
								isColliding[1] = false;
								world.getEntity(i).bounding_box.correctPosition(bounding_box, collision);
								world.getEntity(i).transform.getPosition().set(world.getEntity(i).bounding_box.getCenter().x, world.getEntity(i).bounding_box.getCenter().y, 0);
							} else {
								isColliding[1] = true;
							}
						}
					}
				}
			}
		}
		world = null;
	}
	
	public void update(float delta, Window window, Camera camera, WorldRenderer world, Audio audioPlayer) {	
		if(!this.isStatic()) {
			////////// Blocking player to go outside of the map ///////////
			if(transform.getPosition().x < 1)
				transform.getPosition().add(new Vector3f(1*delta, 0,0));
			
			if(transform.getPosition().x > world.getWorld().getWidth()*2-1.8f)
				transform.getPosition().add(new Vector3f(-1*delta, 0,0));
			
			if(transform.getPosition().y > -1)
				transform.getPosition().add(new Vector3f(0, -1*delta,0));
			
			if(transform.getPosition().y < -(world.getWorld().getHeight()*2-1.6f))
				transform.getPosition().add(new Vector3f(0, 1*delta,0));
			///////////////////////////////////////////////////////////////

		}
	}
	
	public Entity setIsStatic(boolean state) {
		this.isStatic = state;
		return this;
	}
	
	public Entity setFullBoundingBox(boolean state) {
		this.fullBoundingBox = state;
		return this;
	}
	
	public boolean getFullBoundingBox() {
		return fullBoundingBox;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public AABB getBoduningBox() {
		return bounding_box;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	protected void setTexture(Direction direction, Texture texture) {
		texture.prepare();
		this.currentDirection = direction;
		this.texture = texture;
	}
	
	public String getName() {
		return entityName;
	}

	public void setName(String entityName) {
		this.entityName = entityName;
	}

	public int getObjectId() {
		return objectId;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public boolean isEntityColliding() {
		return(isColliding[0]||isColliding[1]);
	}
	public int getRespownInSec() {
		return respownInSec;
	}
	public void setRespownInSec(int respownInSec) {
		this.respownInSec = respownInSec;
	}
	
	public abstract boolean isClicked();

	@Override
	public boolean isClickable() {
		return isClickable;
	}

	@Override
	public boolean isMovable() {
		return isMovable;
	}

	@Override
	public boolean isOverable() {
		return isOverable;
	}

	@Override
	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}

	@Override
	public void setMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}

	@Override
	public void setOverable(boolean isOverable) {
		this.isOverable = isOverable;
	}
}