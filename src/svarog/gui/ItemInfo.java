package svarog.gui;

public class ItemInfo {
	private int hpRegeneration;
	private int attackBonus;
	private int lvlRequired;
	private String name;
	private String description;
	
	public ItemInfo() {
		this.hpRegeneration = 0;
		this.attackBonus = 0;
		this.lvlRequired = 0;
		this.name = "name";
		this.description = "description";
	}
	
	public ItemInfo(int hpRegeneration, int attackBonus, int lvlRequired, String name, String description) {
		this.hpRegeneration = hpRegeneration;
		this.attackBonus = attackBonus;
		this.lvlRequired = lvlRequired;
		this.name = name;
		this.description = description;
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
	
	
}
