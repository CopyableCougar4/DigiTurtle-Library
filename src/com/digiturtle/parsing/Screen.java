package com.digiturtle.parsing;

import java.io.File;

import com.digiturtle.ui.UI;
import com.digiturtle.ui.UIFactory;

public class Screen {
	
	private UI ui;
	
	public Screen(UI ui) {
		this.ui = ui;
	}
	
	public void render() {
		ui.render();
	}
	
	public static Screen getScreen(File file) {
		UI ui = UIFactory.buildFromXML(file);
		ui.build();
		return new Screen(ui);
	}
	
	public UI getUI() {
		return ui;
	}

}
