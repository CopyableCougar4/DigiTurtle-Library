package com.digiturtle.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.digiturtle.ui.Component;
import com.digiturtle.ui.UI;

public class Nashorn {
	
	private static HashMap<String, String> fileDataMap = new HashMap<String, String>();
	
	public static void parseJavaScript(File file) {
		ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            nashorn.eval(getFile(file));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
	}
	
	public static void parseJavaScript(File file, String function, UI ui) {
		ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            nashorn.eval(filter(ui, getFile(file)));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
		Invocable invokeEngine = (Invocable) nashorn;
		try {
			invokeEngine.invokeFunction(function);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}
	
	public static String filter(UI ui, String string) {
		Matcher matcher = Pattern.compile("\\$\\[(.*?)\\]").matcher(string); //  format $[getLayer(3).getValue()]
		while (matcher.find()) {
			String match = matcher.group();
			String nmatch = match.substring(2, match.length() - 1);
			String[] methodData = nmatch.split("\\.");
			int layer = Integer.parseInt(methodData[0].substring(9, methodData[0].length() - 1));
			String fullMethod = methodData[1];
			String[] fullMethodData = fullMethod.split("\\(");
			String method = fullMethodData[0];
//			String args = fullMethodData[1].substring(0, fullMethodData[1].length() - 1);
			Component component = ui.getLayer(layer);
			String value = "";
			try {
				Method reflectedMethod = component.getClass().getDeclaredMethod(method, null);
				reflectedMethod.setAccessible(true);
				value = "\"" + String.valueOf(reflectedMethod.invoke(component)) + "\"";
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			System.out.println(match + " --> " + value);
			string = string.replace(match, value);
		}
		return string;
	}
	public static void main(String[] args) { filter(null, "$[getLayer(3).getValue(args, method)]"); }
	
	public static void parseJavaScript(File file, String function, UI ui, Object... args) {
		ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");
        try {
        	nashorn.eval(filter(ui, getFile(file)));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
		Invocable invokeEngine = (Invocable) nashorn;
		try {
			invokeEngine.invokeFunction(function, args);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}
	
	private static String getFile(File file) {
		if (fileDataMap.keySet().contains(file.getName())) {
			return fileDataMap.get(file.getName());
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String output = "";
			String line;
			while ((line = reader.readLine()) != null) {
				output += line;
			}
			fileDataMap.put(file.getName(), output);
			return output;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}

}
