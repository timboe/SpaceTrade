package com.timboe.spacetrade.enumerator;

import com.timboe.spacetrade.utility.Rnd;

public enum Fluctuate {
	downSmall(1f), //0.1
	downLarge(1f), //1.8
	downHuge(1f), //0.5
	upSmall(1f), //1.1
	upLarge(1f), //1.2
	upHuge(1f), //1.5
	chance(1f); //THAR BE A BUG IN HERE SOMEWHERE //TODO
	
	float fluctuate;
	Fluctuate(float _f) {
		fluctuate = _f;
	}
	
	private static final Rnd rnd = new Rnd();
	public float get() {
//		if (Math.abs(fluctuate - 1f) < 0.001f) { //Chance
//			if (rnd.getRandChance(0.33f) == true) {
//				return Fluctuate.downSmall.get();
//			} else if (rnd.getRandChance(0.33f) == true) {
//				return Fluctuate.upSmall.get();
//			}
//		}
		return fluctuate;
	}
}
