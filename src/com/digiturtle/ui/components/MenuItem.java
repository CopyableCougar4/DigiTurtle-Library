package com.digiturtle.ui.components;

import java.awt.Color;
import java.util.ArrayList;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.DisplayList;
import com.digiturtle.parsing.ScriptedAction;
import com.digiturtle.ui.GLFont;
import com.digiturtle.ui.Theme;
import com.digiturtle.ui.UI;

public class MenuItem {
	
	protected boolean showInnerMenu = false;
	
	private ArrayList<MenuItem> subMenu = new ArrayList<MenuItem>();
	private MenuItem parent;
	
	private Action action;
	private String name;
	private ComponentRegion region;
	private Menu menu;
	private DisplayList text;
	private ScriptedAction clickAction;
	private UI ui;
	
	private MenuItem(Theme theme, float x, float y, String name) {
		GLFont font = new GLFont(24, theme);
		text = font.drawCachedText(x + 2, y + 2, name, Color.CYAN);
	}
	public MenuItem(String name, Action action, ComponentRegion region, MenuItem parent, Menu menu) {
		this(menu.theme, region.x, region.y, name);
		this.action = action;
		this.region = region;
		this.name = name;
		this.parent = parent;
	}
	
	public MenuItem setClickAction(String data, UI ui) {
		this.ui = ui;
		clickAction = new ScriptedAction(data);
		return this;
	}
	
	public static MenuItem buildItem(String name, Action action, Menu menu, float x, float y, MenuItem parent) {
		MenuItem item = new MenuItem(menu.theme, x, y, name);
		item.name = name;
		item.action = action;
		item.menu = menu;
		item.parent = parent;
		item.region = new ComponentRegion(x, y, menu.getRegion().width, menu.getRegion().height);
		return item;
	}
	
	public String getName() {
		return name;
	}
	
	public void addSubItem(MenuItem item) {
		subMenu.add(item);
	}
	
	public boolean containsOverall(int x, int y) {
		boolean contains = true;
		for (MenuItem item : subMenu) {
			contains = contains || region.contains(x, y) || item.containsOverall(x, y);
		}
		return contains;
	}
	
	public void mouse(int x, int y, boolean down) {
		if (parent == null) {
			MenuItem openTab = menu.getOpenTab();
			if (openTab == this || openTab == null) {
				showInnerMenu = containsOverall(x, y);
			} else {
				showInnerMenu = false;
			}
		} else {
			if (parent.showInnerMenu) { 
				showInnerMenu = containsOverall(x, y);
			} else {
				showInnerMenu = false;
			}
		}
		if (subMenu.size() > 0) {
			for (MenuItem item : subMenu) {
				item.mouse(x, y, down);
			}
		}
	}
	
	public void click(int x, int y) {
		if (parent == null) {
			if (menu.getOpenTab() == this) {
				if (action != null && region.contains(x, y)) {
					action.complete();
					if (clickAction != null) clickAction.run(ui);
				}
			}
		} else {
			if (parent.showInnerMenu) { 
				if (action != null && region.contains(x, y)) {
					action.complete();
					if (clickAction != null) clickAction.run(ui);
				}
			}
		}
		if (subMenu.size() > 0) {
			for (MenuItem item : subMenu) {
				item.click(x, y);
			}
		}
	}
	
	public void render() {
		// Traverse the hierarchy to render inner components
		if (showInnerMenu) {
			menu.hover.render(region.x, region.y);
		} else {
			menu.unhover.render(region.x, region.y);
		}
		text.render();
		if (subMenu.size() > 0 && showInnerMenu) {
			for (MenuItem item : subMenu) {
				item.render();
			}
		}
	}

}
