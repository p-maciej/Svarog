package svarog.objects;

public class ItemInfo implements ItemProperties {
	
	private int tileID=-1;
	private int globalID;
	private int localID;
	private int hpRegeneration;
	private int attackBonus;
	private int lvlRequired;
	private String name;
	private String description;
	private ItemType itemType;
	
	public ItemInfo() {
		this.globalID = -1;
		this.localID = -1;
		this.hpRegeneration = 0;
		this.attackBonus = 0;
		this.lvlRequired = 0;
		this.name = "name";
		this.description = "description";
		this.itemType = ItemType.none;
	}

	public ItemInfo(int globalID, int localID, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, ItemType itemType) {
		this.globalID = globalID;
		this.localID = localID;
		this.hpRegeneration = hpRegeneration;
		this.attackBonus = attackBonus;
		this.lvlRequired = lvlRequired;
		this.name = name;
		this.description = description;
		this.itemType = itemType;
	}

	public int getHpRegeneration() {
		return hpRegeneration;
	}

	public void setHpRegeneration(int hpRegeneration) {
		this.hpRegeneration = hpRegeneration;
	}

	public int getAttackBonus() {
		return attackBonus;
	}

	public void setAttackBonus(int attackBonus) {
		this.attackBonus = attackBonus;
	}

	public int getLvlRequired() {
		return lvlRequired;
	}

	public void setLvlRequired(int lvlRequired) {
		this.lvlRequired = lvlRequired;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
	
	public int getLocalID() {
		return localID;
	}

	public void setLocalID(int localID) {
		this.localID = localID;
	}

	public int getGlobalID() {
		return globalID;
	}

	public void setGlobalID(int globalID) {
		this.globalID = globalID;
	}
	
	public int getTileID() {
		return tileID;
	}

	public void setTileID(int tileID) {
		this.tileID = tileID;
	}
}
