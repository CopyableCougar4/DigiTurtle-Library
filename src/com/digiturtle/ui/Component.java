package com.digiturtle.ui;

import java.util.Collection;

import com.digiturtle.common.ComponentRegion;

public interface Component {

	public boolean isLocked();
	public void unlock();
	public void lock();
	
	public float getX();
	public float getY();
	public void setLocation(float x, float y);
	
	public void click(int x, int y);
	public void mouse(int x, int y, boolean down);
	
	public void key(int keycode);
	
	public void drag(int dx, int dy, int sx, int sy);
	
	public boolean overlaps(Component component);
	
	public ComponentRegion getRegion();
	
	public void render();
	
	public void update();
	
	public void addChild(String name, Component component);
	public Collection<Component> getChildren();
	
	public int getLayer();
	public void setLayer(int layer);
	
	public boolean handleAllClicks();
	
	public void applyEffect(Effect effect);
	public void runEffects();
	
}
