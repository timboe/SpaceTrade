package com.timboe.spacetrade.screen;

import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.PlanetFX;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.windows.PlanetWindow;

public class PlanetScreen extends SpaceTradeRender {


	public PlanetScreen() {
		init();
	}
	
	@Override
	protected void renderBackground(float delta) {
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
		leftTable.defaults().pad(20);
		leftTable.left();
		leftTable.add(PlanetWindow.getWindow());
		super.show();
		PlanetWindow.fadeIn();
	}
	
}
