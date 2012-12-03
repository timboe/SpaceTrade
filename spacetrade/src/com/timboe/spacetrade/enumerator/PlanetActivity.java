package com.timboe.spacetrade.enumerator;

public enum PlanetActivity {
	None		(0.00f),
	Few			(0.02f),
	Some		(0.04f),
	Many		(0.06f),
	Swarms		(0.08f);
	
	float chance;
	private PlanetActivity(float _chance) {
		chance = _chance;
	}
	
	public float getChance() {
		return chance;
	}
}
