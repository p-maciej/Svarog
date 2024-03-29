package svarog.entity;

public class HP {
	private int hp;
	private int maxHP;
	
	public HP(int hp) {
		this.hp = hp;
		this.maxHP = hp;
	}
	
	public void SetHP(int hp) {
		if(hp<=maxHP) {
			this.hp = hp;
		}else {
			this.hp = maxHP;
		}
	}
	
	public void SetMaxHP(int hp) {
		this.hp = hp;
		this.maxHP = hp;
	}
	
	public void AddHP(int health) {
		if(health>0) {
			if(maxHP >= hp + health) {
				hp = hp + health;
			}else {
				hp = maxHP;
			}
		}else {
			hp = -1;
		}
	}
	
	public void DecreaseHP(int damage) {
		if(damage>hp) {
			hp = -1;
		}else {
			hp = hp - damage;
		}
	}
	
	public void SetMaxHPnoChange(int hp) {
		this.maxHP = hp;
		if(this.hp > maxHP) {
			this.hp = hp;
		}
	}
	
	public int GetHP() {
		return hp;
	}
	
	public int getMaxHP() {
		return maxHP;
	}
	
	public float GetHPfloat() {
		return (float)hp/maxHP;
	}
}
