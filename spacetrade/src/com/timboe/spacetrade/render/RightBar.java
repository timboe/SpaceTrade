package com.timboe.spacetrade.render;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.world.Starmap;

public class RightBar {

	private static Table rightTable = null;
	private static ImageButton galaxyButton;
	private static ImageButton planetButton;
	private static ImageButton shipButton;
	private static ImageButton sellButton;
	private static TextButton debugButton;

	private static Label credz;
	private static Label cargo;
	private static Label hull;
	private static Label heat;
	private static Label shield;
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
		rightTable.debug();
		rightTable.align(Align.top | Align.center );
		rightTable.setSize(SpaceTrade.GUI_WIDTH, SpaceTrade.GAME_HEIGHT);

//		final TextButton galaxyButton = new TextButton("GALAXY", skin.get("toggle", TextButtonStyle.class));
		
		galaxyButton = new ImageButton(skin.get("galaxy", ImageButtonStyle.class));
		planetButton = new ImageButton(skin.get("planet", ImageButtonStyle.class));
		shipButton = new ImageButton(skin.get("ship", ImageButtonStyle.class));
		sellButton = new ImageButton(skin.get("sell", ImageButtonStyle.class));
		debugButton = new TextButton("DEBUG",skin);

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
				ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().theBuySellScreen  );
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
		
		credz = new Label("", skin.get("background", LabelStyle.class));
		credz.setAlignment(Align.center);
		cargo = new Label("", skin.get("background", LabelStyle.class));
		cargo.setAlignment(Align.center);
		hull = new Label("", skin.get("background", LabelStyle.class));
		hull.setAlignment(Align.center);
		heat = new Label("", skin.get("background", LabelStyle.class));
		heat.setAlignment(Align.center);
		shield = new Label("", skin.get("background", LabelStyle.class));
		shield.setAlignment(Align.center);
		timeShip = new Label("", skin.get("background", LabelStyle.class));
		timeShip.setAlignment(Align.center);
		timeUniverse = new Label("", skin.get("background", LabelStyle.class));
		timeUniverse.setAlignment(Align.center);
		
		rightTable.add(galaxyButton).fillX();
		rightTable.row();
		rightTable.add(planetButton).fillX();
		rightTable.row();		
		rightTable.add(shipButton).fillX();
		rightTable.row();		
		rightTable.add(sellButton).fillX();
		rightTable.row();		
		rightTable.add(debugButton);
		rightTable.row();		
		rightTable.add(credz).fillX();
		rightTable.row();
		rightTable.add(cargo).fillX();
		rightTable.row();
		rightTable.add(hull).fillX();
		rightTable.row();
		rightTable.add(shield).fillX();
		rightTable.row();
		rightTable.add(heat).fillX();
		rightTable.row();
		rightTable.add(timeShip).fillX();		
		rightTable.row();
		rightTable.add(timeUniverse).fillX();
	}
	
	
	public static void update() {
		credz.setText("Credz:\n$"+Player.getCredz());
		cargo.setText("Cargo:\n"+Player.getTotalCargo()+"/"+Player.getShip().getMaxCargo());
		hull.setText("Hull:\n"+Player.getShip().getHull()+"/"+Player.getShip().getMaxHull());
		shield.setText("Shields:\n"+Player.getShip().getShields()+"/"+Player.getShip().getMaxShields());
		heat.setText("Heat:\n"+Player.getShip().getHeat()+"/"+Player.getShip().getMaxHeat());
		timeShip.setText("ShipYear:\n"+Starmap.getShipDateDisplay());
		timeUniverse.setText("Galactic\nYear:\n"+Starmap.getStarDateDisplay());

	}

	
}
