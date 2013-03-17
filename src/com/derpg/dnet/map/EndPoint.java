package com.derpg.dnet.map;

import com.derpg.dnet.math.Circle;
import com.derpg.dnet.math.Collisions;
import com.derpg.dnet.math.Mask;
import com.derpg.dnet.math.Triangle;
import com.derpg.dnet.math.Vector3D;

public class EndPoint {

	private Vector3D endpoint;
	private String tomap;
	
	private Triangle a, b;
	
	public EndPoint(Vector3D p, String tomap) {
		this.endpoint = p;
		this.tomap = tomap;
		
		a = new Triangle(new Vector3D(p.x, p.y - 16, p.z), new Vector3D(p.x - 32, p.y, p.z), new Vector3D(p.x + 32, p.y, p.z));
		b = new Triangle(new Vector3D(p.x, p.y + 16, p.z), new Vector3D(p.x - 32, p.y, p.z), new Vector3D(p.x + 32, p.y, p.z));
	}
	
	public Vector3D getEndPoint() {
		return endpoint;
	}
	
	public String getToMap() {
		return tomap;
	}

	public boolean collides(Circle c) {
		if(Collisions.collides(a, c) || Collisions.collides(b, c)) {
			return true;
		}
		return false;
	}
	
}
