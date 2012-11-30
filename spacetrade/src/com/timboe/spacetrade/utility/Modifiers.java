package com.timboe.spacetrade.utility;

import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.Fluctuate;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.Government;
import com.timboe.spacetrade.enumerator.PlanetActivity;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.world.Planet;

public class Modifiers {

	public static void setPrices(Government govType, Civilisation civType, WorldSize worldSize, Planet _p) {
		switch (civType) {
		//Have matching pair of up and down fluctuations per type
		case Agricultural:
			_p.setGoodsSold(Goods.HeavyWater, false);
			_p.setGoodsSold(Goods.Computers, false);
			_p.setGoodsSold(Goods.MedicalGel, false);
			_p.setGoodsSold(Goods.AI, 		false);
			_p.setGoodsSold(Goods.Singularity,false);

			changePrice(_p, Goods.Grain, 		Fluctuate.downSmall);
			changePrice(_p, Goods.Minerals, 	Fluctuate.upSmall);
			changePrice(_p, Goods.Machinery,	Fluctuate.upSmall);
			break;
		case Renaissance:
			_p.setGoodsSold(Goods.HeavyWater,	false);
			_p.setGoodsSold(Goods.Computers, 	false);
			_p.setGoodsSold(Goods.AI, 		false);
			_p.setGoodsSold(Goods.Singularity,false);

			changePrice(_p, Goods.Textiles, 	Fluctuate.downSmall);
			changePrice(_p, Goods.MedicalGel,	Fluctuate.upSmall);
			changePrice(_p, Goods.SpaceCrack,	Fluctuate.downSmall);
			_p.addShipSold(ShipClass.Tiny);
			_p.addShipSold(ShipClass.Starting);
			_p.addWeapons(2, 1); //two xLevel1
			break;		
		case Industrial:
			_p.setGoodsSold(Goods.AI, 		false);
			_p.setGoodsSold(Goods.Singularity,false);
			
			changePrice(_p, Goods.Grain, 		Fluctuate.upSmall);
			changePrice(_p, Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Machinery,	Fluctuate.downSmall);
			changePrice(_p, Goods.HeavyWater,	Fluctuate.downSmall);
			changePrice(_p, Goods.SpaceCrack,	Fluctuate.upSmall);
			
			for (ShipClass _sc : ShipClass.values()) {
				if (_sc == ShipClass.TradeC || _sc == ShipClass.FightC || _sc == ShipClass.AllroundC) continue;
				if (_sc == ShipClass.TradeB || _sc == ShipClass.FightB) continue;
				_p.addShipSold(_sc);
			}
			_p.addWeapons(1, 2); //one xLevel2
			_p.addWeapons(2, 1); //two xLevel1
			break;		
		case Technological:
			changePrice(_p, Goods.Textiles, 	Fluctuate.upSmall);
			changePrice(_p, Goods.HeavyWater,	Fluctuate.upSmall);
			changePrice(_p, Goods.Computers,	Fluctuate.downSmall);
			
			for (ShipClass _sc : ShipClass.values()) {
				if (_sc == ShipClass.TradeC || _sc == ShipClass.FightC || _sc == ShipClass.AllroundC) continue;
				_p.addShipSold(_sc);
			}
			_p.addWeapons(1, 3); //one xLevel3
			_p.addWeapons(1, 2); //one xLevel2
			_p.addWeapons(2, 1); //two xLevel1
			break;		
		case Cybernetic:
			changePrice(_p, Goods.Computers,	Fluctuate.upSmall);
			changePrice(_p, Goods.MedicalGel,	Fluctuate.downSmall);
			changePrice(_p, Goods.AI,			Fluctuate.upSmall);
			changePrice(_p, Goods.Singularity,	Fluctuate.downSmall);
			
			for (ShipClass _sc : ShipClass.values()) {
				_p.addShipSold(_sc);
			}
			_p.addWeapons(1, 3); //one xLevel3
			_p.addWeapons(2, 2); //two xLevel2
			_p.addWeapons(2, 1); //two xLevel1
			break;		
		case Transcendental:
			_p.setGoodsSold(Goods.Grain, 		false);
			_p.setGoodsSold(Goods.Textiles, 	false);
			_p.setGoodsSold(Goods.SpaceCrack, false);
			_p.setGoodsSold(Goods.MedicalGel, false);
			_p.setGoodsSold(Goods.AI, 		false);
			
			changePrice(_p, Goods.AI,			Fluctuate.downSmall);
			changePrice(_p, Goods.Singularity,	Fluctuate.upSmall);
			
			for (ShipClass _sc : ShipClass.values()) {
				_p.addShipSold(_sc);
			}
			_p.addWeapons(2, 3); //two xLevel3
			_p.addWeapons(2, 2); //two xLevel2
			_p.addWeapons(2, 1); //two xLevel1
		}
		
		switch (govType) {
		case Aristocracy:
			changePrice(_p, Goods.Grain, 		Fluctuate.upSmall);
			changePrice(_p, Goods.Textiles, 	Fluctuate.upLarge);
			changePrice(_p, Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Machinery, 	Fluctuate.downSmall);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.chance);
			changePrice(_p, Goods.Computers,	Fluctuate.chance);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upLarge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.upSmall);
			changePrice(_p, Goods.AI, 			Fluctuate.upHuge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.upLarge);
			
			_p.setActivity(PlanetActivity.Few , PlanetActivity.Few, PlanetActivity.Few);
			break;
		case Autocracy:
			changePrice(_p, Goods.Grain, 		Fluctuate.downLarge);
			changePrice(_p, Goods.Textiles, 	Fluctuate.downLarge);
			changePrice(_p, Goods.Minerals, 	Fluctuate.downLarge);
			changePrice(_p, Goods.Machinery, 	Fluctuate.upSmall);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.upHuge);
			changePrice(_p, Goods.Computers,	Fluctuate.chance);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.downHuge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(_p, Goods.AI, 			Fluctuate.upLarge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.chance);
			
			_p.setActivity(PlanetActivity.Swarms, PlanetActivity.Few, PlanetActivity.Many);
			break;
		case Bureaucracy:
			changePrice(_p, Goods.Grain, 		Fluctuate.upLarge);
			changePrice(_p, Goods.Textiles, 	Fluctuate.upSmall);
			changePrice(_p, Goods.Minerals, 	Fluctuate.upLarge);
			changePrice(_p, Goods.Machinery, 	Fluctuate.downSmall);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Computers,	Fluctuate.chance);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.upLarge);
			changePrice(_p, Goods.AI, 			Fluctuate.upHuge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.upSmall);
			
			_p.setActivity(PlanetActivity.Few, PlanetActivity.Swarms, PlanetActivity.Some);
			break;
		case Democracy:
			changePrice(_p, Goods.Grain, 		Fluctuate.chance);
			changePrice(_p, Goods.Textiles, 	Fluctuate.chance);
			changePrice(_p, Goods.Minerals, 	Fluctuate.chance);
			changePrice(_p, Goods.Machinery, 	Fluctuate.chance);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.upSmall);
			changePrice(_p, Goods.Computers,	Fluctuate.chance);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(_p, Goods.AI, 			Fluctuate.downSmall);
			changePrice(_p, Goods.Singularity, 	Fluctuate.chance);
			
			_p.setActivity(PlanetActivity.Some, PlanetActivity.Some, PlanetActivity.Many);
			break;			
		case Dictatorship:
			changePrice(_p, Goods.Grain, 		Fluctuate.upSmall);
			changePrice(_p, Goods.Textiles, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Minerals, 	Fluctuate.chance);
			changePrice(_p, Goods.Machinery, 	Fluctuate.downLarge);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.upSmall);
			changePrice(_p, Goods.Computers,	Fluctuate.chance);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.downHuge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.upSmall);
			changePrice(_p, Goods.AI, 			Fluctuate.upLarge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.upLarge);
			
			_p.setActivity(PlanetActivity.Swarms, PlanetActivity.None, PlanetActivity.Few);
			break;
		case Ergatocracy:  //Proles
			changePrice(_p, Goods.Grain, 		Fluctuate.downSmall);
			changePrice(_p, Goods.Textiles, 	Fluctuate.chance);
			changePrice(_p, Goods.Minerals, 	Fluctuate.chance);
			changePrice(_p, Goods.Machinery, 	Fluctuate.downLarge);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Computers,	Fluctuate.upLarge);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.downLarge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(_p, Goods.AI, 			Fluctuate.upSmall);
			changePrice(_p, Goods.Singularity, 	Fluctuate.chance);
			
			_p.setActivity(PlanetActivity.Many, PlanetActivity.None, PlanetActivity.Some);
			break;
		case Geniocracy: //wise
			changePrice(_p, Goods.Grain, 		Fluctuate.chance);
			changePrice(_p, Goods.Textiles, 	Fluctuate.chance);
			changePrice(_p, Goods.Minerals, 	Fluctuate.chance);
			changePrice(_p, Goods.Machinery, 	Fluctuate.upSmall);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Computers,	Fluctuate.downLarge);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upLarge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(_p, Goods.AI, 			Fluctuate.downHuge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.downHuge);
			
			_p.setActivity(PlanetActivity.Some, PlanetActivity.Some, PlanetActivity.Some);
			break;
		case Kratocracy: //The strong
			changePrice(_p, Goods.Grain, 		Fluctuate.chance);
			changePrice(_p, Goods.Textiles, 	Fluctuate.upSmall);
			changePrice(_p, Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Machinery, 	Fluctuate.downLarge);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.downLarge);
			changePrice(_p, Goods.Computers,	Fluctuate.upLarge);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upSmall);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(_p, Goods.AI, 			Fluctuate.downSmall);
			changePrice(_p, Goods.Singularity, 	Fluctuate.downLarge);
			
			_p.setActivity(PlanetActivity.Few, PlanetActivity.Many, PlanetActivity.Some);
			break;
		case Monarchy:
			changePrice(_p, Goods.Grain, 		Fluctuate.upLarge);
			changePrice(_p, Goods.Textiles, 	Fluctuate.downLarge);
			changePrice(_p, Goods.Minerals, 	Fluctuate.upSmall);
			changePrice(_p, Goods.Machinery, 	Fluctuate.upLarge);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.chance);
			changePrice(_p, Goods.Computers,	Fluctuate.chance);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upSmall);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.downSmall);
			changePrice(_p, Goods.AI, 			Fluctuate.upSmall);
			changePrice(_p, Goods.Singularity, 	Fluctuate.chance);
			
			_p.setActivity(PlanetActivity.Some, PlanetActivity.Many, PlanetActivity.Many);
			break;
		case Oligarchy: //ruled by the few
			changePrice(_p, Goods.Grain, 		Fluctuate.chance);
			changePrice(_p, Goods.Textiles, 	Fluctuate.chance);
			changePrice(_p, Goods.Minerals, 	Fluctuate.upSmall);
			changePrice(_p, Goods.Machinery, 	Fluctuate.upSmall);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.downLarge);
			changePrice(_p, Goods.Computers,	Fluctuate.downLarge);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upLarge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.downSmall);
			changePrice(_p, Goods.AI, 			Fluctuate.upLarge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.downLarge);
			
			_p.setActivity(PlanetActivity.Many , PlanetActivity.Many, PlanetActivity.Swarms);
			break;
		case Plutocracy: //wealthy
			changePrice(_p, Goods.Grain, 		Fluctuate.downLarge);
			changePrice(_p, Goods.Textiles, 	Fluctuate.upLarge);
			changePrice(_p, Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Machinery, 	Fluctuate.downSmall);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.chance);
			changePrice(_p, Goods.Computers,	Fluctuate.upSmall);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.upLarge);
			changePrice(_p, Goods.AI, 			Fluctuate.upHuge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.upLarge);
			
			_p.setActivity(PlanetActivity.Many, PlanetActivity.Some, PlanetActivity.Many);
			break;
		case Republic:
			changePrice(_p, Goods.Grain, 		Fluctuate.downSmall);
			changePrice(_p, Goods.Textiles, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Minerals, 	Fluctuate.upSmall);
			changePrice(_p, Goods.Machinery, 	Fluctuate.upLarge);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.chance);
			changePrice(_p, Goods.Computers,	Fluctuate.chance);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.downLarge);
			changePrice(_p, Goods.AI, 			Fluctuate.downLarge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.downSmall);
			
			_p.setActivity(PlanetActivity.None, PlanetActivity.Many, PlanetActivity.Swarms);
			break;
		case Technocracy: //scientists, engineers
			changePrice(_p, Goods.Grain, 		Fluctuate.chance);
			changePrice(_p, Goods.Textiles, 	Fluctuate.chance);
			changePrice(_p, Goods.Minerals, 	Fluctuate.upLarge);
			changePrice(_p, Goods.Machinery, 	Fluctuate.upSmall);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Computers,	Fluctuate.downHuge);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upLarge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.downLarge);
			changePrice(_p, Goods.AI, 			Fluctuate.downHuge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.downLarge);
			
			_p.setActivity(PlanetActivity.None, PlanetActivity.Few, PlanetActivity.Many);
			break;
		case Theocracy:
			changePrice(_p, Goods.Grain, 		Fluctuate.chance);
			changePrice(_p, Goods.Textiles, 	Fluctuate.downLarge);
			changePrice(_p, Goods.Minerals, 	Fluctuate.downLarge);
			changePrice(_p, Goods.Machinery, 	Fluctuate.downSmall);
			changePrice(_p, Goods.HeavyWater, 	Fluctuate.upLarge);
			changePrice(_p, Goods.Computers,	Fluctuate.upSmall);
			changePrice(_p, Goods.SpaceCrack, 	Fluctuate.upHuge);
			changePrice(_p, Goods.MedicalGel, 	Fluctuate.chance);
			changePrice(_p, Goods.AI, 			Fluctuate.upHuge);
			changePrice(_p, Goods.Singularity, 	Fluctuate.upHuge);
			
			_p.setActivity(PlanetActivity.Some, PlanetActivity.Swarms, PlanetActivity.Few);
			break;
		}
		
		switch (worldSize) {
		case Small:
			for (Goods _g : Goods.values()) {
				changePrice(_p, _g, Fluctuate.upSmall);
				changeTargetStock(_p, _g, Fluctuate.downLarge);
			}
			break;
		case Medium:
			break;
		case Large:
			for (Goods _g : Goods.values()) {
				changePrice(_p, _g, Fluctuate.downSmall);
				changeTargetStock(_p, _g, Fluctuate.upLarge);
			}
			break;
		}
	}
	
	private static void changePrice(Planet _p, Goods _g, Fluctuate _f) {
		final int current = _p.getPrice(_g);
		final int updated = Math.round(current * _f.get());
		_p.setPrice(_g, updated);
	}
	
	private static void changeVolatility(Planet _p, Goods _g, Fluctuate _f) { //TODO change volatility
		final int current = _p.getVolatility(_g);
		final int updated = Math.round(current * _f.get());
		_p.setVolatility(_g, updated);
	}
	
	private static void changeTargetStock(Planet _p, Goods _g, Fluctuate _f) {
		final int current = _p.getStockTarget(_g);
		final int updated = Math.round(current * _f.get());
		_p.setStockTarget(_g, updated);
	}
}
