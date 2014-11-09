package com.digiturtle.ui;

import org.lwjgl.opengl.Display;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.Dimension;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;

public class SplashScreen {

	private StaticVBO vbo;
	private Runnable action;
	
	public SplashScreen(Texture texture, Dimension imageSize, Dimension screen) {
		float offsetx = (screen.width - imageSize.width) / 2, offsety = (screen.height - imageSize.height) / 2;
		ComponentRegion region = new ComponentRegion(offsetx, offsety, imageSize.width, imageSize.height);
		vbo = new StaticVBO(4, texture.getID());
		vbo.uploadVertices(region);
		vbo.uploadTextures(0, 0, 1, 1);
	}
	
	public SplashScreen setAction(Runnable action) {
		this.action = action;
		return this;
	}
	
	public void render() {
		vbo.render();
		Display.update();
		action.run();
	}
	
}
