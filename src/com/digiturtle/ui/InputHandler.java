package com.digiturtle.ui;

public interface InputHandler {

	public void click(int x, int y);
	public void mouse(int x, int y, boolean down);
	
	public void key(int keycode);
	
}
