package com.timboe.spacetrade.enumerator;

import com.timboe.spacetrade.world.Starmap;

public enum ShipClass {
	//	         Cargo 	Weapn	Tech	Range	Hull	Cost	name
	Tiny		(5,		1,		0,		30,		100,	500,	"Tiny"),
	Starting	(10,	1,		1,		20,		400,	1000,	"Starting"),
	TradeA		(20,	1,		1,		22,		1000,	10000,	"TradeA"),
	TradeB		(40,	2,		2,		24,		1700,	75000,	"TradeB"),
	TradeC		(60,	2,		3,		26,		2400,	150000,	"TradeC"),
	FightA		(10,	2,		1,		21,		1400,	15000,	"FightA"),
	FightB		(20,	3,		2,		22,		2200,	60000,	"FightB"),
	FightC		(35,	3,		3,		23,		2800,	175000,	"FightC"),
	AllroundA	(15,	2,		1,		21,		1200,	12000,	"AllroundA"),
	AllroundC	(50,	3,		3,		25,		2600,	160000,	"AllroundC");

	String name;
	int cargo;
	int weaponSlots;
	int techSlots;
	int range;
	int hull;
	int cost;
	
	
	private ShipClass(int _c, int _ws, int _ts, int _r, int _h, int _cst, String _n) {
		cargo = _c;
		weaponSlots = _ws;
		techSlots = _ts;
		range = _r;
		hull = _h;
		cost = _cst;
		name = _n;
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
		return (int) (range * Starmap.toLightYears);
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
}
