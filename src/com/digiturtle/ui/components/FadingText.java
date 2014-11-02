package com.digiturtle.ui.components;

import java.awt.Color;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.GLFont;

import static org.lwjgl.opengl.GL11.*;

public class FadingText extends Text {

	private long ticks = 0, mticks = 0;
	
	public FadingText(int layer, GLFont font, Color color, String text,
			ComponentRegion region) {
		super(layer, font, color, text, region);
	}
	
	public FadingText setFrames(long ticks) {
		this.ticks = ticks;
		mticks = ticks;
		return this;
	}
	
	@Override
	public void render() {
		float alpha = ticks / mticks;
		glDisable(GL_TEXTURE_2D);
		glColor4f(1, 1, 1, alpha);
		super.render();
		glColor3f(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		ticks--;
	}

}
