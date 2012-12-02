package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Rnd;

public enum Equipment {
	SmallShield		(5000,		1,	"Small Shield"),
	MiniRadiator	(5000,		1,	"Mini Rad"),
	Window			(5000,		1,	"Window"),
	MediumShield	(20000,		2,	"Small Shield"),
	MediRadiator	(20000,		2,	"Medi Rad"),
	ZMRadio			(20000,		2,	"ZMRadio"),
	LargeShield		(60000,		3,	"Large Shield"),
	MaxiRadiator	(60000,		3,	"Maxi Rad"),
	Cloak			(60000,		3,	"ZMRadio");
	//TargetingComputer;
	
	String name;
	int cost;
	int level;
	
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
