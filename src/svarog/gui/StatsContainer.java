package svarog.gui;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import svarog.entity.Player;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.font.Color;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.render.Texture;

public class StatsContainer {
	private Group playerStats;
	private static Font font;	
	
	public StatsContainer() {}
	
	public void playerStatsDynamic(GuiRenderer guiRenderer, Player player) {
		if(font != null) {
			if(playerStats != null)
				guiRenderer.removeGroup(playerStats);
				
			playerStats = new Group();
			
			int width = 200;
			int height = 12;
			
			Color hpRemaining = new Color((byte)255, (byte)0, (byte)0);
			Color hpLost = new Color((byte)150, (byte)0, (byte)0);
			
			int remHPWidth = (int)(player.getHP().GetHPfloat()*width);
			
			// HP //
			ByteBuffer HPbuffer = BufferUtils.createByteBuffer(width*height*4);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					if(i <= remHPWidth) {
						HPbuffer.put(hpRemaining.getR());
						HPbuffer.put(hpRemaining.getG());
						HPbuffer.put(hpRemaining.getB());
						HPbuffer.put((byte)255);
					} else {
						HPbuffer.put(hpLost.getR());
						HPbuffer.put(hpLost.getG());
						HPbuffer.put(hpLost.getB());
						HPbuffer.put((byte)255);
					}
				}
			}
			HPbuffer.flip();
			
			TextureObject hpTexture = new TextureObject(new Texture(HPbuffer, width, height));
			hpTexture.setStickTo(stickTo.BottomLeft);
			hpTexture.move(120, -42);
			playerStats.addTextureObject(hpTexture);
			
			Color xpGained = new Color((byte)255, (byte)255, (byte)0);
			Color xpRemaining = new Color((byte)150, (byte)150, (byte)0);
	
			
			int remXPWidth = (int)(player.getXP().getXPpercentage()*width);
			
			// HP //
			ByteBuffer XPbuffer = BufferUtils.createByteBuffer(width*height*4);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					if(i <= remXPWidth) {
						XPbuffer.put(xpGained.getR());
						XPbuffer.put(xpGained.getG());
						XPbuffer.put(xpGained.getB());
						XPbuffer.put((byte)255);
					} else {
						XPbuffer.put(xpRemaining.getR());
						XPbuffer.put(xpRemaining.getG());
						XPbuffer.put(xpRemaining.getB());
						XPbuffer.put((byte)255);
					}
				}
			}
			XPbuffer.flip();
			
			TextureObject xpTexture = new TextureObject(new Texture(XPbuffer, width, height));
			xpTexture.setStickTo(stickTo.BottomLeft);
			xpTexture.move(120, -14);
			playerStats.addTextureObject(xpTexture);
			
			Line level = new Line(GuiRenderer.stickTo.BottomRight);
			level.setString(Integer.toString(player.getXP().GetLevel()) + "lvl", font);
			level.move(-70, -20);
			playerStats.addTextureObject(level);
			
			guiRenderer.addGroup(playerStats);
			guiRenderer.updatePositions();
		} else {
			throw new IllegalStateException("Set static font in StatsContainer!");
		}
	}
	
	
	public static void setFont(Font font) {
		StatsContainer.font = font;
	}
}
