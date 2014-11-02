package com.digiturtle.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Client {

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
		try {
			socket.send(new DatagramPacket(packet.construct(2048), 2048, ip, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		alive = true;
		while (isAlive()) {
			byte[] data = new byte[1024];
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
	
	private boolean alive = false;
	
	public boolean isAlive() {
		return alive;
	}
	
}
