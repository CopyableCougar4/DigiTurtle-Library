package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import com.digiturtle.ui.Component;
import com.digiturtle.ui.DragAndDrop;
import com.digiturtle.ui.Effect;
import com.digiturtle.ui.InputSystem;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.Theme;

public class Slider implements Component {
	
	public static int HORIZONTAL = 0, VERTICAL = 1;
	
	private boolean locked = false, down = false;
	private int direction;
	private float maxValue, value = 0, orig;
	private StaticVBO background, slider;
	private Texture bgTexture, controlTexture;
	private ComponentRegion region;
	private int layer;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private boolean hover;
	
	public Slider(int direction, ComponentRegion region, int layer, float width, float value, Theme theme) {
		this(direction, region, layer, width, theme, Theme.SLIDER_GENERIC);
		this.value = value;
	}
	public Slider(int direction, ComponentRegion region, int layer, float width, Theme theme) {
		this(direction, region, layer, width, theme, Theme.SLIDER_GENERIC);
	}
	public Slider(int direction, ComponentRegion region, int layer, float width, Theme theme, String target) {
		orig = region.x;
		this.direction = direction;
		maxValue = width - (region.width);
		bgTexture = theme.get(target);
		controlTexture = theme.get(target + Theme._CONTROL);
		this.region = region;
		this.layer = layer;
		slider = new StaticVBO(4, controlTexture.getID());
		slider.uploadVertices(region);
		slider.uploadTextures(0, 0, 1, 1);
		background = new StaticVBO(4, bgTexture.getID());
		background.uploadVertices(new ComponentRegion(region.x, region.y + (region.height / 2) + 10, width, region.height / 2));
		background.uploadTextures(0, 0, 1, 1);
		attach();
	}

	private boolean dragging;
	public void attach() {
		InputSystem.getSystem().attachListener(new DragAndDrop() {
			@Override
			public void drag(int dx, int dy) {
				if (!dragging) return;
				if (direction == HORIZONTAL) {
					value = Math.max(0, Math.min(maxValue, value + dx));
					region.x = value + orig - (region.width / 2);
				}
				else if (direction == VERTICAL) {
					value = Math.max(0, Math.min(maxValue, value + dy));
					region.y = value + orig - (region.height / 2);
				}
			}
			@Override
			public void startDrag(int startx, int starty) {
				System.out.println("Dragging from " + startx + "," + starty);
				dragging = region.contains(startx, starty);
				System.out.println(dragging);
			}

			@Override
			public void endDrag() {
				dragging = false;
			}
		});
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
		// Ignore
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
		background.render();
		if (!down) GL11.glColor4f(1, 1, 1, 0.75f);
		if (direction == HORIZONTAL) slider.render(value, 0);
		else slider.render(0, value);
		if (!down) GL11.glColor3f(1, 1, 1);
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
