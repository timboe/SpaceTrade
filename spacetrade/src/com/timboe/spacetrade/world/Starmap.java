package com.timboe.spacetrade.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.utility.Utility;

public class Starmap {
	
	private static Starmap singleton;
	public static final Starmap getStarmap() {
		return singleton;
	}
	public static final void newStarmap() {
		singleton = new Starmap();
	}
	
	private static int starBuffer = Textures.getTextures().getStar().getWidth() * 2;
	private final static int nPlanets = 128;
	private final static float toLightYears = 4.2f;
	private Array<Planet> thePlanets = new Array<Planet>();
	private static int starDate = 0;	
	private final static float G = 1.03f; //lightyears per year per year
	private final static float C = 1.f; //lightyear per year

	public Starmap() {
		populate();
		Gdx.app.log("Starmap", "Populating, size:"+thePlanets.size);
	}
	
	public synchronized Array<Planet> getPlanets() {
		return thePlanets;
	}
	
	public static int getStarDate() {
		return starDate;
	}
	
	public void newYear(int _n_years) {
		for (Planet _p : thePlanets) {
			_p.newYear(_n_years);
		}
		starDate += _n_years;
	}
	
	private void populate() {
		int ID = 0;
		while (thePlanets.size < getNPlanets()) {
			int _x = Utility.getUtility().getRandI( (int) (Utility.GAME_WIDTH  - (2 * starBuffer)) ) + starBuffer;
			int _y = Utility.getUtility().getRandI( (int) (Utility.GAME_HEIGHT - (2 * starBuffer)) ) + starBuffer;
			//Check we're not too close
			boolean tooClose = false;
			for (Planet p : thePlanets) {
				if (p.dst(_x, _y) < starBuffer) tooClose = true;
			}
			if (tooClose == false) {
				final Planet newPlanet = new Planet(_x, _y, ID++);
				newPlanet.addListener(new ClickListener() {
			        @Override
			        public void clicked(InputEvent event, float x, float y) {
			        	if (isOver() == true) {
			        		Gdx.app.log("PlanetButton","Interact:"+event.toString()+" "+newPlanet.getName());
			        		StarmapScreen.setPlanetClickedID(newPlanet.getID());
			        		if (event.getButton() != 0) {
			        			Player.getPlayer().move(newPlanet);
			        		}
			        	}
			        }
			    });
				thePlanets.add( newPlanet );
			}
		}
		//Get the economy going!
		newYear(1000);
	}
	
	public static float getDistanceLightyear(Planet _local, Planet _remote) {
		final float _sep = _local.dst( _remote );
		return  _sep * ( toLightYears / starBuffer); 
		
	}
	
	public static float getTravelTimeGalactic(Planet _local, Planet _remote, float _g) {
		final float d = getDistanceLightyear(_local, _remote);
		final float a = _g * G;
		final float c = C;
		//sqrt[(d/c)2 + 2d/a]
		return (float) Math.sqrt( Math.pow((d/c),2) +  ((2*d)/a) );
	}
	
	public static float getTravelTimeShip(Planet _local, Planet _remote, float _g) {
		final float d = getDistanceLightyear(_local, _remote);
		final float a = _g * G;
		final float c = C;
		//(c/a) ch-1 [ad/c2 + 1]
		return (float) ( (c/a) * Utility.acosh( ((a*d)/(c*c)) + 1 ) );
	}
	


	public static int getNPlanets() {
		return nPlanets;
	}

	public Planet getPlanet(int _ID) {
		return thePlanets.get(_ID);
	}

	public Planet getRandomPlanet() {
		return thePlanets.get( Utility.getUtility().getRandI(getNPlanets()) );
	}

	public static void setStarmap(Starmap _s) {
		singleton = _s;
	}

	public void refresh() {
		for (final Planet _p : thePlanets) {
			_p.refresh();
			_p.addListener(new ClickListener() {
		        @Override
		        public void clicked(InputEvent event, float x, float y) {
		        	if (isOver() == true) {
		        		Gdx.app.log("PlanetButton","Interact:"+event.toString()+" "+_p.getName());
		        		Player.getPlayer().move(_p);
		        	}
		        }
		    });
		}
	}

	public void unHookListners() {
		for (final Planet _p : thePlanets) {
			_p.removeListener( _p.getListeners().first() );
		}		
	}
	
}
