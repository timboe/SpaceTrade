package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.utility.Utility;
import com.timboe.spacetrade.render.Textures;

public class RightBar {

	public final Table rightTable;
	public final TextButton galaxyButton;
	public final TextButton planetButton;
	public final TextButton shipButton;
	public final TextButton sellButton;
	
	private static final RightBar singleton = new RightBar();
	public static RightBar getRightBar() {
		return singleton;
	}
	
	public static Table getRightBarTable() {
		return singleton.rightTable;
	}
	
	public void setTouchable(Touchable _t) {
		galaxyButton.setTouchable(_t);
		planetButton.setTouchable(_t);
		shipButton.setTouchable(_t);
		sellButton.setTouchable(_t);
	}
	
	private RightBar() {
		Skin skin = Textures.getTextures().getSkin();
		
		rightTable = new Table();
		if (SpaceTrade.debug == true) rightTable.debug();
		rightTable.align(Align.top | Align.center );
		rightTable.setSize(Utility.GUI_WIDTH, Utility.GAME_HEIGHT);

//		final TextButton galaxyButton = new TextButton("GALAXY", skin.get("toggle", TextButtonStyle.class));
		
		galaxyButton = new TextButton("GALAXY",  skin.get("toggle", TextButtonStyle.class));
		planetButton = new TextButton("WORLD",  skin.get("toggle", TextButtonStyle.class));
		shipButton = new TextButton("SHIP",  skin.get("toggle", TextButtonStyle.class));
		sellButton = new TextButton("SELL",  skin.get("toggle", TextButtonStyle.class));
		ButtonGroup group = new ButtonGroup(galaxyButton, planetButton, shipButton, sellButton);
		group.setMaxCheckCount(1);
				
		galaxyButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theStarmap );
				return false;
			}
		});
		
		planetButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().thePlanetScreen );
				return false;
			}
		});		
		
		shipButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theShipScreen );
				return false;
			}
		});
		
		sellButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theSellScreen  );
				return false;
			}
		});
		
		rightTable.add(galaxyButton);
		rightTable.row();
		rightTable.add(planetButton);
		rightTable.row();		
		rightTable.add(shipButton);
		rightTable.row();		
		rightTable.add(sellButton);

	}
	

	
}
