package com.digiturtle.common;

import java.awt.geom.Line2D;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class ComponentRegion {
	
	public float x, y, width, height;
	
	public Line2D.Float[] lines = new Line2D.Float[4];
	
	public ComponentRegion(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		// build the component border
		lines[0] = new Line2D.Float(x, y, x + width, y);
		lines[1] = new Line2D.Float(x + width, y, x + width, y + height);
		lines[2] = new Line2D.Float(x + width, y + height, x, y + height);
		lines[3] = new Line2D.Float(x, y + height, x, y);
	}
	
	public ComponentRegion(float width, float height) {
		this(0, 0, width, height);
	}
	
	public ComponentRegion flip() {
		return new ComponentRegion(x + width, y, -width, height);
	}
	
	public boolean intersects(ComponentRegion region) {
		for (Line2D.Float line : lines) {
			for (Line2D.Float oLine : region.lines) {
				if (line.intersectsLine(oLine)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean contains(ComponentRegion region) {
		return (region.x > x && region.y > 0) && 
				(region.width - region.x > width - x) && (region.height - region.y > height - y);
	}
	
	public boolean contains(int tx, int ty) {
		return (tx >= x && ty >= y) && (tx <= (x + width) && ty <= (y + height));
	}
	
	public String toString() {
		return "ComponentRegion[x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}
	
	// Immediate mode.. because I can.
	public void drawQuad() {
		glDisable(GL_TEXTURE_2D);
		glColor3f(1, 1, 1);
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
		glColor3f(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
	}
	
	public void drawQuad(Texture texture) {
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(x, y);
		glTexCoord2f(1, 0);
		glVertex2f(x + width, y);
		glTexCoord2f(1, 1);
		glVertex2f(x + width, y + height);
		glTexCoord2f(0, 1);
		glVertex2f(x, y + height);
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
	
	public void debug() {
		glDisable(GL_TEXTURE_2D);
		glColor3f(1, 0, 0);
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
		glColor3f(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
	}

	public void crop(Renderable renderable, int HEIGHT) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(Math.round(x), (HEIGHT - Math.round(y)) - Math.round(height), Math.round(width), Math.round(height));
		renderable.render();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

}
