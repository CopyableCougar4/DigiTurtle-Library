package com.digiturtle.ui.components;

import java.awt.Color;

import com.digiturtle.common.DisplayList;
import com.digiturtle.ui.GLFont;

public class ComboBoxOption {
	
	private DisplayList option;
	
	protected ComboBoxOption(String text, GLFont font) {
		Color color = new Color(0, 50, 70);
		option = font.drawCachedText(0, 0, text, color);
	}
	
	public void render(float x, float y) {
		option.renderAt(x, y);
	}

}
