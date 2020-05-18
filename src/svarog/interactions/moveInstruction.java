package svarog.interactions;

public class moveInstruction {
	private int direction;
	private int numberOfTiles;
	public moveInstruction(int direction, int numberOfTiles) {
		super();
		this.direction = direction;
		this.numberOfTiles = numberOfTiles;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getNumberOfTiles() {
		return numberOfTiles;
	}
	public void setNumberOfTiles(int numberOfTiles) {
		this.numberOfTiles = numberOfTiles;
	}
}
