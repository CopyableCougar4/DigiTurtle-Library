package com.digiturtle.sound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.BufferUtils;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

public class OggDecoder extends InputStream {
	
	private InputStream inputStream;				// the input stream to decode
	private int available;							// the number of readable bytes in the input stream
	private SyncState syncState = new SyncState();	// handles the syncing of the input stream
	private boolean endOfBitStream = true;			// is the bit stream completely read
	private boolean inited = false;					// is the OGG info block initialized 
	private Info oggInfo = new Info();				// bitstream settings
	private Page page = new Page(); 						// one Ogg bitstream page
	private StreamState streamState = new StreamState(); 	// weld the pages together
	private Packet packet = new Packet(); 					// one raw packet of data for decode
	private DspState dspState = new DspState(); 		// central working state for the packet->PCM decoder
	private Block vorbisBlock = new Block(dspState); 	// local working space for packet->PCM decode
	private int convsize = 4096 * 4;					// size of the data segments
	private byte[] convbuffer = new byte[convsize]; 	// take 8k out of the data segment, not the stack
	private boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);	// byte ordering for data
	private ByteBuffer pcmBuffer = BufferUtils.createByteBuffer(4096 * 500);			// buffer for decoded PCM data
	private int bytes = 0;							// number of bytes read
	private boolean endOfStream;					// whether there is more available data
	private byte[] buffer;							// scratch buffer
	private Comment comment = new Comment();		// bitstream user comments
	private int readIndex;							// read data like a normal input stream
	
	public OggDecoder(InputStream inputstream) throws IOException {
		if (inputstream == null) {
			throw new IOException("Null input");
		}
		inputStream = inputstream;
		available = inputstream.available();
		syncState.init();	// initialize the streaming of OGG data
		readPCM();		// decode the OGG data
	}
	
	public int getLength() {
		return available;
	}
	
	// audio settings
	public int getChannels() {
		return oggInfo.channels;
	}
	public int getRate() {
		return oggInfo.rate;
	}
	
	public void readPCM() {
		boolean wrote = false;
		while (true) {
			if (endOfBitStream) {
				if (!getPageAndPacket()) {	// check if the next stream is valid
					break;
				}
				endOfBitStream = false;
			}
			if (!inited) {
				inited = true; // mark the OGG block as initialized
				return;
			}
			float[][][] _pcm = new float[1][][]; // storage for raw PCM data
			int[] _index = new int[oggInfo.channels]; // ogg channel data
			// decode data for as long as possible
			while (!endOfBitStream) {
				while (!endOfBitStream) {
					int result = syncState.pageout(page); // validate the current page of ogg data
					if (result == 0) {
						break; // get more data
					}
					if (result == -1) {
						System.out.println("Corrupt or missing data in bitstream");
					} else {
						streamState.pagein(page); // stream the page to turn synced data into ogg pages
						while (true) {
							result = streamState.packetout(packet);	// validate the next packet
							if (result == 0) {
								break; // we need more data to read
							}
							if (result == -1) {
								// ignore corrupt data
							} else {
								// decode the next packet
								int samples;
								if (vorbisBlock.synthesis(packet) == 0) { // read a block for OGG data
									dspState.synthesis_blockin(vorbisBlock);
								}
								// **pcm is a multichannel float vector. In stereo, for
								// example, pcm[0] is left, and pcm[1] is right. samples is
								// the size of each channel. Convert the float values
								// (-1.<=range<=1.) to whatever PCM format and write it out
								while ((samples = dspState.synthesis_pcmout(_pcm, _index)) > 0) {
									// read the raw PCM data
									float[][] pcm = _pcm[0]; // store PCM data
									int bout = (samples < convsize ? samples : convsize); // min(samples, convsize)
									for (int i = 0; i < oggInfo.channels; i++) {
										int ptr = i * 2;
										//int ptr=i;
										int mono = _index[i];
										for (int j = 0; j < bout; j++) {
											int val = (int) (pcm[i][mono + j] * 32767.);
											// might as well guard against clipping
											if (val > 32767) {
												val = 32767;
											}
											if (val < -32768) {
												val = -32768;
											}
											if (val < 0)
											val = val | 0x8000;
											if (bigEndian) {
												convbuffer[ptr] = (byte) (val >>> 8);
												convbuffer[ptr + 1] = (byte) (val);
											} else {
												convbuffer[ptr] = (byte) (val);
												convbuffer[ptr + 1] = (byte) (val >>> 8);
											}
											// load the raw PCM data into a conversion buffer
											ptr += 2 * (oggInfo.channels);
										}
									}
									// read the decoded PCM data into the resulting buffer
									int bytesToWrite = 2 * oggInfo.channels * bout;
									if (bytesToWrite >= pcmBuffer.remaining()) {
										System.out.println("Read block from OGG that was too big to be buffered: " + bytesToWrite);
									} else {
										pcmBuffer.put(convbuffer, 0, bytesToWrite);
									}
									wrote = true;
									dspState.synthesis_read(bout); // tell vorbis the latest number of samples
								}
							}
						}
						// check if a new page needs to be read / decoded
						if (page.eos() != 0) {
							endOfBitStream = true;
							}
						if ((!endOfBitStream) && (wrote)) {
							return;	// check if the data should still be written
						}
					}
				}
				if (!endOfBitStream) {
					bytes = 0;
					int index = syncState.buffer(4096);
					if (index >= 0) {
						buffer = syncState.data;
						try {
						bytes = inputStream.read(buffer, index, 4096);
						} catch (Exception e) {
						System.out.println("Failure during vorbis decoding");
						e.printStackTrace(System.out);
						endOfStream = true;
						return;
						}
					} else {
						bytes = 0;
					}
					syncState.wrote(bytes);
					if (bytes == 0) {
						endOfBitStream = true;
					}
				}
			}
			streamState.clear(); // clean up the current bitstream
			// tell Vorbis to clear its data
			vorbisBlock.clear();
			dspState.clear();
			oggInfo.clear();
		}
		syncState.clear(); // clean up data
		endOfStream = true;
	}
	
	public boolean getPageAndPacket() {
		// validate the data
		int index = syncState.buffer(4096); // send Vorbis an ogg page
		buffer = syncState.data;	// add the synced bitstream as the scratch buffer
		if (buffer == null) {
			endOfStream = true;	// validation of the scratch buffer
			return false;
		}
		try {
			bytes = inputStream.read(buffer, index, 4096); // read a block of raw data
		} catch (Exception e) {
			System.out.println("Failure reading in vorbis");
			e.printStackTrace(System.out);
			endOfStream = true;
			return false;
		}
		syncState.wrote(bytes); // mark these bytes as loaded
		// Get the first page.
		if (syncState.pageout(page) != 1) {
			// have we simply run out of data?  If so, we're done.
			if (bytes < 4096)
				return false;
			// error case.  Must not be Vorbis data
			System.out.println("Input does not appear to be an Ogg bitstream.");
			endOfStream = true;
			return false;
		}
		// Use the serial number to prepare the bitstream
		streamState.init(page.serialno());
		// validate the OGG bitstream
		oggInfo.init();
		comment.init();
		if (streamState.pagein(page) < 0) {
			// error; stream version mismatch perhaps
			System.out.println("Error reading first page of Ogg bitstream data.");
			endOfStream = true;
			return false;
		}

		if (streamState.packetout(packet) != 1) {
			// no page? must not be vorbis
			System.out.println("Error reading initial header packet.");
			endOfStream = true;
			return false;
		}

		if (oggInfo.synthesis_headerin(comment, packet) < 0) {
			// error case; not a vorbis header
			System.out.println("This Ogg bitstream does not contain Vorbis audio data.");
			endOfStream = true;
			return false;
		}
		// Read the comment and codebook headers
		int i = 0;
		while (i < 2) {
			while (i < 2) {
				int result = syncState.pageout(page);	// load a page
				if (result == 0)
					break; // Need more data
				if (result == 1) {
					streamState.pagein(page); // weld a logical OGG page together
					while (i < 2) {
						result = streamState.packetout(packet);	// weld a packet together
						if (result == 0)
							break;
						if (result == -1) {
							// Data at some point was corrupted or missing!
							System.out.println("Corrupt secondary header.  Exiting.");
							endOfStream = true;
							return false;
						}
						oggInfo.synthesis_headerin(comment, packet);
						i++;
					}
				}
			}
			// no harm in not checking before adding more
			index = syncState.buffer(4096);
			buffer = syncState.data;
			try {
				bytes = inputStream.read(buffer, index, 4096);	// read the next block
			} catch (Exception e) {
				System.out.println("Failed to read Vorbis: ");
				e.printStackTrace(System.out);
				endOfStream = true;
				return false;
			}
			if (bytes == 0 && i < 2) {
				System.out.println("End of file before finding all Vorbis headers!");
				endOfStream = true;
				return false;
			}
			syncState.wrote(bytes);
		}
		convsize = 4096 / oggInfo.channels;	// calculate the size of the conversion buffers
		// initialized the decoder
		dspState.synthesis_init(oggInfo); // central decode state
		vorbisBlock.init(dspState); // local state for most of the decode		
		return true;
	}
	
	public int available() {
		return endOfStream ? 0 : 1;
	}
	
	public boolean atEnd() {
		return endOfStream && (readIndex >= pcmBuffer.position());
	}
	
	public int read() throws IOException {
		if (readIndex >= pcmBuffer.position()) {
			pcmBuffer.clear();
			readPCM();
			readIndex = 0;
		}
		if (readIndex >= pcmBuffer.position()) {
			return -1;
		}
		int value = pcmBuffer.get(readIndex);
		if (value < 0) {
			value = 256 + value;
		}
		readIndex++;		
		return value;
	}
	
	public int read(byte[] b, int off, int len) {
		for (int i=0;i<len;i++) {
			try {
				int value = read();
				if (value >= 0) {
					b[i] = (byte) value;
				} else {
					if (i == 0) {						
						return -1;
					} else {
						return i;
					}
				}
			} catch (IOException e) {
				e.printStackTrace(System.out);
				return i;
			}
		}
		
		return len;
	}
	
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	public void close() {
		
	}
	
	public static OggData decode(InputStream stream) {
		try {
			ByteArrayOutputStream dataout = new ByteArrayOutputStream();
			OggDecoder oggInput = new OggDecoder(stream);
			while (!oggInput.atEnd()) {
				dataout.write(oggInput.read());
			}
			OggData ogg = new OggData();
			ogg.channels = oggInput.getChannels();
			ogg.rate = oggInput.getRate();
			byte[] data = dataout.toByteArray();
			ogg.data = ByteBuffer.allocateDirect(data.length);
			ogg.data.put(data);
			ogg.data.rewind();
			oggInput.close();
			return ogg;
		} catch (IOException e) {
			return null;
		}
	}

}
