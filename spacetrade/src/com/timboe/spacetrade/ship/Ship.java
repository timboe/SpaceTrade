package com.timboe.spacetrade.ship;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.timboe.spacetrade.enumerator.Equipment;
import com.timboe.spacetrade.enumerator.OpponentStance;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipProperty;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.enumerator.Weapons;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.utility.Rnd;

public class Ship {
	static int shipCount = 0;
	
	private ShipProperty property;
	private ShipTemplate template;
	private ShipClass shipClass;
	private ArrayList<Weapons> weaponLoadout = new ArrayList<Weapons>();
	private ArrayList<Equipment> techLoadout = new ArrayList<Equipment>();
	private Rnd rnd = new Rnd(++shipCount);

	private int hull;
	private int heat;
	private int sheilding;
	private float age;
	
	//private boolean isAttacking;
	private OpponentStance stance;
	
	private float acceleration = 5;
	
	public Ship(ShipTemplate _st) { //Others ship constructor
		final int _cash = Player.getWorth(); //How much do we have to spend?
		
		template = _st;
		property = ShipProperty.random();
		
		boolean _chosen = false;
		final ShipClass[] values = ShipClass.values();
		final int _length = values.length - 1;
		int _loop = 0;
		while (_chosen == false) {
			++_loop;
			for (int i = _length; i >= 0; --i) {
				ShipClass _sc = values[i];
				if (_sc == ShipClass.Tiny) continue;
				if (_sc.getCost() > _cash) continue;
				shipClass = _sc;
				weaponLoadout.clear();
				techLoadout.clear();
				int _spend = _sc.getCost();
				//choose weapons
				while (getFreeWeaponSlots() > 0) {
					Weapons _w;
					if (getFilledWeaponSlots() == 0) {
						_w = Weapons.randomLevel( shipClass.getLevel() ); //get at level
					} else {
						_w = Weapons.random( shipClass.getLevel() ); //get up to level
					}
					arm(_w);
					_spend += _w.getCost();
				}
				while (getFreeEquipmentSlots() > 0) {
					Equipment _e;
					if (getFilledEquipmentSlots() == 0) {
						_e = Equipment.randomLevel( shipClass.getLevel() ); //get at level
					} else {
						_e = Equipment.random( shipClass.getLevel() ); //get up to level-1
					}
					arm(_e);
					_spend += _e.getCost();
				}
				//have we gone overbudget?
				if (_spend < _cash) {
					_chosen = true;
					break;
				}
			}
			//If we're having no luck then just take the smallest config after 5 tries
			if (_loop == 5) {
				_chosen = true;
			}
		}
		//add some damage
		hull = shipClass.getMaxHull();
		int dmg = rnd.getRandI( hull/4 );
		hull -= dmg;
		
		if (template == ShipTemplate.Pirate) {
			//TODO check if feared and not attack then
			stance = OpponentStance.Attack;
		} else if (template == ShipTemplate.Police) {
			if (rnd.getRandChance(0.5f) == true) {
				stance = OpponentStance.RequestInspect; //TODO put reputation here
			} else {
				stance = OpponentStance.Ignore;
			}
		} else if (template == ShipTemplate.Trader) {
			if (rnd.getRandChance(0.5f) == true) {
				stance = OpponentStance.OfferTrade; 
			} else {
				stance = OpponentStance.Ignore;
			}
		}
		
		print();
	}
	
	public void doCooldown() {
		heat -= getCooldown();
		//TODO add equipment;
		heat = Math.max(heat, 0);
		Gdx.app.log("Cooldown", this+" cooling down "+getCooldown()+" to heat "+heat);

	}
	
	private int getCooldown() {
		//TODO add equipmnt
		return shipClass.getCooldown();
	}

	public void print() {
		Gdx.app.log("ShipDebug", template.toString() +" "+ property.toString() + " " + shipClass.getName()+ ", value:"+getWorth());
		for (Weapons _w : weaponLoadout) {
			Gdx.app.log("ShipDebug"," ---> Level "+ _w.getLevel() + " " + _w.getWeaponClass().toString() + " [" + _w.getName() + "]");
		}
		for (Equipment _e : techLoadout) {
			Gdx.app.log("ShipDebug"," ###> Level "+ _e.getLevel() + " " + _e.getName());
		}
	}
	
	public Ship(ShipClass _sc) { //Player ship constructor
		shipClass = _sc;
		template = null;
		property = ShipProperty.random();
		hull = shipClass.getMaxHull();
		heat = 0;
		sheilding = 0;
	}
	
	public int getFreeWeaponSlots() {
		return shipClass.getWeaponSlots() - weaponLoadout.size();
	}
	
	public int getFreeEquipmentSlots() {
		return shipClass.getTechSlots() - techLoadout.size();
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
		return sheilding;
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

	public ShipTemplate getTemplate() {
		return template;
	}

	public OpponentStance getStance() {
		return stance;
	}
	
	public int getHeatPerFire() {
		int _h = 0;
		for (Weapons _w : weaponLoadout) {
			_h += _w.getHeat();
		}
		return _h;
	}
	
	public int getMinHeatPerFire() {
		int _h = getHeatPerFire();
		for (Weapons _w : weaponLoadout) {
			if (_w.getHeat() < _h) {
				_h = _w.getHeat();
			}
		}
		return _h;
	}

	public int getRemainingHeat() {
		return getMaxHeat() - getHeat(); 
	}

	public boolean sendAttack(Ship _shipToAttack) {
		int _dmg = 0;
		int _heat = 0; //debug
		for (Weapons _w : weaponLoadout) {
			if (getRemainingHeat() > _w.getHeat()) {
				//TODO check if hits etc.
				_dmg += _w.getDamage(_shipToAttack.getMod()); //contains modifying code
				_heat += _w.getHeat();
				heat += _w.getHeat();
			}
		}
		Gdx.app.log("Attack", "Attacking for damage "+_dmg+" at heat cost "+_heat);
		return _shipToAttack.recieveAttack(_dmg);
	}
	
	public boolean recieveAttack(int _dmg) {
		if (sheilding >= _dmg) {
			sheilding -= _dmg;
			return false;
		} else if (getShields() > 0) {
			_dmg -= sheilding;
			sheilding = 0;
		}
		
		if (hull >= _dmg) {
			hull -= _dmg;
			return false;
		} else { //Uh-oh!
			hull = 0;
			return true;
		}
	}

//	public String getAction() {
//		if (template == null) return null;
//		switch (template) {
//		case Pirate: 
//			if (hull / getMaxHull() > 0.1f) return "ATTACK";
//			else return "FLEE";
//		case Police:
//			return "FLEE";
//		case Trader:
//			return "FLEE";
//			
//		}
//		return null;
//	}
}
