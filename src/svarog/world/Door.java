package svarog.world;

public class Door {
	private int positionX;
	private int positionY;
	private int destinationX;
	private int destinationY;
	
	public Door(int positionX, int positionY, int destinationX, int destinationY) {
		this.setPositionX(positionX);
		this.setPositionY(positionY);
		this.setDestinationX(destinationX);
		this.setDestinationY(destinationY);
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getDestinationX() {
		return destinationX;
	}

	public void setDestinationX(int destinationX) {
		this.destinationX = destinationX;
	}

	public int getDestinationY() {
		return destinationY;
	}

	public void setDestinationY(int destinationY) {
		this.destinationY = destinationY;
	}
}
