package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import com.digiturtle.ui.Effect;
import com.digiturtle.ui.Theme;
import com.digiturtle.ui.Component;
import com.digiturtle.common.ComponentRegion;

public class Spinner implements Component {

	private boolean locked = false;
	private ComponentRegion region;
	private int layer, value = 0;
	private EditField output;
	private Button up, down;
	private int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	
	public Spinner(int layer, ComponentRegion region, Theme theme) {
		this(layer, region, theme, Theme.SPINNER);
	}
	public Spinner(int layer, ComponentRegion region, Theme theme, String target) {
		this.layer = layer;
		this.region = region;
		output = new EditField(layer, region, theme);
		output.lock();
		up = new Button(layer, new ComponentRegion(region.x + region.width + 2, region.y, region.height, region.height / 2), theme) {
			public void click() {
				delta(1);
			}
		};
		down = new Button(layer, new ComponentRegion(region.x + region.width + 2, region.y + (region.height / 2), region.height, region.height / 2), theme) {
			public void click() {
				delta(-1);
			}
		};
	}
	
	public void setRange(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public void delta(int delta) {
		value = Math.max(min, Math.min(max, value + delta));
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
		up.click(x, y);
		down.click(x, y);
	}

	@Override
	public void mouse(int x, int y, boolean down) {
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
		output.render();
		output.input.setLength(0);
		output.input.append(value);
		up.render();
		down.render();
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
