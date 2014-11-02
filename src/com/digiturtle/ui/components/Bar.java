package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.InputSystem;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.Theme;

public class Bar implements Component {

	private boolean locked = false;
	private int layer;
	private ComponentRegion region;
	private Texture stylesheet;
	private StaticVBO border, insides;
	private float cropPadding, actualWidth;
	private float percent;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	
	public Bar(int layer, ComponentRegion region, Theme theme) {
		this(layer, region, theme, Theme.BAR);
	}
	public Bar(int layer, ComponentRegion region, Theme theme, String target) {
		this.layer = layer;
		this.region = region;
		stylesheet = theme.get(target);
		border = new StaticVBO(4, stylesheet.getID());
		border.uploadVertices(region);
		border.uploadTextures(0, 0, 1, 0.5f);
		cropPadding = region.width * 0.05f;
		actualWidth = region.width - (cropPadding * 2);
		insides = new StaticVBO(4, stylesheet.getID());
		insides.uploadVertices(region);
		insides.uploadTextures(0, 0.5f, 1, 1);
	}
	
	/** @param percent -- Integer from 0 to 100 */
	public void setPercent(float percent) {
		this.percent = percent / 100.0f;
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
	
	public void glScissor(Runnable renderable, float x, float y, float width, float height) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(Math.round(x), (InputSystem.HEIGHT - Math.round(y)) - Math.round(height), Math.round(width), Math.round(height));
		renderable.run();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	@Override
	public void render() {
		border.render();
		glScissor(new Runnable() {
			public void run() {
				insides.render();
			}
		}, getX(), getY(), cropPadding + (actualWidth * percent), region.height);
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
