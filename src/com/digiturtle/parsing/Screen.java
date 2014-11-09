package com.digiturtle.parsing;

import java.io.File;

import com.digiturtle.common.Renderable;
import com.digiturtle.ui.UI;
import com.digiturtle.ui.UIFactory;

public class Screen {
	
	private UI ui;
	private Renderable overlay = new Renderable() {
		public void render() {
			
		}
	};
	
	public Screen(UI ui) {
		this.ui = ui;
	}
	
	public void render() {
		ui.render();
		overlay.render();
	}
	
	public void attachOverlay(Renderable renderable) {
		overlay = renderable;
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
