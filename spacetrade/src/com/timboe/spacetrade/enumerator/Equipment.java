package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Rnd;

public enum Equipment {
	SmallShield		(10000,		1,	"Small Shield"),
	MiniRadiator	(10000,		1,	"Mini Rad"),
	Window			(10000,		1,	"Window"),
	MediumShield	(50000,		2,	"Small Shield"),
	MediRadiator	(50000,		2,	"Medi Rad"),
	ZMRadio			(50000,		2,	"ZMRadio"),
	LargeShield		(100000,	3,	"Large Shield"),
	MaxiRadiator	(100000,	3,	"Maxi Rad"),
	Cloak			(100000,	3,	"ZMRadio");
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
	
	
}
