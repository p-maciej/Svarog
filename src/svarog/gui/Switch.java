package svarog.gui;

import svarog.gui.GuiRenderer.stickTo;

public class Switch extends Button {
	private boolean state;
	
	private Button buttonOn;
	private Button buttonOff;
	
	public Switch(Button buttonOff, Button buttonOn) {
		super(buttonOff);
		
		this.buttonOff = buttonOff;
		this.buttonOn = buttonOn;
		
		this.state = false;
	}
	
	public Switch(Button buttonOff, Button buttonOn, float X, float Y) {
		super(buttonOff);
		super.setPosition(X, Y);
		buttonOn.setPosition(X, Y);
		
		this.buttonOff = buttonOff;
		this.buttonOn = buttonOn;
		
		this.state = false;
	}
	
	public Switch(Button buttonOff, Button buttonOn, stickTo stickTo) {
		super(buttonOff);
		super.setStickTo(stickTo);
		buttonOn.setStickTo(stickTo);
		
		this.buttonOff = buttonOff;
		this.buttonOn = buttonOn;
		
		this.state = false;
	}
	
	public boolean isOn() {
		return state;
	}
	
	public Switch setIsOn(boolean state) {
		this.state = state;
		
		setState(state);
		super.hover = false;
		super.setTexture(super.copy);
		
		return this;
	}
	
	@Override
	public void update() {
		boolean forceUpdate = false;
		if(super.isClicked()) {
			toggle();
			forceUpdate = true;
		}
		
		if(GuiRenderer.getMouseOverObjectId() == super.getId() && (hover == false || forceUpdate == true)) {
			super.hover = true;
			super.setTexture(super.hoverTexture);
		} else if(GuiRenderer.getMouseOverObjectId() != super.getId() && (hover == true || forceUpdate == true)) {
			super.hover = false;
			super.setTexture(super.copy);
		}
	}
	
	private void toggle() {
		state = !state;
		
		setState(state);
	}
	
	private void setState(Boolean state) {
		if(state) {
			super.copy = buttonOn.copy;
			super.hoverTexture = buttonOn.hoverTexture;
		} else {
			super.copy = buttonOff.copy;
			super.hoverTexture = buttonOff.hoverTexture;
		}
	}
}
