package com.timboe.spacetrade.enumerator;

public enum Equipment {
	//Shields
	SmallShield		(10000,		"Small Shield"),
	LargeShield		(100000,	"Large Shield"),
	//Heat disperser
	MiniRadiator	(10000,		"Mini Rad"),
	MaxiRadiator	(100000,	"Maxi Rad"),
	//
	Window			(50000,		"Window"),
	ZMRadio			(50000,		"ZMRadio");
	//
	//TargetingComputer;
	
	String name;
	int cost;
	
	private Equipment (int _cst, String _n) {
		name = _n;
		cost = _cst;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getName() {
		return name;
	}
	
	
}
