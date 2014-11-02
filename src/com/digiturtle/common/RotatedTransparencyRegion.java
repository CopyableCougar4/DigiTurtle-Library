package com.digiturtle.common;

import java.awt.Point;
import java.util.ArrayList;

import com.digiturtle.ui.Theme;

public class RotatedTransparencyRegion {

	private TransparencyRegion region;
	private ArrayList<Point> transparency = new ArrayList<Point>();
	private float cx, cy;
	private float angleDegrees;
	
	public RotatedTransparencyRegion(String texture, Theme theme) {
		region = new TransparencyRegion(texture, theme);
		cx = region.width / 2;
		cy = region.height / 2;
		transparency = region.transparency;
	}
	
	public float searchX(float y) {
		return 0;
	}
	// FIXME
	public float searchY(float x) {
		return 0;
	}
	
	public float width() {
		return getMaxX() - getMinX();
	}
	
	public float height() {
		return getMaxY() - getMinY();
	}
	
	public float getMinX() {
		float minx = Float.MAX_VALUE;
		for (Point point : transparency) {
			minx = Math.min(minx, point.x);
		}
		return minx;
	}
	public float getMinY() {
		float miny = Float.MAX_VALUE;
		for (Point point : transparency) {
			miny = Math.min(miny, point.y);
		}
		return miny;
	}
	public float getMaxX() {
		float maxx = Float.MIN_VALUE;
		for (Point point : transparency) {
			maxx = Math.max(maxx, point.x);
		}
		return maxx;
	}
	public float getMaxY() {
		float maxy = Float.MIN_VALUE;
		for (Point point : transparency) {
			maxy = Math.max(maxy, point.y);
		}
		return maxy;
	}
	
	private boolean transparent(int x, int y) {
		for (Point transparent : transparency) {
			if (transparent.x == x && transparent.y == y) {
				return true;
			}
		}
		return false;
	}
	
	public boolean transparent(int x, int y, int startx, int starty) {
		return transparent(x - startx, y - starty);
	}
	
	public float getAngle() {
		return angleDegrees;
	}
	
	public float getCX() {
		return cx;
	}
	public float getCY() {
		return cy;
	}
	
	public void angle(float angleRadians) {
		angleDegrees = angleRadians / (float)Math.PI * 180.0f;
		transparency.clear();
		for (Point point : region.transparency) {
			transparency.add(rotatePoint(point, angleRadians));
		}
	}
	
	private Point rotatePoint(Point point, float angle) {
		return rotatePoint(point.x, point.y, angle);
	}
	private Point rotatePoint(float x, float y, float angle) {
		float[] point = rotateAroundCenter(x, y, angle, cx, cy);
		return new Point(Math.round(point[0]), Math.round(point[1]));
	}
	private float[] rotateAroundCenter(float x, float y, float angle, float cx, float cy) {
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
	
	private static float getAngle(float mousex, float mousey, float playerx, float playery) {
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
		if (degrees > 180.0f) {
			degrees -= 360.0f;
		}
		return (float) Math.toRadians(degrees);
	}
	
}
