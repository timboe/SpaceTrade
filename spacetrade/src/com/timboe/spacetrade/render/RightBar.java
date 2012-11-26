package com.timboe.spacetrade.render;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.world.Starmap;

public class RightBar {

	private static Table rightTable = null;
	private static TextButton galaxyButton;
	private static TextButton planetButton;
	private static TextButton shipButton;
	private static TextButton sellButton;
	private static TextButton debugButton;

	private static Label credz;
	private static Label cargo;
	private static Label timeUniverse;
	private static Label timeShip;
	
	public static Table getRightBarTable() {
		if (rightTable == null) {
			populate();
		}
		return rightTable;
	}
	
	public static void setTouchable(Touchable _t) {
		if (rightTable == null) {
			populate();
		}
		galaxyButton.setTouchable(_t);
		planetButton.setTouchable(_t);
		shipButton.setTouchable(_t);
		sellButton.setTouchable(_t);
		debugButton.setTouchable(_t);
	}
	
	private static void populate() {
		Skin skin = Textures.getSkin();
		
		rightTable = new Table();
		if (SpaceTrade.debug == true) rightTable.debug();
		rightTable.align(Align.top | Align.center );
		rightTable.setSize(SpaceTrade.GUI_WIDTH, SpaceTrade.GAME_HEIGHT);

//		final TextButton galaxyButton = new TextButton("GALAXY", skin.get("toggle", TextButtonStyle.class));
		
		galaxyButton = new TextButton("GALAXY",  skin.get("toggle", TextButtonStyle.class));
		planetButton = new TextButton("WORLD",  skin.get("toggle", TextButtonStyle.class));
		shipButton = new TextButton("SHIP",  skin.get("toggle", TextButtonStyle.class));
		sellButton = new TextButton("SELL",  skin.get("toggle", TextButtonStyle.class));
		debugButton = new TextButton("DEBUG",  skin.get("toggle", TextButtonStyle.class));

		ButtonGroup group = new ButtonGroup(galaxyButton, planetButton, shipButton, sellButton, debugButton);
		group.setMaxCheckCount(1);
				
		galaxyButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().theStarmap );
				return false;
			}
		});
		
		planetButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().thePlanetScreen );
				return false;
			}
		});		
		
		shipButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().theShipScreen );
				return false;
			}
		});
		
		sellButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().theSellScreen  );
				return false;
			}
		});
		
		debugButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				SpaceTrade.debug = !SpaceTrade.debug;
				return false;
			}
		});
		
		credz = new Label("", skin);
		credz.setAlignment(Align.center);
		cargo = new Label("", skin);
		cargo.setAlignment(Align.center);
		timeShip = new Label("", skin);
		timeShip.setAlignment(Align.center);
		timeUniverse = new Label("", skin);
		timeUniverse.setAlignment(Align.center);
		
		rightTable.add(galaxyButton);
		rightTable.row();
		rightTable.add(planetButton);
		rightTable.row();		
		rightTable.add(shipButton);
		rightTable.row();		
		rightTable.add(sellButton);
		rightTable.row();		
		rightTable.add(debugButton);
		rightTable.row();		
		rightTable.add(credz);
		rightTable.row();
		rightTable.add(cargo);
		rightTable.row();
		rightTable.add(timeShip);		
		rightTable.row();
		rightTable.add(timeUniverse);
	}
	
	
	public static void update() {
		credz.setText("Credz:\n"+Player.getCredz());
		cargo.setText("Cargo:\n"+Player.getTotalCargo()+"/"+Player.getShip().getCargo());
		timeShip.setText("ShipYear:\n"+Starmap.getShipDateDisplay());
		timeUniverse.setText("GalacticYear:\n"+Starmap.getStarDateDisplay());

	}

	
}
