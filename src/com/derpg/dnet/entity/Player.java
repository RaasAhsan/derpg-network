package com.derpg.dnet.entity;

import java.awt.Color;
import java.util.List;

import com.derpg.dnet.DKM;
import com.derpg.dnet.Input;
import com.derpg.dnet.event.EventRunner;
import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.map.Ramp;
import com.derpg.dnet.math.Circle;
import com.derpg.dnet.math.Collisions;
import com.derpg.dnet.math.Triangle;
import com.derpg.dnet.math.Vector3D;
import com.derpg.net.dialogengine.DialogEngine;

public class Player extends OverworldEntity {
	
	private Input input;
	
	private boolean running = false;
	private Vector3D velocity = new Vector3D(0, 0);
	
	private String tomap;
	
	private DKM dkm;
	private DialogEngine dengine;
	
	boolean unz = false;

	public Player(Vector3D pos, Input input, DialogEngine dengine) {
		super(0, pos);
		this.input = input;
		this.dengine = dengine;
		
		this.mask = new Circle(pos, 4);
		
		this.sprite = new Sprite(64, 64, "gfx/megaman.png");
		this.tomap = "";
	}
	
	public String getToMap() {
		return tomap;
	}
	
	public void setToMap(String s) {
		this.tomap = s;
	}

	@Override
	public void update() {
		mask.update(position);
		
		if(!dengine.isRunning()) {
			int delay = 0;
			if(input.isKeyPressed(Input.KEY_X)) {
				delay = 5;
				sprite.setDelay(5);
				running = true;
			} else {
				delay = 10;
				sprite.setDelay(10);
				running = false;
			}
			
			if(input.isKeyPressed(Input.KEY_UP) && input.isKeyPressed(Input.KEY_RIGHT)) {
				velocity.x = 1;
				velocity.y = -0.5;
				direction = 7;
			} else if(input.isKeyPressed(Input.KEY_UP) && input.isKeyPressed(Input.KEY_LEFT)) {
				velocity.x = -1;
				velocity.y = -0.5;
				direction = 5;
			} else if(input.isKeyPressed(Input.KEY_RIGHT) && input.isKeyPressed(Input.KEY_DOWN)) {
				velocity.x = 1;
				velocity.y = 0.5;
				direction = 1;
			} else if(input.isKeyPressed(Input.KEY_LEFT) && input.isKeyPressed(Input.KEY_DOWN)) {
				velocity.x = -1;
				velocity.y = 0.5;
				direction = 3;
			} else if(input.isKeyPressed(Input.KEY_UP)) {
				velocity.y = -1;
				velocity.x = 0;
				direction = 6;
			} else if(input.isKeyPressed(Input.KEY_DOWN)) {
				velocity.y = 1;
				velocity.x = 0;
				direction = 2;
			} else if(input.isKeyPressed(Input.KEY_LEFT)) {
				velocity.y = 0;
				velocity.x = -1;
				direction = 4;
			} else if(input.isKeyPressed(Input.KEY_RIGHT)) {
				velocity.y = 0;
				velocity.x = 1;
				direction = 0;
			} else {
				velocity.x = velocity.y = 0;
			}
			
			if(velocity.x == 0 && velocity.y == 0)
				sprite.setAnimation(direction, direction, 0);
			else if(velocity.x != 0 || velocity.y != 0) {
				if(!sprite.isAnimation(8 + direction * 6, 8 + direction * 6 + 5, delay))
					sprite.setAnimation(8 + direction * 6, 8 + direction * 6 + 5, delay);
			}
			
			if(running) {
				velocity.x *= 2;
				velocity.y *= 2;
			}
			position.x += velocity.x;
			position.y += velocity.y;
			
			if(input.isKeyPressed(Input.KEY_A)) {
				sprite.setAnimation(2,2,0);
				dengine.say("Let's go find#something to do!");
			} 
			if(input.isKeyPressed(Input.KEY_Z) && !unz) {
				Vector3D np = new Vector3D(position.x + Math.cos(this.getAngleOfDirection()) * 15, position.y + Math.sin(this.getAngleOfDirection()) * 15);
				Circle c = new Circle(np, 10);
				List<OverworldEntity> loe = dkm.getAllEntities();
				for(OverworldEntity oe : loe) {
					if(oe != this) {
						if(oe instanceof NPC) {
							if(Collisions.collides(oe.getMask(), c)) {
								((NPC)oe).interact(this);
								dkm.nmap.interact((int)((NPC)oe).getID());
								sprite.setAnimation(direction,direction,0);
							}
						}
					}
				}
			}
		}
		
		if(input.isKeyPressed(Input.KEY_Z) && !unz)
			unz = true;
		
		if(!input.isKeyPressed(Input.KEY_Z))
			unz = false;
		
		sprite.update();
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector3D(position.x - 32, position.y - 46 + position.z / 4), true);
		
		//Vector3D np = new Vector3D(position.x + Math.cos(this.getAngleOfDirection()) * 15, position.y + Math.sin(this.getAngleOfDirection()) * 15);
		//renderer.setColor(Color.RED);
		//renderer.drawLine(position.getX(), position.getY(), np.getX(), np.getY());
	}
	
	public void handleCollision(Triangle t) {
		position.x -= velocity.x;
		position.y -= velocity.y;
		
		if(input.isKeyPressed(Input.KEY_UP) && input.isKeyPressed(Input.KEY_RIGHT)) {
		} else if(input.isKeyPressed(Input.KEY_UP) && input.isKeyPressed(Input.KEY_LEFT)) {
		} else if(input.isKeyPressed(Input.KEY_DOWN) && input.isKeyPressed(Input.KEY_RIGHT)) {
		} else if(input.isKeyPressed(Input.KEY_DOWN) && input.isKeyPressed(Input.KEY_LEFT)) {
		} else if(input.isKeyPressed(Input.KEY_UP)) {
			double xadd, yadd;
			if(running) {
				xadd = 2;
				yadd = -1;
			} else {
				xadd = 1;
				yadd = -0.5;
			}
			
			if(dkm.isFree(new Vector3D(position.x + xadd, position.y + yadd))) {
				position.x += xadd;
				position.y += yadd;
			} else if(dkm.isFree(new Vector3D(position.x - xadd, position.y + yadd))) {
				position.x -= xadd;
				position.y += yadd;
			}
		} else if(input.isKeyPressed(Input.KEY_DOWN)) {
			double xadd, yadd;
			if(running) {
				xadd = 2;
				yadd = 1;
			} else {
				xadd = 1;
				yadd = 0.5;
			}
			
			if(dkm.isFree(new Vector3D(position.x + xadd, position.y + yadd))) {
				position.x += xadd;
				position.y += yadd;
			} else if(dkm.isFree(new Vector3D(position.x - xadd, position.y + yadd))) {
				position.x -= xadd;
				position.y += yadd;
			}
		} else if(input.isKeyPressed(Input.KEY_LEFT)) {
			double xadd, yadd;
			if(running) {
				xadd = -2;
				yadd = 1;
			} else {
				xadd = -1;
				yadd = 0.5;
			}
			
			if(dkm.isFree(new Vector3D(position.x + xadd, position.y + yadd))) {
				position.x += xadd;
				position.y += yadd;
			} else if(dkm.isFree(new Vector3D(position.x + xadd, position.y - yadd))) {
				position.x += xadd;
				position.y -= yadd;
			}
		} else if(input.isKeyPressed(Input.KEY_RIGHT)) {
			double xadd, yadd;
			if(running) {
				xadd = 2;
				yadd = 1;
			} else {
				xadd = 1;
				yadd = 0.5;
			}
			
			if(dkm.isFree(new Vector3D(position.x + xadd, position.y + yadd))) {
				position.x += xadd;
				position.y += yadd;
			} else if(dkm.isFree(new Vector3D(position.x + xadd, position.y - yadd))) {
				position.x += xadd;
				position.y -= yadd;
			}
		}
	}
	
	@Override
	public void interact(Entity starter) {
		
	}

	public boolean isRunning() {
		return running;
	}
	
	public void setMap(DKM dkm) {
		this.dkm = dkm;
	}

	public void handleRamp(Ramp r) {
		if(r.getDirection() == 0) {
			
		} else if(r.getDirection() == 1) {
			double zadd = 0;
			if(velocity.x == -1 && velocity.y == -0.5) {
				zadd = -2;
			} else if(velocity.x == 1 && velocity.y == 0.5) {
				zadd = 2;
			} else if(velocity.x == 1) {
				zadd = 1;
			} else if(velocity.x == -1) {
				zadd = -1;
			} else if(velocity.y == 0.5) {
				zadd = 1;
			} else if(velocity.y == -0.5) {
				zadd = -1;
			}
			position.z += zadd;
			
			// walls
			/*if(r.wallCollides(this.position)) {
				position.z -= zadd;
			}*/
		}
	}

}
