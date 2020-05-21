package svarog.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;


// This class determines the position and size of the element
public class Transform {
	private Vector3f position;
	private Vector3f scale;
	
	private int offsetY;
	private int offsetX;
	
	public Transform() {
		position = new Vector3f();
		scale = new Vector3f();
		this.setOffsetY(0);
		this.setOffsetX(0);
	}
	
	public Transform set(Transform transform) {
		this.position.set(transform.position);
		this.scale.set(transform.scale);
		this.offsetY = transform.offsetY;
		this.offsetX = transform.offsetX;
		
		transform = null;
		
		return this;
	}
	
	public Matrix4f getProjection(Matrix4f target) {
		target.translate(position);
		target.scale(scale);
		return target;
	}
	
	public Transform setPosition(float x, float y) {
		position.x = x;
		position.y = -y;
		
		return this;
	}
	
	public Transform setScale(float x, float y) {
		scale.x = x;
		scale.y = y;
		
		return this;
	}
	
	public Transform setPosition(Vector3f position) {
		this.position.set(scale);
		
		return this;
	}
	
	public Transform setScale(Vector3f scale) {
		this.scale.set(scale);
		
		return this;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getScale() {
		return scale;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offset) {
		this.offsetY = offset;
	}
	
	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offset) {
		this.offsetX = offset;
	}
}
