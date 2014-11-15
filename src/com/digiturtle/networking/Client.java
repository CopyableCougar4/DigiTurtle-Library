package com.digiturtle.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.digiturtle.common.Logger.LoggingSystem;

public class Client implements Runnable {

	private ArrayList<PacketRouter> routers = new ArrayList<PacketRouter>();
	
	public void handlePacket(Packet packet, int port, InetAddress ip) {
		for (PacketRouter router : routers) {
			if (router.isValid(packet)) router.handlePacket(packet, port, ip);
		}
	}
	
	public void addRouter(PacketRouter router) {
		routers.add(router);
	}
	
	private DatagramSocket socket;
	
	public void sendPacket(Packet packet, InetAddress ip, int port) {
		if (socket == null) throw new RuntimeException("invalid or unbound socket");
		if (packet == null) throw new RuntimeException("packet == null");
		if (ip == null) throw new RuntimeException("ip == null");
		if (new Integer(port) == null) throw new RuntimeException("port == null");
		try {
			DatagramPacket datagram = new DatagramPacket(packet.construct(2048), 2048, ip, port);
			socket.send(datagram);
		} catch (IOException e) {
			LoggingSystem.error("IOException in sendPacket(Packet, InetAddress, int)", e);
		}
	}
	
	public void run() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			LoggingSystem.error("SocketException in run()", e);
			return;
		}
		alive = true;
		while (isAlive()) {
			byte[] data = new byte[2048];
			DatagramPacket _packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(_packet);
			} catch (IOException e) {
				LoggingSystem.error("IOException in run()", e);
			}
			Packet packet = Packet.destruct(_packet.getData());
			handlePacket(packet, _packet.getPort(), _packet.getAddress());
		}
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	private boolean alive = false;
	
	public boolean isAlive() {
		return alive;
	}
	
}
