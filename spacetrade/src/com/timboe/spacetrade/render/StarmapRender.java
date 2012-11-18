package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.timboe.spacetrade.utility.Utility;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class StarmapRender extends Render {
	
	private Starmap theStarmap = Starmap.getStarmap();
	private ShapeRenderer g2 = new ShapeRenderer();
	
	private SpriteBatch spriteBatch = new SpriteBatch();
	public Stage starStage;

	public StarmapRender() {
		
		Label titleLabelA = new Label("AAAAA", skin);
		Label titleLabelB = new Label("BBBB", skin);
		Label titleLabelC = new Label("CCCC", skin);
		leftTable.add(titleLabelA);
		leftTable.add(titleLabelB);
		leftTable.add(titleLabelC);
		
		
		init();
		starStage = new Stage();
		for (Planet _p : Starmap.getStarmap().getPlanets()) {
			starStage.addActor(_p);
		}
	}
	
	public void render() {
		g2.begin(ShapeType.FilledCircle);
		g2.setColor(0f, 1f, 0f, 0f);
		for (Planet p : theStarmap.getPlanets()) {
			g2.filledCircle(p.getX(), p.getY(), 5);
		}
		g2.end();
				
		super.render();
		starStage.act(Gdx.graphics.getDeltaTime());
		starStage.draw();
	}
	
}
