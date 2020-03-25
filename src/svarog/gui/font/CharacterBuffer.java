package svarog.gui.font;

import java.nio.ByteBuffer;

public class CharacterBuffer {
	private char ascii;
	private ByteBuffer character;
	private int width;
	private int height;
	
	CharacterBuffer(ByteBuffer character, char ascii, int width, int height) {
		this.setBuffer(character);
		this.setWidth(width);
		this.setHeight(height);
		this.setAscii(ascii);
	}
	
	ByteBuffer getBuffer() {
		return character;
	}
	
	void setBuffer(ByteBuffer character) {
		this.character = character;
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
	
	char getAscii() {
		return ascii;
	}

	void setAscii(char ascii) {
		this.ascii = ascii;
	}
}
