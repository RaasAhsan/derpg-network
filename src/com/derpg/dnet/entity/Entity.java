package com.derpg.dnet.entity;

import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.gfx.Sprite;
import com.derpg.dnet.math.Mask;
import com.derpg.dnet.math.Vector3D;

public abstract class Entity {

	protected long id;
	protected Vector3D position;
	protected Sprite sprite;
	protected Mask mask;
	
	public Entity(long id, Vector3D pos) {
		this.id = id;
		this.position = pos;
	}
	
	public abstract void update();
	public abstract void render(Renderer renderer);
	
	public long getID() {
		return id;
	}
	
	public Vector3D getPosition() {
		return position;
	}
	
	public void setPosition(Vector3D p) {
		this.position = p.copy();
	}
	
	public Mask getMask() {
		return mask;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
}
