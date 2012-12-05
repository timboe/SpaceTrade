package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Rnd;

public enum Weapons {
											//Lev	Heat	Dmg		Cst		
	PewPew 			(WeaponClass.Missile,		1,	2,		3,		2500,	"Pew Pew Missile"),
	Cannon			(WeaponClass.MassDriver,	1,	12,		12,		1100,	"A Cannon"),
	Twinkler		(WeaponClass.Laser,			1,	6,		7,		1700,	"Twinkler"),
	Stinger			(WeaponClass.Missile,		2,	6,		13,		26000,	"Stinger Missile"),
	Slugger			(WeaponClass.MassDriver,	2,	27,		18,		15000,	"Slugger"),
	ChemicalLaser	(WeaponClass.Laser,			2,	18,		15,		21000,	"Chem Later"),
	UberBoom		(WeaponClass.Missile,		3,	14,		25,		41000,	"3rd Missile"),
	Railgun			(WeaponClass.MassDriver,	3,	61,		35,		32000,	"Rail Gun"),
	Sunbeam			(WeaponClass.Laser,			3,	37,		27,		37000,	"Sunbeam");
	
	WeaponClass weaponClass;
	String name;
	int cost;
	int level;
	int heat;
	int damage;
	float bonus = 0.25f; //TODO, tweak me?
	
	private Weapons(WeaponClass _c, int _l, int _heat, int _dmg, int _cst, String _n) {
		weaponClass = _c;
		name = _n;
		cost = _cst;
		level = _l;
		heat = _heat;
		damage = _dmg;
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

	public int getHeat() {
		return heat;
	}

	public int getDamage(ShipProperty _sp) {
		return Math.round( damage * getDamageMod(_sp) );
	}
	
	public float getDamageMod(ShipProperty _sp) {
		if (weaponClass.getStrongAgainst(_sp)	== true) return (1.f + bonus);
		if (weaponClass.getWeakAgainst(_sp)		== true) return (1.f - bonus);
		return 1f;
	}
}
