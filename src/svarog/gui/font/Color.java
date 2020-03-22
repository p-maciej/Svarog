package svarog.gui.font;

public class Color {
	private byte R;
	private byte G;
	private byte B;
	
	public Color(byte red, byte green, byte blue) {
		this.setR(red);
		this.setG(green);
		this.setB(blue);
	}

	public void set(Color color) {
		this.setR(color.getR());
		this.setG(color.getG());
		this.setB(color.getB());
	}
	
	public byte getR() {
		return R;
	}
	public void setR(byte r) {
		this.R = r;
	}
	public byte getG() {
		return G;
	}
	public void setG(byte g) {
		this.G = g;
	}
	public byte getB() {
		return B;
	}
	public void setB(byte b) {
		this.B = b;
	}
}
