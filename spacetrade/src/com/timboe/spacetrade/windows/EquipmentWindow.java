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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.Equipment;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.ImageButtonEquipment;
import com.timboe.spacetrade.world.ImageButtonWeapon;

public class EquipmentWindow {
	private static final EnumMap<Equipment, ImageButtonEquipment> equipmentMinus = new EnumMap<Equipment, ImageButtonEquipment>(Equipment.class);
	private static final EnumMap<Equipment, ImageButtonEquipment> equipmentPlus = new EnumMap<Equipment, ImageButtonEquipment>(Equipment.class);
	private static final EnumMap<Equipment, Label> equipmentCargo = new EnumMap<Equipment, Label>(Equipment.class);
	private static final EnumMap<Equipment, Label> equipmentPrice = new EnumMap<Equipment, Label>(Equipment.class);

	private static ChangeListener equipmentClick;

	private static Window equipmentWindow = null;
	private static boolean equipmentWindowPopulated = false;
	
	public static Window getWindow() {
		if (equipmentWindowPopulated == true) return equipmentWindow;
		equipmentWindowPopulated = true;
		populateWindow();
		return equipmentWindow;
	}

	private static void populateWindow() {
		Skin _skin = Textures.getSkin();
		
		equipmentClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("ShipButton","Interact:"+event.toString()+" "+((ImageButtonWeapon)actor).getWeaponClass());
				updateList();
			}
		};
		
		equipmentWindow = new Window("Equipment", _skin);
		equipmentWindow.defaults().pad(5);
		equipmentWindow.setMovable(false);
		equipmentWindow.debug();
		
		for (final Equipment _e : Equipment.values()) {
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_e);
				}
			});
			equipmentWindow.add( tempIButton );
			
			equipmentWindow.add( new Label( _e.getName(), _skin ) );	
			
			Label temp = new Label("$0", _skin.get("background", LabelStyle.class) );
			equipmentPrice.put(_e, temp);
			equipmentWindow.add( temp );

			ImageButtonEquipment buttonTemp = new ImageButtonEquipment(_skin.get("minus", ImageButtonStyle.class), _e, false);
			buttonTemp.addListener(equipmentClick);
			equipmentMinus.put(_e, buttonTemp);
			equipmentWindow.add( buttonTemp );
			
			temp = new Label("0", _skin.get("background", LabelStyle.class) );
			equipmentCargo.put(_e, temp);
			equipmentWindow.add( temp );
			
			buttonTemp = new ImageButtonEquipment(_skin.get("plus", ImageButtonStyle.class), _e, true);
			buttonTemp.addListener(equipmentClick);
			equipmentPlus.put(_e, buttonTemp);
			equipmentWindow.add( buttonTemp );	
			
			equipmentWindow.row();
		}
			
		updateList();		
	}

	private static void updateList() {

		for (final Equipment _e : Equipment.values()) {
			//TODO check if sold!
			
			//set price
			int _price = _e.getCost();
			equipmentPrice.get(_e).setText("$" + Integer.toString((int) (_price * Player.getPlanet().getEquipmentPriceMod())) );
			if (_price > Player.getCredz()) {
				equipmentPrice.get(_e).setColor(Color.RED);
			} else {
				equipmentPrice.get(_e).setColor(Color.GREEN);
			}

		}
	}
}
