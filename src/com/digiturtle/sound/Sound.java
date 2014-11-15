package com.digiturtle.sound;

import java.io.InputStream;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

import com.digiturtle.common.Logger.LoggingSystem;

import static org.lwjgl.openal.AL10.*;

public class Sound {
	
	private int buffer, source;
	private FloatBuffer 
		sourcePos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }),
		sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }),
		listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }),
		listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }),
		listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f });
	
	private static boolean inited = false;
	
	private void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			LoggingSystem.error("LWJGLException in init()", e);
		}
		inited = true;
	}
	
	public Sound(InputStream inputStream) {
		if (!inited) init();
		OggData decoder = OggDecoder.decode(inputStream);
		if(loadALData(decoder) == AL_FALSE) {
			LoggingSystem.warn("Error loading data.");
			return;
	    }
	}
	
	public Sound(OggData decoder) {
		if (!inited) init();
		if(loadALData(decoder) == AL_FALSE) {
			LoggingSystem.warn("Error loading data.");
			return;
	    }
	}

	private int loadALData(OggData data) {
		buffer = alGenBuffers();
		if (alGetError() != AL_NO_ERROR) return AL_FALSE;
		alBufferData(buffer, data.channels > 1 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16, data.data, data.rate);
		source = alGenSources();
	    if (alGetError() != AL_NO_ERROR)
	      return AL_FALSE;
	    alSourcei(source, AL_BUFFER, buffer);
	    alSourcef(source, AL_PITCH, 1.0f);
	    alSourcef(source, AL_GAIN, 1.0f);
	    alSource(source, AL_POSITION, (FloatBuffer) sourcePos.flip());
	    alSource(source, AL_VELOCITY, (FloatBuffer) sourceVel.flip());
	    alListener(AL_POSITION, (FloatBuffer) listenerPos.flip());
	    alListener(AL_VELOCITY, (FloatBuffer) listenerVel.flip());
	    alListener(AL_ORIENTATION, (FloatBuffer) listenerOri.flip());
	    if (alGetError() == AL_NO_ERROR)
	        return AL_TRUE;
	    return AL_FALSE;
	}
	
	protected void wipe() {
		alDeleteSources(source);
	    alDeleteBuffers(buffer);
	}
	
	// Manipulate the sound and whether it's playing or not
	
	public void play() {
		alSourcePlay(source);
	}
	
	public void pause() {
		alSourcePause(source);
	}
	
	public void stop() {
		alSourceStop(source);
	}
	
}
