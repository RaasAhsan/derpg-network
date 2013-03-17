package com.derpg.dnet.readers;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

import com.derpg.dnet.DKM;
import com.derpg.dnet.Debug;
import com.derpg.dnet.gfx.BackgroundRenderer;
import com.derpg.dnet.map.Ramp;
import com.derpg.dnet.math.Vector3D;

public class DKMReader extends Reader {
	
	public DKMReader() {
		
	}
	
	public DKM read(String filename) {
		try {
			this.loadData("/maps/" + filename);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		int check = this.readSignedByte();
		int version = this.readSignedByte();
		
		Debug.print("Map Loading: " + filename);
		if(version == 2) {
			DKM dkm = this.readV2();
			dkm.setFileName(filename);
			return dkm;
		}
		
		return null;
	}
	
	private DKM readV2() {
		DKM dkm = new DKM();
		
		int ct = 0;
		
		// instances/decorations
		ct = this.readSignedByte();
		Debug.print("Instances: " + ct);
		for(int i = 0; i < ct; i++) {
			int dx = this.readSignedByte();
			int dy = this.readSignedByte();
			int dz = this.readSignedByte();
			int depth = this.readSignedByte();
			int sn = this.readSignedByte();
			
			//System.out.println(sn);
			dkm.addDecoration(new Vector3D(dx, dy, dz), DKM.getTextureAt(sn));
		}
		
		// start points
		ct = this.readSignedByte();
		Debug.print("Start Points: " + ct);
		for(int i = 0; i < ct; i++) {
			int sx = this.readSignedByte();
			int sy = this.readSignedByte();
			int sz = this.readSignedByte() / 4;
			String prevmap = this.readString();
			int wt = this.readSignedByte();
			int dir = this.readSignedByte();
			
			dkm.addSpawnPoint(new Vector3D(sx, sy, sz), prevmap, dir);
			
			if(prevmap.equals("<begin>")) {
				dkm.begin = new Vector3D(sx, sy, sz);
			}
			
			Debug.print("\t" + prevmap);
		}
		
		// end points
		ct = this.readSignedByte();
		Debug.print("End Points: " + ct);
		for(int i = 0; i < ct; i++) {
			int sx = this.readSignedByte();
			int sy = this.readSignedByte();
			int sz = this.readSignedByte();
			String tomap = this.readString();
			int wt = this.readSignedByte();
			
			dkm.addEndPoint(new Vector3D(sx, sy, sz), tomap);
			
			Debug.print("\t" + tomap);
		}
		
		// blocks
		ct = this.readSignedByte();
		Debug.print("Blocks: " + ct);
		for(int i = 0; i < ct; i++) {
			int bx = this.readSignedByte();
			int by = this.readSignedByte();
			int bz = this.readSignedByte();
			
			dkm.addRestrict(new Vector3D(bx, by, bz));
		}
		
		// virus battles
		ct = this.readSignedByte();
		Debug.print("Possible Virus Battles: " + ct);
		for(int i = 0; i < ct; i++) {
			int mx = this.readSignedByte();
			int my = this.readSignedByte();
			
			int v1x = this.readSignedByte();
			int v1y = this.readSignedByte();
			int v1type = this.readSignedByte();
			
			int v2x = this.readSignedByte();
			int v2y = this.readSignedByte();
			int v2type = this.readSignedByte();
			
			int v3x = this.readSignedByte();
			int v3y = this.readSignedByte();
			int v3type = this.readSignedByte();
			
			int v4x = this.readSignedByte();
			int v4y = this.readSignedByte();
			int v4type = this.readSignedByte();
		}
		
		// triggers
		ct = this.readSignedByte();
		Debug.print("Triggers: " + ct);
		for(int i = 0; i < ct; i++) {
			int tx = this.readSignedByte();
			int ty = this.readSignedByte();
			int tz = this.readSignedByte();
			String event = this.readString();
		}
		
		// ramps
		ct = this.readSignedByte();
		Debug.print("Ramps: " + ct);
		for (int i = 0; i < ct; i++) {
			int rx = this.readSignedByte();
			int ry = this.readSignedByte();
			int rz = this.readSignedByte();
			int rz2 = this.readSignedByte();
			int dir = this.readSignedByte();
			int length = this.readSignedByte();
			
			dkm.addRamp(new Ramp(new Vector3D(rx, ry, rz), dir, rz2));
		}

		// jackers
		ct = this.readSignedByte();
		Debug.print("Jack-in Slots: " + ct);
		for (int i = 0; i < ct; i++) {
			int jx = this.readSignedByte();
			int jy = this.readSignedByte();
			int jz = this.readSignedByte();
			String mapload = this.readString();
			Debug.print(mapload);
		}
		
		// background configuration
		int bgtype = this.readSignedByte();
		int bgtexid = this.readSignedByte();
		int bgspeed = this.readSignedByte();
		int xbgspeed = this.readSignedByte();
		int ybgspeed = this.readSignedByte();
		int bgr = this.readSignedByte();
		int bgg = this.readSignedByte();
		int bgb = this.readSignedByte();
		
		BackgroundRenderer bgrnd = null;
		if(bgtype == 0) {
			bgrnd = new BackgroundRenderer(bgr,bgg,bgb);
		} else if(bgtype == 1) {
			System.out.println(bgtexid);
			bgrnd = new BackgroundRenderer(bgtexid, bgspeed, xbgspeed, ybgspeed);
		}
		dkm.setBackgroundRenderer(bgrnd);
		
		// map config
		String mapname = this.readString();
		Debug.print(mapname);
		
		// quartus
		ct = this.readSignedByte();
		Debug.print("Half-block: " + ct);
		for(int i = 0; i < ct; i++) {
			int qx = this.readSignedByte();
			int qy = this.readSignedByte();
			int qz = this.readSignedByte();
			int qtype = this.readSignedByte();
		}
		
		// mdata
		ct = this.readSignedByte();
		Debug.print("Mystery Datas: " + ct);
		for (int i = 0; i < ct; i++) {
			int mx = this.readSignedByte();
			int my = this.readSignedByte();
			int mz = this.readSignedByte();
		}
		
		return dkm;
	}
	
}
