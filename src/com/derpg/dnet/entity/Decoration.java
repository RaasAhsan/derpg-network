package com.derpg.dnet.entity;

import java.awt.image.BufferedImage;

import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.math.Vector3D;

public class Decoration extends Entity {

	public Decoration(Vector3D pos,BufferedImage bi) {
		super(-1, pos);
		
		this.sprite = new Sprite(bi);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector3D(position.x, position.y), true);
	}
	
	public long getDepth() {
		return -position.getY() - (position.getZ() * 10000);
	}

}
