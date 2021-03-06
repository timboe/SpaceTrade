package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Rnd;

public enum Goods {
	Grain		(50,	 25,	"Grain"),
	Textiles	(100,	 30,	"Textiles"),
	Minerals	(200,	 20,	"Minerals"),
	Machinery	(400,	 5,		"Machinery"),
	HeavyWater	(250,	 10,	"Deuterated Water"),
	Computers	(500,	 50,	"Computers"),
	SpaceCrack	(750,	 50,	"Space Crack"),
	MedicalGel	(1000,	 30,	"Medical Gel"),
	AI			(1500,	 20,	"Artificial Intelgence"),
	Singularity	(3000,	 25,	"Singularities");
	
	private static final List<Goods> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();
	private static final Rnd rnd = new Rnd();
	
	private int basePrice;
	private int baseAmount;
	private String displayText;
	Goods(int _bp, int _ba, String _dt) { 
		basePrice = _bp;
		baseAmount = _ba;
		displayText = _dt;
	}
	
	public int getBaseAmount() {
		return baseAmount;
	}
	
	public int getBasePrice() {
		return basePrice;
	}
	
	public String toDisplayString() {
		return displayText;
	}

	public static Goods random() {
		return content.get( rnd.getRandI(size) );
	}
	
	
}
