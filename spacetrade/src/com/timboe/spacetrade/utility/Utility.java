package com.timboe.spacetrade.utility;

import java.util.Random;

import com.timboe.spacetrade.SpaceTrade;
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
	private SpaceTrade spaceTrade;
	private int starDate = 0;

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
	
	
	public SpaceTrade getSpaceTrade() {
		return spaceTrade;
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
	
	public void setSpaceTrade(SpaceTrade _st) {
		spaceTrade = _st;
	}
	
}
