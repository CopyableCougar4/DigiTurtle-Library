package com.digiturtle.entities;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.digiturtle.common.StaticVBO;

public class Limb {
	
	public ArrayList<Action> renderCycle = new ArrayList<Action>();
	
	public StaticVBO vbo;
	
	public Limb(StaticVBO vbo) {
		this.vbo = vbo;
	}
	
	public void render() {
		GL11.glPushMatrix();
		for (Action action : renderCycle) {
			action.trigger();
		}
		vbo.render();
		GL11.glPopMatrix();
	}
	
	public void addActions(Action... actions) {
		for (Action action : actions) {
			renderCycle.add(action);
		}
	}

}
