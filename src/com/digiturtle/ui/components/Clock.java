package com.digiturtle.ui.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import com.digiturtle.common.ClockThread;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.Timed;
import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.ui.GLFont;
import com.digiturtle.ui.Theme;

public class Clock implements Component {
	
	private int layer;
	private boolean locked = false;
	private ComponentRegion region;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private boolean down;
	private GLFont regular, millis, amPm;
	private Timed timed = new Timed();
	
	public Clock(int layer, ComponentRegion region, int fontsize, Theme theme) {
		this.layer = layer;
		this.region = region;
		regular = new GLFont(fontsize, theme);
		amPm = new GLFont(Math.round(fontsize / 2), theme);
		millis = new GLFont(Math.round(fontsize / 3), theme);
		ClockThread.getInstance().addTimed(timed);
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
		this.down = region.contains(x, y);
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
		String bigText = timed.getHours() + ":" + (timed.getMinutes() < 10 ? "0" : "") + timed.getMinutes() +
				":" + (timed.getSeconds() < 10 ? "0" : "") + timed.getSeconds();
		String midText = timed.getAM() ? "AM" : "PM";
		String smallText = "" + (int)timed.getMillis();
		Color color = new Color(255, 255, 255, 125);
		if (down) color = new Color(255, 255, 255, 175);
		regular.drawText(region.x, region.y, bigText, color);
		float cycleOffset = regular.getWidth(bigText) + 5;
		amPm.drawText(region.x + cycleOffset, region.y + regular.getSize() / 2, midText, color);
		millis.drawText(region.x + regular.getWidth(bigText) - millis.getWidth(smallText), region.y + regular.getHeight(bigText), smallText, color);
		region.width = regular.getWidth(bigText) + 5 + amPm.getWidth(midText);
		region.height = regular.getHeight(bigText) + millis.getHeight(smallText);
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
