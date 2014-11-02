package com.digiturtle.ui.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import com.digiturtle.ui.Component;
import com.digiturtle.ui.Effect;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.Theme;

public class RadioButtons implements Component {
	
	private ArrayList<Checkbox> buttons = new ArrayList<Checkbox>();
	private ComponentRegion region;
	private int layer;
	private float buttonHeight;
	private boolean locked = false;
	private Theme theme;
	private String target;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	
	public RadioButtons(int layer, ComponentRegion regionPerButton, Theme theme) {
		this(layer, regionPerButton, theme, Theme.RADIO_BUTTON);
	}
	public RadioButtons(int layer, ComponentRegion regionPerButton, Theme theme, String target) {
		this.layer = layer;
		this.region = regionPerButton;
		buttonHeight = regionPerButton.height;
		this.theme = theme;
		this.target = target;
	}
	
	public void addOption(String optionText) {
		buttons.add(new Checkbox(layer, new ComponentRegion(region.x, region.y + (buttons.size() * buttonHeight), buttonHeight, buttonHeight), theme, target).setMessage(optionText, Math.round(buttonHeight) / 2, Color.CYAN));
	}
	public void addOption(Checkbox checkbox) {
		buttons.add(checkbox);
	}
	
	public Checkbox getSelected() {
		for (Checkbox button : buttons) {
			if (button.checked()) return button;
		}
		return null;
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
		Checkbox box = getSelected();
		for (Checkbox checkbox : buttons) {
			checkbox.RADIO_BUTTON = true;
			checkbox.click(x, y);
			if (box == null) {
				checkbox.setChecked(true);
				box = getSelected();
			}
			if (checkbox.checked()) {
				for (Checkbox checkbox2 : buttons) {
					if (checkbox != checkbox2) {
						checkbox2.setChecked(false);
					}
				}
				break;
			}
		}
		if (getSelected() == null) {
			box.setChecked(true);
		}
	}

	@Override
	public void mouse(int x, int y, boolean down) {
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
		for (Checkbox button : buttons) {
			button.render();
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void addChild(String name, Component component) {
	}

	@Override
	public Collection<Component> getChildren() {
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
		return true;
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
