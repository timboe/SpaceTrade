package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.utility.Serialiser;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.render.Textures;

public class TitleScreen extends SpaceTradeRender {
	
	public TitleScreen() {
		
		TextButton newGame = new TextButton("New Game", Textures.getTextures().getSkin());
		newGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				RightBar.getRightBar().setTouchable(Touchable.enabled);
				//new!
				Starmap.newStarmap();
				Player.newPlayer();
				//
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theShipScreen );
				Gdx.app.log("NewGame", "Player Credz:"+Player.getPlayer().getCredz());
				Player.getPlayer().modCredz(555);
				Gdx.app.log("NewGame", "Player Credz:"+Player.getPlayer().getCredz());
			}
		});
		
		TextButton resumeGame = new TextButton("Resume Game", Textures.getTextures().getSkin());
		resumeGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				RightBar.getRightBar().setTouchable(Touchable.enabled);
				Serialiser.loadState();
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theShipScreen );
				Gdx.app.log("LoadGame", "Player Credz:"+Player.getPlayer().getCredz());
			}
		});
		
		leftTable.defaults().pad(20);
		leftTable.add(newGame).width(400).height(50);
		leftTable.row();
		leftTable.add(resumeGame).width(400).height(50);
		
		RightBar.getRightBar().setTouchable(Touchable.disabled);
		
		init();
	}


}
