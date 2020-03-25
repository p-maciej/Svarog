package svarog.gui.font;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import svarog.render.Texture;

public class Font {
	private FontMap fontMap;
	private BufferedImage fontImage;
	private List<CharacterBuffer> characters;
	
	public Font(String font) {
		characters = new ArrayList<CharacterBuffer>();
		fontMap = new FontMap("fonts/"+font+".fnt");
		fontImage = Texture.getImageBuffer("fonts/" + font + ".png");
		
		cacheCharacters();
	}
	
	CharacterBuffer getCharacterBuffer(char ch) {
		for(CharacterBuffer character : characters)
			if(character.getAscii() == ch)
				return character;

		return null;
	}
	
	private void cacheCharacters() {
		for(int i = 0; i < fontMap.getNumerOfCharacters(); i++) {
			int width = fontMap.getWidth(i);
			int height = fontMap.getHeight(i);
			int startX = fontMap.getX(i);
			int startY = fontMap.getY(i);
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(width*height*4);
			
			for(int x = startX; x < startX+width; x++) {
				for(int y = startY; y < startY+height; y++) {
					int pixel = fontImage.getRGB(x, y);
					buffer.put(((byte)((pixel >> 16) & 0xFF))); // red
					buffer.put(((byte)((pixel >> 8) & 0xFF)));  // green
					buffer.put((byte)(pixel & 0xFF)); 			// blue
					buffer.put(((byte)((pixel >> 24) & 0xFF))); // alpha
				}
			}
			
			characters.add(new CharacterBuffer(buffer, (char)fontMap.getCharAscii(i), width, height));
		}
	}
	
	
	
	public ByteBuffer getTextBlock(String string, Color color) {
		int width[] = new int[string.length()];
		int height[] = new int[string.length()];
		int x[] = new int[string.length()];
		int y[] = new int[string.length()];
		
		int stringWidth = 0;
		int stringHeight = 0;
		
		for(int i = 0; i < string.length(); i++) {
			width[i] = fontMap.getWidth(string.charAt(i));
			height[i] = fontMap.getHeight(string.charAt(i));
			x[i] = fontMap.getX(string.charAt(i));
			y[i] = fontMap.getY(string.charAt(i));
			
			stringWidth += width[i];
			stringHeight = height[i] > stringHeight ? height[i] : stringHeight;
		}
		
		ByteBuffer pixels = BufferUtils.createByteBuffer(stringWidth*stringHeight*4);
		
		for(int i = 0; i < string.length(); i++) {
			for(int lx = x[i]; lx < x[i]+width[i]; lx++) {
				for(int ly = y[i]; ly < y[i]+height[i]; ly++) {
					int pixel = fontImage.getRGB(lx, ly);
					if(((byte)((pixel >> 24) & 0xFF)) != 0) {
						pixels.put(color.getR());
						pixels.put(color.getG());
						pixels.put(color.getB());
						pixels.put(((byte)((pixel >> 24) & 0xFF)));
					} else {
						pixels.put((byte)0);
						pixels.put((byte)0);
						pixels.put((byte)0);
						pixels.put(((byte)((pixel >> 24) & 0xFF))); 
					}		
				}
			}
		}
		pixels.flip();
		
		
		return pixels;
	}	
}
