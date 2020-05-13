package svarog.save;

import java.util.ArrayList;
import java.util.List;

public class EnemyParameters {
	
	private List<ItemParameters>itemParameters = new ArrayList<>();
	
	private int globalEnemyID =-1;
	private String texture;
	private int posX=0;
	private int posY=0;
	private boolean fullBoundingBox = false;
	private int minAttack=0;
	private int maxAttack=0;
	private int xpForKilling=0;
	private int hp=1;
	private int reward=0;
	
	private String name;
	
	public EnemyParameters(List<ItemParameters> itemParameters, int globalEnemyID, String texture, int posX, int posY,
			boolean fullBoundingBox, int minAttack, int maxAttack, int xpForKilling, int hp, int reward, String name) {
		super();
		this.itemParameters = itemParameters;
		this.globalEnemyID = globalEnemyID;
		this.texture = texture;
		this.posX = posX;
		this.posY = posY;
		this.fullBoundingBox = fullBoundingBox;
		this.minAttack = minAttack;
		this.maxAttack = maxAttack;
		this.xpForKilling = xpForKilling;
		this.hp = hp;
		this.reward = reward;
		this.name = name;
	}
	public String getTexture() {
		return texture;
	}
	public void setTexture(String texture) {
		this.texture = texture;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public boolean isFullBoundingBox() {
		return fullBoundingBox;
	}
	public void setFullBoundingBox(boolean fullBoundingBox) {
		this.fullBoundingBox = fullBoundingBox;
	}
	public int getMinAttack() {
		return minAttack;
	}
	public void setMinAttack(int minAttack) {
		this.minAttack = minAttack;
	}
	public int getMaxAttack() {
		return maxAttack;
	}
	public void setMaxAttack(int maxAttack) {
		this.maxAttack = maxAttack;
	}
	public int getXpForKilling() {
		return xpForKilling;
	}
	public void setXpForKilling(int xpForKilling) {
		this.xpForKilling = xpForKilling;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getReward() {
		return reward;
	}
	public void setReward(int reward) {
		this.reward = reward;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGlobalEnemyID() {
		return globalEnemyID;
	}
	public void setGlobalEnemyID(int globalEnemyID) {
		this.globalEnemyID = globalEnemyID;
	}
	public List<ItemParameters> getItemParameters() {
		return itemParameters;
	}
	public void setItemParameters(List<ItemParameters> itemParameters) {
		this.itemParameters = itemParameters;
	}	
}
