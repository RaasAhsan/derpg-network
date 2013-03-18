package com.derpg.net.dialogengine;

import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.math.Vector3D;

import com.derpg.dnet.entity.OverworldEntity;

public class Mugshot {

	private int mugid;
	private Sprite mugshot;
	
	private int frames;
	
	private Mugshot(int mugid) {
		this.mugid = mugid;
		
		if(mugid == OverworldEntity.MEGAMAN) {
			frames = 3;
			mugshot = new Sprite(40, 48, "gfx/mugshot_megaman.png");
		} else if(mugid == OverworldEntity.NORMALNAVI) {
			frames = 1;
			mugshot = new Sprite(40, 48, "gfx/mugshot_normalnavi.png");
		} else if(mugid == OverworldEntity.HEELNAVI) {
			frames = 1;
			mugshot = new Sprite(40, 48, "gfx/mugshot_heelnavi.png");
		} else if(mugid == OverworldEntity.DROKOKIEO) {
			frames = 3;
			mugshot = new Sprite(40, 48, "gfx/mugshot_idesty.png");
		} else if(mugid == OverworldEntity.MRPROG) {
			frames = 3;
			mugshot = new Sprite(40, 48, "gfx/mugshot_mrprog.png");
		} else if(mugid == OverworldEntity.BN4GUYNAVI) {
			frames = 1;
			mugshot = new Sprite(40, 48, "gfx/mugshot_bn4guy.png");
		}
	}
	
	public void update() {
		mugshot.update();
	}
	
	public void render(Renderer renderer) {
		renderer.drawSprite(mugshot, new Vector3D(13, 138), false);
	}
	
	public void setSilent() {
		this.mugshot.setAnimation(0,0,0);
	}
	
	public void setTalking() {
		this.mugshot.setAnimation(0, frames - 1, 5);
	}
	
	public int getMugshotID() {
		return mugid;
	}
	
	public static Mugshot getMugshotOf(int mugid) {
		return new Mugshot(mugid);
	}
	
}
