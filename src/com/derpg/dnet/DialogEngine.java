package com.derpg.dnet;

import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.math.Vector3D;

public class DialogEngine {

	private Input input;
	
	private String buffer;
	private int position;
	private boolean running;
	private int speed = 5;
	
	private boolean waiting = false;
	
	int counter = 0;
	int line = 0;
	
	private Sprite dialogbox, font, arrow;
	private Mugshot mugshot;
	
	public DialogEngine(Input input) {
		this.input = input;
		buffer = "";
		running = false;
		position = 0;
		
		dialogbox = new Sprite(238, 60, "gfx/dialogengine_textbox.png");
		font = new Sprite(7, 14, "gfx/dialogengine_font.png");
		arrow = new Sprite(11, 13, "gfx/dialogengine_arrow.png");
		arrow.setAnimation(0, 3, 4);
		
		mugshot = Mugshot.getMugshotOf(0);
		mugshot.setTalking();
	}
	
	public void update() {
		if(running) {
			if(!waiting) {
				counter++;
				if(counter >= speed) {
					counter = 0;
					
					while(buffer.charAt(position) == '{') {
						int i = buffer.indexOf('}', position);
						String command = buffer.substring(position + 1, i);
						
						if(command.equals("NB")) {
							waiting = true;
							mugshot.setSilent();
							buffer = buffer.replaceFirst("\\{NB\\}", "");
							position -=2;
						} else if(command.equals("NBC")) {
							line = 0;
							buffer = buffer.substring(i+1);
							position = 0;
						} else if(command.startsWith("NS")) {
							int speaker = Integer.valueOf(command.substring(2));
							buffer = buffer.replaceFirst("\\{NS[0-9][0-9][0-9]\\}", "");
							mugshot = Mugshot.getMugshotOf(speaker);
						}
					}
					
					if(buffer.charAt(position) == '#') {
						if(line == 2) {
							waiting = true;
							mugshot.setSilent();
						} else {
							line++;
						}
						position++; // position increase 
					}
					
					if(position < buffer.length()-1) {
						position++;
					}
					
					if(position == buffer.length() - 1) {
						waiting = true;
						mugshot.setSilent();
					}
				}
			} else {
				if(input.isKeyPressed(Input.KEY_Z) || input.isKeyPressed(Input.KEY_X)) {
					if(position == buffer.length()-1) {
						buffer = "";
						waiting = false;
						running = false;
					} else {
						waiting = false;
						mugshot.setTalking();
						buffer = buffer.substring(position+1);
						line = 0;
						position = 0;
					}
				}
			}
			mugshot.update();
			arrow.update();
		}
	}
	
	public void render(Renderer renderer) {
		if(running) {
			renderer.drawSprite(dialogbox, new Vector3D(9, 130), false);
			mugshot.render(renderer);
		
			int dx = 64;
			int dy = 141;
			int nstart = 0;
			for(int i = 0; i <= position; i++) {
				char c = buffer.charAt(i);
				if(c == '#') {
					dx = 64;
					dy += 15;
				} else if(c == '{') { 
					nstart = 1;
				} else if(c == '}') { 
					nstart = 0;
				} else {
					if(nstart == 0) {
						font.setAnimation(c - 32, c - 32, 0);
						font.update();
						renderer.drawSprite(font, new Vector3D(dx, dy), false);
						dx += 8;
					}
				}
			}
			
			if(waiting) {
				renderer.drawSprite(arrow, new Vector3D(237, 175), false);
			}
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public void say(String say) {
		mugshot.setTalking();
		buffer = say;
		running = true;
		position = 0;
		line = 0;
	}

	public boolean isComplete() {
		return buffer.length() == 0;
	}
	
}
