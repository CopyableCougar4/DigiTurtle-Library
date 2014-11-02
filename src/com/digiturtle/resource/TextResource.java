package com.digiturtle.resource;

public class TextResource implements Resource {

	private String value, name;
	
	public TextResource(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public void glCreate() {
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}

}
