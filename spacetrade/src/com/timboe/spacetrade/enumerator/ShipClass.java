package com.timboe.spacetrade.enumerator;

import com.timboe.spacetrade.world.Starmap;

public enum ShipClass {
	//	         Cargo	Level 	Weapn	Tech	Range	Hull	Heat	Cooldn	Cost	name
	Tiny		(5,		1,		1,		0,		20,		25,		10,		5,		500,	"Tiny"),
	Starting	(10,	1,		1,		1,		14,		100,	20,		10,		10000,	"Starting"),
	TradeA		(20,	1,		1,		1,		17,		100,	35,		10,		25000,	"TradeA"),
	AllroundA	(15,	1,		2,		1,		15,		100,	50,		15,		32000,	"AllroundA"),
	FightA		(10,	1,		2,		1,		13,		125,	60,		20,		37000,	"FightA"),
	FightB		(20,	2,		3,		2,		15,		150,	100,	40,		70000,	"FightB"),
	TradeB		(40,	2,		2,		2,		16,		125,	40,		25,		100000,	"TradeB"),
	TradeC		(60,	3,		2,		3,		15,		175,	70,		35,		240000,	"TradeC"),
	AllroundC	(50,	3,		3,		3,		14,		200,	125,	55,		280000,	"AllroundC"),
	FightC		(35,	3,		3,		3,		13,		200,	200,	70,		300000,	"FightC");

	String name;
	int cargo;
	int weaponSlots;
	int techSlots;
	int range;
	int hull;
	int cost;
	int heat;
	int level;
	int cooldown;
	
	private ShipClass(int _c, int _level, int _ws, int _ts, int _r, int _h, int _heat, int _cool, int _cst, String _n) {
		cargo = _c;
		level = _level;
		weaponSlots = _ws;
		techSlots = _ts;
		range = _r;
		hull = _h;
		cost = _cst;
		name = _n;
		heat = _heat;
		cooldown = _cool;
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

	public int getCooldown() {
		return cooldown;
	}
}
