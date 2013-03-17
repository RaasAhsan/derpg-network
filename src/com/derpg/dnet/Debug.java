package com.derpg.dnet;

public class Debug {
	
	public static boolean enabled = false;
	
	public static void print(String s) {
		if(enabled) {
			System.out.println(s);
		}
	}
	
	public static void enable() {
		enabled = true;
	}
	
	public static void disable() {
		enabled = false;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}

}
