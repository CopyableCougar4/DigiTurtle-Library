package com.digiturtle.networking;

import java.util.HashMap;

public class Types {

	private static HashMap<String, Byte> byteSet = new HashMap<String, Byte>();
	
	public static void addType(String type, byte header) {
		byteSet.put(type, header);
	}
	
	public static byte get(String type) {
		return byteSet.get(type);
	}
	
}
