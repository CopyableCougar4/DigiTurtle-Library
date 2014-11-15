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
import com.digiturtle.common.Logger.LoggingSystem;

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
			LoggingSystem.error("UnknownHostException in Server(int)", e);
		}
	}
	public Server(int port, String ip) {
		this.port = port;
		try {
			this.ip = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			LoggingSystem.error("UnknownHostException in Server(int, String)", e);
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
			LoggingSystem.error("IOException in sendPacket(Packet)", e);
		}
	}
	public void sendPacket(Packet packet, InetAddress ip, int port) {
		try {
			socket.send(new DatagramPacket(packet.construct(2048), 2048, ip, port));
		} catch (IOException e) {
			LoggingSystem.error("IOException in sendPacket(Packet, InetAddress, int)", e);
		}
	}
	
	private DatagramSocket socket;
	
	public void start() {
		try {
			socket = new DatagramSocket(port, ip);
			LoggingSystem.debug("Server Started: " + ip.getHostAddress() + ":" + port);
		} catch (SocketException e) {
			LoggingSystem.error("SocketException in start()", e);
			return;
		}
		alive = true;
		while (isAlive()) {
			byte[] data = new byte[2048];
			DatagramPacket _packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(_packet);
			} catch (IOException e) {
				LoggingSystem.error("IOException in start()", e);
			}
			Packet packet = Packet.destruct(_packet.getData());
			LoggingSystem.debug(packet.getData() + " from " + _packet.getData().length + "bytes");
			handlePacket(packet, _packet.getPort(), _packet.getAddress());
		}
	}
	
	private boolean alive = false;
	
	public boolean isAlive() {
		return alive;
	}
	
}
