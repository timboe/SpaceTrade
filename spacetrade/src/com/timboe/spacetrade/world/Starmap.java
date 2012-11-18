package com.timboe.spacetrade.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.utility.Utility;

public class Starmap {
	
	private static final Starmap singleton = new Starmap();
	public static final Starmap getStarmap() {
		return singleton;
	}
	
	private final int starBuffer = Textures.getTextures().getStar().getWidth() * 2;
	private final int nStars = 256;
	private final Array<Planet> thePlanets = new Array<Planet>(); 
	
	private Starmap() {
		populate();
	}
	
	public synchronized Array<Planet> getPlanets() {
		return thePlanets;
	}
	
	public void newYear(int _n_years) {
		for (Planet _p : thePlanets) {
			_p.newYear(_n_years);
		}
		Utility.getUtility().newYear(_n_years);
	}
	
	private void populate() {
		while (thePlanets.size < nStars) {
			int _x = Utility.getUtility().getRandI( (int) (Utility.GAME_WIDTH  - (2 * starBuffer)) ) + starBuffer;
			int _y = Utility.getUtility().getRandI( (int) (Utility.GAME_HEIGHT - (2 * starBuffer)) ) + starBuffer;
			//Check we're not too close
			boolean tooClose = false;
			for (Planet p : thePlanets) {
				if (p.dst(_x, _y) < starBuffer) tooClose = true;
			}
			if (tooClose == false) {
				final Planet newPlanet = new Planet(_x, _y);
				newPlanet.addListener(new ClickListener() {
			        @Override
			        public void clicked(InputEvent event, float x, float y) {
			        	if (isOver() == true) {
			        		Gdx.app.log("PlanetButton","Interact:"+event.toString()+" "+newPlanet.getName());
			        	}
			        }
			    });
				thePlanets.add( newPlanet );
			}
		}
	}
	
}
