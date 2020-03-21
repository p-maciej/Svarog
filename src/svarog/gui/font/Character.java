package svarog.gui.font;

public class Character {
	char character;

	int x;
	int y;
	int width;
	int height;
	
	Character(char character) {
		this.setCharacter(character);
	}
	
	int getX() {
		return x;
	}


	void setX(int x) {
		this.x = x;
	}


	int getY() {
		return y;
	}


	void setY(int y) {
		this.y = y;
	}


	int getWidth() {
		return width;
	}


	void setWidth(int width) {
		this.width = width;
	}


	int getHeight() {
		return height;
	}


	void setHeight(int height) {
		this.height = height;
	}


	char getCharacter() {
		return character;
	}

	void setCharacter(char character) {
		this.character = character;
	}
}
