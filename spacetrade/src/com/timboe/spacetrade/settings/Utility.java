package com.timboe.spacetrade.settings;

import java.util.Random;

public class Utility {
	
	public static final float CAMERA_WIDTH = 1000f;
	public static final float CAMERA_HEIGHT = 600f;

	private static Utility singleton = new Utility(); 
	
	public static Utility getUtility() {
		return singleton;
	}
	
	private Utility() {
	}
	
	private Random rand = new Random(0);
	
	public float getRandF() {
		return rand.nextFloat();
	}
	
	public int getRandI() {
		return rand.nextInt();
	}
	
	public int getRandI(int _n) {
		return rand.nextInt(_n);
	}
	
	public boolean getRandChance(float _c) {
		return (getRandF() < _c);
	}
	
}
