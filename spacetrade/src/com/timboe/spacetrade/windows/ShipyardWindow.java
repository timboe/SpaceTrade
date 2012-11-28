package com.timboe.spacetrade.windows;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.TextButtonShip;

public class ShipyardWindow {

	private static final EnumMap<ShipClass, TextButtonShip> doShip = new EnumMap<ShipClass, TextButtonShip>(ShipClass.class);
	private static ChangeListener shipClick;

	private static Window shipyardWindow = null;
	private static boolean shipyardWindowPopulated = false;
	
	public static Window getWindow() {
		if (shipyardWindowPopulated == true) return shipyardWindow;
		shipyardWindowPopulated = true;
		populateWindow();
		return shipyardWindow;
	}

	private static void populateWindow() {
		Skin _skin = Textures.getSkin();
		
		shipClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("ShipButton","Interact:"+event.toString()+" "+((TextButtonShip)actor).getShipClass());
				updateList();
			}
		};
		
		shipyardWindow = new Window("Shipyard", _skin);
		shipyardWindow.defaults().pad(5);
		shipyardWindow.setMovable(false);
		shipyardWindow.debug();
		
		for (final ShipClass _s : ShipClass.values()) {
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_s);
				}
			});
			shipyardWindow.add( tempIButton );
			
			shipyardWindow.add( new Label( _s.getName(), _skin ) );	

			TextButtonShip buttonTemp = new TextButtonShip("BUY", _skin, _s);
			buttonTemp.addListener(shipClick);
			doShip.put(_s, buttonTemp);
			shipyardWindow.add( buttonTemp );	
			
			shipyardWindow.row();
		}
			
		updateList();		
	}

	private static void updateList() {

		for (final ShipClass _s : ShipClass.values()) {
			//TODO check if sold!
			if (_s == Player.getShip().getShipClass()) {
				doShip.get(_s).setVisible(false);
				continue;
			}
			
			doShip.get(_s).setVisible(true);
			int _tradeInPrice = Math.round(Player.getShip().getTradeInPrice() * Player.getPlanet().getEquipmentPriceMod());
			int cost = Math.round((Player.getPlanet().getEquipmentPriceMod() *_s.getCost()) - _tradeInPrice);
			if (cost > 0) {
				doShip.get(_s).setColor(Color.GREEN);
				doShip.get(_s).setText("UPGRADE FOR\n$" + Integer.toString(cost) );
			} else {
				doShip.get(_s).setColor(Color.RED);
				doShip.get(_s).setText("DOWNSIZE\n$" + Integer.toString(cost) );
			}
		}
	}
}
