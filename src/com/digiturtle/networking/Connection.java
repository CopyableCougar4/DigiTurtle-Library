package com.digiturtle.networking;

import java.net.InetAddress;

public class Connection {

	private EntryPoint entryPoint;
	private String username;
	private boolean online = false;
	
	public Connection(String username) {
		this.username = username;
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public void connect(InetAddress ip, int port) {
		entryPoint = new EntryPoint(ip, port);
		online = true;
	}
	
	public void disconnect() {
		online = false;
	}
	
	public String getUsername() {
		return username;
	}
	
	public EntryPoint getEntryPoint() {
		return entryPoint;
	}
	
}
