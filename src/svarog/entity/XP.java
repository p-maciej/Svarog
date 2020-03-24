package svarog.entity;

public class XP {
	private int xp = 0;
	private Level level= new Level(0);
	
	private int[] level_barriers = {0,20,40,80,160,320};
	
	public XP(int xp) {
		this.xp = xp;
		CheckLvlStatus();
	}
	
	public void setXP(int xp) {
		this.xp = xp;
		CheckLvlStatus();
	}
	
	public int GetXP() {
		return xp;
	}
	
	public Level GetLevel() {
		return this.level;
	}
	
	public void AddXP(int points) {
		xp = xp + points;
		CheckLvlStatus();
	}
	
	private void CheckLvlStatus() {
		int iter = 1;
		for(int i: level_barriers) {
			if(this.xp > i) {
				this.level.setLevel(iter);
			}else {
				break;
			}
			iter++;
		}
	}
}
