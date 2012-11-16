package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.timboe.spacetrade.utility.Utility;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class StarmapRender extends Render {
	
	public Starmap theStarmap;
	private ShapeRenderer g2 = new ShapeRenderer();

	public StarmapRender() {
		theStarmap = new Starmap();
		Utility.getUtility().setStarmap(theStarmap);
		
		Label titleLabelA = new Label("AAAAA", skin);
		Label titleLabelB = new Label("BBBB", skin);
		Label titleLabelC = new Label("CCCC", skin);
		leftTable.add(titleLabelA);
		leftTable.add(titleLabelB);
		leftTable.add(titleLabelC);
		
		
		init();
	}
	
	public void render() {
		g2.begin(ShapeType.FilledCircle);
		g2.setColor(0f, 1f, 0f, 0f);
		for (Planet p : theStarmap.thePlanets) {
			g2.filledCircle(p.getX(), p.getY(), 5);
		}
		g2.end();
		
		super.render();
	}
	
}
