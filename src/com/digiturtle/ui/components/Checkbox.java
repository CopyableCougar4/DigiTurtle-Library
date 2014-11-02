package com.digiturtle.ui.components;

import java.awt.Color;
import java.util.ArrayList;

import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.GLFont;
import com.digiturtle.common.DisplayList;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.Theme;

public class Checkbox implements Component {
	
	private int layer;
	private boolean locked = false, checkedOff = false;
	private ComponentRegion region;
	private Texture stylesheet;
	private StaticVBO checked, unchecked;
	private DisplayList text;
	private Theme theme;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private boolean hover = false;
	
	public Checkbox(int layer, ComponentRegion region, Theme theme) {
		this(layer, region, theme, Theme.CHECKBOX);
	}
	public Checkbox(int layer, ComponentRegion region, Theme theme, String target) {
		this.layer = layer;
		this.region = region;
		stylesheet = theme.get(target);
		unchecked = new StaticVBO(4, stylesheet.getID());
		unchecked.uploadTextures(0, 0, 1, .5f);
		unchecked.uploadVertices(region);
		checked = new StaticVBO(4, stylesheet.getID());
		checked.uploadTextures(0, .5f, 1, 1);
		checked.uploadVertices(region);
		this.theme = theme;
	}
	
	public Checkbox setMessage(String text, int fontSize, Color color) {
		GLFont font = new GLFont(Math.min(fontSize, region.height), theme);
		float offset = region.height - fontSize;
		offset /= 2;
		this.text = font.drawCachedText(region.x + region.width + 5, region.y + offset, text, color);
		return this;
	}
	
	public void setChecked(boolean checked) {
		checkedOff = checked;
	}
	
	public boolean checked() {
		return checkedOff;
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

	protected boolean RADIO_BUTTON = false;
	@Override
	public void click(int x, int y) {
		if (getRegion().contains(x, y)) {
			if (!RADIO_BUTTON) checkedOff = !checkedOff;
			else checkedOff = true;
		} else {
			checkedOff = false;
		}
	}

	@Override
	public void mouse(int x, int y, boolean down) {
		hover = region.contains(x, y);
	}

	@Override
	public void key(int keycode) {
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
		if (checkedOff) {
			checked.render();
		} else {
			unchecked.render();
		}
		if (text != null) {
			text.render();
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
	public ArrayList<Component> getChildren() {
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
		return false;
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
