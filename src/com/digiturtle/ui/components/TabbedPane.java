package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.GLFont;
import com.digiturtle.common.Dimension;
import com.digiturtle.common.Renderable;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.Theme;

public class TabbedPane implements Component {
	
	private int layer;
	private boolean locked = false;
	protected ComponentRegion overall;
	protected StaticVBO tabBackground, activeTabBackground, border;
	protected GLFont font;
	protected Dimension tabSize;
	private Texture stylesheet;
	private ArrayList<Tab> tabs = new ArrayList<Tab>();
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	
	public TabbedPane(int layer, ComponentRegion overall, Dimension tabSize, Theme theme) {
		this(layer, overall, tabSize, theme, Theme.TABBED_PANE);
	}
	public TabbedPane(int layer, ComponentRegion overall, Dimension tabSize, Theme theme, String target) {
		this.layer = layer;
		this.overall = overall;
		this.tabSize = tabSize;
		font = new GLFont(tabSize.height * .75f, theme);
		stylesheet = theme.get(target);
		tabBackground = new StaticVBO(4, stylesheet.getID());
		tabBackground.uploadVertices(new ComponentRegion(0, 0, tabSize.width, tabSize.height));
		tabBackground.uploadTextures(0, 0, 1, .33f);
		activeTabBackground = new StaticVBO(4, stylesheet.getID());
		activeTabBackground.uploadVertices(new ComponentRegion(0, 0, tabSize.width, tabSize.height));
		activeTabBackground.uploadTextures(0, .33f, 1, .66f);
		border = new StaticVBO(4, stylesheet.getID());
		border.uploadVertices(overall);
		border.uploadTextures(0, .66f, 1, 1);
	}
	
	public void addTab(String name, Renderable renderable) {
		tabs.add(new Tab(this, name, overall.x + ((tabs.size()) * tabSize.width), overall.y - tabSize.height, renderable));
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
		return overall.x;
	}

	@Override
	public float getY() {
		return overall.y;
	}

	@Override
	public void setLocation(float x, float y) {
		overall.x =	x;
		overall.y = y;
	}

	@Override
	public void click(int x, int y) {
		Tab clicked = null;
		for (Tab tab : tabs) {
			if (tab.click(x, y)) clicked = tab;
		}
		if (clicked != null) {
			for (Tab tab : tabs) {
				tab.select(tab == clicked);
			}
		}
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
		return false;
	}

	@Override
	public ComponentRegion getRegion() {
		return overall;
	}

	@Override
	public void render() {
		for (Tab tab : tabs) {
			tab.render();
		}
		border.render();
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
