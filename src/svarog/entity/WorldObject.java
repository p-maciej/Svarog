package svarog.entity;

import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.WorldRenderer;

public class WorldObject {
	private Texture texture;
	private Transform transform;
	
	public WorldObject(Texture texture, Transform transform) {
		this.setTexture(texture);
		this.setTransform(transform);
		
		transform.getScale().x = 1;
		transform.getScale().y = 1;
		
		float diff = (float)texture.getHeight() / (float)texture.getWidth();
		if(texture.getHeight() > texture.getWidth()) {
			transform.getScale().y = diff;
		}
		else if(texture.getWidth() > texture.getHeight()) {
			diff = (float)texture.getWidth() / (float)texture.getHeight();
			transform.setOffsetX((int)WorldRenderer.getScale());
			transform.getScale().x = diff;
		}
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}
}
