package svarog.gui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import svarog.gui.GuiRenderer.State;
import svarog.gui.font.Line;
import svarog.render.Texture;

public class BubbleContainer {
	private static TextureObject bubbleLeft;
	private static TextureObject bubbleRight;
	private static BufferedImage bubbleCenter;
	
	private int bubbleXoffest;
	private int bubbleYoffset;

	BubbleContainer(int bubbleXoffset, int bubbleYoffset) {
		this.setBubbleXoffest(bubbleXoffset);
		this.setBubbleYoffset(bubbleYoffset);
	}
	
	Group getBubble(Line line, double posX, double posY) {
		if(bubbleLeft != null && bubbleRight != null && bubbleCenter != null) {
			Group group = new Group(State.dynamicImage);
			bubbleLeft.setPosition((float)posX+bubbleXoffest, (float)posY+bubbleYoffset);
			group.addTextureObject(bubbleLeft);
			
			ByteBuffer contentBackground = BufferUtils.createByteBuffer((int)(bubbleCenter.getHeight()*line.getWidth()*4));
			
			for(int i = 0; i < line.getWidth(); i++) {
				for(int j = 0; j < bubbleCenter.getHeight(); j++) {
					int pixel = bubbleCenter.getRGB(0, j);
					contentBackground.put(((byte)((pixel >> 16) & 0xFF)));
					contentBackground.put(((byte)((pixel >> 8) & 0xFF)));
					contentBackground.put((byte)(pixel & 0xFF));
					contentBackground.put(((byte)((pixel >> 24) & 0xFF)));
				}
			}
			contentBackground.flip();
			
			TextureObject center = new TextureObject(new Texture(contentBackground, line.getWidth(), bubbleCenter.getHeight()), (float)(posX+line.getWidth()/2+bubbleLeft.getWidth()/2+bubbleXoffest), (float)(posY+bubbleYoffset));
			group.addTextureObject(center);
			
			line.setPosition((float)(posX+line.getWidth()/2+bubbleLeft.getWidth()/2+bubbleXoffest), (float)(posY+bubbleYoffset-bubbleCenter.getHeight()/2 +line.getHeight()/2));
			group.addTextureObject(line);
			
			bubbleRight.setPosition((float)posX+bubbleXoffest+line.getWidth()+bubbleLeft.getWidth()-3, (float)posY+bubbleYoffset);
			group.addTextureObject(bubbleRight);
			
			return group;
		} else 
			throw new IllegalStateException("Bubble textures isn't declared!");
	}
	
	int getBubbleXoffest() {
		return bubbleXoffest;
	}

	void setBubbleXoffest(int bubbleXoffest) {
		this.bubbleXoffest = bubbleXoffest;
	}

	int getBubbleYoffset() {
		return bubbleYoffset;
	}

	void setBubbleYoffset(int bubbleYoffset) {
		this.bubbleYoffset = bubbleYoffset;
	}
	
	public static void setBubbleLeft(BufferedImage bubbleL) {
		TextureObject tempBubbleLeft = new TextureObject(new Texture(bubbleL));
		bubbleLeft = tempBubbleLeft;
	}

	public static void setBubbleRight(BufferedImage bubbleR) {
		TextureObject tempBubbleRight = new TextureObject(new Texture(bubbleR));
		bubbleRight = tempBubbleRight;
	}

	public static void setBubbleCenter(BufferedImage bubbleC) {
		bubbleCenter = bubbleC;
	}
}
