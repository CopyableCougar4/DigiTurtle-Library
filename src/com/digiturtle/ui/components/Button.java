package com.digiturtle.ui.components;

import java.awt.Color;
import java.util.ArrayList;

import com.digiturtle.parsing.ScriptedAction;
import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.ui.GLFont;
import com.digiturtle.ui.NinePatch;
import com.digiturtle.ui.UI;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.DisplayList;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.Theme;

public class Button implements Component {
	
	private int layer;
	private ComponentRegion region;
	private boolean locked, down = false;
	protected Texture stylesheet;
	private ScriptedAction clickAction;
	protected StaticVBO normal, pressed;
	private UI ui;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private boolean hover = false;
	private DisplayList text;
	
	public Button(int layer, ComponentRegion region, Theme theme) {
		this(layer, region, theme, Theme.BUTTON);
	}
	public Button(int layer, ComponentRegion region, Texture stylesheet) {
		this.layer = layer;
		this.region = region;
		this.stylesheet = stylesheet;
		normal = NinePatch.generate(new float[]{ 0, 0, 1, 0.5f }, region, stylesheet, new float[]{ 100, 200 });
//		normal = new StaticVBO(4, stylesheet.getID());
//		normal.uploadTextures(0, 0, 1, .5f);
//		normal.uploadVertices(region);
		pressed = NinePatch.generate(new float[]{ 0, 0.5f, 1, 1 }, region, stylesheet, new float[]{ 100, 200 });
//		pressed = new StaticVBO(4, stylesheet.getID());
//		pressed.uploadTextures(0, .5f, 1, 1);
//		pressed.uploadVertices(region);
	}
	public Button(int layer, ComponentRegion region, Theme theme, String target) {
		this.layer = layer;
		this.region = region;
		stylesheet = theme.get(target);
		normal = NinePatch.generate(new float[]{ 0, 0, 1, 0.5f }, region, stylesheet, new float[]{ 100, 200 });
//		normal = new StaticVBO(4, stylesheet.getID());
//		normal.uploadTextures(0, 0, 1, .5f);
//		normal.uploadVertices(region);
		pressed = NinePatch.generate(new float[]{ 0, 0.5f, 1, 1 }, region, stylesheet, new float[]{ 100, 200 });
//		pressed = new StaticVBO(4, stylesheet.getID());
//		pressed.uploadTextures(0, .5f, 1, 1);
//		pressed.uploadVertices(region);
	}
	
	public Button setText(String text, int fontsize, Theme theme, Color color) {
		GLFont font = new GLFont(fontsize, theme);
		float offsetx = (region.width - font.getWidth(text)) / 2;
		float offsety = (region.height - font.getHeight(text)) / 2;
		this.text = font.drawCachedText(region.x + offsetx, region.y + offsety, text, color);
		return this;
	}
	
	public Button setClickAction(String data, UI ui) {
		clickAction = new ScriptedAction(data);
		this.ui = ui;
		return this;
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
		return region.x;
	}

	@Override
	public float getY() {
		return region.y;
	}

	@Override
	public void setLocation(float x, float y) {
		region.x = x;
		region.y = y;
	}

	@Override
	public void click(int x, int y) {
		if (getRegion().contains(x, y) && valid(x, y)) {
			click();
			clickAction.run(ui);
		}
	}
	
	public boolean valid(int x, int y) {
		return true;
	}
	
	@Override
	public void mouse(int x, int y, boolean down) {
		hover = region.contains(x, y);
		if (down && this.down) {
			return; // good
		}
		if (getRegion().contains(x, y)) {
			if (!this.down) {
				if (down) {
					this.down = true;
				}
			}
		}
		this.down = down;
	}

	@Override
	public void key(int keycode) {
	}

	@Override
	public void drag(int dx, int dy, int sx, int sy) {
	}

	@Override
	public boolean overlaps(Component component) {
		if (getRegion().intersects(component.getRegion())) {
			if (component.getLayer() > getLayer()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public ComponentRegion getRegion() {
		return region;
	}

	@Override
	public void render() {
		if (down) {
			pressed.render();
			if (hover) {
				runEffects();
				hover = false;
			}
		} else {
			normal.render();
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void addChild(String name, Component component) {
	}

	@Override
	public ArrayList<Component> getChildren() {
		return null;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public void setLayer(int layer) {
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
	
	public void click() {
		
	}

}
