package com.digiturtle.physics;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Shape {
	
	private ArrayList<Coordinate> points = new ArrayList<Coordinate>();
	private ArrayList<Line2D.Float> intersectionTest = new ArrayList<Line2D.Float>(); 
	
	public int weight = 100;
	
	public Shape() {
	}
	
	public boolean blocked() {
		return weight > 75;
	}

	public void addPoint(float x, float y) {
		points.add(new Coordinate(x, y));
		if (points.size() >= 2) {
			Coordinate last = points.get(points.size() - 2);
			intersectionTest.add(new Line2D.Float(last.x, last.y, x, y));
		}
	}
	public void create() {
		// last point
		Coordinate last = points.get(points.size() - 1);
		Coordinate first = points.get(0);
		intersectionTest.add(new Line2D.Float(last.x, last.y, first.x, first.y));
	}
	
	public boolean intersects(float x, float y) {
		int intersectionsOne = 0, intersectionsTwo = 0;
		Line2D.Float original = new Line2D.Float(x - 1000, y, x, y);
		Line2D.Float originalTwo = new Line2D.Float(x + 1000, y, x, y);
		for (Line2D.Float intersectionTestLine : intersectionTest) {
			if (original.intersectsLine(intersectionTestLine)) {
				intersectionsOne++;
			}
			if (originalTwo.intersectsLine(intersectionTestLine)) {
				intersectionsTwo++;
			}
		}
		return (intersectionsOne % 2 == 1) || (intersectionsTwo % 2 == 1);
	}
	
	public void intersects(float x, float y, Callbacks callbackHandler, Callback callbackSuccess, Callback callbackFailure) {
		callbackHandler.handleCallback(intersects(x, y) ? callbackSuccess : callbackFailure);
	}
	
	public boolean intersects(Double line) {
		for (Line2D.Float intersectionTestLine : intersectionTest) {
			if (line.intersectsLine(intersectionTestLine)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean intersects(Shape shape) {
		for (Line2D.Float intersectionTestLine : intersectionTest) {
			for (Line2D.Float intersectionOtherLine : shape.intersectionTest) {
				if (intersectionOtherLine.intersectsLine(intersectionTestLine)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void intersects(Shape shape, Callbacks callbackHandler, Callback callbackSuccess, Callback callbackFailure) {
		callbackHandler.handleCallback(intersects(shape) ? callbackSuccess : callbackFailure);
	}
	
	public void rotate(float angle, float cx, float cy) {
		ArrayList<Coordinate> nPoints = new ArrayList<Coordinate>();
		for (Coordinate point : points) {
			float[] nPoint = rotateAroundCenter(point.x, point.y, angle, cx, cy);
			nPoints.add(new Coordinate(nPoint[0], nPoint[1]));
		}
		points.clear();
		intersectionTest.clear();
		for (Coordinate point : nPoints) {
			addPoint(point.x, point.y);
		}
		create();
	}
	
	public float[] rotateAroundCenter(float x, float y, float angle, float cx, float cy) {
		float dx = x - cx;
		float dy = y - cy;
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		float originalAngle = getAngle(cx, cy, x, y);
		float newangle = originalAngle + angle;
		float ndx = distance * (float)Math.sin(newangle);
		float ndy = distance * (float)Math.cos(newangle);
		float nx = cx + ndx;
		float ny = cy + ndy;
		return new float[]{ nx, ny };
	}
	
	protected static float getAngle(float mousex, float mousey, float playerx, float playery) {
		float deltax = mousex - playerx;
		float deltay = mousey - playery;
		double rawangle = Math.atan2(deltay, deltax);
		float degrees = (float) Math.toDegrees(rawangle);
		degrees += 90.0f; // add 90 so opengl can use it
		if (degrees < 0) {
			degrees += 360.0f; // fix all angles less than 0
		}
		if (degrees > 360.0f) {
			degrees -= 360.0f; // fix all angles more than 360
		}
		degrees %= 360.0f;
		return (float) Math.toRadians(degrees);
	}
	
	private static class Coordinate {
		private float x, y;
		public Coordinate(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public void debug() {
		glColor3f(0.0f, 0.0f, 1.0f);
		glBegin(GL_LINE_STRIP);
		for (int index = 0; index < intersectionTest.size(); index++) {
			Line2D.Float line = intersectionTest.get(index);
			drawLine(line);
		}
		glEnd();
		glColor3f(1.0f, 1.0f, 1.0f);
	}
	private void drawLine(Line2D.Float line) {
		glVertex2f(line.x1, line.y1);
		glVertex2f(line.x2, line.y2);
	}
	
	public String toString() {
		String data = "";
		for (Coordinate point : points) {
			data += point.x + "," + point.y + "x";
		}
		data = data.substring(0, data.length() - 1);
		return data;
	}

}
