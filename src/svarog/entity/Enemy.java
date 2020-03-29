package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.gui.Item;
import svarog.gui.ItemInfo;
import svarog.render.Texture;
import svarog.render.Transform;

public class Enemy extends Entity {
	
	private List<Item> items = new ArrayList<Item>();
	
	//Animacja potem siê ogarnie
	//private String texturesPath;
	//private String fileName;
	
	private int maxAttack;
	private int minAttack;
	private int xpForKilling;
	
	public Enemy(String texturePath, String filename, Transform transform, boolean fullBoundingBox) {
		super(new Texture("textures/" + texturePath + "" + filename + ".png"), transform, fullBoundingBox);
		
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(false); // Non-static - default setting for player 
	}
	
	public Enemy(String texturePath, String filename, Transform transform, boolean fullBoundingBox, int minAttack, int maxAttack, int xpForKilling) {
		super(new Texture("textures/" + texturePath + "" + filename + ".png"), transform, fullBoundingBox);
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(false); // Non-static - default setting for player 
		
		this.maxAttack = maxAttack;
		this.minAttack = minAttack;
		this.xpForKilling = xpForKilling;
	}
	
	public Enemy(String texturePath, String filename, Transform transform, boolean fullBoundingBox, int minAttack, int maxAttack, int xpForKilling, List<Item> items) {
		super(new Texture("textures/" + texturePath + "" + filename + ".png"), transform, fullBoundingBox);
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(false); // Non-static - default setting for player 
		
		this.maxAttack = maxAttack;
		this.minAttack = minAttack;
		this.xpForKilling = xpForKilling;
		this.items = items;
	}
	
	public void AddItem(Item item) {
		items.add(item);
	}
	
	public void AddItem(Texture texture, Vector2f position, ItemInfo itemInfo, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description) {
		items.add(new Item(texture, position, itemInfo, hpRegeneration, attackBonus, lvlRequired, name, description));
	}
	
	//Setters
	public void SetMinAttack(int minAttack) {
		this.minAttack = minAttack;
	}
	public void SetMaxAttack(int maxAttack) {
		this.maxAttack = maxAttack;
	}
	public void SetXpForKilling(int xpForKilling) {
		this.xpForKilling = xpForKilling;
	}
	public void SetAttackRewardXP(int minAttack, int maxAttack, int xpForKilling) {
		this.minAttack = minAttack;
		this.xpForKilling = xpForKilling;
		this.maxAttack = maxAttack;
	}
	
	//Getters
	public int GetXpForKilling() {
		return xpForKilling;
	}
	public int GetRandomAttack() {
		return (int)((maxAttack-minAttack)*Math.random() + minAttack);
	}
	public int GetMinAttack() {
		return minAttack;
	}
	public int GetMaxAttack() {
		return maxAttack;
	}
}
