package svarog.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextLoader {
	private List<String> lines; 

	public TextLoader(String fileName) {
		lines = new ArrayList<String>();
		
		try(BufferedReader file = new BufferedReader(new FileReader("./resources/" + fileName))) {

		    String line = file.readLine();
		    while (line != null) {
		        lines.add(line);
		        line = file.readLine();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getLine(int lineNumber) {
		return lines.get(lineNumber-1);
	}
	
	public int getNumberOfLines() {
		return lines.size();
	}
}
