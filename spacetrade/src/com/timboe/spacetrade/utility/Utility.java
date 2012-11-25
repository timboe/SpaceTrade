package com.timboe.spacetrade.utility;

import java.util.Random;

public class Utility {	
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

	public static void setSeed(int x) {
		rand.setSeed(x);
	}
	
	
}
