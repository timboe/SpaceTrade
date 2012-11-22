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
	
	private static final Random rand = new Random(0);


	public static float getRandF() {
		return rand.nextFloat();
	}
	
	public static int getRandI() {
		return rand.nextInt();
	}
	
	public static int getRandI(int _n) {
		return rand.nextInt(_n);
	}
	
	public static float getRandG(float _m, float _s) {
		return (float) (_m + (rand.nextGaussian() * _s));
	}
	
	public static boolean getRandChance(float _c) {
		return (getRandF() < _c);
	}

	public static float acosh(float _v) {
		return (float) Math.log(_v + Math.sqrt(_v * _v - 1));
	}
	
	
}
