package com.digiturtle.entities;

public class SkeletalSprite implements Sprite {

	private Limb head, chest, leftArm, rightArm, leftLeg, rightLeg;
	
	public static final int HEAD = 0, CHEST = 1, LEFT_ARM = 2, RIGHT_ARM = 3, LEFT_LEG = 4, RIGHT_LEG = 5;
	
	public void setLimb(int pointer, Limb vbo) {
		switch (pointer) {
			case HEAD:
				head = vbo; break;
			case CHEST:
				chest = vbo; break;
			case LEFT_ARM:
				leftArm = vbo; break;
			case RIGHT_ARM:
				rightArm = vbo; break;
			case LEFT_LEG:
				leftLeg = vbo; break;
			case RIGHT_LEG:
				rightLeg = vbo; break;
		}
	}
	
	public Limb getLimb(int pointer) {
		switch (pointer) {
			case HEAD:
				return head;
			case CHEST:
				return chest;
			case LEFT_ARM:
				return leftArm;
			case RIGHT_ARM:
				return rightArm;
			case LEFT_LEG:
				return leftLeg;
			case RIGHT_LEG:
				return rightLeg;
		}
		return null;
	}
	
	@Override
	public void render() {
		leftLeg.render();
		rightLeg.render();
		chest.render();
		leftArm.render();
		rightArm.render();
		head.render();
	}

}
