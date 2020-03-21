package svarog.gui.font;

import java.util.ArrayList;
import java.util.List;

import svarog.io.strings.IntAttribute;

public class Character {
	char character;
	List<IntAttribute> attributes;
	
	Character(char character) {
		attributes = new ArrayList<IntAttribute>();
		this.setCharacter(character);
	}

	void addAttribute(IntAttribute attribute) {
		attributes.add(attribute);
	}
	
	int getAttributeNameValue(String attributeName) throws Exception {
		for(int i = 0; i < attributes.size(); i++)
			if(attributes.get(i).getName() == attributeName)
				return attributes.get(i).getValue();
		
		throw new Exception("Attribute doesn't exists");
	}
	
	char getCharacter() {
		return character;
	}

	void setCharacter(char character) {
		this.character = character;
	}
}
