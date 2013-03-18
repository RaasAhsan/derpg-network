package com.derpg.dnet;

import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.math.Vector3D;

public class Mugshot {
	
	public static final int MEGAMAN = 0;
	public static final int NORMAL_NAVI = 1;
	public static final int HEEL_NAVI = 2;
	public static final int DROKOKIEO = 3;

	private int mugid;
	private Sprite mugshot;
	
	private int frames;
	
	private Mugshot(int mugid) {
		this.mugid = mugid;
		
		if(mugid == MEGAMAN) {
			frames = 3;
			mugshot = new Sprite(40, 48, "gfx/mugshot_megaman.png");
		} else if(mugid == NORMAL_NAVI) {
			frames = 1;
			mugshot = new Sprite(40, 48, "gfx/mugshot_normalnavi.png");
		} else if(mugid == HEEL_NAVI) {
			frames = 1;
			mugshot = new Sprite(40, 48, "gfx/mugshot_heelnavi.png");
		} else if(mugid == DROKOKIEO) {
			frames = 3;
			mugshot = new Sprite(40, 48, "gfx/mugshot_idesty.png");
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
