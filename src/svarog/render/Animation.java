package svarog.render;

import svarog.io.Timer;

public class Animation extends Texture {
	private Texture[] frames;
	private int pointer;

	private double elapsedTime;
	private double currentTime;
	private double lastTime;
	private double fps;

	public Animation(int amount, int fps, String filename) {
		super("textures/animations/"+filename +"_0.png");
		this.pointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0/(double)fps;

		this.frames = new Texture[amount];
		for(int i = 0; i < amount; i++) {
			this.frames[i] = new Texture("textures/animations/" + filename + "_" + i + ".png");
			this.frames[i].prepare();
		}
	}
	
	@Override
	public void prepare() {
		for(int i = 0; i < frames.length; i++) {
			this.frames[i].prepare();
		}
	}
	
	@Override
	public void bind(int sampler) {
		this.currentTime = Timer.getTime();
		this.elapsedTime += currentTime - lastTime;

		if(elapsedTime >= fps) {
			elapsedTime -= fps;
			pointer++;
		}

		if(pointer >= frames.length) 
			pointer = 0;

		this.lastTime = currentTime;

		
		frames[pointer].bind(sampler);
	}
}