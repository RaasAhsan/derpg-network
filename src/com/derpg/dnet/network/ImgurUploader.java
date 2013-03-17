package com.derpg.dnet.network;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class ImgurUploader {

	public static final String api_key = "54c25d29968ab5001c317754f222914f";
	public static final String imgur_loc = "http://api.imgur.com/2/upload.json";
	
	private BufferedImage screenshot;
	
	public ImgurUploader(BufferedImage screen) {
		this.screenshot = screen;
	}
	
	public String uploadImage(String title) throws IOException, AWTException {
		ImageIO.write(screenshot, "png", new File("dnet_screenshot.png"));
		RandomAccessFile raf = new RandomAccessFile(new File("dnet_screenshot.png"), "r");
		
		byte[] data = new byte[(int) raf.length()];
		raf.read(data);
		
		raf.close();
		
		String params = 
				"key=" + URLEncoder.encode("54c25d29968ab5001c317754f222914f", "UTF-8") 
				+ "&image=" + URLEncoder.encode(Base64.encodeBase64String(data), "UTF-8")
				+ "&title=" + URLEncoder.encode(title, "UTF-8");
		
		HttpURLConnection imgur = (HttpURLConnection) new URL("http://api.imgur.com/2/upload.json").openConnection();
		imgur.setDoOutput(true);
		imgur.setRequestMethod("POST");
		imgur.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		imgur.setRequestProperty("Content-Length", "" + params.length());
		
		DataOutputStream writer = new DataOutputStream(imgur.getOutputStream());
		writer.writeBytes(params);
		writer.flush();
		writer.close();
		
		InputStream is;
		if (imgur.getResponseCode() >= 400) {
		    is = imgur.getErrorStream();
		} else {
		    is = imgur.getInputStream();
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		reader.close();
		
		String link = response.toString().substring(response.toString().indexOf("{\"original\":\""));
		link = link.split(",")[0];
		link = link.substring(link.indexOf("http:"));
		link = link.replaceAll("\"", "");
		link = link.replaceAll("\\\\", "");
		System.out.println("Uploaded image: " + link);
		
		imgur = null;
		
		return link;
	}
	
}
