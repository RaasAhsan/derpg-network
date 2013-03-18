package com.derpg.dnet.entity;

import java.util.ArrayList;
import java.util.List;

import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.math.Interpolation;
import com.derpg.dnet.math.Vector3D;

public class External extends OverworldEntity {
	
	public Vector3D last_pos;
	public Vector3D interp_pos;
	
	public int ipos = 0;
	
	private boolean running = false;

	private List<Vector3D> positions;
	
	public External(long id, Vector3D pos) {
		super(id, pos);
		
		positions = new ArrayList<Vector3D>();
		
		this.sprite = new Sprite(64, 64, "gfx/megaman.png");
		sprite.setAnimation(0, 0, 0);
		
		this.last_pos = position;
		this.interp_pos = position;
		
		for(int i = 0 ; i < 10; i++) {
			positions.add(new Vector3D(0, 0));
		}
	}
	
	public void pushPosition(Vector3D pos) {
		positions.add(pos);
		
		if(positions.size() > 10) {
			positions.remove(0);
		}
	}

	@Override
	public void update() {
		sprite.update();
		
		ipos++;
		
		Vector3D a = positions.get(7);
		Vector3D b = positions.get(8);
		
		Vector3D dir = new Vector3D(b.x - a.x, b.y - a.y);
		dir.normalize();
		
		int interp = 0;
		
		if(b.x != a.x || b.y != a.y) {
			if(dir.x > 0) {
				interp += ipos;
			} else if(dir.x < 0) {
				interp -= ipos;
			}
			
			int delay = 10;
			if(running) {
				delay = 5;
				interp *= 2;
			}
	
			interp_pos = Interpolation.interpolate(interp, a, b);
			
			if(dir.x == 0) {
				if(dir.y > 0)
					interp += ipos;
				else if(dir.y < 0)
					interp -= ipos;
				
				if(running) {
					interp *= 2;
				}
				
				Vector3D change = new Vector3D(a.y, a.x);
				Vector3D cpos = new Vector3D(b.y, b.x);
				
				interp_pos = Interpolation.interpolate(interp, change, cpos);
				interp_pos = new Vector3D(interp_pos.y, interp_pos.x);
			}
			
			/*System.out.println("----------------");
			System.out.println("Position A: " + a.x + ", " + a.y);
			System.out.println("Position B: " + b.x + ", " + b.y);
			System.out.println("Interpolation value: " + interp);
			System.out.println("Interpolated Position: " + interp_pos.x + ", " + interp_pos.y);
			*/
			if(!sprite.isAnimation(8 + direction * 6, 8 + direction * 6 + 5, delay))
				sprite.setAnimation(8 + direction * 6, 8 + direction * 6 + 5, delay);
		} else {
			interp_pos = b.copy();
			sprite.setAnimation(direction, direction, 0);
		}
		
		this.position = interp_pos;
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector3D(interp_pos.x - 32, interp_pos.y - 46), true);
	}
	
	@Override
	public void interact(Entity starter) {
		
	}

	public void setRunning(boolean r) {
		running = r;
	}

}
