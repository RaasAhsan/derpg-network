package com.derpg.dnet.gfx;

import java.awt.Color;

import com.derpg.dnet.DKM;
import com.derpg.dnet.math.Vector3D;

public class BackgroundRenderer {
	
	int type;
	
	// textured bg
	Sprite texture;
	double xspeed;
	double yspeed;
	Vector3D translate;
	
	// solid
	Color color;
	
	int ticks;

	public BackgroundRenderer(int bgtexid, int bgspeed, int xbgspeed, int ybgspeed) {
		this.type = 1;
		this.texture = new Sprite(128, 128, DKM.getBackgroundAt(bgtexid));
		this.texture.setAnimation(0, 0, bgspeed);
		this.xspeed = xbgspeed / 10;
		this.yspeed = ybgspeed / 10;
		translate = new Vector3D();
		
		ticks = 0;
	}

	public BackgroundRenderer(int bgr, int bgg, int bgb) {
		this.type = 0;
		this.color = new Color(bgr, bgg, bgb);
	}
	
	public void update() {
		if(type == 1) {
			texture.update();
			
			if(ticks % 2 == 0) {
				translate.x += 1;
				translate.y += 1;
			}
			
			if(translate.x >= 256) {
				translate.x = 0;
			} else if(translate.y >= 256) {
				translate.y = 0;
			}
		}
		
		ticks ++;
		if(ticks >= 60)
			ticks = 0;
	}
	
	public void render(Renderer renderer) {
		if(type == 0) {
			
		} else if(type == 1) {
			drawBackgroundAt(renderer,new Vector3D(256 - 256, 192 - 256));
			drawBackgroundAt(renderer,new Vector3D(256 - 256 - 256, 192 - 256));
			drawBackgroundAt(renderer,new Vector3D(256 - 256, 192 - 256 - 256));
			drawBackgroundAt(renderer,new Vector3D(256 - 256 - 256, 192 - 256 - 256));
		}
	}
	
	public void drawBackgroundAt(Renderer renderer, Vector3D pos) {
		for(int j = 0; j < 4; j++) {
			for(int i = 0; i < 4; i++) {
				renderer.drawSprite(texture, new Vector3D(pos.x + translate.x + i * 128,pos.y + translate.y + j * 128), false);
			}
		}
	}

}
