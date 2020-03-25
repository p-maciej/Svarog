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
	
	public Font(String font, Color color) {
		characters = new ArrayList<CharacterBuffer>();
		fontMap = new FontMap("fonts/"+font+".fnt");
		fontImage = Texture.getImageBuffer("fonts/" + font + ".png");
		
		cacheCharacters(color);
	}
	
	CharacterBuffer getCharacterBuffer(char ch) {
		for(CharacterBuffer character : characters)
			if(character.getAscii() == ch)
				return character;

		return null;
	}
	
	private void cacheCharacters(Color color) {
		int typicalHeight = 0;
		if(fontMap.getNumerOfCharacters() > 0) 
			typicalHeight = fontMap.getHeight(0);
		
		for(int i = 0; i < fontMap.getNumerOfCharacters(); i++) {
			int width = fontMap.getWidth(i);
			int height = fontMap.getHeight(i);
			if(typicalHeight != height)
				throw new IllegalStateException("Height of all characters mus be same size");
			
			int startX = fontMap.getX(i);
			int startY = fontMap.getY(i);
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(width*height*4);
			
			for(int x = startX; x < startX+width; x++) {
				for(int y = startY; y < startY+height; y++) {
					int pixel = fontImage.getRGB(x, y);
					if(((byte)((pixel >> 24) & 0xFF)) != 0) {
						buffer.put(color.getR());
						buffer.put(color.getG());
						buffer.put(color.getB());
						buffer.put(((byte)((pixel >> 24) & 0xFF)));
					} else {
						buffer.put((byte)0);
						buffer.put((byte)0);
						buffer.put((byte)0);
						buffer.put(((byte)((pixel >> 24) & 0xFF))); 
					}	
				}
			}
			
			characters.add(new CharacterBuffer(buffer, (char)fontMap.getCharAscii(i), width, height));
		}
	}	
}
