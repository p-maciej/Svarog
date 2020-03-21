package svarog.io;

public class IntAttribute {
	String name;
	int value;
	
	public IntAttribute(String name, int value) {
		this.setName(name);
		this.setValue(value);
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
