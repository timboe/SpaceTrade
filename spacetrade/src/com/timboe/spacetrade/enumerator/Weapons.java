package com.timboe.spacetrade.enumerator;

public enum Weapons {
	PewPew 			(WeaponClass.Missile,		1000,	"Pew Pew Missile"),
	Stinger			(WeaponClass.Missile,		10000,	"Stinger Missile"),
//	UberBoom		(WeaponClass.Missile,		100000,	"3rd Missile"),
	Cannon			(WeaponClass.MassDriver,	1000,	"A Cannon"),
	Slugger			(WeaponClass.MassDriver,	10000,	"Slugger"),
//	Railgun			(WeaponClass.MassDriver,	100000,	"Rail Gun"),
	Twinkler		(WeaponClass.Laser,			1000,	"Twinkler"),
	ChemicalLaser	(WeaponClass.Laser,			10000,	"Chem Later");
//	Sunbeam			(WeaponClass.Laser,			100000,	"Sunbeam");
	
	
	WeaponClass weaponClass;
	String name;
	int cost;
	
	private Weapons(WeaponClass _c, int _cst, String _n) {
		weaponClass = _c;
		name = _n;
		cost = _cst;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getName() {
		return name;
	}
	
	public WeaponClass getWeaponClass() {
		return weaponClass;
	}
	
	
}
