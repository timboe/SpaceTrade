package com.timboe.spacetrade.windows;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.Weapons;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.ImageButtonWeapon;
import com.timboe.spacetrade.world.TextButtonShip;

public class WeaponsWindow {
	private static final EnumMap<Weapons, ImageButtonWeapon> weaponMinus = new EnumMap<Weapons, ImageButtonWeapon>(Weapons.class);
	private static final EnumMap<Weapons, ImageButtonWeapon> weaponPlus = new EnumMap<Weapons, ImageButtonWeapon>(Weapons.class);
	private static final EnumMap<Weapons, Label> weaponCargo = new EnumMap<Weapons, Label>(Weapons.class);
	private static final EnumMap<Weapons, Label> weaponPrice = new EnumMap<Weapons, Label>(Weapons.class);

	private static ChangeListener weaponClick;

	private static Window weaponsWindow = null;
	private static boolean weaponsWindowPopulated = false;
	
	public static Window getWindow() {
		if (weaponsWindowPopulated == true) return weaponsWindow;
		weaponsWindowPopulated = true;
		populateWindow();
		return weaponsWindow;
	}

	private static void populateWindow() {
		Skin _skin = Textures.getSkin();
		
		weaponClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("ShipButton","Interact:"+event.toString()+" "+((ImageButtonWeapon)actor).getWeaponClass());
				updateList();
			}
		};
		
		weaponsWindow = new Window("Armoury", _skin);
		weaponsWindow.defaults().pad(5);
		weaponsWindow.setMovable(false);
		weaponsWindow.debug();
		
		for (final Weapons _w : Weapons.values()) {
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_w);
				}
			});
			weaponsWindow.add( tempIButton );
			
			weaponsWindow.add( new Image(_skin.getDrawable(_w.getWeaponClass().toString() ) ) );
			
			weaponsWindow.add( new Label( _w.getName(), _skin ) );	
			
			Label temp = new Label("$0", _skin.get("background", LabelStyle.class) );
			weaponPrice.put(_w, temp);
			weaponsWindow.add( temp );

			ImageButtonWeapon buttonTemp = new ImageButtonWeapon(_skin.get("minus", ImageButtonStyle.class), _w, false);
			buttonTemp.addListener(weaponClick);
			weaponMinus.put(_w, buttonTemp);
			weaponsWindow.add( buttonTemp );
			
			temp = new Label("0", _skin.get("background", LabelStyle.class) );
			weaponCargo.put(_w, temp);
			weaponsWindow.add( temp );
			
			buttonTemp = new ImageButtonWeapon(_skin.get("plus", ImageButtonStyle.class), _w, true);
			buttonTemp.addListener(weaponClick);
			weaponPlus.put(_w, buttonTemp);
			weaponsWindow.add( buttonTemp );	
			
			weaponsWindow.row();
		}
			
		updateList();		
	}

	private static void updateList() {

		for (final Weapons _w : Weapons.values()) {
			//TODO check if sold!
			
			//set price
			int _price = _w.getCost();
			weaponPrice.get(_w).setText("$" + Integer.toString((int) (_price * Player.getPlanet().getEquipmentPriceMod())) );
			if (_price > Player.getCredz()) {
				weaponPrice.get(_w).setColor(Color.RED);
			} else {
				weaponPrice.get(_w).setColor(Color.GREEN);
			}

		}
	}
}
