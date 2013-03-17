package com.derpg.dnet.event;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class EventObject {
	
	private ByteBuffer object;
	private List<Command> commands;

	private EventObject(ByteBuffer object) {
		this.object = object;
		commands = new ArrayList<Command>();
	}
	
	public ByteBuffer getEventData() {
		return object;
	}
	
	// this method just converts everything to Command form
	public void translate() { 
		
	}
	
	public static EventObject loadEventFile(String event) throws IOException {
		RandomAccessFile f = new RandomAccessFile(event, "r");
		byte[] b = new byte[(int)f.length()];
		f.read(b);
		f.close();
		
		ByteBuffer buffer = ByteBuffer.allocate((int) f.length());
		buffer.put(b);
		
		EventObject obj = new EventObject(buffer);
		obj.translate();
		
		return obj;
	}
	
	public List<Command> getCommands() {
		return commands;
	}
	
}
