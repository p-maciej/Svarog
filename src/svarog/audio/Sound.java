package svarog.audio;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.system.MemoryStack;

public class Sound {
	private int sourcePointer;
	private int bufferPointer;
	
	private float volume;
	
	public Sound(String filename) throws Exception {
		init(filename, false);
		this.volume = 1f;
	}
	
	public Sound(String filename, boolean continuous) throws Exception {
		init(filename, continuous);
		this.volume = 1f;
	}
	
	private void init(String filename, boolean continuous) throws Exception {
		ShortBuffer rawAudioBuffer;
		
		int channels;
		int sampleRate;

		try (MemoryStack stack = stackPush()) {
		    IntBuffer channelsBuffer   = stack.mallocInt(1);
		    IntBuffer sampleRateBuffer = stack.mallocInt(1);
		    
		    rawAudioBuffer = stb_vorbis_decode_filename("./resources/audio/" + filename, channelsBuffer, sampleRateBuffer);

		    channels = channelsBuffer.get(0);
		    sampleRate = sampleRateBuffer.get(0);
		}

		if(rawAudioBuffer != null) {
			int format = -1;
			if (channels == 1) {
			    format = AL_FORMAT_MONO16;
			} else if (channels == 2) {
			    format = AL_FORMAT_STEREO16;
			}
	
			bufferPointer = alGenBuffers();
			
			alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);
	
			free(rawAudioBuffer);
	
			sourcePointer = alGenSources();
	
			alSourcei(sourcePointer, AL_BUFFER, bufferPointer);
			alSourcei(sourcePointer, AL_LOOPING, continuous ? AL_TRUE : AL_FALSE);
			
		} else
			throw new Exception("Error while playing audio");
	}
	
	public void setLooping(boolean state) {
		alSourcei(sourcePointer, AL_LOOPING, state ? AL_TRUE : AL_FALSE);
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
		alSourcef(sourcePointer, AL_GAIN, volume);
	}
	
	public float getVolume() {
		return volume;
	}
	
	int getPointer() {
		return sourcePointer;
	}
	
	protected void finalize() throws Throwable {
		alDeleteSources(sourcePointer);
		alDeleteBuffers(bufferPointer);
	}
}
