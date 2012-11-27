package com.timboe.spacetrade.enumerator;

public enum Weapons {
	PewPew 			(WeaponClass.Missile),
	Stinger			(WeaponClass.Missile),
	Cannon			(WeaponClass.MassDriver),
	Slugger			(WeaponClass.MassDriver),
	Twinkler		(WeaponClass.Laser),
	ChemicalLaser	(WeaponClass.Laser);
	
	
	WeaponClass weaponClass;
	
	private Weapons(WeaponClass _c) {
		weaponClass = _c;
	}
	
	
}
