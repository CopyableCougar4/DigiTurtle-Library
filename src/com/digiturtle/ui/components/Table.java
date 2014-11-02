package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.Dimension;
import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.ui.UI;

public class Table implements Component {
	
	private int layer;
	private ComponentRegion region;
	private boolean locked = false;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private UI[][] uiMap;
	private float[] widths, heights;
	
	public Table(int layer, ComponentRegion region, int rows, int cols, float[] widths, float[] heights) {
		uiMap = new UI[rows][cols];
	}
	
	public void setCell(int index, UI ui) {
		int row = (int)Math.floor(index / uiMap[0].length);
		int col = index % row;
		setCell(row, col, ui);
	}
	public void setCell(int row, int col, UI ui) {
		Dimension size = ui.getSize();
		ui.setSize(Math.min(widths[col], size.width), Math.min(heights[row], size.height));
		float offsetx = 0, offsety = 0;
		for (int x = 0; x < col; x++) offsetx += widths[x];
		for (int y = 0; y < row; y++) offsety += widths[y];
		ui.setOffset(Math.round(region.x + offsetx), Math.round(region.y + offsety));
		uiMap[row][col] = ui;
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
		for (UI[] UIs : uiMap) {
			for (UI ui : UIs) {
				ui.click(x, y);
			}
		}
	}

	@Override
	public void mouse(int x, int y, boolean down) {
		for (UI[] UIs : uiMap) {
			for (UI ui : UIs) {
				ui.mouse(x, y, down);
			}
		}
	}

	@Override
	public void key(int keycode) {
		for (UI[] UIs : uiMap) {
			for (UI ui : UIs) {
				ui.key(keycode);
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
		for (UI[] UIs : uiMap) {
			for (UI ui : UIs) {
				ui.render();
			}
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
			effect.apply(region);
		}
	}

}
