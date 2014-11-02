package com.digiturtle.world;

import java.util.ArrayList;

import com.digiturtle.common.RotatedTransparencyRegion;
import com.digiturtle.entities.EntitySystem;
import com.digiturtle.networking.Packet;
import com.digiturtle.physics.Shape;

public interface World {

	public ArrayList<Shape> getObjects();
	
	public boolean collision(RotatedTransparencyRegion region);
	
	public void render();
	
	public EntitySystem getBatcher();
	
	public void click(int x, int y);
	public void mouse(int x, int y, boolean down);
	public void key(int keycode);
	
	public void handlePacket(Packet packet);
	
	public void generate(long address);
	
	public float getSpeed(float x, float y);
	
}
