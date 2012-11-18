package com.timboe.spacetrade.utility;

import java.util.Random;

import com.badlogic.gdx.Gdx;

public class Utility {
	
	public static final float CAMERA_WIDTH = 1280;
	public static final float CAMERA_HEIGHT = 800;
	
	public static int getGameWidthPix() {
		return (int) (GAME_WIDTH * ((float)Gdx.graphics.getWidth() / (float)CAMERA_WIDTH));
	}
	
	public static int getGameHeightPix() {
		return (int) (GAME_HEIGHT * ((float)Gdx.graphics.getHeight() / (float)CAMERA_HEIGHT));
	}

	public static final float GAME_HEIGHT = 800;
	public static final float GAME_WIDTH = 1200;
	public static final float GUI_WIDTH = 80;
	
	private static final Utility singleton = new Utility(); 
	private final Random rand;
	private int starDate = 0;
	
	public static Utility getUtility() {
		return singleton;
	}
	
	private Utility() {
		rand = new Random(0);
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
	
	public float getRandG(float _m, float _s) {
		return (float) (_m + (rand.nextGaussian() * _s));
	}
	
	public boolean getRandChance(float _c) {
		return (getRandF() < _c);
	}
	
	public int getStarDate() {
		return starDate;
	}
	
	public void newYear(int _n_years) {
		starDate += _n_years;
	}
	
	
	
}
