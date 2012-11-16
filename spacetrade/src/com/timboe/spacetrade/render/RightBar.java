package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.timboe.spacetrade.utility.Utility;

public class RightBar {

	public final Table rightTable;
	public final TextButton galaxyButton;
	public final TextButton planetButton;
	public final TextButton shipButton;
	public final TextButton sellButton;
	public final Skin skin;
	
	private static final RightBar singleton = new RightBar();
	public static RightBar getRightBar() {
		return singleton;
	}
	
	public static Table getRightBarTable() {
		return singleton.rightTable;
	}
	
	private RightBar() {
		skin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));

		rightTable = new Table();
		rightTable.debug();
		rightTable.align(Align.top | Align.center );
		rightTable.setSize(Utility.GUI_WIDTH, Utility.GAME_HEIGHT);

//		final TextButton galaxyButton = new TextButton("GALAXY", skin.get("toggle", TextButtonStyle.class));
		galaxyButton = new TextButton("GALAXY", skin);
		planetButton = new TextButton("WORLD", skin);
		shipButton = new TextButton("SHIP", skin);
		sellButton = new TextButton("SELL", skin);
		
		galaxyButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;
				planetButton.setChecked(false);
				shipButton.setChecked(false);
				sellButton.setChecked(false);
				Utility.getUtility().getSpaceTrade().setScreen( Utility.getUtility().getSpaceTrade().theStarmap );
				return false;
			}
		});
		
		
		planetButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;

				Utility.getUtility().getSpaceTrade().setScreen( Utility.getUtility().getSpaceTrade().thePlanetScreen );
				
				galaxyButton.setChecked(false);
				shipButton.setChecked(false);
				sellButton.setChecked(false);
				return false;
			}
		});		
		
		
		shipButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;

				Utility.getUtility().getSpaceTrade().setScreen( Utility.getUtility().getSpaceTrade().theShipScreen );
				galaxyButton.setChecked(false);
				planetButton.setChecked(false);
				sellButton.setChecked(false);
				return false;
			}
		});
		
		sellButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (button != Buttons.LEFT) return false;

				Utility.getUtility().getSpaceTrade().setScreen( Utility.getUtility().getSpaceTrade().theSellScreen  );
				galaxyButton.setChecked(false);
				shipButton.setChecked(false);
				planetButton.setChecked(false);
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
