package com.derpg.dnet;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JPanel;

import com.derpg.dnet.entity.External;
import com.derpg.dnet.entity.Player;
import com.derpg.dnet.entity.NPC;
import com.derpg.dnet.event.EventRunner;
import com.derpg.dnet.event.NPCMapObject;
import com.derpg.dnet.event.QuestController;
import com.derpg.dnet.gfx.Fade;
import com.derpg.dnet.gfx.Renderer;
import com.derpg.dnet.math.Vector3D;
import com.derpg.dnet.network.Protocol;
import com.derpg.dnet.readers.DKMReader;
import com.derpg.dnet.readers.TextureReader;
import com.derpg.net.dialogengine.DialogEngine;
import com.jxware.client.Client;
import com.jxware.client.Packet;

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {
	
	private Thread thread;
	
	private BufferedImage offscreen;
	private Graphics offscrgfx;
	
	private BufferedImage sshot;
	private Graphics sshotgfx;
	
	private Input input;
	public static Console console;
	
	public static int ticks = 0;
	public static Client client;

	public static float interp;

	public static String chat = "";
	
	private Player p;
	
	private DKM dkmmap;
	private View view;
	private DialogEngine dialogengine;
	private Fade currentfade;
	private int fadetype;
	private QuestController controller;
	
	private int fps = 60;
	private int frameCount = 0;
	
	public Game(String username) {
		this.setPreferredSize(new Dimension(512, 384));
		
		offscreen = new BufferedImage((int) 256, (int) 192, BufferedImage.TYPE_INT_RGB);
		offscrgfx = offscreen.getGraphics();
		
		sshot = new BufferedImage((int) 256, (int) 192, BufferedImage.TYPE_INT_RGB);
		sshotgfx = sshot.getGraphics();
		
		input = new Input(p);
		this.addKeyListener(input);
		
		controller = new QuestController();
		dialogengine = new DialogEngine(input);
		EventRunner.init(this);
		
		p = new Player(new Vector3D(200, 200), input, dialogengine);
		view = new View(p, new Vector3D(256, 192));
		
		client = new Client("108.79.20.223", 1338);
		Packet login = new Packet(Protocol.AUTHENTICATE);
		login.writeString(username);
		client.write(login);
		
		TextureReader tr = new TextureReader();
		BufferedImage[] textures = tr.read("/texture.dxen");
		DKM.setTexturePack(textures);
		DKM.loadBackgrounds();
		
		DKMReader reader = new DKMReader();
		dkmmap = reader.read("central1.dkm");
		dkmmap.setPlayer(p);
		
		try {
			URLClassLoader classLoader = URLClassLoader
					.newInstance(new URL[] { new URL("file:./mapevents/centralarea1.jar") });
		    
		    Class<?> clazz = classLoader.loadClass("com.derpg.dnet.mapevents.CentralArea1");
		    NPCMapObject nmap = (NPCMapObject) clazz.newInstance();
		    dkmmap.nmap = nmap;
		    
		   nmap.init(dkmmap, EventRunner.eventRunner, this.controller);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		p.setPosition(dkmmap.begin);
		p.setMap(dkmmap);
		
		//dkmmap.spawn(new NPC(new Vector3D(544+32, 616), null));
		
		currentfade = null;
		
		console = new Console(this);
	}

	public void addNotify() {
		super.addNotify();
		
		this.requestFocus();
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		final double GAME_HERTZ = 60.0;
		
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		
		final int MAX_UPDATES_BEFORE_RENDER = 1;
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		// If we are able to get as high as this FPS, don't render again.
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
		
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);
		
		
		while (true) {
			double now = System.nanoTime();
			int updateCount = 0;

			// Do as many game updates as we need to, potentially playing
			// catchup.
			while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
					&& updateCount < MAX_UPDATES_BEFORE_RENDER) {
				
				if(currentfade == null) {
					update();
				} else {
					currentfade.update();
					if(currentfade.done()) {
						currentfade = null;
						if(fadetype == 0) {
							currentfade = new Fade(8, new Color(0,0,0), 1);
							fadetype = 1;
							
							dkmmap = DKM.gotoMap(p, dkmmap);
						}
					}
				}
				
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				updateCount++;
			}

			if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}

			float interpolation = Math.min(1.0f,
					(float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
			Game.interp = interpolation;
			this.render(offscrgfx);
			
			Graphics g = this.getGraphics();
			g.drawImage(offscreen, 0, 0, this.getWidth(), this.getHeight(), 0,
					0, this.getWidth() / 2, this.getHeight() / 2, null);
			g.dispose();
			
			lastRenderTime = now;
			
			if(!p.getToMap().equals("")) {
				if(currentfade == null) {
					currentfade = new Fade(8, new Color(0, 0, 0), 0);
					fadetype = 0;
					
					
				}
			}

			int thisSecond = (int) (lastUpdateTime / 1000000000);
			if (thisSecond > lastSecondTime) {
				fps = frameCount;
				frameCount = 0;
				lastSecondTime = thisSecond;
			}

			while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
					&& now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
				Thread.yield();

				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}

				now = System.nanoTime();
			}
		}
	}
	
	public void update() {
		dkmmap.update();
		dialogengine.update();
		EventRunner.updateRunner();
		
		if(!Game.chat.equals("")) {
			System.out.println(Game.chat);
			Packet chat = new Packet(Protocol.CHAT);
			chat.writeString(Game.chat);
			Game.client.write(chat);
			
			console.log("You: " + Game.chat, 3);
			
			Game.chat = "";
		}
		
		this.updateMultiplayer();
		
		ticks++;
		if(ticks % 60 == 0) {
			ticks = 0;
		}
	}
	
	public void updateMultiplayer() {
		if(Game.ticks % 5 == 0) {
			Packet pos = new Packet(Protocol.POSITION);
			pos.writeInteger(p.getPosition().getX());
			pos.writeInteger(p.getPosition().getY());
			if(p.isRunning()) {
				pos.writeShort((short) 1);
			} else {
				pos.writeShort((short) 0);
			}
			pos.write((byte) p.getDirection());
			client.write(pos);
		}
		
		Packet p = client.receive();
		if(p != null) {
			if(p.getHeader() == Protocol.NEW_CONNECTION) {
				int id = p.readInteger();
				dkmmap.spawn(new External(id, new Vector3D(0, 0)));
			} else if(p.getHeader() == Protocol.CHAT) {
				console.log(p.readString(), p.read());
			} else if(p.getHeader() == Protocol.POSITION) {
				int id = p.readInteger();
				int x = p.readInteger();
				int y = p.readInteger();
				short run = p.readShort();
				byte dir = p.read();
				
				External e = (External) dkmmap.getPlayerWithID(id);
				e.ipos = 0;
				e.setDirection(dir);
				if(run == 0)
					e.setRunning(false);
				else
					e.setRunning(true);
				e.pushPosition(new Vector3D(x, y));
			} else if(p.getHeader() == Protocol.ROOM_CHANGE) {
				int id = p.readInteger();
				int change = p.read();
				
				if(change == 0) {
					dkmmap.removePlayer(id);
				} else {
					dkmmap.spawn(new External(id, new Vector3D(0, 0)));
				}
			}
		}
		
		client.update();
	}
	
	public void render(Graphics g) {
		super.paint(g);
		
		Renderer renderer = new Renderer(g, view);
		dkmmap.render(renderer);
		dialogengine.render(renderer);
		
		if(currentfade != null)
			currentfade.render(renderer);
	}
	
	public BufferedImage getScreen() {
		render(sshotgfx);
		return sshot;
	}

	public DialogEngine getDialogEngine() {
		return dialogengine;
	}

	public QuestController getQuestController() {
		return controller;
	}

}
