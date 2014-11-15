package com.digiturtle.resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.digiturtle.common.Logger.LoggingSystem;
import com.digiturtle.parsing.XMLReader;

public class Resources {

	public static File FILE = new File("output.jar");
	
	private static HashMap<String, Resource> resources= new HashMap<String, Resource>();
	
	public static TextResource getText(String name) {
		return (TextResource) resources.get(name);
	}
	
	public static VBOResource getVBO(String name) {
		return (VBOResource) resources.get(name);
	}
	
	public static void glLoad() {
		for (Resource resource : resources.values()) {
			if (resource instanceof VBOResource) {
				VBOResource vboResource = (VBOResource) resource;
				vboResource.glCreate();
			}
		}
	}
	
	public static void load() {
		try {
			JarFile jarFile = new JarFile(FILE);
			Enumeration<JarEntry> enumeration = jarFile.entries();
			ArrayList<JarEntry> entries = new ArrayList<JarEntry>();
			while (enumeration.hasMoreElements()) {
				JarEntry jarEntry = enumeration.nextElement();
				entries.add(jarEntry);
			}
			for (JarEntry jarEntry : entries) {
				if (jarEntry.getName().contains(".xml")) {
					try {
						XMLReader xmlReader = new XMLReader(jarFile.getInputStream(jarEntry), "file");
						Element parentElement = (Element) xmlReader.getNodes("resources").item(0);
						NodeList children = parentElement.getChildNodes();
						for (int index = 0; index < children.getLength(); index++) {
							if (!(children.item(index) instanceof Element)) {
								LoggingSystem.warn("Invalid: " + children.item(index));
								continue;
							}
							Element element = (Element) children.item(index);
							if (element.getTagName().equalsIgnoreCase("vbo")) {
								VBOResource resource = new VBOResource(element.getAttribute("source"), 
										jarFile.getInputStream(jarFile.getJarEntry(element.getAttribute("source"))), 
										element.getAttribute("tex-coords"), element.getAttribute("vertices"));
								resources.put(element.getAttribute("source"), resource);
							} 
							else if (element.getTagName().equalsIgnoreCase("text")) {
								String text = element.getAttribute("value");
								resources.put(element.getAttribute("title"), new TextResource(element.getAttribute("title"), text));
							}
						}
					} catch (ParserConfigurationException | SAXException e) {
						LoggingSystem.error("XML Exception in load()", e);
					}
				}
			}
			jarFile.close();
		} catch (IOException e) {
			LoggingSystem.error("IOException in load()", e);
		}
	}
	
}
