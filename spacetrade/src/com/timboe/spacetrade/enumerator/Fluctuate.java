package com.timboe.spacetrade.enumerator;

import com.timboe.spacetrade.utility.Utility;

public enum Fluctuate {
	downSmall(0.9f),
	downLarge(0.8f),
	downHuge(0.5f),
	upSmall(1.1f),
	upLarge(1.2f),
	upHuge(1.5f),
	chance(1f);
	
	float fluctuate;
	Fluctuate(float _f) {
		fluctuate = _f;
	}
	
	private static final Utility util = Utility.getUtility();
	public float get() {
		if (Math.abs(fluctuate - 1f) < 0.001f) {
			if (util.getRandChance(0.33f) == true) {
				return Fluctuate.downSmall.get();
			} else if (util.getRandChance(0.33f) == true) {
				return Fluctuate.upSmall.get();
			}
		}
		return fluctuate;
	}
}
