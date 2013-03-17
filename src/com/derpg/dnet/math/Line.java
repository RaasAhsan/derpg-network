package com.derpg.dnet.math;

import java.util.Random;

public class Line extends Mask {
	
	public Vector3D a, b;
	
	static Random rand = new Random();
	
	public Line(Vector3D av, Vector3D bv) {
		this.a = av;
		this.b = bv;
	}
	
	public Vector3D project(Vector3D pj) {
		Vector3D hp = new Vector3D(pj.x - a.x, pj.y - a.y);
		
		Vector3D ab = new Vector3D(b.x - a.x, b.y - a.y);
		Vector3D abn = ab.copy();
		abn.normalize();
		
		double proj = hp.dotProduct(abn);
		if(proj < 0)
			return a.copy();
		else if(proj > ab.length())
			return b.copy();
		
		Vector3D prj = new Vector3D(a.x + proj * abn.x, a.y + proj * abn.y);
		
		return prj;
	}
	
	/*
	 * closest = closest_point_on_seg(seg_a, seg_b, circ_pos) dist_v =
	 * circ_pos - closest if dist_v.len() > circ_rad: return vec(0, 0) if
	 * dist_v.len() <= 0: raise ValueError,
	 * "Circle's center is exactly on segment" offset = dist_v /
	 * dist_v.len() * (circ_rad - dist_v.len()) return offset
	 */
	public boolean collides(Circle c) {
		Vector3D closest = this.project(c.position);
		if(closest.distanceSquared(c.position) <= c.radius * c.radius)
			return true;
		
		return false;
	}
	
	@Override
	public void update(Vector3D pos) {
		// TODO Auto-generated method stub
		
	}

}

