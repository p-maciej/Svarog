package svarog.gui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import svarog.game.WorldLoader;
import svarog.gui.GuiRenderer.State;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.font.Color;
import svarog.gui.font.TextBlock;
import svarog.render.Texture;

public class ArenaContainer {
	private Arena arena;
	
	private static TextureObject arenaImage;
	private static BufferedImage arenaLogBackground;
	private static Color backgtroundColor;

	private Button closeArenaButton;
	private Group arenaGroup;
	
	private static boolean isArenaClosing = false;
	
	ArenaContainer() {
		this.arena = null;
		this.closeArenaButton = null;
		this.arenaGroup = null;
	}
	
	Group getArenaGroup(int windowWidth, int windowHeight) {
		if(arena != null && arenaGroup == null) {
			if(arenaLogBackground != null && arenaImage != null && backgtroundColor != null) {
				Group group = new Group(State.staticImage);

				/// LOG ///
				ByteBuffer logBackground = BufferUtils.createByteBuffer((arenaLogBackground.getWidth()*(windowHeight-70)*4));
				
				for(int i = 0; i <  arenaLogBackground.getWidth(); i++) {
					for(int j = 0; j < windowHeight-70; j++) {
						int pixel = arenaLogBackground.getRGB(i, 0);
						logBackground.put(((byte)((pixel >> 16) & 0xFF)));
						logBackground.put(((byte)((pixel >> 8) & 0xFF)));
						logBackground.put((byte)(pixel & 0xFF));
						logBackground.put(((byte)((pixel >> 24) & 0xFF)));
					}
				}
				logBackground.flip();
				
				TextureObject log = new TextureObject(new Texture(logBackground, arenaLogBackground.getWidth(), windowHeight-70));
				log.setStickTo(stickTo.TopLeft);
				group.addTextureObject(log);
				
				int tempHeight = 0;
				int minIndex = 0;
				for(int i = arena.getLog().size()-1; i >= 0; i--) {
					tempHeight += arena.getLog().get(i).getHeight();
					if(tempHeight < windowHeight-70)
						minIndex = i;
					else 
						break;
				}
				
				tempHeight = 10;
				for(int i = minIndex; i < arena.getLog().size() ; i++) {
					TextBlock tempBlock = arena.getLog().get(i);
					tempBlock.setStickTo(stickTo.TopLeft);
					tempBlock.setPosition(10, -tempHeight);
					tempHeight += tempBlock.getHeight();
					group.addTextBlock(tempBlock);
				}
				/////////
				
				/// MAIN BACKGROUND ///
				int arenaWidth = windowWidth-arenaLogBackground.getWidth()-350;
				int arenaHeight = windowHeight-70;
				ByteBuffer arenaBackground = BufferUtils.createByteBuffer(arenaWidth*arenaHeight*4);
				
				for(int i = 0; i < arenaWidth; i++) {
					for(int j = 0; j < arenaHeight; j++) {
						arenaBackground.put(backgtroundColor.getR());
						arenaBackground.put(backgtroundColor.getG());
						arenaBackground.put(backgtroundColor.getB());
						arenaBackground.put((byte)255);
					}
				}
				arenaBackground.flip();
				
				TextureObject arenaBg = new TextureObject(new Texture(arenaBackground, arenaWidth, arenaHeight));
				arenaBg.setStickTo(stickTo.TopLeft);
				arenaBg.setPosition(arenaLogBackground.getWidth(), 0);
				group.addTextureObject(arenaBg);
				///////////////////////
				
				/// ARENA IMAGE ///
				arenaImage.setPosition(-25, 70);
				group.addTextureObject(arenaImage);
				//////////////////
				
				/// PLAYER ///
				arena.getPlayer().setPosition(-25, 0);
				group.addTextureObject(arena.getPlayer());
				//////////////
				
				/// ENEMY ///
				arena.getEnemy().setPosition(-25, 130);
				group.addTextureObject(arena.getEnemy());
				////////////
				
				/// CLOSE BUTTON ///
				Button closeArena = new Button(new Texture("images/dialog/close_dialog.png"), stickTo.TopRight);
				closeArena.move(-360, 10);
				closeArenaButton = closeArena;
				group.addTextureObject(closeArena);
				///////////////////

				
				this.arenaGroup = group;

				return group;
			} else 
				throw new IllegalStateException("Arena textures isn't declared!");
		}
		return null;
	}
	
	void setArena(Arena arena) {
		this.arena = arena;
	}
	
	Arena getArena() {
		return arena;
	}

	Button getCloseArenaButton() {
		return closeArenaButton;
	}
	
	public void closeArena(GuiRenderer renderer) {
		if(arenaGroup != null) {
			this.arena = null;
			renderer.removeGroup(arenaGroup);
			arenaGroup = null;
		}
	}
	
	void reload(GuiRenderer renderer) {
		if(arenaGroup != null) {
			renderer.removeGroup(arenaGroup);
			arenaGroup = null;
		}
	}
	
	public static void setArenaLogBackground(BufferedImage arenaLogBg) {
		arenaLogBackground = arenaLogBg;
	}

	public static void setArenaImage(BufferedImage arenaImg) {
		TextureObject tempArenaImage = new TextureObject(new Texture(arenaImg));
		arenaImage = tempArenaImage;
	}
	
	public static void setBackgtroundColor(Color backgtroundColor) {
		ArenaContainer.backgtroundColor = backgtroundColor;
	}

	public static boolean isArenaClosing() {
		return isArenaClosing;
	}

	static void setArenaClosing(boolean isArenaClosing) {
		ArenaContainer.isArenaClosing = isArenaClosing;
	}
}
