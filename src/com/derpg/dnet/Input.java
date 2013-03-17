package com.derpg.dnet;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.derpg.dnet.entity.Player;

public class Input implements KeyListener {

	public static final int KEY_X = KeyEvent.VK_X;
	public static final int KEY_A = KeyEvent.VK_A;
	public static final int KEY_Z = KeyEvent.VK_Z;
	public static int KEY_UP = KeyEvent.VK_UP;
	public static int KEY_DOWN = KeyEvent.VK_DOWN;
	public static int KEY_LEFT = KeyEvent.VK_LEFT;
	public static int KEY_RIGHT = KeyEvent.VK_RIGHT;
	public static int KEY_F3 = KeyEvent.VK_F3;
	
	private Player local;

	private boolean[] keysPressed = new boolean[256];
	private boolean[] keysReleased = new boolean[256];

	private boolean anyKeyPressed = false;

	public Input(Player l) {
		this.local = l;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyId = (e.getKeyCode() & 255);
		keysPressed[keyId] = true;
		keysReleased[keyId] = false;
		anyKeyPressed = true;
		
		if(keyId == KEY_F3) {
			Game.console.setVisible(!Game.console.isVisible());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyId = (e.getKeyCode() & 255);
		keysPressed[keyId] = false;
		keysReleased[keyId] = true;
		anyKeyPressed = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public boolean isKeyPressed(int key) {
		return keysPressed[key];
	}

	public boolean isKeyReleased(int key) {
		return keysReleased[key];
	}

	public boolean isAnyKeyPressed() {
		return anyKeyPressed;
	}

	public void resetKeys() {
		keysReleased = new boolean[256];
	}

}
