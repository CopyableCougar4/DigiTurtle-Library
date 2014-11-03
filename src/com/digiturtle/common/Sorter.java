package com.digiturtle.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Sorter<E> {

	private HashMap<Integer, ArrayList<E>> sorted = new HashMap<Integer, ArrayList<E>>();
	
	public int size() {
		return sorted.keySet().size();
	}
	
	public void file(E object, int index) {
		ArrayList<E> already = sorted.get(index);
		if (already == null) {
			already = new ArrayList<E>();
		}
		already.add(object);
		sorted.put(index, already);
	}
	
	public ArrayList<E> get(int index) {
		return sorted.get(index);
	}
	
	public Set<Integer> getKeys() {
		return sorted.keySet();
	}

}
