package com.derpg.dnet.readers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class TextureReader extends Reader {
	
	public TextureReader() {
		
	}
	
	public BufferedImage[] read(String filename) {
		try {
			this.loadData(filename);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return this.readTextureFile();
	}
	
	private BufferedImage[] readTextureFile() {
		int ct = this.readSignedByte();
		
		BufferedImage[] textures = new BufferedImage[ct];
		
		for(int i = 1; i < ct; i++) {
			String tfn = this.readString();
			
			tfn = tfn.substring(1);
			tfn = tfn.replaceAll("\\\\", "/");
			tfn = tfn.toLowerCase();
			textures[i-1] = ImageReader.loadImage(tfn);
		}
		
		return textures;
	}
	
}
