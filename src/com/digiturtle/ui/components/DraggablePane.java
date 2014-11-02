package com.digiturtle.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.Renderable;
import com.digiturtle.common.Renderable.IRenderable;
import com.digiturtle.ui.Component;
import com.digiturtle.ui.DragAndDrop;
import com.digiturtle.ui.Effect;
import com.digiturtle.ui.ExternalListener;
import com.digiturtle.ui.InputHandler;
import com.digiturtle.ui.InputSystem;
import com.digiturtle.ui.UI;

public class DraggablePane implements Component {

	private int layer;
	private boolean locked = false;
	private InputHandler inputHandler;
	private ComponentRegion croppingRegion, renderedRegion;
	private Renderable innerArea;
	private float dragx, dragy; // render offsets
	private boolean hover;
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	
	private boolean dragging;
	
	public DraggablePane(int layer, ComponentRegion componentRegion, float width, float height, Component...components ) {
		this.layer = layer;
		croppingRegion = componentRegion;
		renderedRegion = new ComponentRegion(0, 0, width, height);
		InputSystem.getSystem().attachListener(new DragAndDrop() {
			@Override
			public void drag(int dx, int dy) {
				if (!dragging) return;
				dragx = crop(dragx + dx, -(renderedRegion.width - croppingRegion.width), 0);
				dragy = crop(dragy + dy, -(renderedRegion.height - croppingRegion.height), 0);
			}
			@Override
			public void startDrag(int startx, int starty) {
				dragging = croppingRegion.contains(startx, starty);
			}
			@Override
			public void endDrag() {
				dragging = false;
			}
		}.addComponents(components));
	}
	public DraggablePane(int layer, ComponentRegion componentRegion, float width, float height) {
		this.layer = layer;
		croppingRegion = componentRegion;
		renderedRegion = new ComponentRegion(0, 0, width, height);
		InputSystem.getSystem().attachListener(new DragAndDrop() {
			@Override
			public void drag(int dx, int dy) {
				if (!dragging) return;
				dragx = crop(dragx + dx, -(renderedRegion.width - croppingRegion.width), 0);
				dragy = crop(dragy + dy, -(renderedRegion.height - croppingRegion.height), 0);
			}
			@Override
			public void startDrag(int startx, int starty) {
				dragging = croppingRegion.contains(startx, starty);
			}
			@Override
			public void endDrag() {
				dragging = false;
			}
		});
	}
	
	private float crop(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
	
	public DraggablePane setRendering(String data, UI ui) {
		return setRendering(IRenderable.parseRenderable(data, ui));
	}
	public DraggablePane setRendering(Renderable renderable) {
		innerArea = renderable;
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
		return croppingRegion.x;
	}

	@Override
	public float getY() {
		return croppingRegion.y;
	}

	@Override
	public void setLocation(float x, float y) {
		croppingRegion.x = x;
		croppingRegion.y = y;
	}

	@Override
	public void click(int x, int y) {
		inputHandler.click(x, y);
	}

	@Override
	public void mouse(int x, int y, boolean down) {
		inputHandler.mouse(x, y, down);
		hover = croppingRegion.contains(x, y);
	}

	@Override
	public void key(int keycode) {
		inputHandler.key(keycode);
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
		return croppingRegion;
	}

	@Override
	public void render() {
		GL11.glTranslatef(dragx, dragy, 0);
		croppingRegion.crop(innerArea, InputSystem.HEIGHT);
		GL11.glTranslatef(-dragx, -dragy, 0);
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
