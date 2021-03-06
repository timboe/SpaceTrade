package com.timboe.spacetrade.player;

import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.Sprites;
import com.timboe.spacetrade.ship.Ship;
import com.timboe.spacetrade.utility.Rnd;
import com.timboe.spacetrade.windows.BankWindow;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class Player extends Actor {
	
	private static EnumMap<Goods, AtomicInteger> stock = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private static EnumMap<Goods, AtomicInteger> avPrice = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private static int credz;
	private static Ship ship = null;
	private static int totalCargo;
	private static int currentLocationID;
	private static boolean dead = false;
	private static String name = "Tim:~$ ";

	private static Player singleton = new Player();
	private static boolean isInsured = false;
	private static float noClaimBonus = 0f;
	private static int insPaidSoFar = 0;
	private static int overdraft = 0;
	private static boolean useOverdraft = true;
	
	public static final Player getPlayer () {
		return singleton;
	}

	public boolean isMoving() {
		if (getActions().size == 0) return false;
		return true;
	}
	
	public static String getPlayerName() {
		return name;
	}
	
	public int getPremium() {
		return Math.round(ship.getShipClass().getCost() * BankWindow.insuranceRate * (1f - noClaimBonus));
	}
	
	public Player() { //Only to be called externally when loading a game! 
		currentLocationID = 0;
		setOrigin(Sprites.getSprites().getPlayerSprite().getWidth()/2f, Sprites.getSprites().getPlayerSprite().getHeight()/2f);
		
		//Setup maps
		for (Goods _g : Goods.values()) {
			avPrice.put(_g, new AtomicInteger(0) );
			stock.put(_g, new AtomicInteger(0) );
		}
		
		Gdx.app.log("Player", "InConstructor stock:" + stock);
	}
	
	public void refresh() { //Call after loading game
		move( Starmap.getPlanet(currentLocationID) );
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		Sprite _ps = Sprites.getSprites().getPlayerSprite();
		_ps.setPosition(getX() - _ps.getWidth()/2, 
						getY() - _ps.getHeight()/2);
		_ps.setRotation(getRotation());
		_ps.draw(batch, parentAlpha);
	}
	
	public void move(Planet _p) {
		currentLocationID = _p.getID();
		setPosition(_p.getX() + _p.getRadius(), _p.getY() + _p.getRadius());
		_p.setVisited();
	}
	
	public static int getPlanetID() {
		return currentLocationID;
	}
	
	public static int getStock(Goods _g) {
		return stock.get(_g).get();
	}
	public static  int getAvPaidPrice(Goods _g) {
		return avPrice.get(_g).get();
	}
	
	public static  int getCredz() {
		return credz;
	}
	
	public static int getFreeCargo() {
		return ship.getMaxCargo() - totalCargo;
	}
	
	public static int getWorth() {
		int worth = credz;
		worth += ship.getWorth();
		for (Goods _g : Goods.values()) {
			worth += (stock.get(_g).get() * avPrice.get(_g).get());
		}
		return worth;
	}
	
	public static void modCredz(int _m) {
		credz += _m;
		updateTotals();
	}
	
	public static void updateTotals() {
		totalCargo = 0;
		for (Goods _g : Goods.values()) {
			totalCargo += stock.get(_g).get();
		}
		RightBar.update();
	}
	
	public static int getTotalCargo() {
		return totalCargo;
	}
	
	public static int getTotalCargoCapacity() {
		return ship.getMaxCargo();
	}
	
	public static void addStock(Goods _g, int _newStock, int _price_per_unit) {
		final int _addingValue = _newStock * _price_per_unit;
		final int _currentStock = stock.get(_g).get();
		final int _currentValue = _currentStock * avPrice.get(_g).get();
		final int _totalValue = _addingValue + _currentValue;
		final int _totalStock = _newStock + _currentStock;
		final int _newAveragePrice = Math.round( (float)_totalValue / (float)(_totalStock)  );
		stock.get(_g).getAndAdd(_newStock);
		avPrice.get(_g).set(_newAveragePrice);
		updateTotals();
	}
	
	public static void removeStock(Goods _g, int _amount) {
		if (stock.get(_g).addAndGet(-_amount) == 0) {	//Note sign flip
			avPrice.get(_g).set(0);
		}
		updateTotals();
	}
	
	public static void setCredz(int _s) {
		credz = _s;
		updateTotals();
	}

	public static Planet getPlanet() {
		return Starmap.getPlanet(currentLocationID);
	}
	
	public static Ship getShip() {
		return ship;
	}

	public static void setShip(Ship _newShip) {
		ship = _newShip;
	}

	public static boolean getCarryIllegal() {
		if (getStock(Goods.SpaceCrack) > 0 || getStock(Goods.AI) > 0) return true;
		return false;
	}

	public static void setDead(boolean d) {
		dead  = d;
		
	}

	public static boolean getInsured() {
		return isInsured; //TODO
	}

	public static int getInsurancePayout() {
		return ship.getShipClass().getCost();
	}

	public static void setNoClaim(float _f) {
		noClaimBonus = _f;
		
	}

	public static void removeAllStock() {
		for (Goods _g : Goods.values()) {
			stock.get(_g).set(0);
			avPrice.get(_g).set(0);
		}
		updateTotals();
	}

	public static boolean getDead() {
		return dead;
	}

	public static int getAvailableCredzIncOD() {
		return overdraft + getCredz();
	}
	
	public static int getAvailableCredz() {
		if (useOverdraft == true) return getAvailableCredzIncOD();
		else if (getCredz() > 0) return getCredz();
		else return 0;
	}

	public static boolean getUseOD() {
		return useOverdraft;
	}
	
	public static void setInsured(boolean _b) {
		isInsured = _b;
	}

	public static float getNoClaims() {
		return noClaimBonus;
	}

	public static void setInsurancePaid(int _i) {
		insPaidSoFar = _i;	
	}

	public static int getInsurancePaid() {
		return insPaidSoFar;
	}

	public static int getOD() {
		return overdraft;
	}
}
