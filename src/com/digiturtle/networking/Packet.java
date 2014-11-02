package com.digiturtle.networking;

import java.util.Arrays;

public class Packet {
	
	protected byte header;
	protected String data;
	
	public Packet(byte header, String data) {
		this.header = header;
		this.data = data;
	}
	
	public byte[] construct(int size) {
		byte[] output = new byte[size];
		output[0] = header;
		int index = 1;
		for (byte b : data.getBytes()) {
			output[index++] = b;
		}
		return output;
	}
	
	public byte getHeader() {
		return header;
	}
	
	public String getData() {
		return data.trim();
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public static Packet destruct(byte[] input) {
		byte header = input[0];
		byte[] string = new byte[input.length];
		Arrays.copyOfRange(input, 1, input.length - 1);
		String data = new String(string);
		return new Packet(header, data.trim());
	}

}
