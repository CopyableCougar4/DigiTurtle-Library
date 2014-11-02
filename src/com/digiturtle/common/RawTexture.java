package com.digiturtle.common;

public class RawTexture {

	public byte[] data;
	public int width, height;
	
	public RawTexture() {
		
	}
	
	public RawTexture(String _data) {
		String[] input = _data.split(":");
		if (input.length < 3) throw new RuntimeException("Not enough data supplied: " + _data);
		width = Integer.parseInt(input[0]);
		height = Integer.parseInt(input[1]);
		String forbytes = input[2];
		String[] rawbyteString = forbytes.split(",");
		data = new byte[rawbyteString.length];
		int index = 0;
		for (String rawbyteText : rawbyteString) data[index++] = Byte.parseByte(rawbyteText);
	}
	
}
