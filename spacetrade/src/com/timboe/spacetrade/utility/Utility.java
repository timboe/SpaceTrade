package com.timboe.spacetrade.utility;

import java.util.Random;

import com.timboe.spacetrade.render.RightBar;

public class Utility {
	
	public static final float CAMERA_WIDTH = 1280;
	public static final float CAMERA_HEIGHT = 800;

	public static final float GAME_HEIGHT = 800;
	public static final float GAME_WIDTH = 1200;
	public static final float GUI_WIDTH = 80;

	
	private static final Utility singleton = new Utility(); 
	private final Random rand;
	private final RightBar rightBar;
	private final AdLib adLib;

	public RightBar getRightBar() {
		return rightBar;
	}
	
	public final AdLib getAdLib() {
		return adLib;
	}
	
	
	public static Utility getUtility() {
		return singleton;
	}
	
	private Utility() {
		rand = new Random(0);
		adLib = new AdLib();
		rightBar = new RightBar();
	}
	
	
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
