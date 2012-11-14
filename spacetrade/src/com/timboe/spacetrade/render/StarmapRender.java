package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.timboe.spacetrade.utility.Utility;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class StarmapRender extends Render {
	
	private Starmap theStarmap;
	
	private OrthographicCamera cam;
	private OrthographicCamera cam2;

	//Base Res 1000x600

	private ShapeRenderer g2 = new ShapeRenderer();

	public StarmapRender() {
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		cam = new OrthographicCamera(Utility.GAME_WIDTH, Utility.GAME_HEIGHT);
//		cam.position.set(250, (Utility.CAMERA_HEIGHT) / 2f, 0);
//		cam.update();
//		
//		cam2 = new OrthographicCamera(Utility.GAME_WIDTH, Utility.GAME_HEIGHT);
//		cam2.position.set(0, (Utility.CAMERA_HEIGHT) / 2f, 0);
//		cam2.update();
		
		theStarmap = new Starmap();
	}
	
	public void render(float delta) {

		//g2.setProjectionMatrix( cam.combined);
		g2.begin(ShapeType.FilledCircle);
		g2.setColor(0f, 1f, 0f, 0f);
		for (Planet p : theStarmap.thePlanets) {
			g2.filledCircle(p.getX(), p.getY(), 5);
		}
		g2.end();
		
		//g2.setProjectionMatrix( cam2.combined);


	}

	public void resize(int _width, int _height) {
		System.out.println("W:"+_width+" H:"+_height);
		width = _width;
		height = _height;
		pper_Wunit = (float)width / Utility.CAMERA_WIDTH;
		pper_Hunit = (float)height / Utility.CAMERA_HEIGHT;
		
		System.out.println("W:"+_width+" ppW:"+pper_Wunit+" H:"+_height);

	}

	
}
