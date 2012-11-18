package com.timboe.spacetrade.screen;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class StarmapScreen extends SpaceTradeRender {
	
	private Starmap theStarmap = Starmap.getStarmap();
	private ShapeRenderer g2 = new ShapeRenderer();
	
	private static StarmapScreen self;

	public StarmapScreen() {
		self = this;
		frontStage = new Stage();
		hookStars();
		init();
	}
	
	public static StarmapScreen getStarmapScreen() {
		return self;
	}
	
	public void hookStars() {
		for (Planet _p : Starmap.getStarmap().getPlanets()) {
			frontStage.addActor(_p);
		}
	}
	
	public void unHookStars() {
		frontStage.clear();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		g2.begin(ShapeType.Circle);
		g2.setColor(0f, 1f, 0f, 0f);
		for (Planet _p : theStarmap.getPlanets()) {
			_p.drawBasic(g2);
		}
		g2.end();
	}
	
}
