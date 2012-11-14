package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.timboe.spacetrade.utility.Utility;

public class RightBar extends Render {

	private OrthographicCamera cam;
	private ShapeRenderer g2 = new ShapeRenderer();

	public RightBar() {
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		cam = new OrthographicCamera(Utility.GUI_WIDTH * pper_Wunit, Utility.CAMERA_HEIGHT * pper_Hunit);
//		cam.position.set(((Utility.GUI_WIDTH * pper_Hunit) / 2f) - ((Utility.CAMERA_WIDTH * pper_Hunit) / 2f), (Utility.CAMERA_HEIGHT * pper_Hunit) / 2f, 0);
//		cam.update();
	}
	
	public void render() {
		
		//g2.setProjectionMatrix(cam.combined);
		g2.begin(ShapeType.Rectangle);
		g2.setColor(1f, 0f, 1f, 0f);
		g2.rect(Utility.GAME_WIDTH, Utility.GAME_HEIGHT - 50, Utility.GUI_WIDTH, 50);
		g2.end();
		
	}
	
	public void resize(int _width, int _height) {
		width = _width;
		height = _height;
		pper_Wunit = (float)width / Utility.GUI_WIDTH;
		pper_Hunit = (float)height / Utility.CAMERA_HEIGHT;
	}
	
}
