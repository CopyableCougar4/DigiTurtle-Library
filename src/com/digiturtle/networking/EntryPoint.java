package com.digiturtle.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

// Entry point to a client

public class EntryPoint {

	private InetAddress ip;
	private int port;
	private DatagramSocket socket;
	
	public EntryPoint(InetAddress ip, int port) {
		this.ip = ip;
		this.port = port;
		try {
			socket = new DatagramSocket(port, ip);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPacket(Packet packet) {
		if (socket != null) {
			DatagramPacket datagram = new DatagramPacket(packet.construct(2048), 2048, ip, port);
			try {
				socket.send(datagram);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
