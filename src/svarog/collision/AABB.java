package svarog.collision;

import org.joml.Vector2f;

public class AABB {
	private Vector2f center;
	private Vector2f half_extent;
	
	public AABB(Vector2f center, Vector2f half_extent) {
		this.center = center;
		this.half_extent = half_extent;
	}
	
	public Collision getCollision(AABB box) {
		Vector2f distance = box.center.sub(this.center, new Vector2f());

		distance.x = (float)Math.abs(distance.x);
		distance.y = (float)Math.abs(distance.y);
		
		distance.sub(this.half_extent.add(box.half_extent, new Vector2f()));
		
		
		box = null;
		return new Collision(distance, distance.x < 0 && distance.y < 0);
	}
	
	public Vector2f getCenter() { 
		return center; 
	}
	
	public Vector2f getHalfExtent() { 
		return half_extent; 
	}

	public void correctPosition(AABB box, Collision data) {
		Vector2f correction = box.center.sub(center, new Vector2f());
		if(data.getDistance().x > data.getDistance().y) {
			if(correction.x > 0) {
				center.add(data.getDistance().x, 0);
			}else{
				center.add(-data.getDistance().x, 0);
			}
		}else{
			if(correction.y > 0) {
				center.add(0, data.getDistance().y);
			}else{
				center.add(0, -data.getDistance().y);
			}
		}
		
		box = null;
		data = null;
	}
}
