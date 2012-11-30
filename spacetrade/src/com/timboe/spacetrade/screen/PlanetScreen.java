package com.timboe.spacetrade.screen;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.timboe.spacetrade.render.Meshes;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.windows.PlanetWindow;

public class PlanetScreen extends SpaceTradeRender {

	//private float rotation = 0;
    private ShaderProgram shader;

	public PlanetScreen() {
		shader = Meshes.createShader();	
		init();
	}
	
	@Override
	protected void renderBackground(float delta) {
		renderPlanetBackdrop();
	}
	
	@Override
	protected void renderFX(float delta) {
		renderPlanet(delta);
	}
	
	@Override
	public void show() {
		leftTable.clear();
		leftTable.defaults().pad(20);
		leftTable.left();
		leftTable.add(PlanetWindow.getWindow());
		super.show();
		//PlanetWindow.fadeIn();
	}
	
}
