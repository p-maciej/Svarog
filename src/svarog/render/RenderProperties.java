package svarog.render;

public interface RenderProperties {
	static final float[] verticesArray = new float[] {
			-1f, 1f, 0,
			1f, 1f, 0,			
			1f, -1f, 0,
			-1f, -1f, 0,
	};
	
	static final float[] textureArray = new float[] {
			0, 0,
			0, 1,	
			1, 1,
			1, 0,
	};
	
	static final int[] indicesArray = new int[] {
			0,1,2,
			2,3,0
	};
	
}
