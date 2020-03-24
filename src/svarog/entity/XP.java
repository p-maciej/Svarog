package svarog.entity;

public class XP {
	private int xp = 0;
	
	public XP(int xp) {
		this.xp = xp;
	}
	
	public int GetXP() {
		return xp;
	}
	
	public void AddXP(int points) {
		xp = xp + points;
	}
}
