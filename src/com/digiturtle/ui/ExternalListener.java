package com.digiturtle.ui;

public interface ExternalListener {

	public void click(int x, int y);
	public void mouse(int x, int y, boolean down);
	
	public void key(int keycode);

	// use this if you only want to handle keyboard input, not clicks and save lines
	public abstract static class KeyListener implements ExternalListener {
		
		public void click(int x, int y) {
			//
		}
		
		public void mouse(int x, int y, boolean down) {
			//
		}
		
	}
	
}
