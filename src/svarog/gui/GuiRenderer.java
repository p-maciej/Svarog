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
	
	private Model model;
	private Shader shader;
	private Camera camera;
	
	private int windowWidth;
	private int windowHeight;
	
	private List<GuiObject> objects;
	
	public GuiRenderer(Window window, Camera camera, Shader shader) {
		this.objects = new ArrayList<GuiObject>();
		
		this.model = new Model(verticesArray, textureArray, indicesArray);
		this.camera = camera;
		this.shader = shader;
		
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
	
	public void renderGuiObjects() {
		Matrix4f projection = camera.getProjection();
		
		for(GuiObject object : objects) {
			if(object instanceof TextureObject)
				((TextureObject) object).getTexture().bind(0);
			
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", object.getTransform().getProjection(projection));
			model.render();	
		}

	}
	
	public void addGuiObject(GuiObject object) {
		objects.add(object);
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
		this.windowHeight = window.getHeight();
		this.windowWidth = window.getWidth();
		updatePositions();
	}
}
