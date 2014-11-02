package com.digiturtle.ui.components;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.Texture;
import com.digiturtle.common.TransparencyRegion;
import com.digiturtle.ui.Theme;

public class SuperButton extends Button {

	private TransparencyRegion transparency;
	
	public SuperButton(int layer, ComponentRegion region, Texture texture, Theme theme) {
		super(layer, region, texture);
		transparency = new TransparencyRegion(texture.filename, theme);
	}
	public SuperButton(int layer, ComponentRegion region, Theme theme) {
		this(layer, region, theme, Theme.BUTTON);
	}
	public SuperButton(int layer, ComponentRegion region, Theme theme,
			String target) {
		super(layer, region, theme, target);
		transparency = new TransparencyRegion(stylesheet.filename, theme);
	}
	
	public boolean valid(int x, int y) {
		return !transparency.transparent(x, y);
	}

}
