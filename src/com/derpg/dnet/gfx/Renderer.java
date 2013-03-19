package com.derpg.dnet.gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.derpg.dnet.View;
import com.derpg.dnet.math.Vector3D;

public class Renderer {

	private Graphics gfx;
	private View view;
	
	public Renderer(Graphics gfx, View view) {
		this.gfx = gfx;
		this.view = view;
	}
	
	public void drawSprite(Sprite s, Vector3D pos, boolean b) {
		// check if you dont need to render it
		if(b) {
			gfx.drawImage(s.getCurrentImage(), pos.getX() - (int)view.getView().x, pos.getY() - (int)view.getView().y, null);
		} else {
			gfx.drawImage(s.getCurrentImage(), pos.getX(), pos.getY(), null);
		}
	}

	public void setColor(Color c) {
		gfx.setColor(c);
	}

	public void drawLine(int x, int y, int x2, int y2) {
		gfx.drawLine(x - view.getView().getX(), y - view.getView().getY(), x2 - view.getView().getX(), y2 - view.getView().getY());
	}

	public void fillRect(Rectangle rect) {
		gfx.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
	}

	public void drawText(String string, Vector3D pos, boolean b) {
		if(b) {
			gfx.drawString(string, pos.getX() - (int)view.getView().x, pos.getY() - (int)view.getView().y);
		} else {
			gfx.drawString(string, pos.getX(), pos.getY());
		}
	}
	
}
