package com.digiturtle.ui;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

public abstract class DragAndDrop implements ExternalListener {

	public boolean wasDown = false;
	protected int lastx, lasty;
	private ArrayList<Component> overlappingComponents = new ArrayList<Component>();
	
	public int getDX(int x, int y, boolean down) {
		Point mouse = new Point(x, y);
		int dx = mouse.x - lastx;
		lastx = mouse.x;
		return dx;
	}
	public int getDY(int x, int y, boolean down) {
		Point mouse = new Point(x, y);
		int dy = mouse.y - lasty;
		lasty = mouse.y;
		return dy;
	}
	
	public void click(int x, int y) {
		
	}
	
	public abstract void drag(int dx, int dy);
	public abstract void startDrag(int startx, int starty);
	public abstract void endDrag();
	
	public void key(int keycode) {
		
	}
	
	public DragAndDrop addComponents(Component... components) {
		for (Component component : components) {
			overlappingComponents.add(component);
		}
		return this;
	}
	
	private boolean dragging = false;
	public void mouse(int x, int y, boolean down) {
		Mouse.poll();
		boolean isDown = Mouse.isButtonDown(0); 
		if (down != isDown) { 
			System.out.println("The mouse was down at trigger, but not anymore.");
			down = isDown;
		}
		if (wasDown) {
			if (!down) {
				System.out.println("Ending drag at " + x + "," + y);
				endDrag();
				dragging = false;
			}
		} else {
			if (down) {
				System.out.println("Starting drag at " + x + "," + y);
				boolean valid = true;
				loop: for (Component component : overlappingComponents) {
					if (component.getRegion().contains(x, y)) {
						valid = false;
						break loop;
					}
				}
				if (!valid) startDrag(x, y);
				dragging = true;
			}
		}
		int dx = getDX(x, y, down);
		int dy = getDY(x, y, down);
		if (dragging) {
			drag(dx, dy);
			System.out.println("dragging " + dx + "," + dy);
		}
		wasDown = down;
	}
	
}