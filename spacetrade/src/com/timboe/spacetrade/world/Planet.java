package com.timboe.spacetrade.world;

import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;

import sun.awt.windows.ThemeReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.Fluctuate;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.Government;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.utility.AdLib;
import com.timboe.spacetrade.utility.Utility;

public class Planet extends Actor {
	
	private final Vector2 v2 = new Vector2();
	private final String name;
	private final Government gov_type;
	private final Civilisation civ_type;
	private final Sprite sprite;
	private final WorldSize worldSize;
	private int diameter;
	
	private final EnumMap<Goods, Boolean> goodsSold = new EnumMap<Goods, Boolean>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> stock = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> stockTarget = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, AtomicInteger> volitility = new EnumMap<Goods, AtomicInteger>(Goods.class);
	private final EnumMap<Goods, Array<AtomicInteger> > price = new EnumMap<Goods, Array<AtomicInteger> >(Goods.class);

	
	Planet(int _x, int _y) {
		diameter = Textures.getTextures().getStar().getWidth();
		worldSize = WorldSize.random();
		diameter = (int)(diameter * worldSize.getSizeMod());
		setPosition(_x, _y); //Actor
		setWidth(diameter);
		setHeight(diameter);
		setTouchable(Touchable.enabled);
		name = AdLib.getAdLib().planets.get();
		gov_type = Government.random();
		civ_type = Civilisation.random();
		sprite = new Sprite(Textures.getTextures().getStar());
		sprite.setScale(worldSize.getSizeMod());
		sprite.setPosition(_x, _y);
		
		//Setup maps
		for (Goods _g : Goods.values()) {
			goodsSold.put(_g, new Boolean(true));
			stock.put(_g, new AtomicInteger(Utility.getUtility().getRandI(_g.getBaseAmount()))); //have up to base amount
			stockTarget.put(_g, new AtomicInteger(_g.getBaseAmount())); 
			volitility.put(_g, new AtomicInteger(20)); //base volatility in % //TODO check
			price.put(_g, new Array<AtomicInteger>() );
			price.get(_g).add( new AtomicInteger( _g.getBasePrice() ));
		}
		
		//Setup planet 
		init();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		sprite.draw(batch, parentAlpha);
	}
	
	public void drawBasic(ShapeRenderer g2) {
		g2.circle(sprite.getX()+(diameter/2), sprite.getY()+(diameter/2), (diameter/2));
	}
	
	private void init() {
		switch (civ_type) {
		//Have matching pair of up and down fluctuations per type
		case Agricultural:
//			goodsSold.put(Goods.Grain, 		new Boolean(true));
//			goodsSold.put(Goods.Textiles, 	new Boolean(true));
//			goodsSold.put(Goods.Minerals, 	new Boolean(true));
//			goodsSold.put(Goods.Machinery, 	new Boolean(true));
			goodsSold.put(Goods.HeavyWater,	new Boolean(false));
			goodsSold.put(Goods.Computers, 	new Boolean(false));
//			goodsSold.put(Goods.SpaceCrack, new Boolean(true));
			goodsSold.put(Goods.MedicalGel, new Boolean(false));
			goodsSold.put(Goods.AI, 		new Boolean(false));
			goodsSold.put(Goods.Singularity,new Boolean(false));

			changePrice(Goods.Grain, 		Fluctuate.downSmall);
			changePrice(Goods.Minerals, 	Fluctuate.upSmall);
			changePrice(Goods.Machinery,	Fluctuate.upSmall);
			break;
		case Enlightenment:
//			goodsSold.put(Goods.Grain, 		new Boolean(true));
//			goodsSold.put(Goods.Textiles, 	new Boolean(true));
//			goodsSold.put(Goods.Minerals, 	new Boolean(true));
//			goodsSold.put(Goods.Machinery, 	new Boolean(true));
			goodsSold.put(Goods.HeavyWater,	new Boolean(false));
			goodsSold.put(Goods.Computers, 	new Boolean(false));
//			goodsSold.put(Goods.SpaceCrack, new Boolean(true));
//			goodsSold.put(Goods.MedicalGel, new Boolean(true));
			goodsSold.put(Goods.AI, 		new Boolean(false));
			goodsSold.put(Goods.Singularity,new Boolean(false));

			changePrice(Goods.Textiles, 	Fluctuate.downSmall);
			changePrice(Goods.MedicalGel,	Fluctuate.upSmall);
			changePrice(Goods.SpaceCrack,	Fluctuate.downSmall);
			break;		
		case Industrial:
//			goodsSold.put(Goods.Grain, 		new Boolean(true));
//			goodsSold.put(Goods.Textiles, 	new Boolean(true));
//			goodsSold.put(Goods.Minerals, 	new Boolean(true));
//			goodsSold.put(Goods.Machinery, 	new Boolean(true));
//			goodsSold.put(Goods.HeavyWater,	new Boolean(true));
//			goodsSold.put(Goods.Computers, 	new Boolean(true));
//			goodsSold.put(Goods.SpaceCrack, new Boolean(true));
//			goodsSold.put(Goods.MedicalGel, new Boolean(true));
			goodsSold.put(Goods.AI, 		new Boolean(false));
			goodsSold.put(Goods.Singularity,new Boolean(false));
			
			changePrice(Goods.Grain, 		Fluctuate.upSmall);
			changePrice(Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(Goods.Machinery,	Fluctuate.downSmall);
			changePrice(Goods.HeavyWater,	Fluctuate.downSmall);
			changePrice(Goods.SpaceCrack,	Fluctuate.upSmall);
			break;		
		case Technological:
//			goodsSold.put(Goods.Grain, 		new Boolean(true));
//			goodsSold.put(Goods.Textiles, 	new Boolean(true));
//			goodsSold.put(Goods.Minerals, 	new Boolean(true));
//			goodsSold.put(Goods.Machinery, 	new Boolean(true));
//			goodsSold.put(Goods.HeavyWater,	new Boolean(true));
//			goodsSold.put(Goods.Computers, 	new Boolean(true));
//			goodsSold.put(Goods.SpaceCrack, new Boolean(true));
//			goodsSold.put(Goods.MedicalGel, new Boolean(true));
//			goodsSold.put(Goods.AI, 		new Boolean(true));
//			goodsSold.put(Goods.Singularity,new Boolean(true));

			changePrice(Goods.Textiles, 	Fluctuate.upSmall);
			changePrice(Goods.HeavyWater,	Fluctuate.upSmall);
			changePrice(Goods.Computers,	Fluctuate.downSmall);
			break;		
		case Cybernetic:
//			goodsSold.put(Goods.Grain, 		new Boolean(true));
//			goodsSold.put(Goods.Textiles, 	new Boolean(true));
//			goodsSold.put(Goods.Minerals, 	new Boolean(true));
//			goodsSold.put(Goods.Machinery, 	new Boolean(true));
//			goodsSold.put(Goods.HeavyWater,	new Boolean(true));
//			goodsSold.put(Goods.Computers, 	new Boolean(true));
//			goodsSold.put(Goods.SpaceCrack, new Boolean(true));
//			goodsSold.put(Goods.MedicalGel, new Boolean(true));
//			goodsSold.put(Goods.AI, 		new Boolean(true));
//			goodsSold.put(Goods.Singularity,new Boolean(true));

			changePrice(Goods.Computers,	Fluctuate.upSmall);
			changePrice(Goods.MedicalGel,	Fluctuate.downSmall);
			changePrice(Goods.AI,			Fluctuate.upSmall);
			changePrice(Goods.Singularity,	Fluctuate.downSmall);
			break;		
		case Transcendental:
			goodsSold.put(Goods.Grain, 		new Boolean(false));
			goodsSold.put(Goods.Textiles, 	new Boolean(false));
//			goodsSold.put(Goods.Minerals, 	new Boolean(true));
//			goodsSold.put(Goods.Machinery, 	new Boolean(true));
//			goodsSold.put(Goods.HeavyWater,	new Boolean(true));
//			goodsSold.put(Goods.Computers, 	new Boolean(true));
			goodsSold.put(Goods.SpaceCrack, new Boolean(false));
			goodsSold.put(Goods.MedicalGel, new Boolean(false));
			goodsSold.put(Goods.AI, 		new Boolean(false));
//			goodsSold.put(Goods.Singularity,new Boolean(true));
			
			changePrice(Goods.AI,			Fluctuate.downSmall);
			changePrice(Goods.Singularity,	Fluctuate.upSmall);
		}
		
		switch (gov_type) {
		case Aristocracy:
			changePrice(Goods.Grain, 		Fluctuate.upSmall);
			changePrice(Goods.Textiles, 	Fluctuate.upLarge);
			changePrice(Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(Goods.Machinery, 	Fluctuate.downSmall);
			changePrice(Goods.HeavyWater, 	Fluctuate.chance);
			changePrice(Goods.Computers,	Fluctuate.chance);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upLarge);
			changePrice(Goods.MedicalGel, 	Fluctuate.upSmall);
			changePrice(Goods.AI, 			Fluctuate.upHuge);
			changePrice(Goods.Singularity, 	Fluctuate.upLarge);
			break;
		case Autocracy:
			changePrice(Goods.Grain, 		Fluctuate.downLarge);
			changePrice(Goods.Textiles, 	Fluctuate.downLarge);
			changePrice(Goods.Minerals, 	Fluctuate.downLarge);
			changePrice(Goods.Machinery, 	Fluctuate.upSmall);
			changePrice(Goods.HeavyWater, 	Fluctuate.upHuge);
			changePrice(Goods.Computers,	Fluctuate.chance);
			changePrice(Goods.SpaceCrack, 	Fluctuate.downHuge);
			changePrice(Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(Goods.AI, 			Fluctuate.upLarge);
			changePrice(Goods.Singularity, 	Fluctuate.chance);
			break;
		case Bureaucracy:
			changePrice(Goods.Grain, 		Fluctuate.upLarge);
			changePrice(Goods.Textiles, 	Fluctuate.upSmall);
			changePrice(Goods.Minerals, 	Fluctuate.upLarge);
			changePrice(Goods.Machinery, 	Fluctuate.downSmall);
			changePrice(Goods.HeavyWater, 	Fluctuate.downSmall);
			changePrice(Goods.Computers,	Fluctuate.chance);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(Goods.MedicalGel, 	Fluctuate.upLarge);
			changePrice(Goods.AI, 			Fluctuate.upHuge);
			changePrice(Goods.Singularity, 	Fluctuate.upSmall);
			break;
		case Democracy:
			changePrice(Goods.Grain, 		Fluctuate.chance);
			changePrice(Goods.Textiles, 	Fluctuate.chance);
			changePrice(Goods.Minerals, 	Fluctuate.chance);
			changePrice(Goods.Machinery, 	Fluctuate.chance);
			changePrice(Goods.HeavyWater, 	Fluctuate.upSmall);
			changePrice(Goods.Computers,	Fluctuate.chance);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(Goods.AI, 			Fluctuate.downSmall);
			changePrice(Goods.Singularity, 	Fluctuate.chance);
			break;			
		case Dictatorship:
			changePrice(Goods.Grain, 		Fluctuate.upSmall);
			changePrice(Goods.Textiles, 	Fluctuate.downSmall);
			changePrice(Goods.Minerals, 	Fluctuate.chance);
			changePrice(Goods.Machinery, 	Fluctuate.downLarge);
			changePrice(Goods.HeavyWater, 	Fluctuate.upSmall);
			changePrice(Goods.Computers,	Fluctuate.chance);
			changePrice(Goods.SpaceCrack, 	Fluctuate.downHuge);
			changePrice(Goods.MedicalGel, 	Fluctuate.upSmall);
			changePrice(Goods.AI, 			Fluctuate.upLarge);
			changePrice(Goods.Singularity, 	Fluctuate.upLarge);
			break;
		case Ergatocracy:  //Proles
			changePrice(Goods.Grain, 		Fluctuate.downSmall);
			changePrice(Goods.Textiles, 	Fluctuate.chance);
			changePrice(Goods.Minerals, 	Fluctuate.chance);
			changePrice(Goods.Machinery, 	Fluctuate.downLarge);
			changePrice(Goods.HeavyWater, 	Fluctuate.downSmall);
			changePrice(Goods.Computers,	Fluctuate.upLarge);
			changePrice(Goods.SpaceCrack, 	Fluctuate.downLarge);
			changePrice(Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(Goods.AI, 			Fluctuate.upSmall);
			changePrice(Goods.Singularity, 	Fluctuate.chance);
			break;
		case Geniocracy: //wise
			changePrice(Goods.Grain, 		Fluctuate.chance);
			changePrice(Goods.Textiles, 	Fluctuate.chance);
			changePrice(Goods.Minerals, 	Fluctuate.chance);
			changePrice(Goods.Machinery, 	Fluctuate.upSmall);
			changePrice(Goods.HeavyWater, 	Fluctuate.downSmall);
			changePrice(Goods.Computers,	Fluctuate.downLarge);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upLarge);
			changePrice(Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(Goods.AI, 			Fluctuate.downHuge);
			changePrice(Goods.Singularity, 	Fluctuate.downHuge);
			break;
		case Kratocracy: //The strong
			changePrice(Goods.Grain, 		Fluctuate.chance);
			changePrice(Goods.Textiles, 	Fluctuate.upSmall);
			changePrice(Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(Goods.Machinery, 	Fluctuate.downLarge);
			changePrice(Goods.HeavyWater, 	Fluctuate.downLarge);
			changePrice(Goods.Computers,	Fluctuate.upLarge);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upSmall);
			changePrice(Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(Goods.AI, 			Fluctuate.downSmall);
			changePrice(Goods.Singularity, 	Fluctuate.downLarge);
			break;
		case Monarchy:
			changePrice(Goods.Grain, 		Fluctuate.upLarge);
			changePrice(Goods.Textiles, 	Fluctuate.downLarge);
			changePrice(Goods.Minerals, 	Fluctuate.upSmall);
			changePrice(Goods.Machinery, 	Fluctuate.upLarge);
			changePrice(Goods.HeavyWater, 	Fluctuate.chance);
			changePrice(Goods.Computers,	Fluctuate.chance);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upSmall);
			changePrice(Goods.MedicalGel, 	Fluctuate.downSmall);
			changePrice(Goods.AI, 			Fluctuate.upSmall);
			changePrice(Goods.Singularity, 	Fluctuate.chance);
			break;
		case Oligarchy:
			changePrice(Goods.Grain, 		Fluctuate.chance);
			changePrice(Goods.Textiles, 	Fluctuate.chance);
			changePrice(Goods.Minerals, 	Fluctuate.upSmall);
			changePrice(Goods.Machinery, 	Fluctuate.upSmall);
			changePrice(Goods.HeavyWater, 	Fluctuate.downLarge);
			changePrice(Goods.Computers,	Fluctuate.downLarge);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upLarge);
			changePrice(Goods.MedicalGel, 	Fluctuate.downSmall);
			changePrice(Goods.AI, 			Fluctuate.upLarge);
			changePrice(Goods.Singularity, 	Fluctuate.downLarge);
			break;
		case Plutocracy:
			changePrice(Goods.Grain, 		Fluctuate.downLarge);
			changePrice(Goods.Textiles, 	Fluctuate.upLarge);
			changePrice(Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(Goods.Machinery, 	Fluctuate.downSmall);
			changePrice(Goods.HeavyWater, 	Fluctuate.chance);
			changePrice(Goods.Computers,	Fluctuate.upSmall);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(Goods.MedicalGel, 	Fluctuate.upLarge);
			changePrice(Goods.AI, 			Fluctuate.upHuge);
			changePrice(Goods.Singularity, 	Fluctuate.upLarge);
			break;
		case Republic:
			changePrice(Goods.Grain, 		Fluctuate.downSmall);
			changePrice(Goods.Textiles, 	Fluctuate.downSmall);
			changePrice(Goods.Minerals, 	Fluctuate.upSmall);
			changePrice(Goods.Machinery, 	Fluctuate.upLarge);
			changePrice(Goods.HeavyWater, 	Fluctuate.chance);
			changePrice(Goods.Computers,	Fluctuate.chance);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(Goods.MedicalGel, 	Fluctuate.downLarge);
			changePrice(Goods.AI, 			Fluctuate.downLarge);
			changePrice(Goods.Singularity, 	Fluctuate.downSmall);
			break;
		case Technocracy:
			changePrice(Goods.Grain, 		Fluctuate.chance);
			changePrice(Goods.Textiles, 	Fluctuate.chance);
			changePrice(Goods.Minerals, 	Fluctuate.upLarge);
			changePrice(Goods.Machinery, 	Fluctuate.upSmall);
			changePrice(Goods.HeavyWater, 	Fluctuate.downSmall);
			changePrice(Goods.Computers,	Fluctuate.downHuge);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upLarge);
			changePrice(Goods.MedicalGel, 	Fluctuate.downLarge);
			changePrice(Goods.AI, 			Fluctuate.downHuge);
			changePrice(Goods.Singularity, 	Fluctuate.downLarge);
			break;
		case Theocracy:
			changePrice(Goods.Grain, 		Fluctuate.chance);
			changePrice(Goods.Textiles, 	Fluctuate.downLarge);
			changePrice(Goods.Minerals, 	Fluctuate.downLarge);
			changePrice(Goods.Machinery, 	Fluctuate.downSmall);
			changePrice(Goods.HeavyWater, 	Fluctuate.upLarge);
			changePrice(Goods.Computers,	Fluctuate.upSmall);
			changePrice(Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(Goods.AI, 			Fluctuate.upHuge);
			changePrice(Goods.Singularity, 	Fluctuate.upHuge);
			break;
		}
	}
	
	public void newYear(int _n_years) {
		while (_n_years-- > 0) {
			for (Goods _g : Goods.values()) {
				final int current = price.get(_g).get(Utility.getUtility().getStarDate()).get();
				final float sigma = (float)current * ((float)volitility.get(_g).get() / 100f);
				final int _new = Math.abs( Math.round( Utility.getUtility().getRandG(current, sigma) ) );
				price.get(_g).add( new AtomicInteger(_new) );
				//Mod stock
				final int _stock = stock.get(_g).get();
				final int _stockTarget = stockTarget.get(_g).get();
				if (_stock > _stockTarget) {	//TODO add something to do with size of planet
					stock.get(_g).decrementAndGet();
				} else if (_stock < _stockTarget) {
					stock.get(_g).incrementAndGet();
				}
			}
		}
	}
	
	private void changePrice(Goods _g, Fluctuate _f) {
		final int current = price.get(_g).get(Utility.getUtility().getStarDate()).get();
		final int updated = Math.round(current * _f.get());
		price.get(_g).get(Utility.getUtility().getStarDate()).set(updated);
	}
	
	private void changeVolatility(Goods _g, Fluctuate _f) { //TODO change volatility
		final int current = volitility.get(_g).get();
		final int updated = Math.round(current * _f.get());
		volitility.get(_g).set(updated);
	}
	
	
	public float dst(Vector2 _comp) {
		return dst(_comp.x, _comp.y);
	}
	
	public float dst(float _x, float _y) {
		v2.set(getX(), getY());
		return v2.dst(_x, _y);
	}
	
	public int getPrice(Goods _g) {
		return getPrice(_g, Utility.getUtility().getStarDate());
	}
	
	public int getPrice(Goods _g, int _starDate) {
		return  price.get(_g).get(_starDate).get();
	}
	
	public int getStock(Goods _g) {
		return stock.get(_g).get();
	}
	
	public void modStock(Goods _g, int _amount) {
		stock.get(_g).getAndAdd(_amount);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void printStat() {
		Gdx.app.log("Planet", "---------- "+name+" is a "+civ_type+" "+gov_type+" ----------");
		Gdx.app.log("Planet", "\tGRN\tTEX\tMIN\tMAC\tH20\tCOM\tCRK\tMED\tAI\tSNG");
		for (int Y=0; Y < Utility.getUtility().getStarDate(); ++Y) {
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


}
