package com.digiturtle.ui.components;

import com.digiturtle.parsing.ScriptedAction;
import com.digiturtle.ui.UI;

public interface Action {

	public void complete();
	
	public static class IAction {
		
		public static Action getAction(final String data, final UI ui) {
			return new Action() {
				ScriptedAction action = new ScriptedAction(data);
				public void complete() {
					action.run(ui);
				}
			};
		}
		
	}
	
}
