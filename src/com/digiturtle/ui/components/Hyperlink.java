package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.Sys;

import com.digiturtle.common.DisplayList;
import com.digiturtle.common.StaticColoredVBO;
import com.digiturtle.common.StaticColoredVBO.Color;
import com.digiturtle.ui.Component;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.Effect;
import com.digiturtle.ui.GLFont;
import com.digiturtle.ui.Theme;

public class Hyperlink implements Component {

	private int layer;
	private ComponentRegion region;
	private boolean locked = false, down = false;
	private String link;
	private StaticColoredVBO off, on;
	private DisplayList text;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private boolean hover;
	
	public Hyperlink(int layer, ComponentRegion region, String link, float fontSize, Theme theme, java.awt.Color color) {
		GLFont font = new GLFont(fontSize, theme);
		this.layer = layer;
		this.region = new ComponentRegion(region.x, region.y, font.getWidth(link), font.getSize());
		this.link = link;
		text = font.drawCachedText(region.x, region.y, link, color);
		off = new StaticColoredVBO(4);
		off.uploadVertices(region.x, region.y + font.getSize() + 2, region.x + font.getWidth(link), region.y + font.getSize() + 4);
		off.uploadColors(new Color(0, 0, 1));
		on = new StaticColoredVBO(4);
		on.uploadVertices(region.x, region.y + font.getSize() + 3, region.x + font.getWidth(link), region.y + font.getSize() + 5);
		on.uploadColors(new Color(0, 0, 1));
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
		Sys.openURL(link);
	}

	@Override
	public void mouse(int x, int y, boolean down) {
		hover = region.contains(x, y);
		if (down && this.down) {
			return; // good
		}
		if (getRegion().contains(x, y)) {
			if (!this.down) {
				if (down) {
					this.down = true;
				}
			}
		}
		this.down = down;
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
		text.render();
		if (down) {
			on.render();
		} else {
			off.render();
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
