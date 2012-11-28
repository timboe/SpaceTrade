package com.timboe.spacetrade.world;

import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.Government;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.render.Sprites;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.AdLib;
import com.timboe.spacetrade.utility.Modifiers;
import com.timboe.spacetrade.utility.Utility;

public class Planet extends Actor {
		
	private Vector2 v2 = new Vector2();
	private String name = AdLib.getAdLib().planets.getStr();
	private Color colour =  AdLib.getAdLib().starColour.getCol();
	private Government govType = Government.random();
	private Civilisation civType = Civilisation.random();
	private WorldSize worldSize = WorldSize.random();
	private int diameter = (int) (Textures.getStar().getWidth());
	public int radius = Math.round(diameter/2f);
	private int ID;
	
	private final EnumMap<Goods, Boolean> goodsSold = new EnumMap<Goods, Boolean>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> stock = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> stockTarget = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> volitility = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, Array<AtomicInteger> > price = new EnumMap<Goods, Array<AtomicInteger> >(Goods.class);
	private final float buyMod = 1.02f; //TODO tweak these 
	private final float sellMod = 0.98f;

	//serialiser
	public Planet() {
//		Gdx.app.log("Planet", "In Serealise Constructor");
//		setOrigin(0, 0);
//		//setWidth(diameter);
//		//setHeight(diameter);
//		scale(0.5f);
//		setTouchable(Touchable.enabled);
//		
//		//Setup maps
//		for (Goods _g : Goods.values()) {
//			goodsSold.put(_g, new Boolean(true));
//			stock.put(_g, new AtomicInteger(Utility.getRandI(_g.getBaseAmount()))); //have up to base amount
//			stockTarget.put(_g, new AtomicInteger(_g.getBaseAmount())); 
//			volitility.put(_g, new AtomicInteger(20)); //base volatility in % //TODO check
//			price.put(_g, new Array<AtomicInteger>() );
//			price.get(_g).add( new AtomicInteger( _g.getBasePrice() ));
//		}
//		
//		//Setup planet 
//		refresh();
//		init();
	}
	
	public Planet(int _x, int _y, int _ID) {
		ID = _ID;
		setPosition(_x, _y); //Actor
		//setOrigin(0, 0);
		setWidth(diameter);
		setHeight(diameter);
		setTouchable(Touchable.enabled);

		//Setup maps
		for (Goods _g : Goods.values()) {
			goodsSold.put(_g, new Boolean(true));
			stock.put(_g, new AtomicInteger(Utility.getRandI(_g.getBaseAmount()))); //have up to base amount
			stockTarget.put(_g, new AtomicInteger(_g.getBaseAmount())); 
			volitility.put(_g, new AtomicInteger(20)); //base volatility in % //TODO check
			price.put(_g, new Array<AtomicInteger>() );
			price.get(_g).add( new AtomicInteger( _g.getBasePrice() ));
		}
		
		//Setup planet 
		refresh();
		init();
	}
	
	public void refresh() {
		float scale = worldSize.getSizeMod();
		Sprites.getSprites().getPlanetSprite(ID).setPosition((getX()+radius)-(radius*scale), (getY()+radius)-(radius*scale));
		Sprites.getSprites().getPlanetSprite(ID).setSize(radius*scale*2, radius*scale*2);
		Sprites.getSprites().getPlanetSprite(ID).setColor(colour);
	}
	
	static boolean ONCE = false;
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		Sprites.getSprites().getPlanetSprite(ID).draw(batch, parentAlpha);
		Textures.getOutlineFont().draw(batch, name, getX() - radius/2, getY() + (1.2f*diameter));
	}
	
	public void drawBasic(ShapeRenderer g2) {
		g2.rect(getX(), getY(), getWidth(), getHeight());
		g2.rect(getX() + radius - 1, getY() + radius - 1, 2, 2);

	}
	
	public float getEquipmentPriceMod() {
		return 1f; //TODO
	}
	
	private void init() {
		Modifiers.setPrices(govType, civType, worldSize, this);
	}
	
	public void newYear(int _n_years) {
		while (_n_years-- > 0) {
			for (Goods _g : Goods.values()) {
				final int current = getPrice(_g);
				final float sigma = (float)current * ((float)volitility.get(_g).get() / 100f);
				final int _new = Math.abs( Math.round( Utility.getRandG(current, sigma) ) );
				price.get(_g).add( new AtomicInteger(_new) );
				//Mod stock
				final int _stock = getStock(_g);
				final int _stockTarget = getStockTarget(_g);
				if (_stock > _stockTarget) {	//TODO add something to do with size of planet
					modStock(_g, -1);
				} else if (_stock < _stockTarget) {
					modStock(_g, +1);
				}
			}
		}
	}
	

	
	
	public float dst(Planet _remote) {
		return dst(_remote.getX(), _remote.getY());
	}
	
	public float dst(float _x, float _y) {
		v2.set(getX(), getY());
		return v2.dst(_x, _y);
	}
	
	public int getPrice(Goods _g) {
		return getPrice(_g, Starmap.getStarDate());
	}
	
	public int getPrice(Goods _g, int _starDate) {
		return  price.get(_g).get(_starDate).get();
	}
	
	public int getPriceBuy(Goods _g) {
		return Math.round(getPrice(_g) * buyMod);
	}
	
	public int getPriceBuy(Goods _g, int _starDate) {
		return Math.round(getPrice(_g, _starDate) * buyMod);
	}
	
	public int getPriceSell(Goods _g) {
		return Math.round(getPrice(_g) * sellMod);
	}
	
	public int getPriceSell(Goods _g, int _starDate) {
		return Math.round(getPrice(_g, _starDate) * sellMod);
	}
	
	public void setPrice(Goods _g, int _price) {
		setPrice(_g, _price, Starmap.getStarDate());
	}
	
	public void setPrice(Goods _g, int _price, int _starDate) {
		price.get(_g).get(_starDate).set(_price);
	}
	
	public int getStock(Goods _g) {
		return stock.get(_g).get();
	}
	
	public Boolean getGoodsSold(Goods _g) {
		return goodsSold.get(_g);
	}
	
	public void setGoodsSold(Goods _g, boolean _sold) {
		goodsSold.put(_g, new Boolean(_sold));
	}
	
	public int getStockTarget(Goods _g) {
		return stockTarget.get(_g).get();
	}
	
	public void modStock(Goods _g, int _amount) {
		stock.get(_g).getAndAdd(_amount);
	}
	
	public void setStock(Goods _g, int _amount) {
		stock.get(_g).set(_amount);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void printStat() {
		Gdx.app.log("Planet", "---------- "+name+" is a "+civType+" "+govType+" ----------");
		Gdx.app.log("Planet", "\tGRN\tTEX\tMIN\tMAC\tH20\tCOM\tCRK\tMED\tAI\tSNG");
		for (int Y=0; Y < Starmap.getStarDate(); ++Y) {
			String _s = Y+")\t";
			for (Goods _g : Goods.values()) {
				if (goodsSold.get(_g) == true) {
					_s += price.get(_g).get(Y)+"\t";
				} else {
					_s += "---\t";
				}
			}
			Gdx.app.log("Planet", _s);
		}
		Gdx.app.log("Planet", "---------- ---------- ---------- ---------- ---------- ----------");
	}

	public boolean getSells(Goods _g) {
		return goodsSold.get(_g).booleanValue();
	}

	public WorldSize getSize() {
		return worldSize;
	}
	
	public Civilisation getCiv() {
		return civType;
	}
	
	public Government getGov() {
		return govType;
	}

	public int getID() {
		return ID;
	}

	public String getFullName() {
		return getName() 
		+ ", the " 
		+ getSize() 
		+ " " + getCiv() 
		+ " " + getGov();
	}

	public int getVolatility(Goods _g) {
		return volitility.get(_g).get();
	}
	
	public void setVolatility(Goods _g, int _v) {
		volitility.get(_g).set(_v);
	}
	
	
	public void setStockTarget(Goods _g, int _st) {
		stockTarget.get(_g).set(_st);
	}


}
