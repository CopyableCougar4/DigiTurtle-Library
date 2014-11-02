package com.digiturtle.ui.components;

import java.awt.Color;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.DisplayList;
import com.digiturtle.ui.InputSystem;
import com.digiturtle.common.Renderable;

public class Tab {

	private TabbedPane pane;
	private DisplayList cache;
	private ComponentRegion region;
	private Renderable renderable;
	private boolean active = false;
	
	public Tab(TabbedPane pane, String name, float x, float y, Renderable renderable) {
		this.pane = pane;
		region = new ComponentRegion(x, y, pane.tabSize.width, pane.tabSize.height);
		cache = pane.font.drawCachedText(x + 2, y + 2, name, Color.cyan);
		this.renderable = renderable;
	}
	
	public boolean click(int x, int y) {
		return region.contains(x, y);
	}
	
	public void select(boolean active) {
		this.active = active;
	}
	
	public void render() {
		if (active) pane.activeTabBackground.render(region.x, region.y);
		else pane.tabBackground.render(region.x, region.y);
		if (active) pane.overall.crop(renderable, InputSystem.HEIGHT);
		cache.render();
	}
	
}
