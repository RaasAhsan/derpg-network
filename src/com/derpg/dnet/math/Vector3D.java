package com.derpg.dnet.math;

public class Vector3D {

	public double x, y, z;
	
	public Vector3D() {
		x = y = z = 0;
	}
	
	public Vector3D(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D copy() {
		return new Vector3D(x, y, z);
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	public int getZ() {
		return (int) z;
	}
	
	public double dotProduct(Vector3D b) {
		return x * b.x + y * b.y + z * b.z;
	}
	
	public double distanceSquared(Vector3D b) {
		return Math.pow(b.x - x, 2) + Math.pow(b.y - y, 2);
	}
	
	public double length() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
	
	public void normalize() {
		double length = this.length();
		x /= length;
		y /= length;
		z /= length;
	}
	
	public double angleTo(Vector3D pos) {
		return Math.atan2(pos.y - y, pos.x - x);
	}

	public double distance(Vector3D a) {
		return Math.sqrt(this.distanceSquared(a));
	}
	
}
