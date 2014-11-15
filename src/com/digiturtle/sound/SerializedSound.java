package com.digiturtle.sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import com.digiturtle.common.Logger.LoggingSystem;

public class SerializedSound {
	
	// Store a sound
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String IN = "click.ogg", OUT = "click.sound";
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(OUT));
		OggData oggData = OggDecoder.decode(new FileInputStream(IN));
		SoundObject object = new SoundObject(oggData);
		objectOutputStream.writeObject(object);
		objectOutputStream.close();
	}
	
	// Load a sound
	
	public static OggData getOggData(String IN) {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(IN));) {
			return ((SoundObject) objectInputStream.readObject()).getOgg();
		} catch (IOException | ClassNotFoundException e) {
			LoggingSystem.error("Exception in getOggData(String)", e);
			return null;
		}		
	}
	

	public static class SoundObject implements Serializable {

		private static final long serialVersionUID = -4046817220425633497L;
		
		/** Raw byte data for @see OggData */
		public byte[] data;
		
		/** @see OggData */
		public int rate;
		
		/** @see OggData */
		public int channels;
		
		public OggData getOgg() {
			OggData oggData = new OggData();
			ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
			buffer.put(data);
			buffer.flip();
			oggData.data = buffer;
			oggData.rate = rate;
			oggData.channels = channels;
			return oggData;
		}
		
		public SoundObject(OggData oggData) {
			rate = oggData.rate;
			channels = oggData.channels;
			data = new byte[oggData.data.remaining()];
			oggData.data.get(data);
		}
		
	}

}
