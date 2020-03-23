package svarog.gui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import svarog.io.Window;
import svarog.render.Texture;

public class GuiPanels {
	private BufferedImage bottomPanel;
	
	public GuiPanels() {}
	
	public void addBottomPanel(BufferedImage image) {
		this.bottomPanel = image;
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
	
	
	public void updateDynamicGuiElements(GuiRenderer guiRenderer, Window window) {
		TextureObject redBlock = new TextureObject(new Texture(bottomPanel(window.getWidth()), window.getWidth(), bottomPanel.getHeight()), GuiRenderer.stickTo.Bottom);
		guiRenderer.addGuiObject(redBlock, GuiRenderer.State.dynamicImage);
	}
}
