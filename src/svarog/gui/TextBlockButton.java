package svarog.gui;

import java.util.List;

import org.joml.Vector2f;

import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.gui.font.TextBlock;

public class TextBlockButton extends TextBlock {

	private List<Line> lines;
	private List<Line> lines_hover;
	private boolean hover;
	
	public TextBlockButton(int maxWidth, Vector2f position) {
		super(maxWidth, position);
		
		super.setClickable(true);
		super.setOverable(true);
		this.hover = false;
	}
	
	public TextBlockButton(int maxWidth, GuiRenderer.stickTo stickTo) {
		super(maxWidth, stickTo);
		
		super.setClickable(true);
		super.setOverable(true);
		this.hover = false;
	}

	
	public void setString(Font font, Font font_hover, String string) {
		super.string = string;
		this.lines = super.lines = super.addLines(font);
		this.lines_hover = super.addLines(font_hover);
		super.setSize(super.getWidth(), super.lineHeight*lines.size());
	}
	
	public boolean isClicked() {
		return GuiRenderer.getClickedObjectId() == super.getId() ? true : false;
	}
	
	@Override
	public void update() {
		if(GuiRenderer.getMouseOverObjectId() == super.getId() && hover == false) {
			hover = true;
			super.lines = lines_hover;
		} else if(GuiRenderer.getMouseOverObjectId() != super.getId() && hover == true) {
			hover = false;
			super.lines = this.lines;
		}
	}
	
}
