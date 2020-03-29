package svarog.gui;

import org.joml.Vector2f;

import svarog.render.Texture;

public class Item extends TextureObject{

	ItemInfo itemInfo;
	
	public Item(Texture texture, svarog.gui.GuiRenderer.stickTo stickTo, ItemInfo itemInfo) {
		super(texture, stickTo);
		this.itemInfo = itemInfo;
	}

	public Item(Texture texture, float X, float Y, ItemInfo itemInfo) {
		super(texture, X, Y);
		this.itemInfo = itemInfo;
	}
	
	public Item(Texture texture, Vector2f position, ItemInfo itemInfo) {
		super(texture, position);
		this.itemInfo = itemInfo;
	}

	public Item(Texture texture, ItemInfo itemInfo) {
		super(texture);
		this.itemInfo = itemInfo;
	}
	
	public Item(Texture texture, Vector2f position, ItemInfo itemInfo, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description) {
		super(texture, position);
		this.itemInfo = new ItemInfo(hpRegeneration, attackBonus, lvlRequired, name, description);
	}
	
	public boolean isClicked() {
		return GuiRenderer.getClickedObjectId() == super.getId() ? true : false;
	}
	
	public void SetBonus(int hpRegeneration, int attackBonus, int lvlRequired, String name, String description) {
		itemInfo.setHpRegeneration(hpRegeneration);
		itemInfo.setAttackBonus(attackBonus);
		itemInfo.setLvlRequired(lvlRequired);
		itemInfo.setName(name);
		itemInfo.setDescription(description);
	}
	
	public String GetItemName() {
		return itemInfo.getName();
	}
	
	public String GetItemDescription() {
		return itemInfo.getDescription();
	}
	
	public ItemInfo getItemInfo() {
		return itemInfo;
	}
}
