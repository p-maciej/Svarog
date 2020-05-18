package svarog.interactions;

public class Node {
	private boolean walkable=false;
	private int x;
	private int y;
	private int gCost=0;//how far is it from starting Node
	private int hCost=0;//howfar is it from end Node
	private int fCost=0;//sum of gCost and hCost
	private int localState=0; //0-not cheched yet, 1-checked, 2-to final path
	
	public Node(boolean _walkable, int _x, int _y) {
		this.walkable=_walkable;
		this.x=_x;
		this.y=_y;
	}
	
	public int countCost(int x_begin,int y_begin, int x_end,int y_end) {
		gCost = Math.abs(x-x_begin)*10 + Math.abs(y-y_begin)*10;
		hCost = Math.abs(x-x_end)*10 + Math.abs(y-y_end)*10;
		fCost = gCost + hCost;
		return fCost;
	}
	
	public int getgCost() {
		return gCost;
	}
	public void setgCost(int gCost) {
		this.gCost = gCost;
		this.fCost = gCost + hCost;
	}
	public int gethCost() {
		return hCost;
	}
	public void sethCost(int hCost) {
		this.hCost = hCost;
		this.fCost = gCost + hCost;
	}
	public int getfCost() {
		return fCost;
	}
	public void setfCost(int fCost) {
		this.fCost = fCost;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}

	public int getLocalState() {
		return localState;
	}

	public void setLocalState(int localState) {
		this.localState = localState;
	}
	
}
