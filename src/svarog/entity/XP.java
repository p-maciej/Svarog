package svarog.entity;

public class XP {
	private int xp = 0;
	private Level level= new Level(0);
	
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
	
	public int GetLevel() {
		return this.level.getLevel();
	}
	
	public void AddXP(int points) {
		xp = xp + points;
		CheckLvlStatus();
	}
	
	private void CheckLvlStatus() {
		int iter = 1;
		for(int i = 20; ; i = i * 2) {
			if(this.xp > i) {
				this.level.setLevel(iter);
			}else {
				break;
			}
			iter++;
		}
	}
	
	public int PointsToNextLvl() {
		int toNextLvl=0;
		for(int i = 20; ; i = i*2 ) {
			if(xp < i){
				toNextLvl = i - xp;
				break;
			}
		}
		return toNextLvl;
	}
}
