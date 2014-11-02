package com.digiturtle.ui.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.input.Keyboard;

import com.digiturtle.ui.Component;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.Effect;
import com.digiturtle.ui.GLFont;
import com.digiturtle.ui.InputSystem;
import com.digiturtle.ui.NinePatch;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.Theme;

public class EditField implements Component {

	protected int layer, pointer = 0, cycle = InputSystem.TARGET_DISPLAY_FPS / 4;
	private ComponentRegion region;
	protected boolean locked = false, active = false, tick = false;
	protected StaticVBO background, backgroundHighlight;
	protected GLFont font;
	protected int charactersShown, ticks;
	protected StringBuilder input = new StringBuilder();
	private Texture stylesheet;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private boolean hover;
	
	public EditField(int layer, ComponentRegion region, Theme theme) {
		this(layer, region, theme, Theme.EDIT_FIELD);
	}
	public EditField(int layer, ComponentRegion region, Theme theme, String target) {
		this.layer = layer;
		this.region = region;
		font = new GLFont(region.height - 12, theme);
		charactersShown = 0;
		while (font.getWidth(get("A", charactersShown)) < (region.width - 12)) {
			charactersShown++;
		}
		stylesheet = theme.get(target);
		background = NinePatch.generate(new float[]{ 0, 0, 1, 0.5f }, region, stylesheet, new float[]{ 75, 50 });
//		background = new StaticVBO(4, stylesheet.getID());
//		background.uploadVertices(region);
//		background.uploadTextures(0, 0, 1, 0.5f);
		backgroundHighlight = NinePatch.generate(new float[]{ 0, 0.5f, 1, 1 }, region, stylesheet, new float[]{ 75, 50 });
//		backgroundHighlight = new StaticVBO(4, stylesheet.getID());
//		backgroundHighlight.uploadVertices(region);
//		backgroundHighlight.uploadTextures(0, 0.5f, 1, 1);
	}
	
	private String get(String letter, int letters) {
		String text = "";
		while (letters-- > 0) {
			text += letter;
		}
		return text;
	}
	
	public String getText() {
		return input.toString();
	}
	
	@Override
	public boolean isLocked() {
		return locked;
	}

	@Override
	public void unlock() {
		locked = false;
	}

	@Override
	public void lock() {
		locked = true;
	}

	@Override
	public float getX() {
		return region.x;
	}

	@Override
	public float getY() {
		return region.y;
	}

	@Override
	public void setLocation(float x, float y) {
		region.x = x;
		region.y = y;
	}

	@Override
	public void click(int x, int y) {
		if (isLocked()) {
			active = false;
			return;
		}
		if (getRegion().contains(x, y)) {
			active = true;
			tick = true;
			ticks = 0;
		} else {
			active = false;
		}
	}
	
	static String[] keyReference = new String[]{"", "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "",
			"", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "", "", "a", "s", "d", "f", "g",
			"h", "j", "k", "l", ";", "'", "`", "", "\\", "z", "x", "c", "v", "b", "n", "m", ",", ".", "/", "", "*",
			"", " ", "", "", "", "", "", "", "", "", "", "", "", "", "", "7", "8", "9", "-",
			"4", "5", "6", "+", "1", "2", "3", "0", ".", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "=", "", "", ":", "_", "", "", "", "", "", "", "", ",", "/", "/", "", "", "fn", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
	static String[] keyShiftReference = new String[]{"", "", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "",
		"", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}", "", "", "A", "S", "D", "F", "G",
		"H", "J", "K", "L", ":", "\"", "~", "", "|", "Z", "X", "C", "V", "B", "N", "M", "<", ">", "?", "", "*",
		"", " ", "", "", "", "", "", "", "", "", "", "", "", "", "", "7", "8", "9", "-",
		"4", "5", "6", "+", "1", "2", "3", "0", ".", "", "", "", "", "", "", "", "", "",
		"", "", "", "", "=", "", "", ":", "_", "", "", "", "", "", "", "", ",", "/", "/", "", "", "fn", "",
		"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
	static String getKey(int keycode, boolean shift) {
		try {
			return shift ? keyShiftReference[keycode] : keyReference[keycode];
		} catch(ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}

	@Override
	public void mouse(int x, int y, boolean down) {
		hover = region.contains(x, y);
	}

	@Override
	public void key(int keycode) {
		if (isLocked()) {
			return;
		}
		Keyboard.poll();
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || InputSystem.getSystem().CAPS_LOCK;
		String key = getKey(keycode, shift);
		boolean pointerShift = keycode == Keyboard.KEY_LEFT || keycode == Keyboard.KEY_RIGHT;
		if (!active) {
			return;
		}
		if (keycode == Keyboard.KEY_BACK) {
			input.deleteCharAt(pointer - 1);
			pointer--;
		} 
		else if(keycode == Keyboard.KEY_DELETE){
			input.deleteCharAt(pointer);
		}
		else {
			if (pointerShift) {
				if (keycode == Keyboard.KEY_LEFT) {
					pointer = Math.max(0, pointer - 1);
				} else {
					pointer = Math.min(input.length(), pointer + 1);
				}
			} else {
				input.insert(pointer, key);
				pointer++;
			}
		}
 	}

	@Override
	public void drag(int dx, int dy, int sx, int sy) {
	}

	@Override
	public boolean overlaps(Component component) {
		if (getRegion().intersects(component.getRegion())) {
			if (component.getLayer() > getLayer()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public ComponentRegion getRegion() {
		return region;
	}

	@Override
	public void render() {
		if (active) {
			backgroundHighlight.render();
		}
		else {
			background.render();
		}
		if (ticks == cycle) {
			if (active) {
				tick = !tick;
			} else {
				tick = false;
			}
			ticks = 0;
		} else {
			ticks++;
		}
		Color color = new Color(0, 0, 0);
		String rawOutput = input.substring(Math.max(0, input.length() - charactersShown), input.length());
		if (tick) {
			String output = rawOutput.substring(0, pointer - Math.max(0, input.length() - charactersShown)) + "|" + rawOutput.substring( pointer - Math.max(0, input.length() - charactersShown), rawOutput.length());
			font.drawText(getX() + 15, getY() + 4, output, color);
			} else {
			font.drawText(getX() + 15, getY() + 4, rawOutput, color);
		}
		if (hover) {
			runEffects();
			hover = false;
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void addChild(String name, Component component) {
	}

	@Override
	public Collection<Component> getChildren() {
		return null;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public void setLayer(int layer) {
	}

	@Override
	public boolean handleAllClicks() {
		return true;
	}
	
	@Override
	public void applyEffect(Effect effect) {
		effects.add(effect);
	}

	@Override
	public void runEffects() {
		for (Effect effect : effects) {
			effect.apply(getRegion());
		}
	}

}
