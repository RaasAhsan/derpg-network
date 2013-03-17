package com.derpg.dnet.math;

public class Collisions {

	public static boolean collides(Mask a, Mask b) {
		if(a instanceof Circle) {
			if(b instanceof Circle) {
				return circleCircle((Circle) a, (Circle) b);
			} else if(b instanceof Triangle) {
				return triangleCircle((Triangle) b, (Circle) a);
			}
		} else if(a instanceof Triangle) {
			if(b instanceof Circle) {
				return triangleCircle((Triangle) a, (Circle) b);
			}
		}
		
		return false;
	}
	
	public static boolean circleCircle(Circle a, Circle b) {
		if(a.position.distanceSquared(b.position) < (a.radius * a.radius) + (b.radius * b.radius))
			return true;
		
		return false;
	}
	
	public static boolean triangleCircle(Triangle t, Circle c) {
		Vector3D tmass = t.getPointOfMass();
		Vector3D pj = c.project(tmass);
		
		return t.pointInTriangle(c.position);
	}
	
}
