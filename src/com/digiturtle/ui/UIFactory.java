package com.digiturtle.ui;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.Dimension;
import com.digiturtle.common.Renderable.IRenderable;
import com.digiturtle.common.Texture;
import com.digiturtle.parsing.ChildIterator;
import com.digiturtle.parsing.XMLReader;
import com.digiturtle.ui.components.*;
import com.digiturtle.ui.components.Action.IAction;

public class UIFactory {

	public static UI buildFromXML(File file) {
		XMLReader reader = null;
		try {
			reader = new XMLReader(file, "screen");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		Element uiXML = (Element) reader.getNodes("ui").item(0);
		return buildFromXML(uiXML);
	}
	
	public static UI buildFromXML(Element uiXML) {
		float x = get(uiXML, "x");
		float y = get(uiXML, "y");
		float width = get(uiXML, "width");
		float height = get(uiXML, "height");
		ComponentRegion region = new ComponentRegion(x, y, width, height);
		UI ui = new UI(region);
		Theme theme = null;
		NodeList xmlChildren = uiXML.getChildNodes();
		for (int itemIndex = 0; itemIndex < xmlChildren.getLength(); itemIndex++) {
			if (xmlChildren.item(itemIndex) instanceof Element) {
				Element childNode = (Element) xmlChildren.item(itemIndex);
				if (childNode.getTagName().equalsIgnoreCase("theme")) {
					// Load the theme for this UI
					theme = new Theme(childNode.getAttribute("source"));
					String background = uiXML.getAttribute("background");
					if (theme.unmapped(background) != null) {
						System.out.println("Mapping background: " + background);
						ui.setBackground(background, theme);
					}
				} 
				else if (childNode.getTagName().equalsIgnoreCase("ui")) {
					// Load an inner UI
					UI innerUI = buildFromXML(childNode);
					ui.addChild("", innerUI);
				}
				else {
					// Load an attribute
					Component component = getComponent(childNode, theme, ui);
					Effect[] effects = childNode.getAttribute("effects") != null ? generateEffects(theme, childNode.getAttribute("effects")) : new Effect[0];
					if (effects.length > 0) {
						for (Effect effect : effects) {
							component.applyEffect(effect);
						}
					}
					ui.addChild("", component);
				}
			}
		}
		return ui;
	}
	
	private static float get(Element element, String attribute) {
		return Float.parseFloat(element.getAttribute(attribute));
	}
	
	private static Component getComponent(Element element, Theme theme, UI ui) {
		int layer = Math.round(get(element, "layer"));
		float x = get(element, "x");
		float y = get(element, "y");
		float width = get(element, "width");
		float height = get(element, "height");
		ComponentRegion region = new ComponentRegion(x, y, width, height);
		switch (element.getTagName().toUpperCase()) {
			case "BAR":
				return new Bar(layer, region, theme);
			case "BUTTON":
				if (element.getAttribute("source") != null) {
					return new Button(layer, region, theme.unmapped(element.getAttribute("source"))).setClickAction(element.getAttribute("action"), ui);
				} else {
					return new Button(layer, region, theme).setClickAction(element.getAttribute("action"), ui);
				}
			case "CHECKBOX":
				String[] data = element.getAttribute("message").split(":");
				String message = data[0];
				int fontSize = Integer.parseInt(data[1]);
				Color color = parse(data[2]);
				return new Checkbox(layer, region, theme).setMessage(message, fontSize, color);
			case "CLOCK":
				int clockFontSize = Math.round(get(element, "size"));
				return new Clock(layer, region, clockFontSize, theme);
			case "COMBOBOX":
				ComboBox box = new ComboBox(layer, region, theme);
				NodeList optionNodes = element.getChildNodes();
				for (int itemIndex = 0; itemIndex < optionNodes.getLength(); itemIndex++) {
					Element childNode = (Element) optionNodes.item(itemIndex);
					if (childNode.getTagName().equalsIgnoreCase("option")) {
						String text = childNode.getAttribute("text");
						box.addOption(text);
					}
				}
				return box;
			case "DRAGGABLEPANE":
				return new DraggablePane(layer, region, get(element, "renderwidth"), get(element, "renderheight")).setRendering(element.getAttribute("renderable"), ui);
			case "EDITFIELD":
				return new EditField(layer, region, theme);
			case "FADINGTEXT":
				int fontSizeFading = Integer.parseInt(element.getAttribute("size"));
				GLFont font = new GLFont(fontSizeFading, theme);
				Color colorFading = parse(element.getAttribute("color"));
				String output = element.getAttribute("text");
				long ticks = Long.parseLong(element.getAttribute("ticks"));
				return new FadingText(layer, font, colorFading, output, region).setFrames(ticks);
			case "HYPERLINK":
				int fontSizeLink = Integer.parseInt(element.getAttribute("size"));
				Color colorLink = parse(element.getAttribute("color"));
				String text = element.getAttribute("text");
				return new Hyperlink(layer, region, text, fontSizeLink, theme, colorLink);
			case "IMAGE":
				return new Image(layer, region, Texture.loadTexture(element.getAttribute("source")));
			case "MENU":
				Menu menu = new Menu(layer, width, height, theme);
				ChildIterator rootIterator = new ChildIterator(element) {;
					public boolean valid(Element e) {
						return e.getTagName().equalsIgnoreCase("menuitem");
					}
				};
				while (rootIterator.hasNext()) {
					Element innerNode = rootIterator.next();
					// Recursively parse
					menu.addMenuItem(getInnerNodes(innerNode, null, ui, menu));
				}
				return menu;
			case "PASSWORD":
				return new PasswordField(layer, region, theme);
			case "RADIOBUTTON":
				RadioButtons radioButtons = new RadioButtons(layer, region, theme);
				NodeList buttons = element.getChildNodes();
				for (int itemIndex = 0; itemIndex < buttons.getLength(); itemIndex++) {
					Element childNode = (Element) buttons.item(itemIndex);
					if (childNode.getTagName().equalsIgnoreCase("BUTTON")) {
						String innerText = childNode.getAttribute("text");
						radioButtons.addOption(innerText);
					}
				}
				return radioButtons;
			case "SLIDER":
				return new Slider(Slider.HORIZONTAL, region, layer, 200, theme);
			case "SPINNER":
				return new Spinner(layer, region, theme);
			case "SUPERBUTTON":
				if (element.getAttribute("source") != null) {
					return new SuperButton(layer, region, Texture.loadTexture(element.getAttribute("source")), theme).setClickAction(element.getAttribute("action"), ui);
				} else {
					return new SuperButton(layer, region, theme);
				}
			case "TABBEDPANE":
				float tabwidth = get(element, "tabwidth");
				float tabheight = get(element, "tabheight");
				TabbedPane tabbedPane = new TabbedPane(layer, region, new Dimension(tabwidth, tabheight), theme);
				ChildIterator rootTabIterator = new ChildIterator(element) {;
					public boolean valid(Element e) {
						return e.getTagName().equalsIgnoreCase("tab");
					}
				};
				while (rootTabIterator.hasNext()) {
					Element innerNode = rootTabIterator.next();
					tabbedPane.addTab(innerNode.getAttribute("name"), IRenderable.parseRenderable(element.getAttribute("action"), ui));
				}
				return tabbedPane;
			case "TABLE":
				
			case "TEXT":
				int textSize = Integer.parseInt(element.getAttribute("size"));
				GLFont glFont = new GLFont(textSize, theme);
				Color colorText = parse(element.getAttribute("color"));
				String textOutput = element.getAttribute("text");
				return new Text(layer, glFont, colorText, textOutput, region);
		}
		return null;
	}
	
	public static MenuItem getInnerNodes(Element element, MenuItem menuItem, UI ui, Menu menu) {
		ChildIterator rootIterator = new ChildIterator(element) {;
			public boolean valid(Element e) {
				return e.getTagName().equalsIgnoreCase("menuitem");
			}
		};
		while (rootIterator.hasNext()) {
			Element innerNode = rootIterator.next(); // Recursively parse
			String name = innerNode.getAttribute("name");
			Action action = IAction.getAction(innerNode.getAttribute("action"), ui);
			float x = get(innerNode, "x");
			float y = get(innerNode, "y");
			menuItem.addSubItem(getInnerNodes(innerNode, MenuItem.buildItem(name, action, menu, x, y, menuItem), ui, menu));
		}
		return menuItem;
	}
	
	private static Color parse(String data) {
		String[] rgba = data.split(",");
		int r = Integer.parseInt(rgba[0]);
		int g = Integer.parseInt(rgba[1]);
		int b = Integer.parseInt(rgba[2]);
		int a = Integer.parseInt(rgba[3]);
		return new Color(r, g, b, a);
	}
	
	private static Effect[] generateEffects(Theme theme, String data) {
		String[] rawData = data.split(":");
		Effect[] effects = new Effect[rawData.length];
		int nIndex = 0;
		for (int index = 0; index < rawData.length; index++) {
			Effect effect = generateEffect(theme, rawData[index]);
			if (effect != null) {
				effects[nIndex] = effect;
				nIndex++;
			}
		}
		int SIZE = nIndex;
		Effect[] resultant = new Effect[SIZE];
		for (int index = 0; index < SIZE; index++) {
			resultant[index] = effects[index];
		}
		return resultant;
	}
	private static Effect generateEffect(Theme theme, String data) {
		String[] rawData = data.split("\\[");
		if (rawData.length < 2) return null;
		String effectTitle = rawData[0].toUpperCase();
		String[] parameters = rawData[1].substring(0, rawData[1].length() - 1).split("|");
		switch (effectTitle) {
			case "HIGHLIGHT": {
				float r = parameters.length < 2 ? 1 : Float.parseFloat(parameters[0]);
				float g = parameters.length < 2 ? 1 : Float.parseFloat(parameters[1]);
				float b = parameters.length < 2 ? 1 : Float.parseFloat(parameters[2]);
				float a = parameters.length < 2 ? Float.parseFloat(parameters[0]) : Float.parseFloat(parameters[3]);
				return Effects.highlight(r, g, b, a);
			}
			case "HOVERTEXT": {
				Texture texture = Texture.loadTexture(parameters[0]);
				float fontsize = Float.parseFloat(parameters[1]);
				String text = parameters[2];
				Color color = parse(parameters[3]);
				return Effects.hoverText(theme, texture, fontsize, text, color);
			}
		}
		return null;
	}
	
}
