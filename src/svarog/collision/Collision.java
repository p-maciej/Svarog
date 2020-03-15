package svarog.collision;

import org.joml.Vector2f;

public class Collision {
	private Vector2f distance;
	private boolean isIntersecting;
	
	public Collision(Vector2f distance, boolean intersects) {
		this.distance = distance;
		this.isIntersecting = intersects;
	}
	
	public void setDistance(Vector2f distance) {
		this.distance = distance;
	}
	
	public Vector2f getDistance() {
		return distance;
	}
	
	public void setIsIntersecting(boolean state) {
		this.isIntersecting = state;
	}
	
	public boolean isIntersecting() {
		return isIntersecting;
	}
}