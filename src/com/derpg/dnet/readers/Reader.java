package com.derpg.dnet.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class Reader {
	
	protected ByteBuffer buffer;

	public void loadData(String filename) throws URISyntaxException {
		InputStream is = null;
		
		is = this.getClass().getResourceAsStream(filename);
		
		ArrayList<Integer> bytes = new ArrayList<Integer>();
		
		int c;
        try {
			while ((c = is.read()) != -1) {
				 bytes.add(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        buffer = ByteBuffer.allocate(bytes.size());
        for(int i = 0; i < bytes.size(); i++) {
        	buffer.put((byte)bytes.get(i).intValue());
        }
        
        buffer.position(0);
		
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected String readString() {
		String s = "";
		
		int len = ((buffer.get() & 0xFF) * 256) + (buffer.get() & 0xFF);
		
		for(int i = 0; i < len; i++) {
			s += (char)buffer.get();
		}
		
		return s;
	}

	protected int readSignedByte() {
		int n = buffer.get() & 0xFF;
		int pt = buffer.get() & 0xFF;
		int rem = buffer.get() & 0xFF;
		
		int res = (256 * pt) + rem;
		
		if(n == 0) {
			return res;
		}
		return -res;
	}
	
}
