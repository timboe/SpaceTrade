package com.timboe.spacetrade.munitions;

import com.timboe.spacetrade.enumerator.WeaponClass;
import com.timboe.spacetrade.ship.Ship;

public class Weapon {

	protected final String name;
	protected final WeaponClass weaponClass;
	
	protected Weapon(String _name, WeaponClass _wc) {
		name = _name;
		weaponClass = _wc;
	}
	
	
	public void attackShip(Ship _s) {
		// TODO Auto-generated method stub
		
	}

	
}