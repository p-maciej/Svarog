package svarog.game;

import org.joml.Vector2f;

import svarog.gui.GuiWindow;
import svarog.gui.TextureObject;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;
import svarog.language.LanguageLoader;
import svarog.objects.Item;

public abstract class StaticUI {
	
	public static GuiWindow createItemWindow(Item item, Font font, LanguageLoader language, TextureObject itemWindowBackground) {
		int yPos = 0;
		// Desc
		GuiWindow itemInfo = new GuiWindow(language.getValue(item.getItemInfo().getName()), font, itemWindowBackground);
		TextBlock description = new TextBlock(itemInfo.getWidth()-30, new Vector2f());
		description.setString(font, language.getValue(item.getItemInfo().getDescription()));
		description.move(-description.getWidth()/2-5, -itemInfo.getHeight()/2+50);
		itemInfo.addTextBlock(description);
		
		yPos = (int)(description.getPosition().y - description.getHeight() - 20);
		// Type
		Line pre_type = new Line(-itemInfo.getWidth()/2, yPos);
		pre_type.setString(language.getValue("categoryItem"), font);
		pre_type.move(pre_type.getWidth()/2+10, 0);
		itemInfo.addTextureObject(pre_type);
		
		Line type = new Line(-50, (int)pre_type.getPosition().y);
		type.setString(language.getValue(item.getItemInfo().getItemType().name()), font);
		type.move(type.getWidth()/2, 0);
		itemInfo.addTextureObject(type);
		
		yPos = (int)pre_type.getPosition().y-20;
		// Level req
		Line pre_levelReq = new Line(-itemInfo.getWidth()/2, yPos);
		pre_levelReq.setString(language.getValue("levelItem"), font);
		pre_levelReq.move(pre_levelReq.getWidth()/2+10, 0);
		itemInfo.addTextureObject(pre_levelReq);
		
		Line levelReq = new Line(-50, (int)pre_levelReq.getPosition().y);
		levelReq.setString(Integer.toString(item.getItemInfo().getLvlRequired()), font);
		levelReq.move(levelReq.getWidth()/2, 0);
		itemInfo.addTextureObject(levelReq);
		
		
		if(item.getItemInfo().getAttackBonus() > 0) {
			yPos -= 20;
			// Attack bonus
			Line pre_attack = new Line(-itemInfo.getWidth()/2, (int)pre_levelReq.getPosition().y-20);
			pre_attack.setString(language.getValue("attackItem"), font);
			pre_attack.move(pre_attack.getWidth()/2+10, 0);
			itemInfo.addTextureObject(pre_attack);
			
			Line attack = new Line(-50, (int)pre_attack.getPosition().y);
			attack.setString(Integer.toString(item.getItemInfo().getAttackBonus()), font);
			attack.move(attack.getWidth()/2, 0);
			itemInfo.addTextureObject(attack);
		}
		
		if(item.getItemInfo().getDefense() > 0) {
			yPos -= 20;
			// Defense bonus
			Line pre_defense = new Line(-itemInfo.getWidth()/2, yPos);
			pre_defense.setString(language.getValue("defenseItem"), font);
			pre_defense.move(pre_defense.getWidth()/2+10, 0);
			itemInfo.addTextureObject(pre_defense);
			
			Line defense = new Line(-50, (int)pre_defense.getPosition().y);
			defense.setString(Integer.toString(item.getItemInfo().getDefense()), font);
			defense.move(defense.getWidth()/2, 0);
			itemInfo.addTextureObject(defense);
		}
		
		if(item.getItemInfo().getHpRegeneration() > 0) {
			yPos -= 20;
			// HP
			Line pre_hp = new Line(-itemInfo.getWidth()/2, yPos);
			pre_hp.setString(language.getValue("hpItem"), font);
			pre_hp.move(pre_hp.getWidth()/2+10, 0);
			itemInfo.addTextureObject(pre_hp);
			
			Line hp = new Line(-50, (int)pre_hp.getPosition().y);
			hp.setString(Integer.toString(item.getItemInfo().getHpRegeneration()), font);
			hp.move(hp.getWidth()/2, 0);
			itemInfo.addTextureObject(hp);
		}
		
		return itemInfo;
	}
}
