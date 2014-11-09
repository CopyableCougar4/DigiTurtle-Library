package com.digiturtle.parsing;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class ChildIterator implements Iterator<Element> {

	private NodeList nodes;
	private int lastElement;
	
	public ChildIterator(Element element) {
		nodes = element.getChildNodes();
	}
	
	@Override
	public boolean hasNext() {
		return lastElement < (nodes.getLength());
	}

	@Override
	public Element next() {
		if (!hasNext()) return null;
		for (int index = lastElement; index < nodes.getLength(); index++) {
			if (!(nodes.item(index) instanceof Element)) {
				continue;
			}
			if (valid((Element) nodes.item(index))) {
				lastElement = index;
				return (Element) nodes.item(lastElement);
			}
		}
		return null;
	}

	@Override
	public void remove() {
		throw new RuntimeException("Children can't be removed at runtime!");
	}
	
	public abstract boolean valid(Element e);

}
