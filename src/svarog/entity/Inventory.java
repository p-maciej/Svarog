package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import svarog.objects.Item;

public class Inventory {
	
	private Item armor;
	private Item weapon;
	private Item potion;
	private Item headArmor;
	private Item legArmor;
	private List<Item> items = new ArrayList<Item>();
	
	public Inventory() {
		//TODO: write some big brain construction here
	}
	
	public Inventory(Item armor, Item weapon, Item potion, Item headArmor, Item legArmor, List<Item> items) {
		this.armor = armor;
		this.weapon = weapon;
		this.potion = potion;
		this.headArmor = headArmor;
		this.legArmor = legArmor;
		this.items = items;
	}
	
	public Item getArmor() {
		return armor;
	}

	public void setArmor(Item armor) {
		this.armor = armor;
	}

	public Item getWeapon() {
		return weapon;
	}

	public void setWeapon(Item weapon) {
		this.weapon = weapon;
	}

	public Item getPotion() {
		return potion;
	}

	public void setPotion(Item potion) {
		this.potion = potion;
	}

	public Item getHeadArmor() {
		return headArmor;
	}

	public void setHeadArmor(Item headArmor) {
		this.headArmor = headArmor;
	}

	public Item getLegArmor() {
		return legArmor;
	}

	public void setLegArmor(Item legArmor) {
		this.legArmor = legArmor;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
