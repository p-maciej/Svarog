package svarog.gui.font;

import java.nio.ByteBuffer;

public class TextBlock {
	private ByteBuffer string;
	private int width;
	private int height;
	
	public TextBlock(ByteBuffer string, int width, int height) {
		this.setBuffer(string);
		this.setWidth(width);
		this.setHeight(height);
	}

	public ByteBuffer getBuffer() {
		return string;
	}

	public void setBuffer(ByteBuffer string) {
		this.string = string;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
