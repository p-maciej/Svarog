package svarog.io.strings;

public class StringAttribute {
	String name;
	String value;
	
	public StringAttribute(String name, String value) {
		this.setName(name);
		this.setValue(value);
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
