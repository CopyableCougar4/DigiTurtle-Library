package com.digiturtle.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

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
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		alive = true;
		while (isAlive()) {
			byte[] data = new byte[2048];
			DatagramPacket _packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(_packet);
			} catch (IOException e) {
				e.printStackTrace();
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
