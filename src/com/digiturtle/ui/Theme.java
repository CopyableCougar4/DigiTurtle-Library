package com.digiturtle.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.digiturtle.common.Logger.LoggingSystem;
import com.digiturtle.common.Texture;

public class Theme {
	
	public static String
					BUTTON = "button",
					CHECKBOX = "checkbox",
					DROPDOWN = "dropdown",
						_CORE = "-core",
						_BUTTON = "-button",
					SLIDER_GENERIC = "slider-generic",
						_CONTROL = "-control",
					EDIT_FIELD = "editfield",
					BAR = "bar",
					RADIO_BUTTON = "radio-button",
					SPINNER = "spinner",
					MENU_ITEM = "menu-item",
					TABBED_PANE = "tabbed-pane";
	
	private Properties properties;
	
	private HashMap<String, Texture> unmappedTextures = new HashMap<String, Texture>();
	private HashMap<String, Texture> textureMap = new HashMap<String, Texture>();
	public Font FONT;
	
	/** Theme objects are constructed from jar files, that can be packed with Jar Builder */
	public Theme(String filename) {
		HashMap<String, String> filenameMap = new HashMap<String, String>();
		File file = new File(filename);
		if (file.getName().contains(".jar")) {
			try {
				JarFile jarFile = new JarFile(file);
				Enumeration<JarEntry> enumeration = jarFile.entries();
				ArrayList<JarEntry> entries = new ArrayList<JarEntry>();
				while (enumeration.hasMoreElements()) {
					JarEntry jarEntry = enumeration.nextElement();
					entries.add(jarEntry);
					if (jarEntry.getName().endsWith(".properties")) {
						properties = new Properties();
						properties.load(jarFile.getInputStream(jarEntry));
						for (Object property : properties.keySet()) {
							filenameMap.put(String.valueOf(properties.get(property)), String.valueOf(property));
						}
					}
				}
				for (JarEntry jarEntry : entries) {
					if (jarEntry.getName().endsWith(".png")) {
						Texture texture = Texture.loadTexture(jarEntry.getName(), jarFile.getInputStream(jarEntry));
						if (filenameMap.containsKey(jarEntry.getName())) {
							String targetName = filenameMap.get(jarEntry.getName());
							LoggingSystem.debug(String.valueOf(texture.getID()));
							textureMap.put(targetName, texture);
						} else {
							LoggingSystem.debug("unmapped resource. " + jarEntry.getName());
							unmappedTextures.put(jarEntry.getName(), texture);
							LoggingSystem.debug("size = [ " + texture.getWidth() + "x" + texture.getHeight() + " ]");
							LoggingSystem.debug(String.valueOf(unmappedTextures.size()));
						}
					} 
					else if (jarEntry.getName().endsWith(".ttf")) {
						try {
							FONT = Font.createFont(Font.TRUETYPE_FONT, jarFile.getInputStream(jarEntry));
						} catch (FontFormatException e) {
							LoggingSystem.error("FontFormatException in Theme(String)", e);
						}
					}
					else {
						LoggingSystem.debug("Resource found: " + jarEntry.getName());
					}
				}
				jarFile.close();
			} catch (IOException e) {
				LoggingSystem.error("IOException in Theme(String)", e);
			}
		}
	}
	
	public Texture get(String target) {
		return textureMap.get(target);
	}
	public Texture unmapped(String source) {
		return unmappedTextures.get(source);
	}

}
