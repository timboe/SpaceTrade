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
import com.timboe.spacetrade.enumerator.Equipment;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.ShipScreen;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.ImageButtonEquipment;

public class EquipmentWindow {
	private static final EnumMap<Equipment, ImageButtonEquipment> equipmentMinus = new EnumMap<Equipment, ImageButtonEquipment>(Equipment.class);
	private static final EnumMap<Equipment, ImageButtonEquipment> equipmentPlus = new EnumMap<Equipment, ImageButtonEquipment>(Equipment.class);
	private static final EnumMap<Equipment, Label> equipmentCargo = new EnumMap<Equipment, Label>(Equipment.class);
	private static final EnumMap<Equipment, Label> equipmentPrice = new EnumMap<Equipment, Label>(Equipment.class);

	private static ChangeListener equipmentClick;

	private static Window equipmentWindow = null;
//	private static boolean equipmentWindowPopulated = false;
	
	public static Window getWindow() {
//		if (equipmentWindowPopulated == true) return equipmentWindow;
//		equipmentWindowPopulated = true;
		populateWindow();
		return equipmentWindow;
	}

	private static void populateWindow() {
		Skin _skin = Textures.getSkin();
		
		equipmentClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Equipment _class = ((ImageButtonEquipment)actor).getEquipment();
				int _cost = Math.round(_class.getCost() * Player.getPlanet().getEquipmentPriceMod());
				Gdx.app.log("EquipmentButton","Interact:"+event.toString()+" "+_class);
				if ( ((ImageButtonEquipment)actor).getAdd() == false ) { //TRY SELL
					if (Player.getShip().getNumberInstalled(_class) == 0) {
						return;
					}
					Player.modCredz(_cost);
					Player.getShip().disarm(_class);
				} else { //TRY BUY
					if (Player.getShip().getFreeEquipmentSlots() == 0) {
						Help.errorOK("\nThere are no free equipment slots.\n\nTry selling some equipment to free a slot,\nor buy a bigger ship!\n ");
						return;
					}
					if (_cost > Player.getAvailableCredzIncOD()) {
						Help.errorOK("\nYou cannot afford to buy this pice of equipment!\n ");
						return;	
					}
					Player.modCredz(-_cost);
					Player.getShip().arm( ((ImageButtonEquipment)actor).getEquipment() );
				}
				ShipScreen.updateAll = true;	
				RightBar.update();
			}
		};
		
		equipmentWindow = new Window("Equipment", _skin);
		equipmentWindow.defaults().pad(5);
		equipmentWindow.setMovable(false);
		equipmentWindow.debug();
		
		if (Player.getPlanet().getCiv() == Civilisation.Agricultural) {
			equipmentWindow.add( new Label( "Not Sold Here", _skin ) );	
		}
		
		for (final Equipment _e : Equipment.values()) {
			if (Player.getPlanet().getSellsEquipment(_e) == false) {
				equipmentPrice.put(_e, null); //used to communicate decision
				continue;
			}
			
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_e);
				}
			});
			equipmentWindow.add( tempIButton );
			
			String drawable = _e.getEquipmentClass().toString();
			equipmentWindow.add( new Image(_skin.getDrawable(drawable) ) );
			
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
			
		ShipScreen.updateAll = true;	
	}

	public static void updateList() {
		if (equipmentWindow == null) return;

		int _slots = Player.getShip().getFreeEquipmentSlots();
		if (_slots == 1) {
			equipmentWindow.setTitle("Equipment, "+_slots+" Slot Free");
		} else {
			equipmentWindow.setTitle("Equipment, "+_slots+" Slots Free");
		}

		
		for (final Equipment _e : Equipment.values()) {
			if (equipmentPrice.get(_e) == null) continue; //not sold
			
			//set price
			int _price = _e.getCost();
			equipmentPrice.get(_e).setText("$" + Integer.toString((int) (_price * Player.getPlanet().getEquipmentPriceMod())) );
			if (_price > Player.getAvailableCredzIncOD()) {
				equipmentPrice.get(_e).setColor(Color.RED);
			} else {
				equipmentPrice.get(_e).setColor(Color.GREEN);
			}
			
			//set n owned
			int _owned = Player.getShip().getNumberInstalled(_e);
			equipmentCargo.get(_e).setText( Integer.toString(_owned) );
		}
	}
}
