package com.derpg.dnet.event;

import com.derpg.dnet.DKM;

public abstract class NPCMapObject {

	protected DKM dkm;
	protected EventRunner eventrunner;
	protected QuestController questcon;
	
	public NPCMapObject() {
		
	}
	
	public abstract void init(DKM dkm, EventRunner eventrunner, QuestController questcon);
	
	public abstract void interact(int npcid);
	
}
