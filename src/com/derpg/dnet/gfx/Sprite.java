package com.derpg.dnet.gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.derpg.dnet.readers.ImageReader;

public class Sprite {
	
	private Rectangle frame;
	private BufferedImage sheet;
	
	private int cframe, start, end, delay;
	private int cdelay;
	private boolean completed;
	
	private ArrayList<Animation> def;

	public Sprite(int width, int height, String filename) {
		frame = new Rectangle(0, 0, width, height);
		cframe = start = end = delay = cdelay = 0;
		
		this.sheet = ImageReader.loadImage(filename);
		
		completed = false;
		
		def = new ArrayList<Animation>();
	}
	
	public Sprite(int x, int y, BufferedImage bi) {
		frame = new Rectangle(0, 0, x, y);
		cframe = start = end = delay = cdelay = 0;
		
		this.sheet = bi;
		for(int j = 0; j < sheet.getHeight(); j++) {
			for(int i = 0; i < sheet.getWidth(); i++) {
				if(sheet.getRGB(i, j) == new Color(255,255,255).getRGB()) {
					sheet.setRGB(i, j, new Color(0,0,0,0).getRGB() & 0xFFFFFF);
				}
			}
		}
		
		completed = false;
		
		def = new ArrayList<Animation>();
	}
	
	public Sprite(BufferedImage bi) {
		frame = new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
		cframe = start = end = delay = cdelay = 0;
		
		this.sheet = bi;
		for(int j = 0; j < sheet.getHeight(); j++) {
			for(int i = 0; i < sheet.getWidth(); i++) {
				if(sheet.getRGB(i, j) == new Color(255,255,255).getRGB()) {
					sheet.setRGB(i, j, new Color(0,0,0,0).getRGB() & 0xFFFFFF);
				}
			}
		}
		
		completed = false;
		
		def = new ArrayList<Animation>();
	}

	public BufferedImage getCurrentImage() {
		return sheet.getSubimage((int)frame.getX(), 0, (int)frame.getWidth(), (int)frame.getHeight());
	}
	
	public boolean isAnimation(int a, int b, int c) {
		return (start == a && end == b && delay == c);
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void update() {
		if(start <= end) {
			cdelay++;
			if(cframe >= end && cdelay >= delay) {
				if(start <= end) {
					completed = true;
					cframe = start;
					frame.x = cframe * frame.width;
					cdelay = 0;
				}
			} else if(cframe < end) {
				if(cdelay >= delay) {
					cframe++;
					cdelay = 0;
					
					frame.x = cframe * frame.width;
				}
			} 
		} else {
			cdelay++;
			if(cframe <= end && cdelay >= delay) {
				completed = true;
				cframe = start;
				frame.x = cframe * frame.width;
				cdelay = 0;
			} else if(cframe > end) {
				if(cdelay >= delay) {
					cframe--;
					cdelay = 0;
					
					frame.x = cframe * frame.width;
				}
			} 
		}
	}
	
	public void render(Graphics g, int x, int y) {
		BufferedImage iframe = sheet.getSubimage((int)frame.getX(), (int)frame.getY(), (int)frame.getWidth(), (int)frame.getHeight());
		
		g.drawImage(iframe, x, y, null);
	}
	
	public void setAnimation(int id) {
		Animation a = null;
		for(int i = 0; i < def.size(); i++) {
			if(def.get(i).id == id) {
				a = def.get(i);
				break;
			}
		}
		
		if(a != null)
			this.setAnimation(a.start, a.end, a.delay);
	}
	
	public void setAnimation(int start, int end, int delay) {
		this.start = this.cframe = start;
		this.end = end;
		this.delay = delay;
		completed = false;
	}
	
	public int getCurrentFrame() {
		return cframe;
	}
	
	public void setCurrentFrame(int f) {
		cframe = f;
	}
	
	public int getStartFrame() {
		return start;
	}
	
	public int getEndFrame() {
		return end;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void defineAnimation(int id, int a, int b, int c) {
		def.add(new Animation(id, a, b, c));
	}
	
	public void defineAnimation(Animation a) {
		def.add(a);
	}
	
	public class Animation {
		
		int id, start, end, delay;
		
		public Animation(int a, int b, int c, int d) {
			id = a;
			start = b;
			end = c;
			delay = d;
		}
		
	}

	public void setDelay(int i) {
		this.delay = i;
	}

	public boolean isAnimation(int i, int j) {
		return (start == i && end == j);
	}
	
}