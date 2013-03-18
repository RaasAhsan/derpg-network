package com.derpg.dnet.entity;

import com.derpg.dnet.event.EventRunner;
import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.math.Circle;
import com.derpg.dnet.math.Vector3D;

public class NPC extends OverworldEntity {
	
	public static final int NORMALNAVI = 0;
	public static final int HEELNAVI = 1;
	
	int npctype;

	public NPC(int npcid, int npctype, Vector3D pos) {
		super(npcid, pos);
		
		this.mask = new Circle(pos, 4);
		
		this.npctype = npctype;
		
		if(npctype == NORMALNAVI)
			this.sprite = new Sprite(64, 64, "gfx/navi_normalnavi.png");
		else if(npctype == HEELNAVI)
			this.sprite = new Sprite(64, 64, "gfx/navi_heelnavi.png");
		
		sprite.setAnimation(0, 0, 0);
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
	}

}
