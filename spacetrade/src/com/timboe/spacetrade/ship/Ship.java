package com.timboe.spacetrade.ship;

import java.util.ArrayList;

import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipProperty;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.munitions.Weapon;

public class Ship {
	
	ShipProperty property;
	ArrayList<Weapon> weapon_loadout = new ArrayList<Weapon>();
	private int range = 100;
	private float acceleration = 5;
	private int cargo = 10;
	private int worth = 10000;
	
	public Ship() {
	}
	
	public Ship(ShipTemplate _st) {
		
	}
	
	public Ship(ShipTemplate _st, ShipClass _sc) {
		
	}
	
	public void fireAt(Ship _s) {
		for (Weapon _w : weapon_loadout) {
			_w.attackShip(_s);
		}
	}
	
	public int getRange() {
		return range;
	}
	
	public float getAcc() {
		return acceleration;
	}
	
	public int getCargo() {
		return cargo;
	}

	public int getWorth() {
		// TODO add equipment
		return worth;
	}
}
