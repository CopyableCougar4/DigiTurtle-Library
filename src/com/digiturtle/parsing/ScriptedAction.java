package com.digiturtle.parsing;

import java.io.File;

import com.digiturtle.ui.UI;

public class ScriptedAction {

	private File script;
	private String function;
	private String args;
	
	public ScriptedAction(String xmlInput) {
		String[] xmlData = xmlInput.split(":"); // format: builder.js:makeHouse(1, 2)
		script = new File(xmlData[0]);
		String[] functionData = xmlData[1].split("\\(");
		function = functionData[0];
		args = functionData[1].replaceAll("\\)", "");
	}
	
	public void run(UI ui) {
		Nashorn.parseJavaScript(script, function, ui, (Object[]) args.split(","));
	}
	
}
