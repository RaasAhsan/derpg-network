package com.derpg.dnet.math;

public class Circle extends Mask {

	public Vector3D position;
	public float radius;
	
	public Circle(Vector3D pos, float r) {
		this.position = pos;
		this.radius = r;
	}

	@Override
	public void update(Vector3D pos) {
		this.position = pos;
	}
	
	public Vector3D project(Vector3D pt) {
		Vector3D d = new Vector3D(pt.y - this.position.y,  pt.y - this.position.y);
		d.normalize();
		
		return new Vector3D(this.position.x + d.x * radius, this.position.y + d.y * radius);
	}
	
}
