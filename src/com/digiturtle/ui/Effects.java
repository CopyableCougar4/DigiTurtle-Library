package com.digiturtle.ui;

import java.awt.Color;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.DisplayList;
import com.digiturtle.common.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Effects {
	
	public static HighlightEffect highlight(float a) {
		return new HighlightEffect(1, 1, 1, a);
	}
	public static HighlightEffect highlight(float r, float g, float b, float a) {
		return new HighlightEffect(r, g, b, a);
	}
	
	public static HoverTextEffect hoverText(Theme theme, Texture texture, float fontsize, String text, Color color) {
		return new HoverTextEffect(theme, texture, fontsize, text, color);
	}
	
	// EFFECTS
	
	public static class HighlightEffect implements Effect {

		private float r, g, b, a;
		public HighlightEffect(float r, float g, float b, float a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}
		
		@Override
		public void apply(ComponentRegion region) {
			glColor4f(r, g, b, a);
			region.drawQuad();
			glColor3f(1, 1, 1);
		}
		
	}
	
	public static class HoverTextEffect implements Effect {
		
		private Texture texture;
		private DisplayList text;
		public HoverTextEffect(Theme theme, Texture texture, float fontsize, String text, Color color) {
			this.texture = texture;
			GLFont glFont = new GLFont(theme.FONT, fontsize);
			this.text = glFont.drawCachedText(3, 3, text, color);
		}
		
		@Override
		public void apply(ComponentRegion region) {
			glTranslatef(region.x , region.y, 0);
			region.drawQuad(texture);
			text.render();
			glTranslatef(-region.x, -region.y, 0);
		}
		
	}

}
