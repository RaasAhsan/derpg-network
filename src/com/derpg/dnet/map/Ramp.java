package com.derpg.dnet.map;

import java.awt.Color;

import com.derpg.dnet.Debug;
import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.math.Circle;
import com.derpg.dnet.math.Line;
import com.derpg.dnet.math.Triangle;
import com.derpg.dnet.math.Vector3D;

public class Ramp {
	
	// 0 = upright
	// 1 = upleft
	
	private Vector3D pos;
	private int direction;
	private int z2;
	
	private Triangle a, b;
	private Line la, lb;

	public Ramp(Vector3D pos, int direction, int z2) {
		this.pos = pos;
		this.direction = direction;
		this.z2 = z2;
		System.out.println(z2);
		
		pos.y += 8;
		if(direction == 0) {
			pos.x -= 16;
			
			a = new Triangle(new Vector3D(pos.x + 16, pos.y - 8), new Vector3D(pos.x - 16, pos.y + 8), new Vector3D(pos.x - 32, pos.y - 32));
			b = new Triangle(new Vector3D(pos.x + 16 - 96, pos.y - 56), new Vector3D(pos.x + 16, pos.y - 8), new Vector3D(pos.x + 32 - 96, pos.y - 56));
		
			la = new Line(new Vector3D(pos.x - 16, pos.y + 8), new Vector3D(2,2));
			lb = new Line(new Vector3D(pos.x + 16, pos.y - 8), new Vector3D(2,2));
		} else if(direction == 1) {
			pos.x += 16;
		
			double fy = (pos.y - 0.25 * z2 * 2) + 8;
			double fx = (pos.x - 0.25 * z2 * 2) - 16;
			
			a = new Triangle(new Vector3D(pos.x + 16, pos.y - 8), new Vector3D(pos.x - 16, pos.y + 8), new Vector3D(fx,fy));
			
			double fyb = (pos.y - 0.25 * z2 * 2) - 8;
			double fxb = (pos.x - 0.25 * z2 * 2) + 16;
			b = new Triangle(new Vector3D(fx,fy), new Vector3D(fxb, fyb), new Vector3D(pos.x + 16, pos.y - 8));
			
			la = new Line(new Vector3D(pos.x - 16, pos.y + 8), new Vector3D(fx,fy));
			lb = new Line(new Vector3D(pos.x + 16, pos.y - 8), new Vector3D(fxb,fyb));
		}
	}
	
	public boolean collides(Vector3D p) {
		return a.pointInTriangle(p) || b.pointInTriangle(p);
	}
	
	public boolean wallCollides(Vector3D p) {
		return la.collides(new Circle(p, 2)) || lb.collides(new Circle(p, 2));
	}
	
	public void render(Renderer renderer) {
		if(Debug.isEnabled()) {
			renderer.setColor(Color.GREEN);
			
			Triangle t = a;
			renderer.drawLine(t.a.getX(), t.a.getY(), t.b.getX(), t.b.getY());
			renderer.drawLine(t.a.getX(), t.a.getY(), t.c.getX(), t.c.getY());
			renderer.drawLine(t.c.getX(), t.c.getY(), t.b.getX(), t.b.getY());
			
			t = b;
			renderer.drawLine(t.a.getX(), t.a.getY(), t.b.getX(), t.b.getY());
			renderer.drawLine(t.a.getX(), t.a.getY(), t.c.getX(), t.c.getY());
			renderer.drawLine(t.c.getX(), t.c.getY(), t.b.getX(), t.b.getY());
			
			renderer.setColor(Color.RED);
			Line l = la;
			renderer.drawLine(l.a.getX(), l.a.getY(), l.b.getX(), l.b.getY());
			l = lb;
			renderer.drawLine(l.a.getX(), l.a.getY(), l.b.getX(), l.b.getY());
		}
	}

	public int getDirection() {
		return direction;
	}
	
}
