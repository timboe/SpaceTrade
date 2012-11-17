package com.timboe.spacetrade.world;

import java.util.ArrayList;
import com.timboe.spacetrade.utility.Utility;

public class Starmap {
	
	private static final Starmap singleton = new Starmap();
	
	public static final Starmap getStarmap() {
		return singleton;
	}
	
	private Starmap() {
		populate();

	}
	
	
	private Utility util = Utility.getUtility();

	
	
	private final int starBuffer = 20;
	private final int nStars = 256;
	private final ArrayList<Planet> thePlanets = new ArrayList<Planet>(); 
	
	
	public synchronized ArrayList<Planet> getPlanets() {
		return thePlanets;
	}
	
	
	public void newYear(int _n_years) {
		for (Planet _p : thePlanets) {
			_p.newYear(_n_years);
		}
		util.newYear(_n_years);
	}
	
	private void populate() {
		while (thePlanets.size() < nStars) {
			int _x = util.getRandI( (int) (Utility.GAME_WIDTH  - (2 * starBuffer)) ) + starBuffer;
			int _y = util.getRandI( (int) (Utility.GAME_HEIGHT - (2 * starBuffer)) ) + starBuffer;
			//Check we're not too close
			boolean tooClose = false;
			for (Planet p : thePlanets) {
				if (p.dist(_x, _y) < starBuffer) tooClose = true;
			}
			if (tooClose == false) {
				thePlanets.add( new Planet(_x, _y) );
			}
		}
	}
	
}
