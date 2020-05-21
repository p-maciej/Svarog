package svarog.gui;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import svarog.entity.Player;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.font.Color;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.language.LanguageLoader;
import svarog.objects.Item;
import svarog.render.Texture;

public class StatsContainer {
	private Group playerStats;
	private Group playerProperties;
	private Group playerInventory;
	private static Font largeFont;	
	private static Font smallFont;

	private boolean update;
	
	public StatsContainer() {
		this.update = false;
	}
	
	public void updatePlayerStats(GuiRenderer guiRenderer, Player player) {
		if(largeFont != null && smallFont != null) {
			if(playerStats != null)
				guiRenderer.removeGroup(playerStats);
				
			this.playerStats = new Group();
			
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
			this.playerStats.addGuiObject(hpTexture);
			
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
			this.playerStats.addGuiObject(xpTexture);
			
			Line level = new Line(stickTo.TopRight);
			level.setString(Integer.toString(player.getXP().GetLevel()), smallFont);
			level.move(-160-level.getWidth()/2, 21);
			
			this.playerStats.addGuiObject(level);
			
			guiRenderer.addGroup(this.playerStats);
			
			this.update = true;
		} else {
			throw new IllegalStateException("Set static font in StatsContainer!");
		}
	}
	
	public void updatePlayerProperties(GuiRenderer guiRenderer, Player player) {
		if(largeFont != null && smallFont != null) {
			if(playerProperties != null)
				guiRenderer.removeGroup(playerProperties);
			
				this.playerProperties = new Group();
				this.playerProperties.setStickTo(stickTo.TopRight);
				
				Line attack = new Line(0,0);
				attack.setString((player.getMinAttack()+player.getPlayerAttackBonus()) + "-" +  (player.getMaxAttack()+player.getPlayerAttackBonus()), smallFont);
				attack.move(-165-attack.getWidth()/2, 62);
				
				Line defense = new Line(0,0);
				defense.setString(Integer.toString(player.getPlayerDefense()), smallFont);
				defense.move(-165-defense.getWidth()/2, 97);
				
				this.playerProperties.addGuiObject(attack);
				this.playerProperties.addGuiObject(defense);
				
				guiRenderer.addGroup(playerProperties);
				
				this.update = true;
		}
	}
	
	public void updatePlayerInventory(GuiRenderer guiRenderer, Player player) {
		if(largeFont != null && smallFont != null) {
			if(playerInventory != null)
				guiRenderer.removeGroup(playerInventory);
			
				this.playerInventory = new Group();
				this.playerInventory.setStickTo(stickTo.TopRight);
				
				Line money = new Line(0,0);
				money.setString(Integer.toString(player.getMoney()), smallFont);
				money.move(-165-money.getWidth()/2, 130);
				
				this.playerInventory.addGuiObject(money);
				
				guiRenderer.addGroup(playerInventory);
				
				this.update = true;
		}
	}
	
	public void update(GuiRenderer guiRenderer) {
		if(update) {
			guiRenderer.updatePositions();
			this.update = false;
		}	
	}
	
	public GuiWindow createItemWindow(Item item, LanguageLoader language, TextureObject itemWindowBackground) {
		int yPos = 0;
		// Desc
		GuiWindow itemInfo = new GuiWindow(language.getValue(item.getItemInfo().getName()), smallFont, itemWindowBackground);
		TextBlock description = new TextBlock(itemInfo.getWidth()-30, new Vector2f());
		description.setString(smallFont, language.getValue(item.getItemInfo().getDescription()));
		description.move(-description.getWidth()/2-5, -itemInfo.getHeight()/2+50);
		itemInfo.addTextureObject(description);
		
		yPos = (int)(description.getPosition().y - description.getHeight() - 20);
		// Type
		Line pre_type = new Line(-itemInfo.getWidth()/2, yPos);
		pre_type.setString(language.getValue("categoryItem"), smallFont);
		pre_type.move(pre_type.getWidth()/2+10, 0);
		itemInfo.addTextureObject(pre_type);
		
		Line type = new Line(-50, (int)pre_type.getPosition().y);
		type.setString(language.getValue(item.getItemInfo().getItemType().name()), smallFont);
		type.move(type.getWidth()/2, 0);
		itemInfo.addTextureObject(type);
		
		yPos = (int)pre_type.getPosition().y-20;
		// Level req
		Line pre_levelReq = new Line(-itemInfo.getWidth()/2, yPos);
		pre_levelReq.setString(language.getValue("levelItem"), smallFont);
		pre_levelReq.move(pre_levelReq.getWidth()/2+10, 0);
		itemInfo.addTextureObject(pre_levelReq);
		
		Line levelReq = new Line(-50, (int)pre_levelReq.getPosition().y);
		levelReq.setString(Integer.toString(item.getItemInfo().getLvlRequired()), smallFont);
		levelReq.move(levelReq.getWidth()/2, 0);
		itemInfo.addTextureObject(levelReq);
		
		
		if(item.getItemInfo().getAttackBonus() > 0) {
			yPos -= 20;
			// Attack bonus
			Line pre_attack = new Line(-itemInfo.getWidth()/2, (int)pre_levelReq.getPosition().y-20);
			pre_attack.setString(language.getValue("attackItem"), smallFont);
			pre_attack.move(pre_attack.getWidth()/2+10, 0);
			itemInfo.addTextureObject(pre_attack);
			
			Line attack = new Line(-50, (int)pre_attack.getPosition().y);
			attack.setString(Integer.toString(item.getItemInfo().getAttackBonus()), smallFont);
			attack.move(attack.getWidth()/2, 0);
			itemInfo.addTextureObject(attack);
		}
		
		if(item.getItemInfo().getDefense() > 0) {
			yPos -= 20;
			// Defense bonus
			Line pre_defense = new Line(-itemInfo.getWidth()/2, yPos);
			pre_defense.setString(language.getValue("defenseItem"), smallFont);
			pre_defense.move(pre_defense.getWidth()/2+10, 0);
			itemInfo.addTextureObject(pre_defense);
			
			Line defense = new Line(-50, (int)pre_defense.getPosition().y);
			defense.setString(Integer.toString(item.getItemInfo().getDefense()), smallFont);
			defense.move(defense.getWidth()/2, 0);
			itemInfo.addTextureObject(defense);
		}
		
		if(item.getItemInfo().getHpRegeneration() > 0) {
			yPos -= 20;
			// HP
			Line pre_hp = new Line(-itemInfo.getWidth()/2, yPos);
			pre_hp.setString(language.getValue("hpItem"), smallFont);
			pre_hp.move(pre_hp.getWidth()/2+10, 0);
			itemInfo.addTextureObject(pre_hp);
			
			Line hp = new Line(-50, (int)pre_hp.getPosition().y);
			hp.setString(Integer.toString(item.getItemInfo().getHpRegeneration()), smallFont);
			hp.move(hp.getWidth()/2, 0);
			itemInfo.addTextureObject(hp);
		}
		
		return itemInfo;
	}
	
	
	public static Font getSmallFont() {
		return smallFont;
	}

	public static void setSmallFont(Font smallFont) {
		StatsContainer.smallFont = smallFont;
	}
	
	public static void setLargeFont(Font font) {
		StatsContainer.largeFont = font;
	}
}
