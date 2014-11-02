package com.digiturtle.resource;

public interface Resource {
	
	// Use for delayed creation / GPU allocation
	public void glCreate();
	
	public String getName();
	
}
