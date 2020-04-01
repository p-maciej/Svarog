package svarog.gui.font;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import svarog.gui.GuiRenderer;

public class TextBlock {
	private static final char SPACE = 32; 
	private List<Line> lines;
	private String string;
	private int maxWidth;
	private Vector2f transform;
	private Vector2f position;
	private GuiRenderer.stickTo stickTo;
	private int lineHeight;
	
	public TextBlock(int maxWidth, Vector2f position) {
		lines = new ArrayList<Line>();
		this.transform = new Vector2f();
		this.position = new Vector2f();
		
		this.setMaxWidth(maxWidth);
		this.setPosition(position);
	}
	
	public TextBlock(int maxWidth, GuiRenderer.stickTo stickTo) {
		lines = new ArrayList<Line>();
		this.transform = new Vector2f();
		this.position = new Vector2f();
		this.stickTo = stickTo;
		
		this.setMaxWidth(maxWidth);
		this.setPosition(transform);
	}

	public List<Line> getLines() {
		return lines;
	}

	void setLines(List<Line> lines) {
		this.lines = lines;
	}

	public String getString() {
		return string;
	}

	public void setString(Font font, String string) {
		this.string = string;
		addLines(font);
	}

	private void addLines(Font font) {
		int lineWidth = 0;
		int wordHeight = 0;
		int lineChars = 0;
		
		if(string.length() > 0)
			lineHeight = wordHeight = font.getCharacterBuffer(string.charAt(0)).getHeight();
		
		
		for(int i = 0; i < string.length(); i++) {	
			Word word = attemptToAddWord(font, i, lineWidth);
			i = word.getLastIndex();
			
			if(word.getWordWidth() > 0) {
				lineWidth += word.getWordWidth();
				lineChars += word.getWordLength();
					
				if(i == string.length()-1) {
					addLine(i+1, wordHeight, lineWidth, lineChars, font);
				}
			} else {
				i = i-(word.getWordLength()-1);
				addLine(i, wordHeight, lineWidth, lineChars, font);
				lineChars = 0;
				lineWidth = 0;
				i--;
			}
		}
	}
	
	private void addLine(int i, int lineHeight, int lineWidth, int lineChars, Font font) {
		ByteBuffer line = BufferUtils.createByteBuffer(lineWidth*lineHeight*4);

		for(int j = i-lineChars; j < i; j++) {
			CharacterBuffer character = font.getCharacterBuffer(string.charAt(j));
			if(character != null)
				for(int n = 0; n < character.getBuffer().limit(); n++)
					line.put(character.getBuffer().get(n));
		}
		line.flip();

		lines.add(new Line(line, lineWidth, lineHeight));
	}
	
	private Word attemptToAddWord(Font font, int index, int lineWidth) {
		int wordWidth = 0;
		int lastIndex = 0;
		int wordLength = 0;
		for(int i = index; i < string.length(); i++) {
			if(string.charAt(i) != SPACE) {
				CharacterBuffer character = font.getCharacterBuffer(string.charAt(i));
				if(character != null) {
					wordWidth += character.getWidth();
					wordLength++;
					lastIndex = i;
				}
			} else {
				CharacterBuffer character = font.getCharacterBuffer(string.charAt(i));
				if(character != null) {
					wordWidth += character.getWidth();
					wordLength++;
					lastIndex = i;
				}
				break;
			}
		}
		if(wordWidth < maxWidth)
			if(lineWidth+wordWidth < maxWidth)
				return new Word(wordWidth, lastIndex, wordLength, false);
			else 
				return new Word(0, lastIndex, wordLength, false);
		else
			throw new IllegalStateException("Box is to small");
	}
	
	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public Vector2f getTranform() {
		return transform;
	}

	public void setTransformPosition(Vector2f position) {
		this.transform.set(position);
	}
	
	public void setTransformPosition(float X, float Y) {
		this.transform.set(new Vector2f(X, Y));
	}
	
	public GuiRenderer.stickTo getStickTo() {
		return stickTo;
	}

	public void setStickTo(GuiRenderer.stickTo stickTo) {
		this.stickTo = stickTo;
	}

	public int getHeight() {
		return lineHeight*lines.size();
	}
	
	public void setPosition(Vector2f direction) {
		this.position.set(direction.x, -direction.y);
		setTransformPosition(direction);
	}
	
	public void setPosition(float X, float Y) {
		this.position.set(X, -Y);
		setTransformPosition(X, Y);
	}
	
	public void move(Vector2f direction) {
		this.position.add(direction.x, -direction.y);
	}
	
	public void move(float X, float Y) {
		this.position.add(X, -Y);
	}
	
	public Vector2f getPosition() {
		return position;
	}
	////////////// NEW CLASS /////////////////////
	private class Word {
		private int wordWidth;
		private int wordLendth;
		private int lastIndex;

		private Word(int wordWidth, int lastIndex, int wordLength, boolean wordException) {
			this.wordWidth = wordWidth;
			this.lastIndex = lastIndex;
			this.wordLendth = wordLength;
		}
		
		private int getWordWidth() {
			return wordWidth;
		}
		
		private int getLastIndex() {
			return lastIndex;
		}

		public int getWordLength() {
			return wordLendth;
		}
	}
	////////////////////////////////////////////
}
