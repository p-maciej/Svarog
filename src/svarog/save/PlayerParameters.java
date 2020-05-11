package svarog.save;

import java.util.ArrayList;
import java.util.List;

import svarog.interactions.Quest;
import svarog.objects.Item;

public class PlayerParameters {
	private int currentWorldId=1;
	private int positionX = 40;
	private int positionY = 25;
	private int playerID = 0;
	private String texturesPath = "player/mavak/";
	private String fileName = "mavak";
	private boolean fullBoundingBox = false;
	private boolean isMovementLocked = false;
	private int HP = 100;
	private int maxHP = 100;
	private int xp = 0;
	private int minAttack = 50;
	private int maxAttack = 60;
	private int money = 0;
	private String playerName = "Ty";
	List<Item> items = new ArrayList<>();
	List<Quest> quests = new ArrayList<>();
	public int getCurrentWorldId() {
		return currentWorldId;
	}
	public void setCurrentWorldId(int currentWorldId) {
		this.currentWorldId = currentWorldId;
	}
	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public String getTexturesPath() {
		return texturesPath;
	}
	public void setTexturesPath(String texturesPath) {
		this.texturesPath = texturesPath;
	}
	public String getFileName() {
		return fileName;
	}
	public boolean isFullBoundingBox() {
		return fullBoundingBox;
	}
	public void setFullBoundingBox(boolean fullBoundingBox) {
		this.fullBoundingBox = fullBoundingBox;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isMovementLocked() {
		return isMovementLocked;
	}
	public void setMovementLocked(boolean isMovementLocked) {
		this.isMovementLocked = isMovementLocked;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int hP) {
		HP = hP;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}
	public int getXp() {
		return xp;
	}
	public void setXp(int xp) {
		this.xp = xp;
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
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public List<Quest> getQuests() {
		return quests;
	}
	public void setQuests(List<Quest> quests) {
		this.quests = quests;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
