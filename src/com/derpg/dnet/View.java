package com.derpg.dnet;

import com.derpg.dnet.entity.Player;
import com.derpg.dnet.math.Vector3D;

public class View {

	private Player local;
	private Vector3D dim;
	
	public View(Player l, Vector3D dimensions) {
		this.local = l;
		this.dim = dimensions.copy();
	}
	
	public Vector3D getView() {
		return new Vector3D(local.getPosition().x - dim.x / 2, local.getPosition().y + (local.getPosition().z / 4) - dim.y / 2);
	}
	
}
