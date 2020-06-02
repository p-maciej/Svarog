package svarog.audio;

import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

public class Audio {
	private String defaultDeviceName;
	private long device;
	long context;
	private int[] attributes = {0};
	private ALCCapabilities alcCapabilities;
	@SuppressWarnings("unused")
	private ALCapabilities  alCapabilities;

	private float volume;
	
	public Audio() {	
		this.defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		this.device = alcOpenDevice(defaultDeviceName);
		this.context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);

		this.alcCapabilities = ALC.createCapabilities(device);
		this.alCapabilities  = AL.createCapabilities(alcCapabilities);
		
		alcMakeContextCurrent(context);	
		
		this.volume = 1f;
	}
	
	public void play(Sound sound) {
		if(this.volume < sound.getVolume())
			sound.setVolume(this.volume);
		
		alSourcePlay(sound.getPointer());
	}
	
	public void stop(Sound sound) {
		alSourceStop(sound.getPointer());
	}
	
	public void pause(Sound sound) {
		alSourcePause(sound.getPointer());
	}
	
	public boolean isPlaying(Sound sound) {
		return alGetSourcei(sound.getPointer(), AL_SOURCE_STATE) == AL_PLAYING;
	}
	
	public void finalize() throws Throwable {
		alcDestroyContext(context);
		alcCloseDevice(device);
	}

	public float getVolume() {
		return this.volume;
	}

	public void setGlobalVolume(float volume) {
		this.volume = volume;
	}
}
