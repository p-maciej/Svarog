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

	public byte getR() {
		return R;
	}
	public void setR(byte r) {
		R = r;
	}
	public byte getG() {
		return G;
	}
	public void setG(byte g) {
		G = g;
	}
	public byte getB() {
		return B;
	}
	public void setB(byte b) {
		B = b;
	}
}
