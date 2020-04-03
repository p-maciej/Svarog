package svarog.gui.font;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import svarog.gui.GuiObject;
import svarog.gui.GuiRenderer;
import svarog.render.Texture;

public class TextBlock  extends GuiObject {
	private static final char SPACE = 32; 
	private List<Line> lines;
	private String string;
	private int lineHeight;
	
	public TextBlock(int maxWidth, Vector2f position) {
		super(maxWidth, 0, position);
		lines = new ArrayList<Line>();
	}
	
	public TextBlock(int maxWidth, GuiRenderer.stickTo stickTo) {
		super(maxWidth, 0, stickTo);
		
		lines = new ArrayList<Line>();
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
		super.setSize(super.getWidth(), lineHeight*lines.size());
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
		if(wordWidth < super.getWidth())
			if(lineWidth+wordWidth < super.getWidth())
				return new Word(wordWidth, lastIndex, wordLength, false);
			else 
				return new Word(0, lastIndex, wordLength, false);
		else
			throw new IllegalStateException("Box is to small");
	}
	
	protected Texture getTexture() { return null; }
	
	protected void update() {}
	
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
