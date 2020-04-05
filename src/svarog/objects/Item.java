package svarog.objects;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer;
import svarog.gui.TextureObject;
import svarog.render.Texture;

public class Item extends TextureObject implements Movable, MouseInteraction{

	private ItemInfo itemInfo;

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
	
	public Item(Texture texture, Vector2f position, ItemInfo itemInfo, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, int itemType) {
		super(texture, position);
		this.itemInfo = new ItemInfo(hpRegeneration, attackBonus, lvlRequired, name, description, itemType);
	}
	
	public boolean isClicked() {
		return GuiRenderer.getClickedObjectId() == super.getId() ? true : false;
	}
	
	public void SetItem(int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, int itemType) {
		itemInfo.setHpRegeneration(hpRegeneration);
		itemInfo.setAttackBonus(attackBonus);
		itemInfo.setLvlRequired(lvlRequired);
		itemInfo.setName(name);
		itemInfo.setDescription(description);
		itemInfo.setItemType(itemType);
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

	@Override
	public float getPositionX() {
		return super.getTransform().getPosition().x;
	}

	@Override
	public float getPositionY() {
		return super.getTransform().getPosition().y;
	}

	public int getItemType() { /// I've added just getter and setter for now. A need this for Tile class for gui.
		return itemInfo.getItemType();
	}

	public void setItemType(int itemType) {
		this.itemInfo.setItemType(itemType);
	}
}
