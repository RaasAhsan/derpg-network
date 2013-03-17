package com.jxware.client;

import java.nio.ByteBuffer;

public class Packet {

	public static final int POSITION = 0x01;
	public static final int NEW_CONNECTION = 0x02;
	public static final int CONNECTION_LEFT = 0x03;
	public static final int CHAT = 0x04;

	protected int header;
	protected ByteBuffer buffer;
	
	private int size;
	
	public Packet(int header) { // writing
		this.header = header;
		buffer = ByteBuffer.allocate(256);
		buffer.clear();
		buffer.put((byte) header);
		size = 1;
	}
	
	public Packet(ByteBuffer buffer) { // reading
		this.header = buffer.get();
		this.buffer = buffer;
	}
	
	public int getHeader() {
		return header;
	}
	
	public ByteBuffer getData() {
		buffer.flip();
		return buffer;
	}
	
	public void write(byte b) {
		buffer.put(b);
		size ++;
	}
	
	public byte read() {
		return buffer.get();
	}
	
	public void writeShort(short i) {
		buffer.putShort(i);
		size += 2;
	}
	
	public short readShort() {
		return buffer.getShort();
	}
	
	public void writeInteger(int i) {
		buffer.putInt(i);
		size += 4;
	}
	
	public int readInteger() {
		return buffer.getInt();
	}
	
	public void writeString(String s) {
		for(int i = 0; i < s.length(); i++) {
			buffer.putChar(s.charAt(i));
		}
		buffer.putChar((char) 0);
		size += s.length();
	}
	
	public String readString() {
		String s = "";
		
		while(true) {
			char c = buffer.getChar();
			if(c == 0x00)
				break;
			s += (char) c;
		}
		
		return s;
	}
	
	public void reset() {
		buffer.position(1);
	}
	
	public void debug() {
		
	}

	public void write(ByteBuffer data) {
		buffer.put(data);
	}

	public int getSize() {
		return size;
	}
	
}

