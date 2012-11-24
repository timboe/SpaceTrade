package com.timboe.spacetrade.player;

import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.Sprites;
import com.timboe.spacetrade.ship.Ship;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class Player extends Actor {
	
	private EnumMap<Goods, AtomicInteger> stock = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private EnumMap<Goods, AtomicInteger> avPrice = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private int credz;
	private Ship ship;
	private int totalCargo;
	private int currentLocationID;

	private static Player singleton = new Player();
	public static final Player getPlayer () {
		return singleton;
	}

	public Player() { //Only to be called externally when loading a game!
		ship = new Ship(ShipTemplate.Player, ShipClass.ClassB);
		move( Starmap.getRandomPlanet() );
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
		_ps.setPosition(getX(), getY());
		_ps.setRotation(getRotation());
		_ps.draw(batch, parentAlpha);
	}
	
	public void move(Planet _p) {
		currentLocationID = _p.getID();
		setPosition(_p.getX() + _p.radius, _p.getY() + _p.radius);
	}
	
	public int getStock(Goods _g) {
		return stock.get(_g).get();
	}
	public int getAvPaidPrice(Goods _g) {
		return avPrice.get(_g).get();
	}
	
	public int getCredz() {
		return credz;
	}
	
	public int getFreeCargo() {
		return ship.getCargo() - totalCargo;
	}
	
	public int getWorth() {
		int worth = credz;
		worth += ship.getWorth();
		for (Goods _g : Goods.values()) {
			worth += (stock.get(_g).get() * avPrice.get(_g).get());
		}
		return worth;
	}
	
	public void modCredz(int _m) {
		credz += _m;
		updateTotals();
	}
	
	public void updateTotals() {
		totalCargo = 0;
		for (Goods _g : Goods.values()) {
			totalCargo += stock.get(_g).get();
		}
		RightBar.update();
	}
	
	public int getTotalCargo() {
		return totalCargo;
	}
	
	public void addStock(Goods _g, int _newStock, int _price_per_unit) {
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
	
	public void removeStock(Goods _g, int _amount) {
		if (stock.get(_g).addAndGet(-_amount) == 0) {	//Note sign flip
			avPrice.get(_g).set(0);
		}
		updateTotals();
	}
	
	public void setCredz(int _s) {
		credz = _s;
		updateTotals();
	}

	public Planet getPlanet() {
		return Starmap.getPlanet(currentLocationID);
	}
	
	public Ship getShip() {
		return ship;
	}
	
}
