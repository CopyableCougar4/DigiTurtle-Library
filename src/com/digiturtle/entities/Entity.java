package com.digiturtle.entities;

import com.digiturtle.vector.Vector2f;

import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.RotatedTransparencyRegion;

public interface Entity {
	
	/*
	 * Attributes:
	 * 	image
	 *  region for collision with a top left corner offset
	 *  health
	 *  balance
	 *  experience / level
	 *  username
	 *  AI
	 */
	
	public Sprite getSprite();
	
	public RotatedTransparencyRegion getRegion();
	
	public float getMaxHealth();
	public float getHealth();
	public void heal();
	public void damage(float damage);
	
	// Manipulates the top left
	public float getX();
	public float getY();
	public void move(float dx, float dy);
	public Vector2f getTopLeft();
	
	public void shoot(float angle);
	
	public float getXP();
	public int getLevel();
	
	public float getBalance();
	public void pay(float balance);
	
	public String getUsername();
	
	public AI getAI();
	
}
