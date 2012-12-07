package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Rnd;

public enum Equipment {
	SmallShield		(5000,		1,	"Mini Shield"),
	MiniRadiator	(5000,		1,	"Heat Fins"),
	ExtraTank		(2000,		1,	"Anti Matter Tank"),
	Window			(5000,		1,	"Window"), //TODO
	CargoBays		(5000,		1,	"5 Extra Cargo Bays"),
	MediumShield	(20000,		2,	"Strong Shield"),
	MediRadiator	(20000,		2,	"Heat Banks"),
	DuckTape		(15000,		2,	"Struck Tape"),
	ZMRadio			(20000,		2,	"ZMRadio"), //TODO
	LargeShield		(60000,		3,	"Giga Shield"),
	AutoRepair		(50000,		3,	"Auto Repair Bot"),
	MaxiRadiator	(60000,		3,	"Fractal Radiator"),
	MaxCargoBays	(20000,		3,	"15 Extra Cargo Bays");
	//Cloak			(60000,		3,	"Photonic Cloak"); //TODO
	//TargetingComputer;
	
	String name;
	int cost;
	int level;
	
	//modifiers:
	
	public float getScanChance(int _l) {
		if (_l == 1) {
			switch (this) {
			case Window: return 0.9f;
			case ZMRadio: return 0.99f;
			default: return 0f;
			}
		} else if (_l == 2) {
			switch (this) {
			case Window: return 0.6f;
			case ZMRadio: return 0.9f;
			default: return 0f;
			}
		} else { //_l == 3
			switch (this) {
			case Window: return 0.4f;
			case ZMRadio: return 0.8f;
			default: return 0f;
			}
		}
	}
	
	
	public int getHeatLoss() {
		switch (this) {
		case MiniRadiator: return 15;
		case MediRadiator: return 25;
		case MaxiRadiator: return 70;
		default: return 0;
		}
	}
	
	public int getHeatCapacity() {
		switch (this) {
		case MiniRadiator: return 25;
		case MediRadiator: return 75;
		case MaxiRadiator: return 35;
		default: return 0;
		}
	}

	public int getShielding() {
		switch (this) {
		case SmallShield: return 25;
		case MediumShield: return 50;
		case LargeShield: return 100;
		default: return 0;
		}
	}
	
	public int getShieldRegen() {
		switch (this) {
		case SmallShield:  return 1 + rnd.getRandI(1);
		case MediumShield: return 2 + rnd.getRandI(3);
		case LargeShield: return 3 + rnd.getRandI(5);
		default: return 0;
		}
	}
	
	public int getHullRegen() {
		switch (this) {
		case AutoRepair: return 3 + rnd.getRandI(7);
		default: return 0;
		}
	}
	
	public int getExtraCargo() {
		switch (this) {
		case CargoBays: return 5;
		case MaxCargoBays: return 15;
		default: return 0;
		}
	}
	
	public int getExtraHull() {
		switch (this) {
		case DuckTape: return 65;
		default: return 0;
		}
	}
	
	public int getExtraRange() {
		switch (this) {
		case ExtraTank: return 5;
		default: return 0;
		}
	}
	
	private Equipment (int _cst, int _l, String _n) {
		name = _n;
		cost = _cst;
		level = _l;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getName() {
		return name;
	}
	
	private static final List<Equipment> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();
	private static final Rnd rnd = new Rnd();

	public static Equipment random()  {
		return content.get( rnd.getRandI(size) );
	}
	
	//TODO put some code in for different classes getting appropriate stuff
	
	public static Equipment random(int _level) {
		assert (_level > 0 && _level <= 3);
		while (true) {
			Equipment _e = content.get( rnd.getRandI(size) );
			if (_e.getLevel() >= (_level - 1)) return _e;
		}
	}
	
	public static Equipment randomLevel(int _level) { //Random of given level
		assert (_level > 0 && _level <= 3);
		while (true) {
			Equipment _e = content.get( rnd.getRandI(size) );
			if (_e.getLevel() == _level) return _e;
		}
	}




	
	
}
