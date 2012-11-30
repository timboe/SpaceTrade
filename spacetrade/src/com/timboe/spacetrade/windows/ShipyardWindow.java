package com.timboe.spacetrade.windows;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.ShipScreen;
import com.timboe.spacetrade.ship.Ship;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.TextButtonShip;

public class ShipyardWindow {

	private static final EnumMap<ShipClass, TextButtonShip> doShip = new EnumMap<ShipClass, TextButtonShip>(ShipClass.class);
	private static final EnumMap<ShipClass, Label> labelShipMod = new EnumMap<ShipClass, Label>(ShipClass.class);
	private static ChangeListener shipClick;

	private static Window shipyardWindow = null;
	
	public static Window getWindow() {
		populateWindow();
		return shipyardWindow;
	}
	
	private static void doShipSwap(ShipClass _sc, int _cost) {
		
		Ship newShip = new Ship(_sc);
		newShip.setMod( Player.getPlanet().getShipMod(_sc) );
		newShip.moveEquipment( Player.getShip() );
		Player.setShip( newShip );
		Player.modCredz(-_cost);
		
		System.out.println("ShipSwapChosen: " + _sc);
		ShipScreen.updateAll = true;	
	}

	private static void populateWindow() {
		final Skin _skin = Textures.getSkin();
		
		shipClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				final ShipClass _class = ((TextButtonShip)actor).getShipClass();
				Gdx.app.log("ShipButton","Interact:"+event.toString()+" "+_class);
				int _tradeInPrice = Math.round(Player.getShip().getTradeInPrice() * Player.getPlanet().getEquipmentPriceMod());
				final int _cost = Math.round((Player.getPlanet().getEquipmentPriceMod() * _class.getCost()) - _tradeInPrice);
				if (_cost > Player.getCredz()) { //CHECK CREDITS
					Help.errorOK("\nYou cannot afford to buy this ship.\n ");
					return;	
				}
				if (Player.getShip().getFilledWeaponSlots() > _class.getWeaponSlots()) { //CHECK WEAPONS
					Help.errorOK("\nYour ship is carying "+Player.getShip().getFilledWeaponSlots()+" weapons."+
							"\n\nA " +_class.getName()+" can only cary "+_class.getWeaponSlots()+" weapons."+
							"\n\nYou must sell some weapons before you can buy this ship.\n ");
					return;	
				}
				if (Player.getShip().getFilledEquipmentSlots() > _class.getTechSlots()) { //CHECK EQUIPMENT
					Help.errorOK("\nYour ship is carying "+Player.getShip().getFilledEquipmentSlots()+" pieces of equipment."+
							"\n\nA " +_class.getName()+" can only cary "+_class.getTechSlots()+" pieces of equipment."+
							"\n\nYou must sell some equipment before you can buy this ship.\n ");
					return;	
				}
				if (Player.getTotalCargo() > _class.getMaxCargo()) { //CHECK CARGO
					Help.errorOK("\nYour ship is carying "+Player.getTotalCargo()+" pieces of cargo."+
							"\n\nA " +_class.getName()+" can only cary "+_class.getMaxCargo()+" pieces of cargo."+
							"\n\nYou must sell some cargo before you can buy this ship.\n ");
					return;	
				}
				//CONFIRM
				String _CostStr = "\n\nThis will cost $"+_cost+".";
				if(_cost < 0) { //DOWNGRADE
					_CostStr = "\n\nThis will refund you $"+Integer.toString(-_cost)+".";
				}
					
				new Dialog("Confirm Ship Purchase", _skin, "dialog") {
					protected void result (Object object) {
						if (((Boolean)object) == true) {
							doShipSwap(_class, _cost);
						} else	System.out.println("BuyCancelled: " + object);
					}
				}.text("\nYou are selling your "+Player.getShip().getFullName()+
						"\nin order to buy a "+Player.getPlanet().getShipMod(_class).toString()+" "+_class.getName()+"."+
						_CostStr +
						"\nIs this what you want to do?\n ")
					.button("  Yes!  ", true, Textures.getSkin().get("large", TextButtonStyle.class))
					.button("  No!  ", false, Textures.getSkin().get("large", TextButtonStyle.class))
					.key(Keys.ENTER, true)
					.key(Keys.ESCAPE, false)
					.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage());
			}
		};
		
		shipyardWindow = new Window("Shipyard", _skin);
		shipyardWindow.defaults().pad(5);
		shipyardWindow.setMovable(false);
		shipyardWindow.debug();
		
		if (Player.getPlanet().getCiv() == Civilisation.Agricultural) {
			shipyardWindow.add( new Label( "Not Sold Here", _skin ) );	
		}
		
		for (final ShipClass _s : ShipClass.values()) {
			if (Player.getPlanet().getSells(_s) == false) {
				labelShipMod.put(_s, null); //To communicate descion
				continue;
			}
			
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_s);
				}
			});
			shipyardWindow.add( tempIButton );
			
			Label temp =  new Label( _s.getName(), _skin );
			labelShipMod.put(_s, temp);
			shipyardWindow.add( temp );	

			TextButtonShip buttonTemp = new TextButtonShip("BUY", _skin, _s);
			buttonTemp.addListener(shipClick);
			doShip.put(_s, buttonTemp);
			shipyardWindow.add( buttonTemp );	
			
			shipyardWindow.row();
		}
			
		ShipScreen.updateAll = true;	
	}

	public static void updateList() {
		if (shipyardWindow == null) return;

		for (final ShipClass _s : ShipClass.values()) {
			if (labelShipMod.get(_s) == null) {
				continue;
			}
			
			labelShipMod.get(_s).setText(Player.getPlanet().getShipMod(_s).toString() + " " + _s.getName());
			
			//if _exact_ same ship then hide buy/sell
			if (_s == Player.getShip().getShipClass() && Player.getPlanet().getShipMod(_s) == Player.getShip().getMod()) {
				doShip.get(_s).setVisible(false);
				continue;
			}
			doShip.get(_s).setVisible(true);
			
			int _tradeInPrice = Math.round(Player.getShip().getTradeInPrice() * Player.getPlanet().getEquipmentPriceMod());
			int cost = Math.round((Player.getPlanet().getEquipmentPriceMod() *_s.getCost()) - _tradeInPrice);
			if (cost > 0) {
				doShip.get(_s).setColor(Color.GREEN);
				if (_s == Player.getShip().getShipClass()) {
					doShip.get(_s).setText("EXCHANGE FOR\n$" + Integer.toString(cost) );
				} else {
					doShip.get(_s).setText("UPGRADE FOR\n$" + Integer.toString(cost) );
				}
			} else {
				doShip.get(_s).setColor(Color.RED);
				doShip.get(_s).setText("DOWNSIZE\n$" + Integer.toString(cost) );
			}
		}
	}
}
