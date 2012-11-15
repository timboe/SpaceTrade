package com.timboe.spacetrade.enumerator;

public enum Goods {
	Grain		(50,	 25),
	Textiles	(100,	 30),
	Minerals	(200,	 20),
	Machinery	(400,	 5),
	HeavyWater	(250,	 10),
	Computers	(500,	 50),
	SpaceCrack	(750,	 50),
	MedicalGel	(1000,	 30),
	AI			(1500,	 20),
	Singularity	(3000,	 25);
	
	private int basePrice;
	private int baseAmount;
	Goods(int _bp, int _ba) { 
		basePrice = _bp;
		baseAmount = _ba;
	}
	
	public int getBaseAmount() {
		return baseAmount;
	}
	
	public int getBasePrice() {
		return basePrice;
	}
}
