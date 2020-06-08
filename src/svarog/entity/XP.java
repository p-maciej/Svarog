package svarog.entity;

public class XP {
	private int xp = 0;
	private int xpmin=0;
	public int getXpmin() {
		return xpmin;
	}

	public void setXpmin(int xpmin) {
		this.xpmin = xpmin;
	}

	public int getXpmax() {
		return xpmax;
	}

	public void setXpmax(int xpmax) {
		this.xpmax = xpmax;
	}

	private int xpmax = 20;
	private Level level= new Level(1);
	
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
		for( ; ; xpmax = xpmax * 2) {
			if(this.xp > xpmax) {
				this.level.setLevel(this.GetLevel()+1);
				xpmin = xpmax;
			}else {
				break;
			}
		}
	}
	
	public float getXPpercentage() {
		CheckLvlStatus();
		return (float)((float)(xp-xpmin)/(float)(xpmax-xpmin));
	}
}
