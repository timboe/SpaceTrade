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
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.Equipment;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.Government;
import com.timboe.spacetrade.enumerator.PlanetActivity;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipProperty;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.enumerator.SpecialEvents;
import com.timboe.spacetrade.enumerator.Weapons;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.render.Sprites;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.utility.AdLib;
import com.timboe.spacetrade.utility.Modifiers;
import com.timboe.spacetrade.utility.Rnd;

public class Planet extends Actor {
		
	private Vector2 v2 = new Vector2();
	private String name = AdLib.getAdLib().planets.getStr();
	private int nameLen = name.length();
	private Color colour =  AdLib.getAdLib().starColour.getCol();
	private Government govType = Government.random();
	private Civilisation civType = Civilisation.random();
	private WorldSize worldSize = WorldSize.random();
	private int diameter = (int) (Textures.getStar().getRegionWidth());
	public int radius = Math.round(diameter/2f);
	private int ID;
	private boolean isVisited = false;

	private final EnumMap<Goods, Boolean> goodsSold = new EnumMap<Goods, Boolean>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> stock = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> stockTarget = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> volitility = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, Array<AtomicInteger> > price = new EnumMap<Goods, Array<AtomicInteger> >(Goods.class);
	private final float buyMod = 1f;//1.02f; //TODO tweak these 
	private final float sellMod = 1f;//0.98f;
	private final float techMod = 1f;
	private final EnumMap<ShipClass, ShipProperty > shipsSold = new EnumMap<ShipClass, ShipProperty >(ShipClass.class);
	private final EnumMap<Weapons, Boolean > weaponsSold = new EnumMap<Weapons, Boolean >(Weapons.class);
	private final EnumMap<Equipment, Boolean > equipmentSold = new EnumMap<Equipment, Boolean >(Equipment.class);
	
	private SpecialEvents specialEvent;
	private PlanetActivity police = PlanetActivity.Some;
	private PlanetActivity pirates = PlanetActivity.Some;
	private PlanetActivity traders = PlanetActivity.Some;

	private final Rnd rnd = new Rnd();

	public void addShipSold(ShipClass _sc) {
		shipsSold.put(_sc, ShipProperty.random() );
	}

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
		rnd.setSeed(SpaceTrade.masterSeed + _ID);

		//Setup maps
		for (Goods _g : Goods.values()) {
			goodsSold.put(_g, Boolean.TRUE);
			stock.put(_g, new AtomicInteger(rnd.getRandI(_g.getBaseAmount()))); //have up to base amount
			stockTarget.put(_g, new AtomicInteger(_g.getBaseAmount())); 
			volitility.put(_g, new AtomicInteger(20)); //base volatility in % //TODO check
			price.put(_g, new Array<AtomicInteger>() );
			price.get(_g).add( new AtomicInteger( _g.getBasePrice() ));
		}
		for (ShipClass _s : ShipClass.values()) {
			shipsSold.put(_s, null);
		}
		for (Weapons _w : Weapons.values()) {
			weaponsSold.put(_w, Boolean.FALSE);
		}
		for (Equipment _e : Equipment.values()) {
			equipmentSold.put(_e, Boolean.FALSE);
		}
		
		specialEvent = SpecialEvents.None;
		
		//Setup planet 
		refresh();
		init();
	}
	
	public void setActivity(PlanetActivity _pirate, PlanetActivity _police, PlanetActivity _trade) {
		police = _police;
		pirates = _pirate;
		traders = _trade;
	}
	
	public void addWeapons(int _n, int _level) {
		addEquipment(_n, _level);//TODO, do this separately?
		while (_n > 0) {
			Weapons _w = Weapons.random();
			if (_w.getLevel() != _level) continue;
			if (weaponsSold.get(_w) == true) continue;
			weaponsSold.put(_w, Boolean.TRUE);
			--_n;
		}
	}
	
	public void addEquipment(int _n, int _level) {
		while (_n > 0) {
			Equipment _e = Equipment.random();
			if (_e.getLevel() != _level) continue;
			if (equipmentSold.get(_e) == true) continue;
			equipmentSold.put(_e, Boolean.TRUE);
			--_n;
		}
	}
	
	public boolean getSellsWeapon(Weapons _w) {
		return weaponsSold.get(_w);
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
		if (StarmapScreen.getPlanetHighlightID() == ID) {
			Textures.getOutlineFont().setColor(1f,0.078f,0.576f,1f); //Hot pink
		} else if (isVisited == true) {
			Textures.getOutlineFont().setColor(0f,0.75f,1f,1f); //Cool blue
		} else {
			Textures.getOutlineFont().setColor(Color.WHITE);
		}
		Textures.getOutlineFont().draw(batch, name, getX() - (nameLen*4f), getY() + (1.2f*diameter));
	}
	
	public void drawBasic(ShapeRenderer g2) {
		g2.rect(getX(), getY(), getWidth(), getHeight());
		g2.rect(getX() + radius - 1, getY() + radius - 1, 2, 2);

	}
	
	public float getEquipmentPriceMod() {
		return techMod;
	}
	
	private void init() {
		Modifiers.setPrices(govType, civType, worldSize, this);
	}
	
	public void newYear(int _n_years) {
		while (_n_years-- > 0) {
			for (Goods _g : Goods.values()) {
				final int current = getPrice(_g);
				final float sigma = (float)current * ((float)volitility.get(_g).get() / 100f);
				int _new = Math.abs( Math.round( rnd.getRandG(current, sigma) ) );
				//DEBUG
				if (SpaceTrade.priceVariation == false) {
					_new = current; //Don't vary price
				}				
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

	public boolean getSells(ShipClass _s) {
		if (shipsSold.get(_s) == null) return false;
		return true;
	}

	public ShipProperty getShipMod(ShipClass _s) {
		return shipsSold.get(_s);
	}

	public boolean getSellsEquipment(Equipment _e) {
		return equipmentSold.get(_e);
	}

	public SpecialEvents getSpecial() {
		return specialEvent;
	}

	public PlanetActivity getPoliceActivity() {
		return police;
	}

	public PlanetActivity getActivity(ShipTemplate _template) {
		assert(_template != ShipTemplate.Player);
		switch (_template) {
		case Pirate: return pirates;
		case Police: return police;
		case Trader: return traders;
		case Player: return PlanetActivity.Some;
		default: return PlanetActivity.Some;
		}
	}

	public void setVisited() {
		isVisited = true;
	}


}
