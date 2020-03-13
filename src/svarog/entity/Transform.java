package svarog.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
	public Vector3f position;
	public Vector3f scale;
	
	public Transform() {
		position = new Vector3f();
		scale = new Vector3f();
	}
	
	public Matrix4f getProjection(Matrix4f target) {
		target.scale(scale);
		target.translate(position);
		return target;
	}
}
