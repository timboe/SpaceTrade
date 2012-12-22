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
import com.timboe.spacetrade.screen.PlanetScreen;
import com.timboe.spacetrade.utility.Rnd;
import com.timboe.spacetrade.world.Starmap;

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
	private boolean surrendered = false; //only surrender once
	private boolean hasEscapePod = false;
	
	//private boolean isAttacking;
	private OpponentStance stance;
	private String logStart;
	
	private float acceleration = 5;
	
	public Ship(ShipTemplate _st) { //Others ship constructor
		final int _cash = Player.getWorth(); //How much do we have to spend?
		
		logStart = "Your opponent ";
		template = _st;
		property = ShipProperty.random();
		
		boolean _chosen = false;
		final ShipClass[] values = ShipClass.values(); //TODO should favour prefered classes
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
					if (rnd.getRandChance(0.2f) == true) break; //small chance to not fully arm //TODO tweak
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
					if (rnd.getRandChance(0.2f) == true) break; //small chance to not fully arm //TODO tweak
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
		if (rnd.getRandChance(0.50f) == true) hull -= rnd.getRandI( hull/4 );
		sheilding = getMaxShields();		
		if (rnd.getRandChance(0.25f) == true && sheilding > 0) sheilding -= rnd.getRandI( sheilding/4 );
		
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
	
	public void doCooldown(boolean _quiet) {
		int _toCool = getCooldown();
		int _cooled = 0;
		if (_toCool > heat) {
			_cooled = heat;
			heat = 0;
		} else {
			_cooled = _toCool;
			heat -= _toCool;
		}
		String _Stxt = "";
		if (template != ShipTemplate.Player) _Stxt = "s";
		if (_quiet == false && _cooled > 0) {
			PlanetScreen.combatLog.add(Player.name+"["+PlanetScreen.turn+"]"+ logStart+" vent"+_Stxt+" "+_cooled+" heat into space.");
		}
	}
	
	public void doMaintenance() {
		int _s = 0;
		int _h = 0;
		for (Equipment _e : techLoadout) {
			_s += _e.getShieldRegen();
			_h += _e.getHullRegen();
		}
		sheilding += _s;
		sheilding = Math.min(sheilding, getMaxShields());
		hull += _h;
		hull = Math.min(hull, getMaxHull());
	}
	
	public int getCooldown() {
		int _cd = shipClass.getCooldown();
		for (Equipment _e : techLoadout) {
			_cd += _e.getHeatLoss();
		}
		return _cd;
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
		logStart = "You ";
		shipClass = _sc;
		template = ShipTemplate.Player;
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
		int _r = shipClass.getRange();
		for (Equipment _e : techLoadout) {
			_r += _e.getExtraRange();
		}
		return _r;
	}
	
	public float getRangeLightYears() {
		//range nominally from 10 to 20
		//take 10 as min spacing
		float _r = getRange();
		_r /= 10f; //now 1 to 2
		return _r * Starmap.toLightYears * Starmap.upperStarRange;
		
	}
	
	public float getRangePixels() {
		return Starmap.getPixelsFromLightyears( getRangeLightYears() );
	}
	
	public float getAcc() {
		return acceleration;
	}
	
	public int getMaxCargo() {
		int _mc = shipClass.getMaxCargo();
		for (Equipment _e : techLoadout) {
			_mc += _e.getExtraCargo();
		}
		return _mc;
	}

	public int getWorth() {
		int worth = shipClass.getCost();
		for (Weapons _w : weaponLoadout) {
			worth += _w.getCost();
		}
		for (Equipment _e : techLoadout) {
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
		int _mh = shipClass.getMaxHull();
		for (Equipment _e : techLoadout) {
			_mh += _e.getExtraHull();
		}
		return _mh;
	}
	
	public int getMaxHeat() {
		int _mh = shipClass.getMaxHeat();
		for (Equipment _e : techLoadout) {
			_mh += _e.getHeatCapacity();
		}
		return _mh;
	}

	public int getMaxShields() {
		int _s = 0;
		for (Equipment _e : techLoadout) {
			_s += _e.getShielding();
		}
		return _s;
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
		hull += _e.getExtraHull();
		sheilding += _e.getShielding();
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
		hull -= _e.getExtraHull();
		sheilding -= _e.getShielding();
		if (hull <= 0) hull = 1;
		if (sheilding <= 0) sheilding = 0;
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
		hull = getMaxHull();
		hasEscapePod = _oldShip.getEscapePod();
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
		for (Weapons _w : weaponLoadout) {
			if (getRemainingHeat() > _w.getHeat()) {
				//TODO check if hits etc.
				int _wDmg = _w.getDamage(_shipToAttack.getMod()); //contains modifying code
				int _wHeat = _w.getHeat();
				_dmg += _wDmg;
				heat += _wHeat;
				String _e = "";
				if (_w.getDamageMod(_shipToAttack.getMod()) > 1f) {
					_e = "It's super effective!";
				} else if (_w.getDamageMod(_shipToAttack.getMod()) < 1f) {
					_e = "It's not very effective.";
				}
				PlanetScreen.combatLog.add(Player.name+"["+PlanetScreen.turn+"]"+ logStart +" attacks with "+_w.getName()+" for "+_wDmg+" damage and "+_wHeat+" heat. "+_e);
			} else if (template == ShipTemplate.Player) { //Only if player
				PlanetScreen.combatLog.add(Player.name+"["+PlanetScreen.turn+"]"+ "WARNING: You cannot fire your "+_w.getName()+", this turn insufficient heat capacity onboard!");
			}
		}
		//Gdx.app.log("Attack", "Attacking for damage "+_dmg+" at heat cost "+_heat);
		return _shipToAttack.recieveAttack(_dmg);
	}
	
	public boolean recieveAttack(int _dmg) {
		boolean _dead = false;
		
		if (sheilding >= _dmg) {
			sheilding -= _dmg;
			_dmg = 0;
		} else if (getShields() > 0) {
			_dmg -= sheilding;
			sheilding = 0;
		}
		
		if (hull >= _dmg) {
			hull -= _dmg;
			_dmg = 0;
		} else { //Uh-oh!
			hull = 0;
			_dead = true;
		}
		
		if (template != ShipTemplate.Player) {
			//change stance.
			if (template == ShipTemplate.Pirate || template == ShipTemplate.Police) { 
				stance = OpponentStance.Attack;
			} else if (template == ShipTemplate.Trader) { //TODO trader may attack
				stance = OpponentStance.Flee;
			}
			
			if ((float)hull / (float)getMaxHull() < 0.25f) { //TODO tweak
				if((template == ShipTemplate.Pirate || template == ShipTemplate.Trader)
						&& rnd.getRandChance(0.5f) == true //TODO tweak
						&& surrendered == false) {
					stance = OpponentStance.Surrender;
					surrendered = true;
				} else {
					stance = OpponentStance.Flee;
				}
			}
			
			if (hull == 0) {
				stance = OpponentStance.Dead;
			}
		}
		
		return _dead;
	}

	public void maxShields() {
		sheilding = getMaxShields();		
	}

	public boolean getEscapePod() {
		return hasEscapePod;
	}

	public void doScanOf(Ship encounter) {
		if (hasScanners() == false) return;
		PlanetScreen.combatLog.add(Player.name +"./activateScanners("+encounter.getShipClass().getName()+") ");		
		String _msg = Player.name +"<Weapons Scan>: ";
		boolean someDetected = false;
		for (Weapons _w : encounter.getWeapons()) {
			int _l = _w.getLevel();
			boolean isDetected = false;
			for (Equipment _e : techLoadout) {
				if (rnd.getRandChance( _e.getScanChance(_l) ) == true) {
					isDetected = true;
					someDetected = true;
				}
			}
			if (isDetected == true) {
				_msg += "L" + _w.getLevel() + " " + _w.getName() + " | ";
			}
		}
		if (someDetected == false) _msg += "None Detected";
		PlanetScreen.combatLog.add(_msg);		
		someDetected = false;
		_msg = Player.name +"<Equipment Scan>: ";
		for (Equipment _E : encounter.getEquipment()) {
			int _l = _E.getLevel();
			boolean isDetected = false;
			for (Equipment _e : techLoadout) {
				if (rnd.getRandChance( _e.getScanChance(_l) ) == true) {
					isDetected = true;
					someDetected = true;
				}
			}
			if (isDetected == true) {
				_msg += "L" + _E.getLevel() + " " + _E.getName() + " | ";
			}
		}
		if (someDetected == false) _msg += "None Detected";
		PlanetScreen.combatLog.add(_msg);		
	}

	private boolean hasScanners() {
		for (Equipment _e : techLoadout) {
			if (_e.getScanChance(1) > 0f) return true;
		}
		return false;
	}

	public void resetHeat() {
		heat = 0;
	}

	public void setEscapePod(boolean _b) {
		hasEscapePod = _b;		
	}

}
