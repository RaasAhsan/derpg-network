package com.derpg.dnet.readers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReader {

	public static BufferedImage loadImage(String fn) {
		try {
			return ImageIO.read(new ImageReader().getClass().getResourceAsStream("/" + fn));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
