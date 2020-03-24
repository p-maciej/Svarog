package svarog.gui.font;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

import svarog.render.Texture;

public class Font {
	private FontMap fontMap;
	private BufferedImage fontImage;

	public Font(String font) {
		fontMap = new FontMap("fonts/"+font+".fnt");
		fontImage = Texture.getImageBuffer("fonts/" + font + ".png");
	}
	
	public TextBlock getTextBlock(String string, Color color) {
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
		
		
		return new TextBlock(pixels, stringWidth, stringHeight);
	}	
}
