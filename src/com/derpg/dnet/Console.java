package com.derpg.dnet;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.derpg.dnet.network.ImgurUploader;
import com.derpg.dnet.network.Protocol;
import com.jxware.client.Packet;

@SuppressWarnings("serial")
public class Console extends JFrame implements KeyListener {
	
	private JTextPane log;
	private JTextField input;
	private JScrollPane pane;
	
	private Game game;

	public static final Color[] types = new Color[] { Color.BLACK, Color.RED, Color.GREEN, Color.YELLOW };
	
	public Console(Game g) {
		super("The Console");
		this.setSize(550, 330);
		
		this.game = g;
		
		JPanel console = new JPanel();
		
		console.setLayout(new BorderLayout());
		
		log = new JTextPane();
		log.setEditable(false);
		log.setBackground(Color.GRAY);
		log.setForeground(Color.WHITE);
		
		pane = new JScrollPane(log);
		console.add(pane, BorderLayout.CENTER);
		
		input = new JTextField(100);
		input.setBackground(Color.GRAY);
		input.setForeground(Color.WHITE);
		console.add(input, BorderLayout.SOUTH);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		console.setBorder(border);
		
		this.getContentPane().add(console);
		this.setVisible(false);
		
		input.addKeyListener(this);
		this.setFocusable(true);
		
		this.log("Welcome to The Console!", 0);
		this.log("You can enter a command here that will be processed by the server.", 0);
		this.log("If you need help, type /help in the text area below.", 0);
	}
	
	public void addNotify() {
		super.addNotify();
		
		this.requestFocus();
	}
	
	public void handle(String l) {
		try {
			this.handleCommand(l);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void log(String c, int type) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			
			Document doc = log.getDocument();
			AttributeSet aset = StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, types[type]);
			log.getDocument().insertString(doc.getLength(), "[" + dateFormat.format(date) + "]: " + c + "\n", aset);
			log.setCaretPosition(log.getDocument().getLength());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleCommand(String command) throws BadLocationException {
		if(!command.startsWith("/")) {
			Game.chat = command;
		} else {
			String[] args = command.replace("/", "").split(" ");
			if(args[0].equals("clear") || args[0].equals("cls")) {
				log.setText("");
			} else if(args[0].equals("invis")) {
				this.log("You are now invisible!", 2);
			} else if(args[0].equals("screenshot")) {
				this.log("Capturing the DERPG Network screen...", 3);
				
				String title = "DERPG Network";
				if(args.length > 1) {
					title = args[1];
				}
				
				ImgurUploader imgup = new ImgurUploader(game.getScreen());
				try {
					this.log("Uploading image...", 3);
					this.log("The uploaded image is at: " + imgup.uploadImage(title),2);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else if(args[0].equals("quit")) {
				this.log("Logging out of DERPG Network...", 1);
				this.log("Closing...", 1);
				
				System.exit(0);
			} else {
				this.log("Unknown command \"" + command + "\". Use /help please.", 1);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
			if(input.getText().length() > 0) {
				String in = input.getText();
				this.handle(in);
				
				input.setText("");
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
}
