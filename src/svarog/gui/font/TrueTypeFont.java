package svarog.gui.font;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;

public class TrueTypeFont {
	private int fontSize;
	private Font font;
	private boolean antialiasing;
	private Color color;
	
	private int stringWidth;
	private int stringHeight;
	
	
	public TrueTypeFont(String fileName, int size, boolean antialising) {
		this.setFont(fileName);
		this.setFontSize(size);
		this.setAntialiasing(antialising);
		
		this.color = new Color((byte)255, (byte)255, (byte)255);
	}

	public BufferedImage getFontImage(char ch) {
		Font tempfont = font.deriveFont((float)fontSize);
		
		BufferedImage tempfontImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
		
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(tempfont);
		
		FontMetrics fm = g.getFontMetrics();
		int charwidth = fm.charWidth(ch);

		if (charwidth <= 0) {
			charwidth = 1;
		}
		int charheight = fm.getHeight();
		if (charheight <= 0) {
			charheight = fontSize;
		}

		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth,charheight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D)fontImage.getGraphics();
		
        if(this.isAntialiasing())
        	gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		gt.setFont(tempfont);
	
		gt.setColor(new java.awt.Color(color.getR() & 0xFF, color.getG() & 0xFF, color.getB() & 0xFF, 255));

		
		gt.drawString(String.valueOf(ch), 0, fm.getAscent());
		
		return fontImage;
	}
	
	
	public ByteBuffer getStringBuffer(String string) {
		BufferedImage[] imageChars = new BufferedImage[string.length()];
		
		int stringWidth = 0;
		int stringHeight = 0;
		
		for(int i = 0; i < string.length(); i++) {
			imageChars[i] = getFontImage(string.charAt(i));
			
			stringWidth += imageChars[i].getWidth();
			stringHeight = imageChars[i].getHeight() > stringHeight ? imageChars[i].getHeight() : stringHeight;
		}

		this.stringWidth = stringWidth;
		this.stringHeight = stringHeight;
		
		ByteBuffer pixels = BufferUtils.createByteBuffer(stringWidth*stringHeight*4);
		
		for(int i = 0; i < string.length(); i++) {
			for(int lx = 0; lx < imageChars[i].getWidth(); lx++) {
				for(int ly = 0; ly < imageChars[i].getHeight(); ly++) {
					int pixel = imageChars[i].getRGB(lx, ly);
					pixels.put(((byte)((pixel >> 16) & 0xFF))); // red
					pixels.put(((byte)((pixel >> 8) & 0xFF)));  // green
					pixels.put((byte)(pixel & 0xFF)); 			// blue
					pixels.put(((byte)((pixel >> 24) & 0xFF))); // alpha	
				}
			}
		}
		pixels.flip();
		return pixels;
	}
	
	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontsize) {
		this.fontSize = fontsize;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(String fileName) {
		try {
			this.font = Font.createFont(Font.TRUETYPE_FONT, new File("./resources/fonts/" + fileName + ".ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isAntialiasing() {
		return antialiasing;
	}

	public void setAntialiasing(boolean antialiasing) {
		this.antialiasing = antialiasing;
	}
	
	public int getStringWidth() {
		return stringWidth;
	}

	public int getStringHeight() {
		return stringHeight;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color.set(color);
	}
}
