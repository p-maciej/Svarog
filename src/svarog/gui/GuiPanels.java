package svarog.gui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import svarog.io.Window;
import svarog.render.Texture;

public class GuiPanels {
	private BufferedImage bottomPanel;
	private BufferedImage rightImage;
	
	public GuiPanels() {}
	
	public void addBottomPanel(BufferedImage image) {
		this.bottomPanel = image;
	}
	
	public void addRightPanel(BufferedImage image) {
		this.rightImage = image;
	}
	
	public ByteBuffer bottomPanel(int windowWidth) { 
		if(bottomPanel != null) {
			if(this.bottomPanel.getWidth() == 1) {
				ByteBuffer pixels = BufferUtils.createByteBuffer(windowWidth*bottomPanel.getHeight()*4);
				
				for(int i = 0; i < windowWidth; i++) {
					for(int j = 0; j < bottomPanel.getHeight(); j++) {
						int pixel = bottomPanel.getRGB(0, j);
						pixels.put(((byte)((pixel >> 16) & 0xFF)));
						pixels.put(((byte)((pixel >> 8) & 0xFF)));
						pixels.put((byte)(pixel & 0xFF));
						pixels.put(((byte)((pixel >> 24) & 0xFF)));
					}
				}
				pixels.flip();
				
				return pixels;
			} else
				throw new IllegalStateException("Wrong image size for bottom panel!");
		} else
			throw new IllegalStateException("Add bottom panel!");
	}
	
	public ByteBuffer rightPanel(int windowHeight) { 
		if(rightImage != null) {
			if(this.rightImage.getHeight() == 1) {
				ByteBuffer pixels = BufferUtils.createByteBuffer(windowHeight*rightImage.getWidth()*4);
				
				
				for(int j = 0; j < rightImage.getWidth(); j++) {
					for(int i = 0; i < windowHeight; i++) {
						int pixel = rightImage.getRGB(j, 0);
						pixels.put(((byte)((pixel >> 16) & 0xFF)));
						pixels.put(((byte)((pixel >> 8) & 0xFF)));
						pixels.put((byte)(pixel & 0xFF));
						pixels.put(((byte)((pixel >> 24) & 0xFF)));
					}
				}
				
				pixels.flip();
				
				return pixels;
			} else
				throw new IllegalStateException("Wrong image size for right panel!");
		} else
			throw new IllegalStateException("Add bottom panel!");
	}
	
	
	public void updateDynamicGuiElements(GuiRenderer guiRenderer, Window window) {		
		TextureObject redBlock = new TextureObject(new Texture(bottomPanel(window.getWidth()-140), window.getWidth()-140, bottomPanel.getHeight()), GuiRenderer.stickTo.Bottom);
		guiRenderer.addGuiObject(redBlock, GuiRenderer.State.guiPanel);
		
		TextureObject rightPanel = new TextureObject(new Texture(rightPanel(window.getHeight()-270), rightImage.getWidth(), window.getHeight()-270), GuiRenderer.stickTo.Right);
		rightPanel.move(0, -35);
		guiRenderer.addGuiObject(rightPanel, GuiRenderer.State.guiPanel);
	}
}
