package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.timboe.spacetrade.utility.Utility;

public class RightBar extends Render {

	private OrthographicCamera cam;
	private ShapeRenderer g2 = new ShapeRenderer();
	
	private int bar_height = 50;

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
		g2.rect(Utility.GAME_WIDTH, Utility.GAME_HEIGHT - (1*bar_height), Utility.GUI_WIDTH, bar_height);
		g2.rect(Utility.GAME_WIDTH, Utility.GAME_HEIGHT - (2*bar_height), Utility.GUI_WIDTH, bar_height);
		g2.rect(Utility.GAME_WIDTH, Utility.GAME_HEIGHT - (3*bar_height), Utility.GUI_WIDTH, bar_height);
		g2.rect(Utility.GAME_WIDTH, Utility.GAME_HEIGHT - (4*bar_height), Utility.GUI_WIDTH, bar_height);
		g2.end();
	}
	
	public boolean handleClick(int _screenX, int _screenY) {
		Utility util = Utility.getUtility();
		Gdx.app.log("Touch", "X:"+_screenX+" Y:"+_screenY);
		if (_screenX < Utility.GAME_WIDTH) return false;
		if (_screenY < (1*bar_height)) {
			Gdx.app.log("Touch", "Change to Ship");
			util.getSpaceTrade().setScreen( util.getSpaceTrade().theShipScreen  );
			return true;
		} else if (_screenY < (2*bar_height)) {
			Gdx.app.log("Touch", "Change to Star");
			util.getSpaceTrade().setScreen( util.getSpaceTrade().theStarmap  );
			return true;
		} else if (_screenY < (3*bar_height)) {
			Gdx.app.log("Touch", "Change to Planet");
			util.getSpaceTrade().setScreen( util.getSpaceTrade().thePlanetScreen  );
			return true;
		} else if (_screenY < (4*bar_height)) {
			Gdx.app.log("Touch", "Change to Sell ");
			util.getSpaceTrade().setScreen( util.getSpaceTrade().theSellScreen  );
			return true;
		}
		return false;
	}
	
	public void resize(int _width, int _height) {
		width = _width;
		height = _height;
		pper_Wunit = (float)width / Utility.GUI_WIDTH;
		pper_Hunit = (float)height / Utility.CAMERA_HEIGHT;
	}
	
}
