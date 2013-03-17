package com.jxware.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Client {

	int port;
	SocketChannel sock;
	
	private List<Packet> buffer;
	
	public Client(String host, int port) {
		this.port = port;
		try {
			sock = SocketChannel.open();
			
			sock.connect(new InetSocketAddress(host, port));
			sock.configureBlocking(false);
			
			while(!sock.finishConnect()) {
				// wait until connect, implement error checking later
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		buffer = new ArrayList<Packet>();
	}
	
	public void write(Packet p) {
		/*try {
			sock.write(p.getData());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		buffer.add(p);
	}

	public int getBufferSize() {
		int i = 0;
		
		for(Packet p : buffer) {
			i += p.getSize();
		}
		
		return i;
	}
	
	public void update() {
		if(buffer.size() > 0) {
			Packet send = new Packet(0);
			send.writeShort((short) this.buffer.size());
			for(Packet p : buffer) {
				send.write(p.getData());
			}
			
			try {
				sock.write(send.getData());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			buffer.clear();
		}
	}
	
	public Packet receive() {
		ByteBuffer buf = ByteBuffer.allocate(256);
		
		try {
			int i = sock.read(buf);
			if(i <= 0) {
				return null;
			}
			buf.flip();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new Packet(buf);
	}
	
	public void close() {
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SocketChannel getSocket() {
		return sock;
	}
	
}

