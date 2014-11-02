package com.digiturtle.physics;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import com.digiturtle.common.FastRand;

public class PhysicsEngine {

	public static float PIXELS_PER_METER = 10;
	
	private static HashMap<Long, PhysicsPoint> points = new HashMap<Long, PhysicsPoint>();
	
	public static long addPoint(float x, float y) {
		FastRand RNG = new FastRand();
		long value = RNG.randLong();
		PhysicsPoint point = new PhysicsPoint(x, y);
		points.put(value, point);
		return value;
	}
	
	public static void applyGravity() {
		for (PhysicsPoint point : points.values()) {
			if (point.canUse()) {
				float dy = point.vy - 9.8f;
				float dx = point.vx;
				point.y = Math.min(point.minY, point.y + dy);
				point.x = Math.min(point.minX, point.x + dx);
				point.use();
			}
		}
	}
	
	public static PhysicsPoint accelerate(float angleDegrees, float velocity, int keycode) {
		angleDegrees = rotate(angleDegrees, keycode);
		float dx = velocity * (float)Math.sin(Math.toRadians(angleDegrees));
		float dy = velocity * (float)Math.cos(Math.toRadians(angleDegrees));
		return new PhysicsPoint(dx, dy);
	}
	
	public static float rotate(float angleDegrees, int keycode) {
		switch (keycode) {
			case Keyboard.KEY_W:
				return angleDegrees;
			case Keyboard.KEY_D:
				return angleDegrees + 90;
			case Keyboard.KEY_S:
				return angleDegrees + 180;
			case Keyboard.KEY_A:
				return angleDegrees + 270;
		}
		return angleDegrees;
	}
	
	public static float getAngle(float mousex, float mousey, float playerx, float playery) {
		return Shape.getAngle(mousex, mousey, playerx, playery);
	}
	
}
