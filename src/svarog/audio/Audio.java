package svarog.audio;

import org.lwjgl.openal.*;
import org.lwjgl.system.*;

import java.nio.*;
//import java.util.ArrayList;
//import java.util.List;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

public class Audio {
	private String defaultDeviceName;
	private long device;
	long context;
	private int[] attributes = {0};
	private ALCCapabilities alcCapabilities;
	@SuppressWarnings("unused")
	private ALCapabilities  alCapabilities;
	
	//private List<Sound> sounds; 
	
	public Audio() {
		//sounds = new ArrayList<Sound>();
		
		defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);

		alcCapabilities = ALC.createCapabilities(device);
		alCapabilities  = AL.createCapabilities(alcCapabilities);
		
		alcMakeContextCurrent(context);	
	}
	
	public void play(String filename, boolean continuous) throws Exception {
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
	
			int bufferPointer = alGenBuffers();
			
			alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);
	
			free(rawAudioBuffer);
	
			int sourcePointer = alGenSources();
	
			alSourcei(sourcePointer, AL_BUFFER, bufferPointer);
			alSourcei(sourcePointer, AL_LOOPING, continuous ? AL_TRUE : AL_FALSE);

			alSourcePlay(sourcePointer);

			//alDeleteSources(sourcePointer);
			//alDeleteBuffers(bufferPointer);
			
		} else
			throw new Exception("Error while playing audio");
	}
	
	public void finalize() {
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
}
