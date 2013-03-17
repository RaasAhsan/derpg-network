package com.derpg.dnet.map;

import com.derpg.dnet.math.Vector3D;

public class StartPoint {

	private Vector3D startpoint;
	private String frommap;
	private int dir;
	
	public StartPoint(Vector3D pos, String fmap, int dir) {
		this.startpoint = pos;
		this.frommap = fmap;
		this.dir = dir;
	}
	
	public Vector3D getStartPoint() {
		return startpoint;
	}
	
	public String getFromMap() {
		return frommap;
	}
	
	public int getDirection() {
		return dir;
	}
	
}
