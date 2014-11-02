package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.Theme;

public class Menu implements Component {
	
	private int layer;
	private boolean locked = false;
	private ComponentRegion region;
	private ArrayList<MenuItem> subMenu = new ArrayList<MenuItem>();
	private Texture stylesheet;
	public Theme theme;
	protected StaticVBO hover, unhover;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	
	public Menu(int layer, float width, float height, Theme theme) {
		this(layer, width, height, theme, Theme.MENU_ITEM);
	}
	public Menu(int layer, float width, float height, Theme theme, String target) {
		this(layer, new ComponentRegion(0, 0, width, height), theme, target);
	}
	public Menu(int layer, ComponentRegion regionPerItem, Theme theme, String target) {
		this.theme = theme;
		this.layer = layer;
		this.region = regionPerItem;
		stylesheet = theme.get(target);
		unhover = new StaticVBO(4, stylesheet.getID());
		unhover.uploadVertices(regionPerItem);
		unhover.uploadTextures(0, 0, 1, 0.5f);
		hover = new StaticVBO(4, stylesheet.getID());
		hover.uploadVertices(regionPerItem);
		hover.uploadTextures(0, 0.5f, 1, 1);
	}
	
	public void addMenuItem(String name, Action action, float x, float y, MenuItem parent) {
		subMenu.add(MenuItem.buildItem(name, action, this, x, y, parent));
	}
	public void addMenuItem(MenuItem item) {
		subMenu.add(item);
	}
	
	public MenuItem getOpenTab() {
		for (MenuItem item : subMenu) {
			if (item.showInnerMenu) {
				return item;
			}
		}
		return null;
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
		for (MenuItem item : subMenu) {
			item.click(x, y);
		}
	}

	@Override
	public void mouse(int x, int y, boolean down) {
		for (MenuItem item : subMenu) {
			item.mouse(x, y, down);
		}
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
		for (MenuItem item : subMenu) {
			item.render();
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
