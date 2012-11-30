package com.timboe.spacetrade.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.Equipment;
import com.timboe.spacetrade.enumerator.Weapons;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.ImageButtonEquipment;
import com.timboe.spacetrade.world.ImageButtonWeapon;

public class ShipWindow {

	//private static final EnumMap<ShipClass, TextButtonShip> doShip = new EnumMap<ShipClass, TextButtonShip>(ShipClass.class);
	//private static final EnumMap<ShipClass, Label> labelShipMod = new EnumMap<ShipClass, Label>(ShipClass.class);
	private static ChangeListener shipClick;
	private static Label hullLabel;
	private static Label heatLabel;
	private static Label priceLabel;
	private static Label rangeLabel;
	private static Label cargoLabel;
	private static Label weaponLabel;
	private static Label equipmentLabel;

	
	
	private static Window shipWindow = null;
	
	public static Window getWindow() {
		populateWindow();
		return shipWindow;
	}

	private static void populateWindow() {
		final Skin _skin = Textures.getSkin();

		shipWindow = new Window("Ship Statistics", _skin);
		shipWindow.defaults().pad(5);
		shipWindow.setMovable(false);
		shipWindow.debug();
		
		
		Label temp =  new Label( "Model: "+Player.getShip().getFullName() , _skin );
		temp.setAlignment(Align.center);
		shipWindow.add(temp).colspan(4).left();
		
		shipWindow.row();
		priceLabel = new Label( "Current Value: $" + Player.getShip().getTradeInPrice() , _skin );
		shipWindow.add(priceLabel).colspan(4).left();
		
		shipWindow.row();
		rangeLabel = new Label( "Range: " + Player.getShip().getRange() + " units." , _skin );//TODO units
		shipWindow.add(rangeLabel).colspan(4).left();
		
		shipWindow.row();
		hullLabel = new Label( "Hull Strength: " + Player.getShip().getHull() +"/"+Player.getShip().getMaxHull()  , _skin );
		shipWindow.add(hullLabel).colspan(4).left();
		
		shipWindow.row();
		heatLabel = new Label( "Heat Capacity: " + Player.getShip().getMaxHeat() +", Heat Loss:"+Player.getShip().getHeatLoss()  , _skin );
		shipWindow.add(heatLabel).colspan(4).left();
		
		shipWindow.row();
		cargoLabel = new Label( "Cargo Bay Use: " + Player.getTotalCargo()+"/"+Player.getTotalCargoCapacity(), _skin );
		shipWindow.add(cargoLabel).colspan(4).left();
	
		shipWindow.row();
		shipWindow.add( new Image(_skin.getDrawable("default-splitpane-vertical")) ).colspan(4).fillX();

		shipWindow.row();
		weaponLabel = new Label( "Weapons Systems: " + Player.getShip().getFilledWeaponSlots() +" of "+Player.getShip().getTotalWeaponSlots() + " slots in use.", _skin );
		shipWindow.add(weaponLabel).colspan(4).left();
		shipWindow.row();

		for (final Weapons _w : Player.getShip().getWeapons()) {
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_w);
				}
			});
			shipWindow.add( tempIButton ).right();
			
			String drawable = _w.getWeaponClass().toString() + Integer.toString(_w.getLevel());
			shipWindow.add( new Image(_skin.getDrawable(drawable) ) ).right();
			
			shipWindow.add( new Label( _w.getName(), _skin ) ).left();	
			
			ImageButtonWeapon buttonTemp = new ImageButtonWeapon(_skin.get("minus", ImageButtonStyle.class), _w, false);
			//buttonTemp.addListener(weaponClick);
			shipWindow.add( buttonTemp ).left();
			
			shipWindow.row();
		}
		
		shipWindow.add( new Image(_skin.getDrawable("default-splitpane-vertical")) ).colspan(4).fillX();
		shipWindow.row();
		
		equipmentLabel = new Label( "Equipment: " + Player.getShip().getFilledEquipmentSlots() +" of "+Player.getShip().getTotalEquipmentSlots() + " slots in use.", _skin );
		shipWindow.add(equipmentLabel).colspan(4).left();
		shipWindow.row();
		
		for (final Equipment _e : Player.getShip().getEquipment()) {
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_e);
				}
			});
			shipWindow.add( tempIButton ).right();
			
			String drawable = "Laser1"; //TODO
			shipWindow.add( new Image(_skin.getDrawable(drawable) ) ).right();
			
			shipWindow.add( new Label( _e.getName(), _skin ) ).left();	
			
			ImageButtonEquipment buttonTemp = new ImageButtonEquipment(_skin.get("minus", ImageButtonStyle.class), _e, false);
			//buttonTemp.addListener(weaponClick);
			shipWindow.add( buttonTemp ).left();
			shipWindow.row();
		}
						
		//ShipScreen.updateAll = true;	
		
	}
	
	public static void updateList() {
	}	
}
