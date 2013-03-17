package com.derpg.dnet.entity;

import com.derpg.dnet.event.EventObject;
import com.derpg.dnet.event.EventRunner;
import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.math.Circle;
import com.derpg.dnet.math.Vector3D;

public class NPC extends OverworldEntity {
	
	private EventObject event;

	public NPC(Vector3D pos, EventObject event) {
		super(-1, pos);
		this.event = event;
		
		this.mask = new Circle(pos, 4);
		
		this.sprite = new Sprite(64, 64, "gfx/navi_normalnavi.png");
		sprite.setAnimation(1, 1, 0);
	}

	@Override
	public void update() {
		sprite.update();
		
		
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector3D(position.x - 32, position.y - 47), true);
	}
	
	@Override
	public void interact(Entity starter) {
		this.direction = OverworldEntity.getDirectionTo(this, starter, 4);
		this.sprite.setAnimation(direction, direction, 0);
		EventRunner.run(null);
	}

}
