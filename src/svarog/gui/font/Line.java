package svarog.gui.font;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import svarog.gui.GuiObject;
import svarog.gui.GuiRenderer.stickTo;

public class Line extends GuiObject {
	ByteBuffer line;
	
	
	public Line() {
		super();
	}
	
	public Line(ByteBuffer buffer, int width, int height) {
		super(width, height, new Vector2f());
		this.setLine(buffer);
	}
	
	public Line(Vector2f position) {
		super(0, 0, position);
	}
	
	public Line(int X, int Y) {
		super(0, 0, new Vector2f(X, Y));
	}
	
	public Line(stickTo stickTo) {
		super(0, 0, stickTo);
	}
	
	public void setString(String string, Font font) {
		int lineWidth = 0;
		int lineHeight = 0;
		if(string.length() > 0)
			lineHeight = font.getCharacterBuffer(string.charAt(0)).getHeight();
		
		for(int i = 0; i < string.length(); i++) {
			CharacterBuffer character = font.getCharacterBuffer(string.charAt(i));
			if(character != null)
				lineWidth += character.getWidth();
		}
		
		ByteBuffer line = BufferUtils.createByteBuffer(lineWidth*lineHeight*4);

		for(int i = 0; i < string.length(); i++) {
			CharacterBuffer character = font.getCharacterBuffer(string.charAt(i));
			if(character != null)
				for(int n = 0; n < character.getBuffer().limit(); n++)
					line.put(character.getBuffer().get(n));
		}

		line.flip();
		
		this.setLine(line);
		super.setSize(lineWidth, lineHeight);
	}

	public ByteBuffer getLine() {
		return line;
	}

	void setLine(ByteBuffer line) {
		this.line = line;
	}
}
