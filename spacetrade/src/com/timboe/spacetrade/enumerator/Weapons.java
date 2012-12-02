package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Rnd;

public enum Weapons {
	PewPew 			(WeaponClass.Missile,		1,	1000,	"Pew Pew Missile"),
	Cannon			(WeaponClass.MassDriver,	1,	1000,	"A Cannon"),
	Twinkler		(WeaponClass.Laser,			1,	1000,	"Twinkler"),
	Stinger			(WeaponClass.Missile,		2,	10000,	"Stinger Missile"),
	Slugger			(WeaponClass.MassDriver,	2,	10000,	"Slugger"),
	ChemicalLaser	(WeaponClass.Laser,			2,	10000,	"Chem Later"),
	UberBoom		(WeaponClass.Missile,		3,	100000,	"3rd Missile"),
	Railgun			(WeaponClass.MassDriver,	3,	100000,	"Rail Gun"),
	Sunbeam			(WeaponClass.Laser,			3,	100000,	"Sunbeam");
	
	WeaponClass weaponClass;
	String name;
	int cost;
	int level;
	
	private Weapons(WeaponClass _c, int _l, int _cst, String _n) {
		weaponClass = _c;
		name = _n;
		cost = _cst;
		level = _l;
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
	
	public int getLevel() {
		return level;
	}
	
	private static final List<Weapons> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();
	private static final Rnd rnd = new Rnd();

	public static Weapons random()  {
		return content.get( rnd.getRandI(size) );
	}

	public static Weapons random(int _level) { //Random of given level or -1
		assert (_level > 0 && _level <= 3);
		while (true) {
			Weapons _w = content.get( rnd.getRandI(size) );
			if (_w.getLevel() >= (_level - 1)) return _w;
		}
	}
	
	public static Weapons randomLevel(int _level) { //Random of given level
		assert (_level > 0 && _level <= 3);
		while (true) {
			Weapons _w = content.get( rnd.getRandI(size) );
			if (_w.getLevel() == _level) return _w;
		}
	}
}
