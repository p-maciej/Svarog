package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import svarog.render.Texture;
import svarog.render.Transform;

public class Enemy extends Entity {
	
	//Animacja potem siê ogarnie
	//private String texturesPath;
	//private String fileName;
	private int maxAttack;
	private int minAttack;
	private int xpForKilling;
	private List<String> droppable = new ArrayList<>(); //TODO: przemyslec to i jak rozwi¹¿emy itemy
	
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
