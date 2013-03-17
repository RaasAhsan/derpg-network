package com.derpg.dnet.event;

public class Quest {

	private int id;
	private String qname;
	private int position;
	private boolean complete;
	
	public Quest(int id, String name) {
		this.id = id;
		this.qname = name;
		position = 0;
		complete = false;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void next() {
		position++;
	}
	
	public int getID() {
		return id;
	}
	
	public String getQuestName() {
		return qname;
	}
	
}
