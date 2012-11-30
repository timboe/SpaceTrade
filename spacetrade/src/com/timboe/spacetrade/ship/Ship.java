package com.timboe.spacetrade.ship;

import java.util.ArrayList;

import com.timboe.spacetrade.enumerator.Equipment;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipProperty;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.enumerator.Weapons;

public class Ship {
	
	private ShipProperty property;
	private ShipTemplate template;
	private ShipClass shipClass;
	private ArrayList<Weapons> weaponLoadout = new ArrayList<Weapons>();
	private ArrayList<Equipment> techLoadout = new ArrayList<Equipment>();

	private int hull;
	private int heat;
	private float sheilding;
	private float age;
	
	private float acceleration = 5;
	
	public Ship(ShipTemplate _st) {
		
	}
	
	public Ship(ShipClass _sc) { //Player ship constructor
		shipClass = _sc;
		template = ShipTemplate.Player;
		property = ShipProperty.random();
		hull = shipClass.getMaxHull();
		heat = shipClass.getMaxHeat();
		sheilding = 0f;
	}
	
	public int getFreeWeaponSlots() {
		return shipClass.getWeaponSlots() - weaponLoadout.size();
	}
	
	public int getFreeEquipmentSlots() {
		return shipClass.getTechSlots() - techLoadout.size();
	}
	

	
	public void fireAt(Ship _s) {
		for (Weapons _w : weaponLoadout) {
			//_w.attackShip(_s);
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
		int worth = shipClass.getCost();
		for (Weapons _w : weaponLoadout) {
			worth += _w.getCost();
		}
		for (Equipment _e : Equipment.values()) {
			worth += _e.getCost();
		}
		return worth;
	}
	
	public void travel(float _shipTime) {
		age += _shipTime;
	}
	
	public int getTradeInPrice() {
		float mod = 1f - (age / 50f); //TODO tweak this range (currently ship years)
		if (mod > 0.9f) mod = 0.9f;
		if (mod < 0.25f) mod = 0.25f;
		return Math.round(shipClass.getCost() * mod);
	}

	public ShipClass getShipClass() {
		return shipClass;
	}

	public int getHull() {
		return hull;
	}

	public int getHeat() {
		return heat;
	}
	
	public int getMaxHull() {
		//TODO add equipment
		return shipClass.getMaxHull();
	}
	
	public int getMaxHeat() {
		//TODO add equipment
		return shipClass.getMaxHeat();
	}

	public int getMaxShields() {
		// TODO shields!
		return 0;
	}

	public int getShields() {
		// TODO shields!
		return 0;
	}

	public void arm(Weapons weapons) {
		assert(getFreeWeaponSlots() > 0);
		weaponLoadout.add(weapons);
	}
	
	public void arm(Equipment _e) {
		assert(getFreeEquipmentSlots() > 0);
		techLoadout.add(_e);
	}
	
	public int getNumberInstalled(Weapons _comp) {
		int _n = 0;
		for (Weapons _w : weaponLoadout) {
			if (_w == _comp) ++_n;
		}
		return _n;
	}
	
	public int getNumberInstalled(Equipment _comp) {
		int _n = 0;
		for (Equipment _e : techLoadout) {
			if (_e == _comp) ++_n;
		}
		return _n;
	}

	public void disarm(Weapons _w) {
		assert(getNumberInstalled(_w) > 0);
		weaponLoadout.remove(_w);	
	}
	
	public void disarm(Equipment _e) {
		assert(getNumberInstalled(_e) > 0);
		techLoadout.remove(_e);	
	}

	public ShipProperty getMod() {
		return property;
	}

	public int getFilledWeaponSlots() {
		return weaponLoadout.size();
	}
	
	public int getFilledEquipmentSlots() {
		return techLoadout.size();
	}

	public String getFullName() {
		return property.toString() + " " + shipClass.getName();
	}

	public void setMod(ShipProperty _shipMod) {
		property = _shipMod;
	}

	public void moveEquipment(Ship _oldShip) {
		//clone loadout
		weaponLoadout.addAll( _oldShip.weaponLoadout  );
		techLoadout.addAll( _oldShip.techLoadout );
	}

	public int getHeatLoss() {
		//TODO
		return 0;
	}

	public int getTotalWeaponSlots() {
		return shipClass.getWeaponSlots();
	}
	
	public int getTotalEquipmentSlots() {
		return shipClass.getTechSlots();
	}

	public ArrayList<Weapons> getWeapons() {
		return weaponLoadout;
	}
	
	public ArrayList<Equipment> getEquipment() {
		return techLoadout;
	}
}
