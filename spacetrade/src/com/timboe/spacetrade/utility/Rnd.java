package com.timboe.spacetrade.utility;

import java.util.Random;

import com.timboe.spacetrade.SpaceTrade;

public class Rnd  {	
	private final Random rand = new Random(SpaceTrade.masterSeed);

	public Rnd() {
	}
	
	public Rnd(int i) {
		setSeed(i);
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

	public void setSeed(int x) {
		rand.setSeed(x);
	}
	
}
