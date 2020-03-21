package svarog.io.strings;

public class StringUtils {
	private static final int SPACE = 32;
	
	public static int getStartIndexAttribute(String line, String attributeName, char spacer) {
		char lastChar = SPACE;
		int index = 0;
		attributeName = attributeName + spacer;
		int startIndex = -1;
		
		for(int i = 0; i < line.length(); i++) {
			if((int)line.charAt(i) != SPACE) {
				if(lastChar == SPACE)
					index = 0;

				if(line.charAt(i) == attributeName.charAt(index)) {
					lastChar = line.charAt(i);
					index++;
					
					if(index == attributeName.length()) {
						startIndex = i+1;
						break;
					}
					
					continue;
				}
					
			} else {
				lastChar = SPACE;
			}
		}
			
		return startIndex;
	}
	
	public static String getAttributeValue(String line, int startIndex) {
		String value = "";
		if(startIndex > 0) {
			while(line.charAt(startIndex) != SPACE) {
				value += line.charAt(startIndex);
				startIndex++;
			}
		}
		return value;
	}
}
