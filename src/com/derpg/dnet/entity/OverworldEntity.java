package com.derpg.dnet.entity;

import com.derpg.dnet.math.Vector3D;

public abstract class OverworldEntity extends Entity {
	
	public static final int RIGHT = 0;
	public static final int DOWN_RIGHT = 1;
	public static final int DOWN = 2;
	public static final int DOWN_LEFT = 3;
	public static final int LEFT = 4;
	public static final int UP_LEFT = 5;
	public static final int UP = 6;
	public static final int UP_RIGHT = 7;
	
	public static final int MEGAMAN = 0;
	public static final int NORMALNAVI = 1;
	public static final int HEELNAVI = 2;
	public static final int DROKOKIEO = 3;
	public static final int OFFICER = 4;
	public static final int MRPROG = 5;
	public static final int BN4GUYNAVI = 6;

	protected int direction;
	int sides;
	
	public OverworldEntity(long id, Vector3D pos) {
		super(id, pos);
		this.direction = UP;
	}
	
	public abstract void interact(Entity starter);
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int d) {
		this.direction = d;
	}
	
	public double getAngleOfDirection() {
		if(direction == 7 || direction == 3)
			return Math.toRadians(direction * 45 + 22.5);
		else if(direction == 5 || direction == 1)
			return Math.toRadians(direction * 45 - 22.5);
			
		return Math.toRadians(direction * 45);
	}
	
	public static int getDirectionTo(Entity src, Entity dst, int sides) {
		double angle = Math.toDegrees(src.getPosition().angleTo(dst.getPosition()));
		angle += 360;
		if(angle >= 360)
			angle -= 360;
		return (int) (angle / 90);
	}

	public double getDepth() {
		return -position.y + position.z;
	}

}
