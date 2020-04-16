package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.objects.Item;
import svarog.objects.ItemInfo;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.WorldRenderer;

public class Enemy extends Entity {
	
	private List<Item> items = new ArrayList<Item>();
	
	//Animacja potem si� ogarnie
	//private String texturesPath;
	//private String fileName;
	
	private HP hp = new HP(20);
	private int maxAttack;
	private int minAttack;
	private int xpForKilling;
	
	public Enemy(int id, Texture texture, Transform transform, boolean fullBoundingBox) {
		super(id, texture, transform, fullBoundingBox);
		super.setClickable(true);
		super.setOverable(true);
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(false); // Non-static - default setting for Enemy
	}
	
	public Enemy(int id, Texture texture, Transform transform, boolean fullBoundingBox, int minAttack, int maxAttack, int xpForKilling, int hp) {
		super(id, texture, transform, fullBoundingBox);
		super.setClickable(true);
		super.setOverable(true);
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(false); // Non-static - default setting for Enemy
		
		this.maxAttack = maxAttack;
		this.minAttack = minAttack;
		this.xpForKilling = xpForKilling;
		this.hp.SetMaxHP(hp);
	}
	
	public Enemy(int id, Texture texture, Transform transform, boolean fullBoundingBox, int minAttack, int maxAttack, int xpForKilling, List<Item> items) {
		super(id, texture, transform, fullBoundingBox);
		super.setClickable(true);
		super.setOverable(true);
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
	
	public void AddItem(Texture texture, Vector2f position, ItemInfo itemInfo, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, int itemType) {
		items.add(new Item(texture, position, itemInfo, hpRegeneration, attackBonus, lvlRequired, name, description, itemType));
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
	public void SetAttackRewardXP(int minAttack, int maxAttack, int xpForKilling, int hp) {
		this.minAttack = minAttack;
		this.xpForKilling = xpForKilling;
		this.maxAttack = maxAttack;
		this.hp.SetHP(hp);
	}
	
	//Changers
	public void DecreaseEnemyHP(int damage) {
		this.hp.DecreaseHP(damage);
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
	public int GetEnemyHP() {
		return this.hp.GetHP();
	}
	
	@Override
	public boolean isClicked() {
		return WorldRenderer.getClickedEntityId() == super.getId() ? true : false;
	}
}
