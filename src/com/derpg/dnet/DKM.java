package com.derpg.dnet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.derpg.dnet.entity.Decoration;
import com.derpg.dnet.entity.External;
import com.derpg.dnet.entity.Player;
import com.derpg.dnet.entity.OverworldEntity;
import com.derpg.dnet.gfx.BackgroundRenderer;
import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.map.EndPoint;
import com.derpg.dnet.map.Ramp;
import com.derpg.dnet.map.StartPoint;
import com.derpg.dnet.math.Circle;
import com.derpg.dnet.math.Collisions;
import com.derpg.dnet.math.Mask;
import com.derpg.dnet.math.Triangle;
import com.derpg.dnet.math.Vector3D;
import com.derpg.dnet.network.Protocol;
import com.derpg.dnet.readers.DKMReader;
import com.derpg.dnet.readers.ImageReader;
import com.jxware.client.Packet;

public class DKM {
	
	public Vector3D begin;

	private static BufferedImage[] textures;
	private static BufferedImage[] backgrounds;
	
	private String mapname;
	private String filename;
	
	private Player local;
	
	private ArrayList<Decoration> decorations;
	private ArrayList<Mask> collisions;
	private ArrayList<StartPoint> spawns;
	private ArrayList<EndPoint> ends;
	private ArrayList<Ramp> ramps;
	
	private ArrayList<OverworldEntity> entities;
	
	private BackgroundRenderer bgrenderer;
	
	public DKM() {
		this.decorations = new ArrayList<Decoration>();
		this.collisions = new ArrayList<Mask>();
		this.spawns = new ArrayList<StartPoint>();
		this.ends = new ArrayList<EndPoint>();
		this.ramps = new ArrayList<Ramp>();
		
		this.entities = new ArrayList<OverworldEntity>();
	}
	
	public void spawn(OverworldEntity e) {
		this.entities.add(e);
	}
	
	public void removePlayer(int id) {
		External e = this.getPlayerWithID(id);
		entities.remove(e);
	}
	
	public StartPoint getStartPointFrom(String map) {
		for(StartPoint sp : spawns) {
			if(sp.getFromMap().equals(map)) {
				return sp;
			}
		}
		return null;
	}
	
	public void addDecoration(Vector3D p, BufferedImage bi) {
		this.decorations.add(new Decoration(p, bi));
	}
	
	public void addRestrict(Vector3D p) {
		collisions.add(new Triangle(new Vector3D(p.x, p.y - 16, p.z), new Vector3D(p.x - 32, p.y, p.z), new Vector3D(p.x + 32, p.y, p.z)));
		collisions.add(new Triangle(new Vector3D(p.x, p.y + 16, p.z), new Vector3D(p.x - 32, p.y, p.z), new Vector3D(p.x + 32, p.y, p.z)));
		//collisions.add(new Triangle(new Vector3D(p.x , p.y + 16), xnew Vector3D(p.x - 32, p.y), new Vector3D(p.x, p.y - 16)));
	}
	
	public void addSpawnPoint(Vector3D pos, String frommap, int dir) {
		this.spawns.add(new StartPoint(pos, frommap, dir));
	}
	
	public void addEndPoint(Vector3D pos, String tomap) {
		this.ends.add(new EndPoint(pos, tomap));
	}
	
	public void addRamp(Ramp r) {
		this.ramps.add(r);
	}
	
	public External getPlayerWithID(int id) {
		for(OverworldEntity oe : entities) {
			if(oe instanceof External) {
				if(oe.getID() == id)
					return (External) oe;
			}
		}
		
		return null;
	}
	
	public String getMapName() {
		return filename.replace(".dkm", "");
	}
	
	public String getFileName() {
		return filename;
	}
	
	public void setFileName(String s) {
		this.filename = s;
	}
	
	public void setPlayer(Player l) {
		this.local = l;
		entities.add(local);
	}
	
	public void setBackgroundRenderer(BackgroundRenderer br) {
		this.bgrenderer = br;
	}
	
	public void update() {
		for(OverworldEntity oe : entities) {
			oe.update();
		}
		
		for(Mask m : collisions) {
			if(m instanceof Triangle) {
				Triangle t = (Triangle) m;
				if(t.getPointOfMass().distanceSquared(local.getPosition()) <= 50 * 50) {
					if(t.a.getZ() == -local.getPosition().getZ())
						if(Collisions.collides(t, local.getMask())) {
							local.handleCollision(t);
						}
				}
			}
		}
		
		for(Ramp r : ramps) { // dont check for distance
			Vector3D lpos = new Vector3D(local.getPosition().x, local.getPosition().y + local.getPosition().z / 4);
			if(r.collides(lpos.copy())) {
				local.handleRamp(r);
			}
		}
		
		for(EndPoint ep : ends) { // dont check for distance because there arent that many
			if(ep.collides((Circle) local.getMask())) {
				local.setToMap(ep.getToMap());
			}
		}
		
		Collections.sort(entities, new Comparator<OverworldEntity>() {

			@Override
			public int compare(OverworldEntity a, OverworldEntity b) {
				if (a.getDepth() > b.getDepth()) {
					return -1;
				} else if (a.getDepth() < b.getDepth()) {
					return 1;
				}
				return 0;
			}

		});
		
		bgrenderer.update();
	}
	
	public void render(Renderer renderer) {
		Collections.sort(decorations, new Comparator<Decoration>() {

			@Override
			public int compare(Decoration a, Decoration b) {
				if(a.getDepth() < b.getDepth())
					return 1;
				else if(a.getDepth() > b.getDepth())
					return -1;
				return 0;
			}
			
		});
		
		bgrenderer.render(renderer);
		
		for(Decoration d : decorations) {
			d.render(renderer);
		}
		
		if(Debug.isEnabled()) {
			renderer.setColor(Color.RED);
			for(Mask m : this.collisions) {
				if(m instanceof Triangle) {
					Triangle t = (Triangle) m;
					renderer.drawLine(t.a.getX(), t.a.getY(), t.b.getX(), t.b.getY());
					renderer.drawLine(t.a.getX(), t.a.getY(), t.c.getX(), t.c.getY());
					renderer.drawLine(t.c.getX(), t.c.getY(), t.b.getX(), t.b.getY());
				}
			}
		}
		
		for(OverworldEntity oe : entities) {
			oe.render(renderer);
		}
		
		for(Ramp r : ramps) {
			r.render(renderer);
		}
	}

	public static void setTexturePack(BufferedImage[] textures) {
		DKM.textures = textures;
	}
	
	public static BufferedImage getTextureAt(int i) {
		return DKM.textures[i-1];
	}
	
	public static void loadBackgrounds() {
		backgrounds = new BufferedImage[4];
		backgrounds[0] = ImageReader.loadImage("backgrounds/bac_net_main [128x128].png");
		backgrounds[1] = ImageReader.loadImage("backgrounds/bac_lan_hp [128x128].png");
		backgrounds[2] = ImageReader.loadImage("backgrounds/bac_acdc_bn4 [128x64].png");
		backgrounds[3] = ImageReader.loadImage("backgrounds/bac_net_mainx [64x64].png");
	}
	
	public static DKM gotoMap(Player p, DKM dkmmap) {
		String tomap = p.getToMap();
		String prevmap = dkmmap.getMapName();
		
		DKMReader reader = new DKMReader();
		
		dkmmap = reader.read(tomap + ".dkm");
		dkmmap.setPlayer(p);
		
		p.setPosition(dkmmap.getStartPointFrom(prevmap).getStartPoint());
		
		int dir = dkmmap.getStartPointFrom(prevmap).getDirection();
		p.getSprite().setAnimation(dir, dir, 0);
		p.getSprite().update();
		
		Packet roomchange = new Packet(Protocol.ROOM_CHANGE);
		roomchange.writeString(p.getToMap());
		Game.client.write(roomchange);
		
		p.setToMap("");
		p.setMap(dkmmap);
		
		return dkmmap;
	}
	
	public boolean isFree(Vector3D pos) {
		Circle c = new Circle(pos, 1);
		
		for(Mask m : collisions) {
			if(Collisions.collides(m, c)) {
				return false;
			}
		}
		
		return true;
	}

	public static BufferedImage getBackgroundAt(int bgtexid) {
		return backgrounds[bgtexid];
	}

	public List<OverworldEntity> getAllEntities() {
		return this.entities;
	}
	
}
