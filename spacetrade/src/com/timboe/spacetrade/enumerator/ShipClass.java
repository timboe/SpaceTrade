package com.timboe.spacetrade.enumerator;

import com.timboe.spacetrade.world.Starmap;

public enum ShipClass {
	//	         Cargo	Level 	Weapn	Tech	Range	Hull	Heat	Cost	name
	Tiny		(5,		1,		1,		0,		30,		100,	80,		500,	"Tiny"),
	Starting	(10,	1,		1,		1,		20,		400,	200,	1000,	"Starting"),
	TradeA		(20,	1,		1,		1,		22,		1000,	600,	10000,	"TradeA"),
	AllroundA	(15,	1,		2,		1,		21,		1200,	1200,	12000,	"AllroundA"),
	FightA		(10,	1,		2,		1,		21,		1400,	2400,	15000,	"FightA"),
	FightB		(20,	2,		3,		2,		22,		2200,	5400,	60000,	"FightB"),
	TradeB		(40,	2,		2,		2,		24,		1700,	2600,	75000,	"TradeB"),
	TradeC		(60,	3,		2,		3,		26,		2400,	5000,	150000,	"TradeC"),
	AllroundC	(50,	3,		3,		3,		25,		2600,	6400,	160000,	"AllroundC"),
	FightC		(35,	3,		3,		3,		23,		2800,	8000,	175000,	"FightC");

	String name;
	int cargo;
	int weaponSlots;
	int techSlots;
	int range;
	int hull;
	int cost;
	int heat;
	int level;
	
	private ShipClass(int _c, int _level, int _ws, int _ts, int _r, int _h, int _heat, int _cst, String _n) {
		cargo = _c;
		level = _level;
		weaponSlots = _ws;
		techSlots = _ts;
		range = _r;
		hull = _h;
		cost = _cst;
		name = _n;
		heat = _heat;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getName() {
		return name;
	}
	
	public int getWeaponSlots() {
		return weaponSlots;
	}
	
	public int getTechSlots() {
		return techSlots;
	}
	
	public int getRange() {
		return (int) (range * Starmap.travelScale);
	}
	
	public int getMaxHull() {
		return hull;
	}
	
	public int getCost() {
		return cost;
	}

	public int getMaxCargo() {
		return cargo;
	}

	public int getMaxHeat() {
		return heat;
	}
}
