package com.timboe.spacetrade.player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.ship.Ship;
import com.timboe.spacetrade.utility.Utility;

public class Player {
	
	private final EnumMap<Goods, AtomicInteger> stock = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> avPrice = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private int credz;
	private Ship ship;
	
	
	private static final Player singleton = new Player();
	public static final Player getPlayer () {
		return singleton;
	}
	
	private Player() {
		credz = 1000;
		ship = new Ship(ShipTemplate.Player, ShipClass.ClassB);
		
		//Setup maps
		for (Goods _g : Goods.values()) {
			avPrice.put(_g, new AtomicInteger(Utility.getUtility().getRandI(500)) );
			stock.put(_g, new AtomicInteger(Utility.getUtility().getRandI(500)) );
		}
	}
	
	public int getStock(Goods _g) {
		return stock.get(_g).get();
	}
	public int getAvPaidPrice(Goods _g) {
		return stock.get(_g).get();
	}
	
	public int getCredz() {
		return credz;
	}
	
	public void modCredz(int _m) {
		credz += _m;
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
	}
	
	public void removeStock(Goods _g, int _amount) {
		stock.get(_g).getAndAdd(-_amount); //Note sign flip
	}
	
	public void setCredz(int _s) {
		credz = _s;
	}
	
}
