package svarog.gui.font;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.TextureObject;
import svarog.render.Texture;

public class Line extends TextureObject {
	
	public Line(ByteBuffer buffer, int width, int height) {
		super(new Texture(buffer, width, height), new Vector2f());
	}
	
	public Line(Vector2f position) {
		super(position);
	}
	
	public Line(int X, int Y) {
		super(new Vector2f(X, Y));
	}
	
	public Line(stickTo stickTo) {
		super(stickTo);
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
		
		super.setTexture(new Texture(line, lineWidth, lineHeight));
		super.setSize(lineWidth, lineHeight);
	}
}
