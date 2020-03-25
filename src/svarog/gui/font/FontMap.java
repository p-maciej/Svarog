package svarog.gui.font;

import java.util.ArrayList;
import java.util.List;

import svarog.io.TextLoader;
import svarog.io.strings.StringUtils;

public class FontMap {
	List<Character> characters;
	TextLoader text;
	
	FontMap(String file) {
		characters = new ArrayList<Character>();
		
		text = new TextLoader(file);
		
		loadCharacters();
	}
	
	int getWidth(char ch) {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getWidth();
		
		return 0;
	}
	
	int getHeight(char ch) {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getHeight();
		
		return 0;
	}
	
	int getX(char ch) {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getX();
		
		return 0;
	}
	
	int getY(char ch) {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getY();
		
		return 0;
	}
	
	int getId(char ch) {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getCharacter();
		
		return 0;
	}
	
	int getWidth(int id) {
		return characters.get(id).getWidth();
	}
	
	int getHeight(int id) {
		return characters.get(id).getHeight();
	}
	
	int getX(int id) {
		return characters.get(id).getX();
	}
	
	int getY(int id) {
		return characters.get(id).getY();
	}
	
	int getCharAscii(int id) {
		return characters.get(id).getCharacter();
	}
	
	int getNumerOfCharacters() {
		return characters.size();
	}
	
	private void loadCharacters() {
		for(int i = 1; i <= text.getNumberOfLines(); i++) {
			String line = text.getLine(i);
			
			Character character = new Character((char)getAttribute(line, "id", '='));
			character.setX(getAttribute(line, "x", '='));
			character.setY(getAttribute(line, "y", '='));
			character.setWidth(getAttribute(line, "width", '='));
			character.setHeight(getAttribute(line, "height", '='));
			characters.add(character);	
		}
	}
	
	private int getAttribute(String line, String attributeName, char spacer) {
		return Integer.parseInt(StringUtils.getAttributeValue(line, StringUtils.getStartIndexAttribute(line, attributeName, spacer)));
	}
}
