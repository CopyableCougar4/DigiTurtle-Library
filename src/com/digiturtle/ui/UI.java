package com.digiturtle.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.Dimension;
import com.digiturtle.common.Renderable;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;

public class UI implements Component {
	
	private boolean locked = false;
	
	public ComponentRegion uiRegion;
	private DataBlock sorted;
	private ArrayList<Component> components = new ArrayList<Component>();
	private StaticVBO backgroundVBO;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private int offsetx = 0, offsety = 0;
	
	public UI(ComponentRegion region) {
		uiRegion = region;
	}
	
	public void setOffset(int offsetx, int offsety) {
		this.offsetx = offsetx;
		this.offsety = offsety;
	}
	
	public void setBackground(String _texture, Theme theme) {
		Texture texture = theme.unmapped(_texture);
		backgroundVBO = new StaticVBO(4, texture.getID());
		backgroundVBO.uploadVertices(uiRegion);
		backgroundVBO.uploadTextures(0, 0, 1, 1);
	}
	
	public void setSize(float width, float height) {
		uiRegion.width = width;
		uiRegion.height = height;
	}
	public Dimension getSize() {
		return new Dimension(uiRegion.width, uiRegion.height);
	}

	@Override
	public boolean isLocked() {
		return locked;
	}
	
	@Override
	public void unlock() {
		locked = false;
	}

	@Override
	public void lock() {
		locked = true;
	}

	@Override
	public float getX() {
		return uiRegion.x;
	}

	@Override
	public float getY() {
		return uiRegion.y;
	}

	@Override
	public void setLocation(float x, float y) {
		uiRegion.x = x;
		uiRegion.y = y;
	}

	@Override
	public void click(int x, int y) {
		if (!isLocked()) {
			Component component = null;
			ArrayList<Component> givenAnyway = new ArrayList<Component>();
			for (Integer key : sorted.keyset) {
				Component targetComponent = sorted.layered.get(key);
//				if (targetComponent == null) continue;
//				if (targetComponent.getRegion() == null) continue;
				if (targetComponent.handleAllClicks()) {
					givenAnyway.add(targetComponent);
				}
				if (targetComponent.getRegion().contains(x, y)) {
					if (component == null) component = targetComponent;
					else {
						if (component.getLayer() > targetComponent.getLayer()) {
							component = targetComponent;
						}
					}
				}
			}
			if (component != null && component != this) {
				component.click(x, y);
			}
			for (Component tComponent : givenAnyway) {
				if (tComponent != this && tComponent != component) {
					tComponent.click(x, y);
				}
			}
		}
	}
	
	public Component getLayer(int layer) {
		return sorted.layered.get(layer);
	}
	
	public static class DataBlock {
		public Integer[] keyset;
		public HashMap<Integer, Component> layered;
		public DataBlock(Integer[] keyset, HashMap<Integer, Component> layered) {
			this.keyset = keyset;
			this.layered = layered;
		}
	}
	
	public DataBlock sortLayers() {
		if (sorted != null) return sorted;
		HashMap<Integer, Component> layered = new HashMap<Integer, Component>();
//		layered.put(0, this);
		for (Component child : getChildren()) {
			layered.put(child.getLayer(), child);
		}
		Integer[] keyset = layered.keySet().toArray(new Integer[layered.keySet().size()]);
		Arrays.sort(keyset);
		return new DataBlock(keyset, layered);
	}

	@Override
	public void key(int keycode) {
		if (!isLocked()) {
			for (Component child : getChildren()) {
				if (!child.isLocked()) {
					child.key(keycode);
				}
			}
		}
	}

	@Override
	public void drag(int dx, int dy, int sx, int sy) {
		if (!isLocked()) {
			Component component = null;
			for (Integer key : sorted.keyset) {
				Component targetComponent = sorted.layered.get(key);
//				if (targetComponent == null) continue;
//				if (targetComponent.getRegion() == null) continue;
				if (targetComponent.getRegion().contains(sx, sy)) {
					if (component == null) component = targetComponent;
					else {
						if (component.getLayer() > targetComponent.getLayer()) {
							component = targetComponent;
						}
					}
				}
			}
			if (component != null && component != this) {
				component.drag(dx, dy, sx, sy);
			}
		}
	}

	@Override
	public boolean overlaps(Component component) {
		return false;
	}

	@Override
	public ComponentRegion getRegion() {
		return uiRegion;
	}

	@Override
	public void render() {
		uiRegion.crop(new Renderable() {
			public void render() {
				if (backgroundVBO != null) backgroundVBO.render();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				HashMap<Integer, Component> layered = new HashMap<Integer, Component>();
				for (Component child : getChildren()) {
					layered.put(child.getLayer(), child);
				}
				Integer[] keyset = layered.keySet().toArray(new Integer[layered.keySet().size()]);
				Arrays.sort(keyset);
				GL11.glTranslatef(offsetx, offsety, 0);
				for (int key : keyset) {
					Texture.unbind();
					layered.get(key).render();
				}
				GL11.glTranslatef(-offsetx, -offsety, 0);
			}
		}, InputSystem.HEIGHT);
	}

	@Override
	public void update() {
		for (Component child : getChildren()) {
			child.update();
		}
	}

	@Override
	public void addChild(String name, Component component) {
		getChildren().add(component);
	}
	
	public void build() {
		sorted = sortLayers();
	}
	
	public Component getChild(String name) {
		return null;
	}

	@Override
	public Collection<Component> getChildren() {
		return components;
	}
	
	@Override
	public int getLayer() {
		return 0;
	}
	
	@Override
	public void setLayer(int layer) {
	}

	private boolean lastdown = false;
	private int lastx = 0, lasty = 0;
	@Override
	public void mouse(int x, int y, boolean down) {
		if (lastx == x) {
			if (lasty == y) {
				if (lastdown == down) {
					lastx = x;
					lasty = y;
					lastdown = down;
					return;
				}
			}
		}
		{
			if (!isLocked()) {
				ArrayList<Component> givenAnyway = new ArrayList<Component>();
				Component component = null;
				for (Integer key : sorted.keyset) {
					Component targetComponent = sorted.layered.get(key);
//					if (targetComponent == null) continue;
//					if (targetComponent.getRegion() == null) continue;
					if (targetComponent.handleAllClicks()) {
						givenAnyway.add(targetComponent);
					}
					if (targetComponent.getRegion().contains(x, y)) {
						if (component == null) component = targetComponent;
						else {
							if (component.getLayer() > targetComponent.getLayer()) {
								component = targetComponent;
							}
						}
					}
				}
				if (component != null && component != this) {
					component.mouse(x, y, down);
				}
				for (Component tComponent : givenAnyway) {
					if (tComponent != this && tComponent != component) {
						tComponent.mouse(x, y, down);
					}
				}
			}
		}
		lastx = x;
		lasty = y;
		lastdown = down;
		
	}

	@Override
	public boolean handleAllClicks() {
		return false;
	}

	@Override
	public void applyEffect(Effect effect) {
		effects.add(effect);
	}

	@Override
	public void runEffects() {
		for (Effect effect : effects) {
			effect.apply(getRegion());
		}
	}

}
