package svarog.render;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import svarog.io.Window;

public class Camera {
	private Vector3f position;
	private Matrix4f projection;
	
	
	public Camera(int width, int height) {
		position = new Vector3f(0,0,0);
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void addPosition(Vector3f position) {
		this.position.add(position);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Matrix4f getProjection() {
		return projection.translate(position, new Matrix4f());
	}
	
	public void setProjection(int width, int height, Window window, int scale, int worldWidth, int worldHeight) {
		projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
		
		
		int widthOffset = (window.getWidth()-worldWidth*(scale/2)*4)/2;		// This set world to center of the window
		int heightOffset = (window.getHeight()-worldHeight*(scale/2)*4)/2;
		
		Vector3f offset = new Vector3f(0, 0, 0);
		if(widthOffset > 0)
			offset.x = -widthOffset;
		if(heightOffset > 0)
			offset.y = heightOffset;

		projection.translate(offset);
		
		offset = null;
		window = null;
	}
}
