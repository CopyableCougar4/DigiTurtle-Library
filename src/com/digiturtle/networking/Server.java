package com.digiturtle.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import com.digiturtle.common.FastRand;

public class Server {

	private HashMap<Long, Connection> connectionMap = new HashMap<Long, Connection>();
	private FastRand RNG = new FastRand();
	private int port;
	private InetAddress ip;
	
	public Server(int port) {
		this.port = port;
		try {
			ip = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	public Server(int port, String ip) {
		this.port = port;
		try {
			this.ip = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public long addConnection(Connection connection) {
		long address = RNG.randLong();
		connectionMap.put(address, connection);
		return address;
	}
	
	public Connection getConnection(long address) {
		return connectionMap.get(address);
	}
	
	private ArrayList<PacketRouter> routers = new ArrayList<PacketRouter>();
	
	public void handlePacket(Packet packet, int port, InetAddress ip) {
		for (PacketRouter router : routers) {
			if (router.isValid(packet)) router.handlePacket(packet, port, ip);
		}
	}
	
	public void addRouter(PacketRouter router) {
		routers.add(router);
	}
	
	public void sendPacket(Packet packet) {
		try {
			socket.send(new DatagramPacket(packet.construct(2048), 2048));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendPacket(Packet packet, InetAddress ip, int port) {
		try {
			socket.send(new DatagramPacket(packet.construct(2048), 2048, ip, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private DatagramSocket socket;
	
	public void start() {
		try {
			socket = new DatagramSocket(port, ip);
			System.out.println("Server Started: " + ip.getHostAddress() + ":" + port);
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
