package svarog.render;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	public int id;
	public int width;
	public int height;
	public String filename;
	
	public Texture(String filename) {
		BufferedImage image;
		this.filename = filename;
		try {
			image = ImageIO.read(new File("./resources/" + filename));
			width = image.getWidth();
			height = image.getHeight();
			
		
			ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					int pixel = image.getRGB(i, j);
					pixels.put(((byte)((pixel >> 16) & 0xFF))); // red
					pixels.put(((byte)((pixel >> 8) & 0xFF)));  // green
					pixels.put((byte)(pixel & 0xFF)); 			// blue
					pixels.put(((byte)((pixel >> 24) & 0xFF))); // alpha
				}
			}
			
			
			pixels.flip();
			
			textureInit(pixels, width, height);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Texture(BufferedImage image, int tileInWidth, int tileInHeight, int tileSize) {
			if(width%tileSize == 0 && height%tileSize == 0) {	
				ByteBuffer pixels = BufferUtils.createByteBuffer(tileSize*tileSize*4);
				
				for(int i = tileInWidth*tileSize; i < tileInWidth*tileSize + tileSize; i++) {
					for(int j = tileInHeight*tileSize; j < tileInHeight*tileSize + tileSize; j++) {
						int pixel = image.getRGB(i, j);
						pixels.put(((byte)((pixel >> 16) & 0xFF))); // red
						pixels.put(((byte)((pixel >> 8) & 0xFF)));  // green
						pixels.put((byte)(pixel & 0xFF)); 			// blue
						pixels.put(((byte)((pixel >> 24) & 0xFF))); // alpha
					}
				}
				pixels.flip();
				
				textureInit(pixels, tileSize, tileSize);
			} else {
				throw new IllegalStateException("Wrong tile size or texture size!");
			}
	}
	
	public Texture(ByteBuffer pixels, int tileSize) {
		this.width = this.height = tileSize;
		
		textureInit(pixels, tileSize, tileSize);
	}
	
	public Texture(ByteBuffer pixels, int width, int height) {
		this.width = width;
		this.height = height;
		
		textureInit(pixels, width, height);
	}
	
	private void textureInit(ByteBuffer buffer, int width, int height) {
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
				
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, height, width, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}
	
	public void bind(int sampler) {
		if(sampler >= 0 && sampler <= 31) {
			glActiveTexture(GL_TEXTURE0 + sampler);
			glBindTexture(GL_TEXTURE_2D, id);
		}
	}
	
	public String getFilename() {
		return filename;
	}
	
	public static BufferedImage getImageBuffer(String filename) {
		BufferedImage image;
			
		try {
			image = ImageIO.read(new File("./resources/" + filename));
				
			return image;
		} catch(IOException e) {
		 	e.printStackTrace();
		}
		
		return null;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
