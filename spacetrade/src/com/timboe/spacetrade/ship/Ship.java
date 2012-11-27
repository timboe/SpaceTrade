package com.timboe.spacetrade.ship;

import java.util.ArrayList;

import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipProperty;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.equipment.Equipment;
import com.timboe.spacetrade.equipment.Weapon;

public class Ship {
	
	ShipProperty property;
	ShipClass shipClass;
	ArrayList<Weapon> weaponLoadout = new ArrayList<Weapon>();
	ArrayList<Equipment> techLoadout = new ArrayList<Equipment>();

	private float hull;
	private float heat;
	private float sheilding;
	
	private float acceleration = 5;
	
	public Ship(ShipTemplate _st) {
		
	}
	
	public Ship(ShipTemplate _st, ShipClass _sc) {
		shipClass = _sc;
		hull = shipClass.getMaxHull();
		heat = 0f;
		sheilding = 0f;
	}
	
	public void fireAt(Ship _s) {
		for (Weapon _w : weaponLoadout) {
			_w.attackShip(_s);
		}
	}
	
	public int getRange() {
		return shipClass.getRange();
	}
	
	public float getAcc() {
		return acceleration;
	}
	
	public int getMaxCargo() {
		return shipClass.getMaxCargo();
	}

	public int getWorth() {
		// TODO add equipment
		int worth = shipClass.getCost();
		for (Weapon _w : weaponLoadout) {
			worth += _w.getCost();
		}
		return worth;
	}
}
