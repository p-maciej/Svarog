package svarog.gui;

import org.joml.Matrix4f;

import svarog.render.Camera;
import svarog.render.Model;
import svarog.render.Shader;
import svarog.render.Texture;

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
	
	private Model model;
	
	public GuiRenderer() {
		model = new Model(verticesArray, textureArray, indicesArray);
	}
	
	public void renderGuiObject(Shader shader, Texture texture, Camera camera) {
		Matrix4f projection = camera.getProjection().scale(100);
		texture.bind(0);
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", projection);
		model.render();	
	}
}
