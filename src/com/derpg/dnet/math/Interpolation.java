package com.derpg.dnet.math;

public class Interpolation {

	public static Vector3D interpolate(int i, Vector3D a, Vector3D b) {
		Vector3D ip = new Vector3D(a.x + i, 0);
		ip.y = (int) (a.y + (b.y - a.y) * (((ip.x) - a.x) / (b.x - a.x)));
		
		return ip;
	}
	
}
