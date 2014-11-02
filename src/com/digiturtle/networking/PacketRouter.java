package com.digiturtle.networking;

import java.net.InetAddress;

public interface PacketRouter {

	public boolean isValid(Packet packet);
	
	public boolean handlePacket(Packet packet, int port, InetAddress ip);
	
	public PacketRouter setServer(Server server);
	
}
