package com.timboe.spacetrade.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.timboe.spacetrade.settings.Utility;
import com.timboe.spacetrade.world.Starmap;

public class StarmapRender {
	
	private Starmap theStarmap;
	
	private OrthographicCamera cam;
	//Base Res 1000x600

	private int width;
	private int height;
	private float pper_Wunit; //pixel per unit width
	private float pper_Hunit; //pixel per unit height
	
	private ShapeRenderer g2 = new ShapeRenderer();

	public StarmapRender() {
		cam = new OrthographicCamera(Utility.CAMERA_WIDTH, Utility.CAMERA_HEIGHT);
		cam.position.set(Utility.CAMERA_WIDTH / 2f, Utility.CAMERA_HEIGHT / 2f, 0);
		cam.update();
		
		theStarmap = new Starmap();
	}
	
	public void render(float delta) {
		System.out.println("InRndr");
//		g2.setProjectionMatrix(cam.combined);
		g2.begin(ShapeType.FilledCircle);
		g2.setColor(0f, 1f, 0f, 0f);
		for (Vector2 s : theStarmap.theStars) {
			g2.filledCircle(s.x, s.y, 5);
		}
		g2.end();
		g2.begin(ShapeType.Rectangle);
		g2.setColor(new Color(1, 0, 0, 1));
		g2.rect(0, 0, 300, 300);
		g2.end();

	}

	public void resize(int _width, int _height) {
		width = _width;
		height = _height;
		pper_Wunit = (float)width / Utility.CAMERA_WIDTH;
		pper_Hunit = (float)height / Utility.CAMERA_HEIGHT;
	}
	
}
