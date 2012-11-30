package com.timboe.spacetrade.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.utility.Rnd;

public class Starmap {
	
	private static final int starBuffer = (int) Math.round((Textures.getStar().getWidth()/2f) * Math.sqrt(2)) * 3 ;
	public static final int travelScale = (int) (starBuffer * 0.1f);
	private static final int nPlanets = 128;
	public static final float toLightYears = 4.2f; //Average solar separation in lightyears
	private static final Array<Planet> thePlanets = new Array<Planet>();
	private static final float G = 1.03f; //lightyears per year per year
	private static final float C = 1.f; //lightyear per year
	public static final float SPEED = 500f;// for animation
	
	private static int starDate = 0;
	private static float starDateGalaxy = 2048;
	private static float starDateShip = 2048;
	private static float remainder = 0;
	private static int localPlanetID = -1;
	private static IntArray localPlanets = new IntArray();
	private static int localArrayLocation = 0;
	
	private static Rnd rnd = new Rnd();

	public static synchronized Array<Planet> getPlanets() {
		return thePlanets;
	}
	
	public static int getStarDate() {
		return starDate;
	}
	
	public static void doTravelTime(float _galaxyTime, float _shipTime) {
		starDateGalaxy += _galaxyTime;
		starDateShip += _shipTime;
		Player.getShip().travel(_shipTime);
		_galaxyTime += remainder; //take into account remainder from last time
		//run on the sim
		newYear((int) Math.floor(_galaxyTime));
		remainder = (float) (_galaxyTime - Math.floor(_galaxyTime));
	}
	
	private static void newYear(int _n_years) {
		for (Planet _p : thePlanets) {
			_p.newYear(_n_years);
		}
		starDate += _n_years;
	}
	
	public static void populate() {
		Gdx.app.log("Starmap", "Populating, size:"+thePlanets.size);

		int ID = 0;
		while (thePlanets.size < getNPlanets()) {
			int _x = rnd.getRandI((int)(Textures.getGalaxyTexture().getWidth()  - starBuffer - SpaceTrade.GUI_WIDTH)) + starBuffer/4;
			int _y = rnd.getRandI((int)(Textures.getGalaxyTexture().getHeight() - starBuffer)) + starBuffer/4;
			//Check we're not too close
			if (thePlanets.size == 0) { //first one in the centre
				_x = Textures.getGalaxyTexture().getWidth()/2;
				_y = Textures.getGalaxyTexture().getHeight()/2;
			}
			boolean tooClose = false;
			boolean tooFar = true;
			for (Planet p : thePlanets) {
				if (p.dst(_x, _y) < 1*starBuffer) tooClose = true;
				if (p.dst(_x, _y) < ShipClass.Starting.getRange()) tooFar = false;

			}
			if (thePlanets.size == 0 || (tooClose == false && tooFar == false)) {
				final Planet newPlanet = new Planet(_x, _y, ID++);
				newPlanet.addListener(new ClickListener() {
			        @Override
			        public void clicked(InputEvent event, float x, float y) {
			        	if (isOver() == true) {
			        		Gdx.app.log("PlanetButton","Interact:"+event.toString()+" "+newPlanet.getName());
			        		StarmapScreen.setPlanetClickedID(newPlanet.getID());
			        	}
			        }
			    });
				thePlanets.add( newPlanet );
				Gdx.app.log("Starmap", "Populating, size:"+thePlanets.size);
			}
		}
		//Get the economy going!
		newYear(200);
	}
	
	public static float getDistanceLightyear(Planet _local, Planet _remote) {
		final float _sep = _local.dst( _remote );
		return  _sep * ( toLightYears / starBuffer); 
		
	}
	
	public static float getTravelTimeGalactic(Planet _local, Planet _remote, float _g) {
		final float d = getDistanceLightyear(_local, _remote)/2f; 		//need to change around at half way point!
		final float a = _g * G;
		final float c = C;
		//sqrt[(d/c)2 + 2d/a]
		
		return (float) (2 * (Math.sqrt( Math.pow((d/c),2) +  ((2*d)/a) )) );
	}
	
	public static float acosh(float _v) {
		return (float) Math.log(_v + Math.sqrt(_v * _v - 1));
	}
	
	public static float getTravelTimeShip(Planet _local, Planet _remote, float _g) {
		final float d = getDistanceLightyear(_local, _remote)/2f; 		//need to change around at half way point!
		final float a = _g * G;
		final float c = C;
		//(c/a) ch-1 [ad/c2 + 1]
		return (float) 2 * ( (c/a) * acosh( ((a*d)/(c*c)) + 1 ) );
	}
	
	public static int prevNearbyPlanet() {
		if (Player.getPlanet().getID() != localPlanetID) {
			recalculateNearbyPlanets();
		}
		--localArrayLocation;
		checkLocalArrayLocation();
		return localPlanets.get(localArrayLocation);
	}
	
	public static int nextNearbyPlanet() {
		if (Player.getPlanet().getID() != localPlanetID) {
			recalculateNearbyPlanets();
		}
		++localArrayLocation;
		checkLocalArrayLocation();
		return localPlanets.get(localArrayLocation);
	}
	
	private static void checkLocalArrayLocation() {
		if (localArrayLocation < 0) {
			localArrayLocation = localPlanets.size - 1;
		} else if (localArrayLocation == localPlanets.size) {
			localArrayLocation = 0;
		}
	}
	
	private static void recalculateNearbyPlanets() {
		localPlanetID = Player.getPlanet().getID();
		localArrayLocation = 0;
		localPlanets.clear();
		for (Planet _p : thePlanets) {
			if (_p.getID() == localPlanetID) continue;
			if (_p.dst( getPlanet(localPlanetID) ) < Player.getShip().getRange()) {
				localPlanets.add(_p.getID());
			}
		}
	}
	
	private static void setNearbyPlanetFocus(int _ID) {
		if (Player.getPlanet().getID() != localPlanetID) {
			recalculateNearbyPlanets();
		}
		for (int i=0; i < localPlanets.size; ++i) {
			if (localPlanets.get(i) == _ID) {
				localArrayLocation = i;
				return;
			}
		}
	}


	public static int getNPlanets() {
		return nPlanets;
	}

	public static Planet getPlanet(int _ID) {
		return thePlanets.get(_ID);
	}

	public static Planet getRandomPlanet() {
		return thePlanets.get( rnd.getRandI(getNPlanets()) );
	}

	public static String getStarDateDisplay() {
		return String.format("%.2f", starDateGalaxy);
	}
	
	public static String getShipDateDisplay() {
		return String.format("%.2f", starDateShip);
	}

	public static String[] getPlanetNames() {
		String[] names = new String[nPlanets];
		int i = 0;
		for (Planet _p : thePlanets) {
			names[i] = _p.getName();
			++i;
		}
		return names;
	}


//	public void refresh() {
//		for (final Planet _p : thePlanets) {
//			_p.refresh();
//			_p.addListener(new ClickListener() {
//		        @Override
//		        public void clicked(InputEvent event, float x, float y) {
//		        	if (isOver() == true) {
//		        		Gdx.app.log("PlanetButton","Interact:"+event.toString()+" "+_p.getName());
//		        		Player.getPlayer().move(_p);
//		        	}
//		        }
//		    });
//		}
//	}

//	public void unHookListners() {
//		for (final Planet _p : thePlanets) {
//			_p.removeListener( _p.getListeners().first() );
//		}		
//	}
//	
}
