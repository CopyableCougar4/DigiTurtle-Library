package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import static org.lwjgl.opengl.GL11.*;

import com.digiturtle.ui.Component;
import com.digiturtle.common.ComponentRegion;
import com.digiturtle.ui.Effect;
import com.digiturtle.ui.GLFont;
import com.digiturtle.ui.InputSystem;
import com.digiturtle.ui.InputSystem.MouseInput;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.ui.Theme;

public class ComboBox implements Component {

	private int layer;
	private ComponentRegion region;
	private boolean locked = false, showOptions = false, bDown = false;
	private Texture stylesheetCore, stylesheetButton;
	private StaticVBO background, innerBG, endBG_1, endBG_2, selectedBG, selectBG_1, selectBG_2;
	private Button open;
	private Theme theme;
	private ComponentRegion dropdownOpen;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	private boolean hover;
	
	private LinkedHashMap<ComboBoxOption, Boolean> options = new LinkedHashMap<ComboBoxOption, Boolean>();
	
	public ComboBox(int layer, ComponentRegion region, Theme theme) {
		this(layer, region, theme, Theme.DROPDOWN);
	}
	public ComboBox(int layer, ComponentRegion _region, Theme theme, String target) {
		this.layer = layer;
		this.region = _region;
		this.theme = theme;
		stylesheetCore = theme.get(target + Theme._CORE);
		stylesheetButton = theme.get(target + Theme._BUTTON);
		ComponentRegion buttonRegion = new ComponentRegion(region.x + region.width, region.y, region.height, region.height);
		open = new Button(layer, buttonRegion, stylesheetButton) {
			public void click() {
				toggle();
			}
		};
		background = new StaticVBO(4, stylesheetCore.getID());
		background.uploadVertices(region);
		background.uploadTextures(0, 0, 1, 0.2f);
		_region.height *= 0.8f;
		innerBG = new StaticVBO(4, stylesheetCore.getID());
		innerBG.uploadVertices(new ComponentRegion(0, 0, region.width + region.height, region.height));
		innerBG.uploadTextures(0, 0.2f, 1, 0.4f);
		selectedBG = new StaticVBO(4, stylesheetCore.getID());
		selectedBG.uploadVertices(new ComponentRegion(0, 0, region.width + region.height, region.height));
		selectedBG.uploadTextures(0, 0.4f, 1, 0.6f);
		endBG_1 = new StaticVBO(4, stylesheetCore.getID());
		endBG_1.uploadVertices(new ComponentRegion(0, 0, (region.width + region.height) / 2, region.height));
		endBG_1.uploadTextures(0, 0.6f, 1, 0.8f);
		endBG_2 = new StaticVBO(4, stylesheetCore.getID());
		endBG_2.uploadVertices(new ComponentRegion((region.width + region.height) / 2 - 1, 0, (region.width + region.height) / 2, region.height));
		endBG_2.uploadTextures(1, 0.6f, 0, 0.8f);
		selectBG_1 = new StaticVBO(4, stylesheetCore.getID());
		selectBG_1.uploadVertices(new ComponentRegion(0, 0, (region.width + region.height) / 2, region.height));
		selectBG_1.uploadTextures(0, 0.8f, 1, 1);
		selectBG_2 = new StaticVBO(4, stylesheetCore.getID());
		selectBG_2.uploadVertices(new ComponentRegion((region.width + region.height) / 2 - 1, 0, (region.width + region.height) / 2, region.height));
		selectBG_2.uploadTextures(1, 0.8f, 0, 1);
	}
	
	public ComboBoxOption getSelected() {
		for (ComboBoxOption option : options.keySet()) {
			if (options.get(option)) {
				return option;
			}
		}
		return null;
	}
	
	public void addOption(String title) {
		options.put(new ComboBoxOption(title, new GLFont(region.height - 8, theme)), false);
		dropdownOpen = new ComponentRegion(region.x, region.y, region.width + region.height, region.height * (1 + options.size()));
	}
	
	public void toggle() {
		showOptions = !showOptions;
	}
	public void close() {
		showOptions = false;
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
		open.click(x, y);
		if (dropdownOpen.contains(x, y) && showOptions && !open.getRegion().contains(x, y)) {
			MouseInput input = new MouseInput(x, y);
			int index = 0;
			for (ComboBoxOption option : options.keySet()) {
				if (index == options.size() - 1) {
					boolean found = false;
					if (input.x > region.x + 15 && input.y > region.y + region.height + (0.8f * region.height * index) + 2) {
						if (input.x - (region.x + 15) < region.width + region.height - 23) {
							if (input.y - (region.y + region.height + (0.8f * region.height * index) + 2) < region.height - 6) {
								options.put(option, true);
								found = true;
								close();
							}
						}
					}
					if (!found) options.put(option, false);
				} else {
					boolean found = false;
					if (input.x > region.x + 15 && input.y > region.y + region.height + (0.8f * region.height * index) + 2) {
						if (input.x - (region.x + 15) < region.width + region.height - 23) {
							if (input.y - (region.y + region.height + (0.8f * region.height * index) + 2) < region.height - 6) {
								options.put(option, true);
								found = true;
								close();
							}
						}
					}
					if (!found) options.put(option, false);
				}
				index++;
			}
		} else {
			if (!open.getRegion().contains(x, y)) {
				close();
			}
		}
	}

	@Override
	public void mouse(int x, int y, boolean down) {
		hover = region.contains(x, y);
		if (down && bDown) {
			return; // good
		}
		if (getRegion().contains(x, y)) {
			if (!bDown) {
				if (down) {
					bDown = true;
				}
			}
		}
		bDown = down;
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
		{	// Render the options
			if (showOptions) {
				int index = 0;
				MouseInput input = InputSystem.getSystem().getEvent();
				for (ComboBoxOption option : options.keySet()) {
					if (index == options.size() - 1) {
						endBG_1.render(region.x + 5, region.y + region.height + (0.8f * region.height * index) - 3);
						endBG_2.render(region.x + 5, region.y + region.height + (0.8f * region.height * index) - 3);
						option.render(region.x + 23, region.y + region.height + (0.8f * region.height * index) + 2);
						
					} else {
						innerBG.render(region.x - 8, region.y + region.height + (0.8f * region.height * index) - 3);
						option.render(region.x + 23, region.y + region.height + (0.8f * region.height * index) + 2);
					}
					index++;
				}
				index = 0;
				for (ComboBoxOption option : options.keySet()) {
					if (index == options.size() - 1) {
						if (input.x > region.x + 15 && input.y > region.y + region.height + (0.8f * region.height * index) + 2) {
							if (input.x - (region.x + 15) < region.width + region.height - 23) {
								if (input.y - (region.y + region.height + (0.8f * region.height * index) + 2) < region.height - 6) {
									glDisable(GL_TEXTURE_2D);
									glColor4f(0, 0, 0, 0.1f);
									glBegin(GL_QUADS);
									glVertex2f(region.x + 18, region.y + region.height + (0.5f * region.height * index) + 8);
									glVertex2f(region.x + region.width + region.height - 9, region.y + region.height + (0.5f * region.height * index) + 8);
									glVertex2f(region.x + region.width + region.height - 9, region.y + region.height + (0.5f * region.height * index) +  region.height + 2);
									glVertex2f(region.x + 18, region.y + region.height + (0.5f * region.height * index) +  region.height + 2);
									glEnd();
									glColor3f(1, 1, 1);
									glEnable(GL_TEXTURE_2D);
								}
							}
						}
					} else {
						if (input.x > region.x + 15 && input.y > region.y + region.height + (0.8f * region.height * index) + 2) {
							if (input.x - (region.x + 15) < region.width + region.height - 23) {
								if (input.y - (region.y + region.height + (0.8f * region.height * index) + 2) < region.height - 6) {
									glDisable(GL_TEXTURE_2D);
									glColor4f(0, 0, 0, 0.1f);
									glBegin(GL_QUADS);
									glVertex2f(region.x + 18, region.y + region.height + (0.5f * region.height * index) + 3);
									glVertex2f(region.x + region.width + region.height - 9, region.y + region.height + (0.5f * region.height * index) + 3);
									glVertex2f(region.x + region.width + region.height - 9, region.y + region.height + (0.5f * region.height * index) - 3 +  region.height);
									glVertex2f(region.x + 18, region.y + region.height + (0.5f * region.height * index) - 3 +  region.height);
									glEnd();
									glColor3f(1, 1, 1);
									glEnable(GL_TEXTURE_2D);
								}
							}
						}
					}
					index++;
				}
			}
		}
		ComboBoxOption selected = getSelected();
		background.render();
		if (bDown) {
			open.pressed.render();
		} else {
			open.normal.render();
		}
		if (selected != null) {
			selected.render(getX() + 15, getY() + 7);
		}
		if (hover) {
			runEffects();
			hover = false;
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
