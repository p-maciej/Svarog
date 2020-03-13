package svarog.render;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
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
			image = ImageIO.read(new File("./resources/textures/" + filename));
			width = image.getWidth();
			height = image.getHeight();
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					int pixel = image.getRGB(j, i);
					pixels.put(((byte)((pixel >> 16) & 0xFF))); // red
					pixels.put(((byte)((pixel >> 8) & 0xFF)));  // green
					pixels.put((byte)(pixel & 0xFF)); 			// blue
					pixels.put(((byte)((pixel >> 24) & 0xFF))); // alpha
				}
			}
			
			pixels.flip();
			
			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			
			pixels.clear();
			image.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Texture(String filename, int tileInWidth, int tileInHeight, int tileSize) {
		BufferedImage image;
		
		try {
			image = ImageIO.read(new File("./resources/textures/" + filename));
			width = height = tileSize;
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(tileSize*tileSize*4);
			
			for(int i = tileInWidth*tileSize; i < tileInWidth*tileSize + tileSize; i++) {
				for(int j = tileInHeight*tileSize; j < tileInHeight*tileSize + tileSize; j++) {
					int pixel = image.getRGB(j, i);
					pixels.put(((byte)((pixel >> 16) & 0xFF))); // red
					pixels.put(((byte)((pixel >> 8) & 0xFF)));  // green
					pixels.put((byte)(pixel & 0xFF)); 			// blue
					pixels.put(((byte)((pixel >> 24) & 0xFF))); // alpha
				}
			}
			
			pixels.flip();
			
			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tileSize, tileSize, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			
			pixels.clear();
			image.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		filename = null;
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
	
	protected void finalize() throws Throwable {
		glDeleteTextures(id);
		super.finalize();
	}
}
