package com.digiturtle.entities;

import com.digiturtle.networking.Packet;

public interface NetworkedEntity extends Entity {

	/*
	 * Attributes:
	 * 	image
	 *  region for collision with a top left corner offset
	 *  health
	 *  balance
	 *  experience / level
	 *  username
	 *  server pointer
	 *  AI
	 */
	
	public long getServerPointer();
	
	public void handlePacket(Packet packet);
	
}
