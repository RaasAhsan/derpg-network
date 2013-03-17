package com.derpg.dnet.gfx;

import java.awt.Color;
import java.awt.Rectangle;

public class Fade {
	
	double speed;
	Color tocolor;
	
	Color curcolor;
	int alpha;
	
	int type;

	public Fade(double speed, Color to, int type) {
		this.speed = speed;
		this.tocolor = to;
		this.type = type;
		
		if(type == 0)
			alpha = 0;
		else if(type == 1)
			alpha = 255;
		
		curcolor = new Color(to.getRed(), to.getGreen(), to.getBlue());
	}
	
	public void update() {
		if(type == 0)
			alpha += speed;
		else if(type == 1)
			alpha -= speed;
		
		if(alpha < 0)
			alpha = 0;
		if(alpha > 255)
			alpha = 255;
		
		curcolor = new Color(curcolor.getRed(), curcolor.getGreen(), curcolor.getBlue(), alpha);
	}
	
	public void render(Renderer renderer) {
		renderer.setColor(curcolor);
		renderer.fillRect(new Rectangle(0, 0, 256, 192));
	}
	
	public boolean done() {
		if(type == 0)
			return alpha >= 255;
		else
			return alpha <= 0;
	}
	
}
