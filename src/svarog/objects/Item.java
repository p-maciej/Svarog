package svarog.objects;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer;
import svarog.gui.TextureObject;
import svarog.render.Texture;

public class Item extends TextureObject implements Movable, MouseInteraction{

	private ItemInfo itemInfo;
	private int itemType;

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

	@Override
	public float getPositionX() {
		return super.getTransform().getPosition().x;
	}

	@Override
	public float getPositionY() {
		return super.getTransform().getPosition().y;
	}

	@Override
	public void setPositionX(float X) {
		super.setPosition(X, super.getTransform().getPosition().y);
	}

	@Override
	public void setPositionY(float Y) {
		super.setPosition(super.getTransform().getPosition().x, Y);
	}

	public int getItemType() { /// I've added just getter and setter for now. A need this for Tile class for gui.
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}
}
