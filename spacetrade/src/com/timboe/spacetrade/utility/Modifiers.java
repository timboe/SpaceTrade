package com.timboe.spacetrade.utility;

import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.Fluctuate;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.Government;
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
			break;		
		case Industrial:
			_p.setGoodsSold(Goods.AI, 		false);
			_p.setGoodsSold(Goods.Singularity,false);
			
			changePrice(_p, Goods.Grain, 		Fluctuate.upSmall);
			changePrice(_p, Goods.Minerals, 	Fluctuate.downSmall);
			changePrice(_p, Goods.Machinery,	Fluctuate.downSmall);
			changePrice(_p, Goods.HeavyWater,	Fluctuate.downSmall);
			changePrice(_p, Goods.SpaceCrack,	Fluctuate.upSmall);
			break;		
		case Technological:
			changePrice(_p, Goods.Textiles, 	Fluctuate.upSmall);
			changePrice(_p, Goods.HeavyWater,	Fluctuate.upSmall);
			changePrice(_p, Goods.Computers,	Fluctuate.downSmall);
			break;		
		case Cybernetic:
			changePrice(_p, Goods.Computers,	Fluctuate.upSmall);
			changePrice(_p, Goods.MedicalGel,	Fluctuate.downSmall);
			changePrice(_p, Goods.AI,			Fluctuate.upSmall);
			changePrice(_p, Goods.Singularity,	Fluctuate.downSmall);
			break;		
		case Transcendental:
			_p.setGoodsSold(Goods.Grain, 		false);
			_p.setGoodsSold(Goods.Textiles, 	false);
			_p.setGoodsSold(Goods.SpaceCrack, false);
			_p.setGoodsSold(Goods.MedicalGel, false);
			_p.setGoodsSold(Goods.AI, 		false);
			
			changePrice(_p, Goods.AI,			Fluctuate.downSmall);
			changePrice(_p, Goods.Singularity,	Fluctuate.upSmall);
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
			break;
		case Oligarchy:
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
			break;
		case Plutocracy:
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
			break;
		case Technocracy:
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
