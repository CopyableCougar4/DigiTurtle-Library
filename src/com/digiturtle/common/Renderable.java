package com.digiturtle.common;

import com.digiturtle.parsing.ScriptedAction;
import com.digiturtle.ui.UI;

public interface Renderable {
	
	public void render();
	
	public static class IRenderable {
		
		public static Renderable parseRenderable(final String string, final UI ui) {
			return new Renderable() {
				ScriptedAction action = new ScriptedAction(string);
				public void render() {
					action.run(ui);
				}
			};
		}
		
	}

}
