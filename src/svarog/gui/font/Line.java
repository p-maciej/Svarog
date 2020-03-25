package svarog.gui.font;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import svarog.gui.GuiObject;

public class Line extends GuiObject {
	ByteBuffer line;
	
	public Line(ByteBuffer buffer, int width, int height) {
		super(width, height, new Vector2f());
		this.setLine(buffer);
	}

	public ByteBuffer getLine() {
		return line;
	}

	void setLine(ByteBuffer line) {
		this.line = line;
	}
}
