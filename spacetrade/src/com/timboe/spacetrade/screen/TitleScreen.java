package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.ship.Ship;

public class TitleScreen extends SpaceTradeRender {
	
	//private ShaderProgram shader;
	//private BitmapFont distanceFieldFont;
	//Texture distanceFieldTexture;
	
	private void doNewGame() {
		RightBar.setTouchable(Touchable.enabled);
		//new!
		Starmap.populate();
		Player.getPlayer().refresh();
		Player.setCredz(250000);

		ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().theStarmap );
		RightBar.getRightBarTable().addAction(Actions.delay(ScreenFade.speed));
		RightBar.getRightBarTable().addAction(Actions.fadeIn(ScreenFade.speed));
		Gdx.app.log("NewGame", "Player Credz:"+Player.getCredz());
	}
	
	public TitleScreen() {

		RightBar.getRightBarTable().addAction(Actions.fadeOut(0));
		RightBar.getRightBarTable().act(1);
		
		leftTable.defaults().pad(10);
		for (int i = 1; i <= 3; ++i) {
		
			TextButton gameButton = new TextButton("Slot "+i+" - New Game", Textures.getSkin().get("large",TextButtonStyle.class));
			gameButton.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					doNewGame();
				}
			});
			leftTable.add(gameButton).width(600);
			ImageButton delButton = new ImageButton(Textures.getSkin().get("cancel", ImageButtonStyle.class));
			leftTable.add(delButton);
			leftTable.row();

		}
		
		RightBar.setTouchable(Touchable.disabled);		
		init();
	}
	
	@Override
	protected void renderBackground(float delta) {
		spriteBatch.setProjectionMatrix(transform_BG);
		spriteBatch.begin();
		spriteBatch.draw(Textures.getStarscape( 0 ),0,0);
		spriteBatch.end();
	}
	
	@Override
	protected void renderFX(float delta) {
		renderPlanet(delta, 
				Textures.getMoonTexture(),
				Textures.getMoonNorm(),
				WorldSize.Medium);
        
	}

}
