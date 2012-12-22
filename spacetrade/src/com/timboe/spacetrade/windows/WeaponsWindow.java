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
import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.Weapons;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.ShipScreen;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.ImageButtonWeapon;

public class WeaponsWindow {
	private static final EnumMap<Weapons, ImageButtonWeapon> weaponMinus = new EnumMap<Weapons, ImageButtonWeapon>(Weapons.class);
	private static final EnumMap<Weapons, ImageButtonWeapon> weaponPlus = new EnumMap<Weapons, ImageButtonWeapon>(Weapons.class);
	private static final EnumMap<Weapons, Label> weaponCargo = new EnumMap<Weapons, Label>(Weapons.class);
	private static final EnumMap<Weapons, Label> weaponPrice = new EnumMap<Weapons, Label>(Weapons.class);

	private static ChangeListener weaponClick;

	private static Window weaponsWindow = null;
//	private static boolean weaponsWindowPopulated = false;
	
	public static Window getWindow() { //Is dynamic so needs to change each time
//		if (weaponsWindowPopulated == true) return weaponsWindow;
//		weaponsWindowPopulated = true;
		populateWindow();
		return weaponsWindow;
	}

	private static void populateWindow() {
		Skin _skin = Textures.getSkin();
		
		weaponClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Weapons _class = ((ImageButtonWeapon)actor).getWeapon();
				int _cost = Math.round(_class.getCost() * Player.getPlanet().getEquipmentPriceMod());
				Gdx.app.log("WeaponButton","Interact:"+event.toString()+" "+_class);
				if ( ((ImageButtonWeapon)actor).getAdd() == false ) { //TRY SELL
					if (Player.getShip().getNumberInstalled(_class) == 0) {
						return;
					}
					Player.modCredz(_cost);
					Player.getShip().disarm(_class);
				} else { //TRY BUY
					if (Player.getShip().getFreeWeaponSlots() == 0) {
						Help.errorOK("\nThere are no free weapon slots.\n\nTry selling a weapon to free a slot,\nor buy a bigger ship!\n ");
						return;
					}
					if (_cost > Player.getAvailableCredzIncOD()) {
						Help.errorOK("\nYou cannot afford to buy this weapon!\n ");
						return;	
					}
					Player.modCredz(-_cost);
					Player.getShip().arm( ((ImageButtonWeapon)actor).getWeapon() );
				}
				ShipScreen.updateAll = true;	
			}
		};
		
		weaponsWindow = new Window("Armoury", _skin);
		weaponsWindow.defaults().pad(5);
		weaponsWindow.setMovable(false);
		weaponsWindow.debug();
		
		if (Player.getPlanet().getCiv() == Civilisation.Agricultural) {
			weaponsWindow.add( new Label( "Not Sold Here", _skin ) );	
		}
		
		for (final Weapons _w : Weapons.values()) {
			if (Player.getPlanet().getSellsWeapon(_w) == false) {
				weaponPrice.put(_w, null); //used to communicate decision
				continue;
			}
			
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_w);
				}
			});
			weaponsWindow.add( tempIButton );
			
			String drawable = _w.getWeaponClass().toString() + Integer.toString(_w.getLevel());
			weaponsWindow.add( new Image(_skin.getDrawable(drawable) ) );
			
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
			
		ShipScreen.updateAll = true;	
	}

	public static void updateList() {
		if (weaponsWindow == null) return;

		int _slots = Player.getShip().getFreeWeaponSlots();
		if (_slots == 1) {
			weaponsWindow.setTitle("Armoury, "+_slots+" Slot Free");
		} else {
			weaponsWindow.setTitle("Armoury, "+_slots+" Slots Free");
		}

		for (final Weapons _w : Weapons.values()) {
			if (weaponPrice.get(_w) == null) continue; //not sold
			
			//set price
			int _price = _w.getCost();
			weaponPrice.get(_w).setText("$" + Integer.toString((int) (_price * Player.getPlanet().getEquipmentPriceMod())) );
			if (_price > Player.getAvailableCredzIncOD()) {
				weaponPrice.get(_w).setColor(Color.RED);
			} else {
				weaponPrice.get(_w).setColor(Color.GREEN);
			}
			
			//set n owned
			int _owned = Player.getShip().getNumberInstalled(_w);
			weaponCargo.get(_w).setText( Integer.toString(_owned) );
		}
	}
}
