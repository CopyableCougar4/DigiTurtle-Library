package com.digiturtle.common;

public interface Interceptor<E> {

	@SuppressWarnings("hiding")
	public <E> E getResult();
	
	public void setResult(E e);
	
}
