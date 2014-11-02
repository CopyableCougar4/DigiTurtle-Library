package com.digiturtle.ui;

import java.awt.Color;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.DisplayList;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.components.Button;

public class Panel extends UI {

	private boolean visible = false;
	private StaticVBO background;
	private Button close;
	private String title = "";
	private DisplayList titleDL;
	private Theme theme;
	private int fontsize;
	
	public Panel(int layer, ComponentRegion region, Theme theme, String textureSrc, int fontsize) {
		super(region);
		Texture texture = theme.unmapped(textureSrc);
		background = new StaticVBO(4, texture.getID());
		background.uploadVertices(region);
		background.uploadTextures(0, 0, 1, 1);
		this.theme = theme;
		this.fontsize = fontsize;
		close = new Button(layer, new ComponentRegion(region.x - 32, region.y + 2, 30, 30), texture) {
			public void click() {
				if (visible) {
					close();
				} else {
					open();
				}
			}
		};
		addChild("CLOSE", close);
	}
	
	public void setTitle(String title) {
		this.title = title;
		attachTitle();
	}
	
	private void attachTitle() {
		titleDL = new GLFont(fontsize, theme).drawCachedText(uiRegion.x + 2, uiRegion.y + 2, title, Color.orange);
	}
	
	public void close() {
		visible = false;
	}
	
	public void open() {
		visible = true;
	}
	
	public boolean isLocked() {
		return super.isLocked() && visible;
	}
	
	public void render() {
		if (visible) {
			super.render();
			background.render();
			titleDL.render();			
		}
	}

}
