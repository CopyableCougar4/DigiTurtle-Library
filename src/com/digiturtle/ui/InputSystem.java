package com.digiturtle.ui;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputSystem {
	
	private static int LEFT_CLICK = 0;
	
	public static int HEIGHT = 600, TARGET_DISPLAY_FPS = 60;
	
	private static final InputSystem singleton = new InputSystem();
	
	private ArrayList<Component> components = new ArrayList<Component>();
	private ArrayList<ExternalListener> externalListeners = new ArrayList<ExternalListener>();
	private ArrayList<KeyEventHandler> keyHandlers = new ArrayList<KeyEventHandler>();

	// Protect instantiation
	private InputSystem() {
		Arrays.fill(keys, false);
	}
	
	public static void setHeight(int height) {
		HEIGHT = height;
	}
	
	public void addComponent(Component component) {
		components.add(component);
	}
	
	public void attachListener(ExternalListener listener) {
		externalListeners.add(listener);
	}
	
	public void attachHandler(KeyEventHandler handler) {
		keyHandlers.add(handler);
	}
	
	public static InputSystem getSystem() {
		return singleton;
	}
	
	private boolean[] keys = new boolean[Keyboard.KEYBOARD_SIZE];
	private boolean mouseDown = false;
	private int lastX = -1, lastY = -1, dx = 0, dy = 0, startx = -1, starty = -1;
	
	public static class MouseInput {
		public int x, y;
		public boolean down;
		
		public MouseInput() {
			
		}
		public MouseInput(int x, int y) {
			this.x = x;
			this.y = y;
			down = false;
		}
	}
	
	public MouseInput getEvent() {
		Mouse.poll();
		MouseInput input = new MouseInput();
		input.x = Mouse.getX();
		input.y = HEIGHT - Mouse.getY();
		input.down = Mouse.isButtonDown(LEFT_CLICK);
		return input;
	}
	
	public boolean CAPS_LOCK = false;
	
	public void collectInput() {
		
		// Keyboard input
		while (Keyboard.next()) {
			int keycode = Keyboard.getEventKey();
			boolean down = Keyboard.getEventKeyState();
			if (down) {
				keys[keycode] = true;
			} else {
				if (keys[keycode]) {
					// released
					if (keycode == Keyboard.KEY_CAPITAL) CAPS_LOCK = !CAPS_LOCK;
					key(keycode);
					keys[keycode] = false;
				}
			}
		}
		// Read the active state
		for (int key = 0; key < Keyboard.KEYBOARD_SIZE; key++) {
			key(key, Keyboard.isKeyDown(key));
		}
		// Mouse Input
		while (Mouse.next()) {
			int button = Mouse.getEventButton();
			boolean down = Mouse.getEventButtonState();
			int x = Mouse.getEventX();
			int y = HEIGHT - Mouse.getEventY();
			mouse(x, y, down);
			if (button == LEFT_CLICK) {
				if (!down) {
					if (mouseDown) {
						click(x, y);
					}
				}
				if (down) {
					if (!mouseDown) {
						// Start the drag
						startx = x;
						starty = y;
					}
				} else {
					if (mouseDown) {
						// End the drag
					}
				}
				mouseDown = down; // KEEP
			}
		}
		if (Mouse.isButtonDown(LEFT_CLICK)) {
			if (mouseDown) {
				Mouse.poll();
				int x = Mouse.getX(), y = HEIGHT - Mouse.getY();
				dx = x - lastX;
				dy = y - lastY;
				if (lastX > 0 && lastY > 0)	drag();
				lastX = x;
				lastY = y;
			}
		}
		
	}
	
	public void key(int keycode) {
		for (int index = 0; index < externalListeners.size(); index++) {
			ExternalListener listener = externalListeners.get(index);
			listener.key(keycode);
		}
		for (int index = 0; index < components.size(); index++) {
			Component component = components.get(index);
			component.key(keycode);
		}
	}
	
	public void key(int keycode, boolean down) {
		for (int index = 0; index < keyHandlers.size(); index++) {
			KeyEventHandler listener = keyHandlers.get(index);
			listener.key(keycode, down);
		}
	}
	
	public void drag() {
		if (!(dx != 0 || dy != 0)) return;
		for (int index = 0; index < components.size(); index++) {
			Component component = components.get(index);
			component.drag(dx, dy, startx, starty);
		}		
	}
	
	public void click(int x, int y) {
		for (int index = 0; index < externalListeners.size(); index++) {
			ExternalListener listener = externalListeners.get(index);
			listener.click(x, y);
		}
		for (int index = 0; index < components.size(); index++) {
			Component component = components.get(index);
			component.click(x, y);
		}
	}
	
	public void mouse(int x, int y, boolean down) {
		for (int index = 0; index < externalListeners.size(); index++) {
			ExternalListener listener = externalListeners.get(index);
			listener.mouse(x, y, down);
		}
		for (int index = 0; index < components.size(); index++) {
			Component component = components.get(index);
			component.mouse(x, y, down);
		}
	}

}
