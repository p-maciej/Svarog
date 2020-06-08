package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.language.LanguageLoader;
import svarog.objects.Item;
import svarog.objects.ItemInfo;
import static svarog.objects.ItemInfo.ItemType;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.save.EnemyParameters;
import svarog.save.EntityHolder;
import svarog.save.ItemParameters;
import svarog.save.Save;
import svarog.world.WorldRenderer;

public class Enemy extends Entity {
	
	private List<Item> items = new ArrayList<Item>();
	private List<Integer> propability = new ArrayList<>();
	
	//Animacja potem siê ogarnie


	private int globalID =-1;
	private HP hp = new HP(20);
	private int maxAttack=0;
	private int minAttack=0;
	private int xpForKilling=0;
	private int reward=0;
	
	private int level;

	public Enemy(int id, Texture texture, Transform transform, boolean fullBoundingBox) {
		super(id, texture, transform, fullBoundingBox);
		super.setClickable(true);
		super.setOverable(true);
		super.setIsStatic(true);
		
	}
	
	public Enemy(int id, Texture texture, Transform transform, boolean fullBoundingBox, int minAttack, int maxAttack, int xpForKilling, int hp, int reward) {
		super(id, texture, transform, fullBoundingBox);
		super.setClickable(true);
		super.setOverable(true);
		super.setIsStatic(true);
		
		this.maxAttack = maxAttack;
		this.minAttack = minAttack;
		this.xpForKilling = xpForKilling;
		this.reward = reward;
		this.hp.SetMaxHP(hp);
	}
	
	public Enemy(EntityHolder entityHolder) {
		super(entityHolder.getId(), new Texture(Save.getEnemyById(entityHolder.getTypeID()).getTexture()),
				new Transform().setPosition(entityHolder.getPosX(), entityHolder.getPosY()),
				Save.getEnemyById(entityHolder.getTypeID()).isFullBoundingBox());
		EnemyParameters enemyParameters = Save.getEnemyById(entityHolder.getTypeID());
		super.setClickable(true);
		super.setOverable(true);
		super.setIsStatic(true);
		
		this.globalID = enemyParameters.getGlobalEnemyID();
		this.maxAttack = enemyParameters.getMaxAttack();
		this.minAttack = enemyParameters.getMinAttack();
		this.xpForKilling = enemyParameters.getXpForKilling();
		this.reward = enemyParameters.getReward();
		this.hp.SetMaxHP(enemyParameters.getHp());
		super.setRespownInSec(enemyParameters.getRespownInSec());
		this.setLevel(enemyParameters.getLevel());
		super.setIsStatic(true);
		if(entityHolder.getName() != null && !(entityHolder.getName().equals(""))) {
			super.setName(LanguageLoader.getLanguageLoader().getValue(entityHolder.getName()));
		}else {
			super.setName(LanguageLoader.getLanguageLoader().getValue(enemyParameters.getName()));
		}
		for(ItemParameters i: enemyParameters.getItemParameters()) {
			this.items.add(new Item(Save.getItemById(i.getItemGlobalID())));
			this.propability.add(i.getItemPropability());
		}
		
	}
	
	public Enemy(int id, EnemyParameters enemyParameters) {
		super(id, new Texture(enemyParameters.getTexture()),
				new Transform().setPosition(enemyParameters.getPosX(), enemyParameters.getPosY()),
				enemyParameters.isFullBoundingBox());
		super.setClickable(true);
		super.setOverable(true);
		super.setIsStatic(true);
		
		this.globalID = enemyParameters.getGlobalEnemyID();
		this.maxAttack = enemyParameters.getMaxAttack();
		this.minAttack = enemyParameters.getMinAttack();
		this.xpForKilling = enemyParameters.getXpForKilling();
		this.reward = enemyParameters.getReward();
		this.hp.SetMaxHP(enemyParameters.getHp());
		super.setRespownInSec(enemyParameters.getRespownInSec());
		this.setLevel(enemyParameters.getLevel());
		super.setName(enemyParameters.getName());
		
		for(ItemParameters i: enemyParameters.getItemParameters()) {
			this.items.add(new Item(Save.getItemById(i.getItemGlobalID())));
			this.propability.add(i.getItemPropability());
		}
		
	}
	
	public void resetEnemy() {
		EnemyParameters temp = Save.getEnemyById(globalID);
		
		this.maxAttack = temp.getMaxAttack();
		this.minAttack = temp.getMinAttack();
		this.hp.SetMaxHP(temp.getHp());
	}
	
	public Enemy(int id, EnemyParameters enemyParameters, Transform transform, String name) {
		super(id, new Texture(enemyParameters.getTexture()),
				transform,
				enemyParameters.isFullBoundingBox());
		super.setClickable(true);
		super.setOverable(true);
		
		super.setIsStatic(true);
		
		this.globalID = enemyParameters.getGlobalEnemyID();
		this.maxAttack = enemyParameters.getMaxAttack();
		this.minAttack = enemyParameters.getMinAttack();
		this.xpForKilling = enemyParameters.getXpForKilling();
		this.reward = enemyParameters.getReward();
		this.hp.SetMaxHP(enemyParameters.getHp());
		super.setRespownInSec(enemyParameters.getRespownInSec());
		this.setLevel(enemyParameters.getLevel());
		super.setName(name);
		
		for(ItemParameters i: enemyParameters.getItemParameters()) {
			this.items.add(new Item(Save.getItemById(i.getItemGlobalID())));
		}
	}
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Enemy(int id, Texture texture, Transform transform, boolean fullBoundingBox, int minAttack, int maxAttack, int xpForKilling, List<Item> items) {
		super(id, texture, transform, fullBoundingBox);
		super.setClickable(true);
		super.setOverable(true);
		
		super.setIsStatic(true);
		
		this.maxAttack = maxAttack;
		this.minAttack = minAttack;
		this.xpForKilling = xpForKilling;
		this.items = items;
	}
	
	public void AddItem(Item item) {
		items.add(item);
	}
	
	public void AddItem(Texture texture, Vector2f position, ItemInfo itemInfo, int globalID, int localID, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, ItemType itemType, int prize) {
		items.add(new Item(texture, position, itemInfo, globalID, localID, hpRegeneration, attackBonus, lvlRequired, name, description, itemType, prize));
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
	public void SetAttackRewardXPHP(int minAttack, int maxAttack, int xpForKilling, int hp, int reward) {
		this.minAttack = minAttack;
		this.xpForKilling = xpForKilling;
		this.maxAttack = maxAttack;
		this.hp.SetHP(hp);
		this.setReward(reward);
	}

	public void setReward(int reward) {
		this.reward = reward;
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
	
	public int getReward() {
		return reward;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public List<Integer> getPropability() {
		return propability;
	}

	public void setPropability(List<Integer> propability) {
		this.propability = propability;
	}

	@Override
	public boolean isClicked() {
		return WorldRenderer.getClickedEntityId() == super.getObjectId() ? true : false;
	}

	public int getGlobalID() {
		return globalID;
	}

	public void setGlobalID(int globalID) {
		this.globalID = globalID;
	}
	
}
