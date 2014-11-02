package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;

public class Image implements Component {

	private int layer;
	private ComponentRegion region;
	private boolean locked = false;
	private StaticVBO image;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private boolean hover;
	
	public Image(int layer, ComponentRegion region, Texture texture) {
		image = new StaticVBO(4, texture.getID());
		image.uploadVertices(region);
		image.uploadTextures(0, 0, 1, 1);
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
		image.render();
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
