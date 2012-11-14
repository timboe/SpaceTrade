package com.timboe.spacetrade;

import com.badlogic.gdx.Game;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.screen.TitleScreen;

public class SpaceTrade extends Game {

	StarmapScreen theStarmap;
	TitleScreen theTitle;
	
	@Override
	public void create() {
		theTitle = new TitleScreen();
		theStarmap = new StarmapScreen();

		setScreen(theStarmap);
	}
	
	@Override
	public void dispose() {
		theTitle.dispose();
		theStarmap.dispose();
	}

}
