package svarog.objects;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer;
import svarog.gui.TextureObject;
import svarog.render.Texture;

public class Item extends TextureObject implements Movable, MouseInteraction, ItemProperties {

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

	public Item(Item item) {
		super(item.getTexture());
		this.itemInfo = item.getItemInfo();
	}
	
	public Item(Texture texture, ItemInfo itemInfo) {
		super(texture);
		this.itemInfo = itemInfo;
	}
	
	public Item(Texture texture, ItemInfo itemInfo, ItemType itemType) {
		super(texture);
		this.itemInfo = itemInfo;
		this.setItemType(itemType);
	}
	
	public Item(Texture texture, Vector2f position, ItemInfo itemInfo, int globalID, int defense, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, ItemType itemType, int prize) {
		super(texture, position);
		this.itemInfo = new ItemInfo(globalID, defense, hpRegeneration, attackBonus, lvlRequired, name, description, itemType, prize);
	}
	
	public boolean isClicked() {
		return GuiRenderer.getClickedObjectId() == super.getId() ? true : false;
	}
	
	public void SetItem(int defense, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, ItemType itemType) {
		itemInfo.setDefense(defense);
		itemInfo.setHpRegeneration(hpRegeneration);
		itemInfo.setAttackBonus(attackBonus);
		itemInfo.setLvlRequired(lvlRequired);
		itemInfo.setName(name);
		itemInfo.setDescription(description);
		itemInfo.setItemType(itemType);
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

	public ItemType getItemType() { /// I've added just getter and setter for now. A need this for Tile class for gui.
		return itemInfo.getItemType();
	}

	public void setItemType(ItemType itemType) {
		this.itemInfo.setItemType(itemType);
	}
}
