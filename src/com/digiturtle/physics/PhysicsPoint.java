package com.digiturtle.physics;

public class PhysicsPoint {

	public float x, y;
	protected float vx, vy; // velocity
	
	public float minY = Float.MIN_VALUE, minX = Float.MIN_VALUE;
	
	public long millis;
	
	public PhysicsPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public PhysicsPoint use() {
		millis = System.currentTimeMillis();
		return this;
	}
	
	public boolean canUse() {
		return System.currentTimeMillis() - millis >= 1000L;
	}
	
	public void setVelocity(float velocity, float angle) {
		vy = velocity * (float) Math.cos(angle);
		vx = velocity * (float) Math.sin(angle);
	}
	
}
