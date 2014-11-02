package com.digiturtle.ui.components;

import java.awt.Color;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.Theme;

public class PasswordField extends EditField {

	public PasswordField(int layer, ComponentRegion region, Theme theme) {
		super(layer, region, theme);
	}
	public PasswordField(int layer, ComponentRegion region, Theme theme, String target) {
		super(layer, region, theme, target);
	}
	
	@Override
	public void render() {
		if (active) {
			backgroundHighlight.render();
		}
		else {
			background.render();
		}
		if (ticks == cycle) {
			if (active) {
				tick = !tick;
			} else {
				tick = false;
			}
			ticks = 0;
		} else {
			ticks++;
		}
		Color color = new Color(0, 0, 0);
		String rawOutput = input.substring(Math.max(0, input.length() - charactersShown), input.length());
		if (tick) {
			String output = rawOutput.substring(0, pointer - Math.max(0, input.length() - charactersShown)) + "|" + rawOutput.substring( pointer - Math.max(0, input.length() - charactersShown), rawOutput.length());
			font.drawText(getX() + 15, getY() + 4, obfuscate("*", output), color);
			} else {
			font.drawText(getX() + 15, getY() + 4, obfuscate("*", rawOutput), color);
		}
	}
	
	public String obfuscate(String letter, String input) {
		String output = "";
		for (char c : input.toCharArray()) {
			if (c != '|') {
				output += letter;
			} else {
				output += c;
			}
		}
		return output;
	}

}
