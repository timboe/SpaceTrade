package com.timboe.spacetrade.utility;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;

public class ScreenFade {

	static SpaceTradeRender currentScreen;
	static SpaceTradeRender newScreen;

	public static final float speed = 0.25f;
	
	private enum Fade {fadingIn,fadeOut,fadingOut,none;}
	static Fade fade = Fade.none;
	
	public static void changeScreen(Screen _new) {
		currentScreen = (SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen();
		newScreen = (SpaceTradeRender)_new;
		if (currentScreen == newScreen) return;
		fade = Fade.fadeOut;
	}
	
	public static boolean checkFade() {
		if (fade == Fade.none) {
			return false;
		} else if (fade == Fade.fadeOut) {
			currentScreen.getBlackSquare().addAction(Actions.fadeIn(speed));
			fade = Fade.fadingOut;
		} else if (fade == Fade.fadingOut) {
			if (currentScreen.getBlackSquare().getActions().size == 0) {
				newScreen.getBlackSquare().addAction(Actions.fadeIn(0));
				newScreen.getBlackSquare().act(1);
				SpaceTrade.getSpaceTrade().setScreen( newScreen );
				newScreen.getBlackSquare().addAction(Actions.fadeOut(speed));
				fade = Fade.fadingIn;
				RightBar.update();
			}
		} else if (fade == Fade.fadingIn) {
			if (newScreen.getBlackSquare().getActions().size == 0) {
				fade = Fade.none;
			}
		}
		return true;
	}
}
