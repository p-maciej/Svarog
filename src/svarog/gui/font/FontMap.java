package svarog.gui.font;

import java.util.ArrayList;
import java.util.List;

import svarog.io.TextLoader;
import svarog.io.strings.IntAttribute;
import svarog.io.strings.StringUtils;

public class FontMap {
	List<Character> characters;
	TextLoader text;

	FontMap() {
		characters = new ArrayList<Character>();
	}
	
	public FontMap(String file) {
		characters = new ArrayList<Character>();
		
		text = new TextLoader(file);
		
		loadCharacters();
	}
	
	int getWidth(char ch) throws Exception {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getAttributeNameValue("width");
		
		throw new Exception("Attribute doesn't exists");
	}
	
	int getHeight(char ch) throws Exception {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getAttributeNameValue("height");
		
		throw new Exception("Attribute doesn't exists");
	}
	
	int getX(char ch) throws Exception {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getAttributeNameValue("x");
		
		throw new Exception("Attribute doesn't exists");
	}
	
	int getY(char ch) throws Exception {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getAttributeNameValue("y");
		
		throw new Exception("Attribute doesn't exists");
	}
	
	int getId(char ch) throws Exception {
		for(int i = 0; i < characters.size(); i++)
			if(characters.get(i).getCharacter() == ch)
				return characters.get(i).getCharacter();
		
		throw new Exception("Attribute doesn't exists");
	}
	
	private void loadCharacters() {
		for(int i = 1; i <= text.getNumberOfLines(); i++) {
			String line = text.getLine(i);
			
			Character character = new Character((char)getAttribute(line, "id", '='));
			character.addAttribute(new IntAttribute("x", getAttribute(line, "x", '=')));
			character.addAttribute(new IntAttribute("y", getAttribute(line, "y", '=')));
			character.addAttribute(new IntAttribute("width", getAttribute(line, "width", '=')));
			character.addAttribute(new IntAttribute("height", getAttribute(line, "height", '=')));
			characters.add(character);	
		}
	}
	
	private int getAttribute(String line, String attributeName, char spacer) {
		return Integer.parseInt(StringUtils.getAttributeValue(line, StringUtils.getStartIndexAttribute(line, attributeName, spacer)));
	}
	

}
