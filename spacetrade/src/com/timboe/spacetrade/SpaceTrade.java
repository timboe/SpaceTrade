package com.timboe.spacetrade;

import com.badlogic.gdx.Game;
import com.timboe.spacetrade.screen.PlanetScreen;
import com.timboe.spacetrade.screen.SellScreen;
import com.timboe.spacetrade.screen.ShipScreen;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.screen.TitleScreen;
import com.timboe.spacetrade.utility.Utility;

public class SpaceTrade extends Game {

	public StarmapScreen theStarmap;
	public TitleScreen theTitle;
	public ShipScreen theShipScreen;
	public PlanetScreen thePlanetScreen;
	public SellScreen theSellScreen;
	Utility util;
	
	@Override
	public void create() {
		util = Utility.getUtility();
		util.setSpaceTrade(this);
		
		theTitle = new TitleScreen();
		theStarmap = new StarmapScreen();
		theShipScreen = new ShipScreen();
		thePlanetScreen = new PlanetScreen();
		theSellScreen = new SellScreen();
		
		setScreen(theSellScreen);

	}
	
	@Override
	public void dispose() {
		theTitle.dispose();
		theStarmap.dispose();
		theShipScreen.dispose();
		thePlanetScreen.dispose();
		theSellScreen.dispose();
	}

}
