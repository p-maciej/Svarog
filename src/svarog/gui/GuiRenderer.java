package svarog.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import svarog.render.Camera;
import svarog.render.Model;
import svarog.render.Shader;
import svarog.io.Window;

public class GuiRenderer {
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
	
	public static enum stickTo {
		Top,
		Bottom,
		Left,
		Right,
		TopLeft,
		TopRight,
		BottomLeft,
		BottomRight
	}
	
	public static enum State {
		staticImage,
		dynamicImage
	}
	
	private Model model;
	private Camera camera;
	
	private int windowWidth;
	private int windowHeight;
	
	private List<GuiObject> objects;
	
	public GuiRenderer(Window window) {
		this.objects = new ArrayList<GuiObject>();
		
		this.model = new Model(verticesArray, textureArray, indicesArray);
		this.camera = new Camera();
		
		camera.setProjection(window.getWidth(), window.getHeight());
		
		this.windowWidth = window.getWidth();
		this.windowHeight = window.getHeight();
	}
	
	public void updatePositions() {
		for(GuiObject object : objects) {
			if(object.getStickTo() != null) {
				switch(object.getStickTo()) {
					case Top:
						object.setPosition(0, getTop() - object.getHeight()/2);
						break;
					case Bottom:
						object.setPosition(0, getBottom() + object.getHeight()/2);
						break;
					case BottomLeft:
						object.setPosition(getLeft() + object.getWidth()/2, getBottom() + object.getHeight()/2);
						break;
					case BottomRight:
						object.setPosition(getRight() - object.getWidth()/2, getBottom() + object.getHeight()/2);
						break;
					case Left:
						object.setPosition(getLeft() + object.getWidth()/2, 0);
						break;
					case Right:
						object.setPosition(getRight() - object.getWidth()/2, 0);
						break;
					case TopLeft:
						object.setPosition(getLeft() + object.getWidth()/2, getTop() - object.getHeight()/2);
						break;
					case TopRight:
						object.setPosition(getRight() - object.getWidth()/2, getTop() - object.getHeight()/2);
						break;
				}
			}
			
			object.getTransform().getPosition().add(object.getMove().x, object.getMove().y, 0);
		}
	}
	
	public void renderGuiObjects(Shader shader) {	
		// Dynamic images render first (they are deleted every window resize)
		for(GuiObject object : objects) {
			if(object.getState() == State.dynamicImage) {
				Matrix4f projection = camera.getProjection();
					((TextureObject) object).getTexture().bind(0);
					
				shader.bind();
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", object.getTransform().getProjection(projection));
				model.render();	
			}
		}
		
		// Then render everything else in adding order
		for(GuiObject object : objects) {
			if(object.getState() != State.dynamicImage) {
				Matrix4f projection = camera.getProjection();
					((TextureObject) object).getTexture().bind(0);
					
				shader.bind();
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", object.getTransform().getProjection(projection));
				model.render();	
			}
		}

	}
	
	public void addGuiObject(GuiObject object) {
		objects.add(object);
	}
	
	public void addGuiObject(GuiObject object, State state) {
		objects.add(object.setState(state));
	}
	
	public float getRight() {
		return windowWidth/2;
	}
	
	public float getLeft() {
		return -windowWidth/2;
	}
	
	public float getTop() {
		return windowHeight/2;
	}
	
	public float getBottom() {
		return -windowHeight/2;
	}
	
	public void update(Window window) {
		camera.setProjection(window.getWidth(), window.getHeight());
		this.windowHeight = window.getHeight();
		this.windowWidth = window.getWidth();
		updatePositions();
	}
	
	public void deleteDynamicElements() {
		for(int i = 0; i < objects.size(); i++)
			if(objects.get(i).getState() != null)
				if(objects.get(i).getState() == State.dynamicImage)
					objects.remove(i);
	}
}
