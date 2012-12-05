package com.timboe.spacetrade.screen;

import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.PlanetFX;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.ship.Ship;
import com.timboe.spacetrade.utility.Rnd;
import com.timboe.spacetrade.windows.PlanetWindow;
import com.timboe.spacetrade.windows.TravelWindow;

public class PlanetScreen extends SpaceTradeRender {
	
	private static int tocks = 0;
	private static float velX = 0f;
	private static Ship encounter = null;
	private Rnd rnd = new Rnd();
	private static boolean triggerRefresh = false;

	public PlanetScreen() {
		init();
	}
	
	private void doApproach() {
		if (tocks > 0) {
			xTarget = tocks * 25;
		} else {
			xTarget = 0;
		}
		if ( xOffset > xTarget ) {
			velX = 0.01f * Math.abs(xOffset - xTarget);
		} else {
			velX = 0f;
		}
		xOffset -= velX;
	}
	
	private void checkForEncounter() {
		while (tocks > 0) {
			if (rnd.getRandChance( Player.getPlanet().getActivity(ShipTemplate.Pirate).getChance() ) == true ) {
				encounter = new Ship(ShipTemplate.Pirate);
				return;
			}
			if (rnd.getRandChance( Player.getPlanet().getActivity(ShipTemplate.Police).getChance() ) == true ) {
				encounter = new Ship(ShipTemplate.Police);
				return;
			}
			if (rnd.getRandChance( Player.getPlanet().getActivity(ShipTemplate.Trader).getChance() ) == true ) {
				encounter = new Ship(ShipTemplate.Trader);
				return;
			}
			endEncounter();
		}
	}
	
	public static String getTocksStr() {
		if (tocks == 1) {
			return " 1 Tock";
		} else {
			return ""+tocks+" Tocks";
		}
	}
	
	public static Ship getEncounter() {
		return encounter;
	}
	
	public static void endEncounter() {
		encounter = null;
		if (--tocks == 0) {
			PlanetWindow.doFadeIn = true;
			//TravelWindow.fadeOut();
			triggerRefresh = true;
		}
	}
	
	@Override
	protected void renderBackground(float delta) {
		if (triggerRefresh == true) {
			triggerRefresh = false;
			show();
		}
		if (encounter == null && tocks > 0) {
			checkForEncounter();
		}
		doApproach();
		if (tocks > 0) {
			TravelWindow.updateList();
		}
		renderPlanetBackdrop();
	}
	
	@Override
	protected void renderFX(float delta) {
		renderPlanet(delta, 
				PlanetFX.getTexture(Player.getPlanetID()),
				PlanetFX.getNormals(Player.getPlanetID()),
				Player.getPlanet().getSize());	}
	
	@Override
	public void show() {
		leftTable.clear();
		leftTable.defaults().pad(10);
		
		if (tocks > 0) {
			leftTable.center().top();
			leftTable.add(TravelWindow.getWindow());
		} else {
			leftTable.center().left();
			leftTable.add(PlanetWindow.getWindow());
		}
		
		super.show();
	}

	public static void newDestination() {
		tocks = 20;
		xOffset = 20 * 25;
//		checkForEncounter();
	}
	
}
