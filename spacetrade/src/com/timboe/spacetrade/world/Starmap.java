package com.timboe.spacetrade.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.utility.Utility;

public class Starmap {
	
	private static final int starBuffer = (int) Math.round((Textures.getStar().getWidth()/2f) * Math.sqrt(2)) * 3 ;
	public static final int travelScale = (int) (starBuffer * 0.1f);
	private static final int nPlanets = 128;
	public static final float toLightYears = 4.2f;
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
			int _x = Utility.getRandI((int)(Textures.getGalaxyTexture().getWidth()  - starBuffer - SpaceTrade.GUI_WIDTH)) + starBuffer/4;
			int _y = Utility.getRandI((int)(Textures.getGalaxyTexture().getHeight() - starBuffer)) + starBuffer/4;
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
			        			setNearbyPlanetFocus(newPlanet.getID());
			        		}
			        	}
			        }
			    });
				thePlanets.add( newPlanet );
			}
//			if (thePlanets.size == getNPlanets()) { //TODO think about this
//				//now we have all the stars, move any which are too far away from each other!
//				//note this is still within the create loop
//				for (Planet p1 : thePlanets) {
//					float minDist = 99999f;
//					float
//					for (Planet p2 : thePlanets) {
//						if (p1 == p2) continue;
//						float dist = 
//						minDist = Math.min(minDist, p1.dst(p2));
//					}
//					if ()
//				}
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
	
	public static float getTravelTimeShip(Planet _local, Planet _remote, float _g) {
		final float d = getDistanceLightyear(_local, _remote)/2f; 		//need to change around at half way point!
		final float a = _g * G;
		final float c = C;
		//(c/a) ch-1 [ad/c2 + 1]
		return (float) 2 * ( (c/a) * Utility.acosh( ((a*d)/(c*c)) + 1 ) );
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
		return thePlanets.get( Utility.getRandI(getNPlanets()) );
	}

	public static String getStarDateDisplay() {
		return String.format("%.2f", starDateGalaxy);
	}
	
	public static String getShipDateDisplay() {
		return String.format("%.2f", starDateShip);
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
