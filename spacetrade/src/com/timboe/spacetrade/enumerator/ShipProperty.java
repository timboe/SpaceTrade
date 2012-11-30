package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Rnd;

public enum ShipProperty {
	Shiny, //Strong v Lasers, Weak v MassDriver
	Rigid, //Strong v MassDriver, Weak v Missile
	Dark; //Strong v Missile, Weak v Laser
	
	private static final List<ShipProperty> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();
	private static final Rnd rnd = new Rnd();

	public static ShipProperty random()  {
		return content.get( rnd.getRandI(size) );
	}
}
