package com.derpg.dnet.math;

public class Triangle extends Mask {

	public Vector3D a, b, c;
	
	public Triangle(Vector3D a, Vector3D b, Vector3D c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public Vector3D getPointOfMass() {
		Vector3D mass = new Vector3D(a.x + b.x + c.x, a.y + b.y + c.y);
		mass.x /= 3;
		mass.y /= 3;
		
		return mass;
	}
	
	public boolean pointInTriangle(Vector3D p) {
	    double as = (a.x - p.x) * (b.y - p.y) - (b.x - p.x) * (a.y - p.y);
	    double bs = (b.x - p.x) * (c.y - p.y) - (c.x - p.x) * (b.y - p.y);
	    double cs = (c.x - p.x) * (a.y - p.y) - (a.x - p.x) * (c.y - p.y);
 	    
	    double sa = as / Math.abs(as);
	    double sb = bs / Math.abs(bs);
	    double sc = cs / Math.abs(cs);
		
		return (sa == sb && sb == sc) || pointLineTest(p);
	}

	private boolean pointLineTest(Vector3D p) {
		Line al = new Line(a, b);
		Line bl = new Line(b, c);
		Line cl = new Line(c, a);
		
		Circle c = new Circle(p, 3);
		return (al.collides(c) || bl.collides(c) || cl.collides(c));
	}

	@Override
	public void update(Vector3D pos) {
		// nothing
	}
	
}
