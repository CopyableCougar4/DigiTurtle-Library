package com.digiturtle.entities;

import java.util.HashMap;

import com.digiturtle.common.DisplayList;
import com.digiturtle.common.Renderable;
import com.digiturtle.common.RotatedTransparencyRegion;
import com.digiturtle.common.Sorter;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.networking.Packet;

import static org.lwjgl.opengl.GL11.*;

public class EntitySystem {
	
	private Sorter<Entity> filedEntities = new Sorter<Entity>();
	private HashMap<Integer, DisplayList> cached = new HashMap<Integer, DisplayList>();
	
	/** Add entities in batch to use caching :) */
	public void registerEntity(int layer, Entity entity) {
		filedEntities.file(entity, layer);
	}
	
	public void render(int folder) {
		for (Entity entity : filedEntities.get(folder)) {
			StaticVBO sprite = entity.getSprite();
			RotatedTransparencyRegion region = entity.getRegion();
			float x = entity.getX();
			float y = entity.getY();
			glPushMatrix();
			glTranslatef(x, y, 0);
			glTranslatef(region.getCX(), region.getCY(), 0);
			glRotatef(region.getAngle(), 0, 0, 1);
			glTranslatef(-region.getCX(), -region.getCY(), 0);
			sprite.render();
			glPopMatrix();
		}
	}
	public void render(int start, int end) {
		for (int folder = start; folder <= end; folder++) {
			render(folder);
		}
	}
	public void render() {
		render(1, filedEntities.size());
	}
	
	public void renderCached(int folder) {
		cached.get(folder).render();
	}
	public void renderCached(int start, int end) {
		for (int folder = start; folder <= end; folder++) {
			renderCached(folder);
		}
	}
	public void renderCached() {
		renderCached(1, filedEntities.size());
	}
	
	public void handlePacket(Packet packet) {
		for (int folder = 1; folder <= filedEntities.size(); folder++) {
			handlePacket(folder, packet);
		}
	}
	public void handlePacket(int layer, Packet packet) {
		for (Entity entity : filedEntities.get(layer)) {
			if (entity instanceof NetworkedEntity) {
				((NetworkedEntity) entity).handlePacket(packet);
			}
		}
	}
	
	public void tick() {
		for (int folder = 1; folder <= filedEntities.size(); folder++) {
			tick(folder);
		}
	}
	public void tick(int layer) {
		for (Entity entity : filedEntities.get(layer)) {
			if (entity.getAI() != null) {
				entity.getAI().tick(entity);
			}
		}
	}
	
	/** Use sparingly */
	public void cache(final int folder) {
		cached.put(folder, new DisplayList(new Renderable() {
			public void render() {
				for (Entity entity : filedEntities.get(folder)) {
					StaticVBO sprite = entity.getSprite();
					RotatedTransparencyRegion region = entity.getRegion();
					float x = entity.getX();
					float y = entity.getY();
					glPushMatrix();
					glTranslatef(x, y, 0);
					glTranslatef(region.getCX(), region.getCY(), 0);
					glRotatef(region.getAngle(), 0, 0, 1);
					glTranslatef(-region.getCX(), -region.getCY(), 0);
					sprite.render();
					glPopMatrix();
				}
			}
		}));
	}

}
