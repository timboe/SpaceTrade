package com.timboe.spacetrade.windows;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.world.TextButtonGoods;

public class WarpBuyWindow {

	private static ChangeListener buyClick;
	private static Window buyWindow;
	private static Label disclaimer;
	private static TextButton prevPlanet;
	private static TextButton nextPlanet;
	private static ImageButton warp;
	private static Label warpInfo;
	private static final EnumMap<Goods, Label> labelName = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelLocalPrice = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelRemotePrice = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Slider> sliderStock = new EnumMap<Goods, Slider>(Goods.class);
	private static final EnumMap<Goods, Label> labelStock = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelCargo = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, TextButtonGoods> buttonBuy = new EnumMap<Goods, TextButtonGoods>(Goods.class);
	private static boolean buyWindowPopulated = false;
	private static int planetClickedID;
	
	private static Planet curPlanet;
	private static Planet targetPlanet;

	private static void populateBuyWindow() {
		
		final Skin _skin = Textures.getSkin();
		
		buyClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("BuyButton","Interact:"+event.toString()+" "+((TextButtonGoods)actor).getGoods());
				final Goods _g = ((TextButtonGoods)actor).getGoods();
				final int _amount = (int) sliderStock.get(_g).getValue();
				final int _price_per_unit = curPlanet.getPriceBuy(_g);
				final int _price = _price_per_unit * _amount;
				if (_price > Player.getCredz()) {
					Gdx.app.log("BuyButton", "Buy Failed, insufficient money!");
					Help.errorOK("You don't have enough Credz for that!\nCost: $"+_price+"\nCredz: $"+Player.getCredz());
					return;
				}
				if (_amount > Player.getFreeCargo()) {
					Help.errorOK("You don't have enough cargo space to store all that!\nRequired Space:"+_amount+"\nAvailable Space:"+Player.getFreeCargo()+"\nConsider purchasing a larger ship.");
					Gdx.app.log("BuyButton", "Buy Failed, insufficient cargo holds!");
					return;
				}
				Player.modCredz(-_price); //note minus
				Player.addStock(_g, _amount, _price_per_unit);
				curPlanet.modStock(_g, -_amount); //note minus

				updateList(true);
			}
		};
		
		buyWindow = new Window("MyWindow", _skin);
		if (SpaceTrade.debug == true) buyWindow.debug();
		Table innerTable = new Table();
		if (SpaceTrade.debug == true) innerTable.debug();
		innerTable.defaults().pad(5);
		
		Label titleLabelA = new Label("GOODS", _skin);
		Label titleLabelB = new Label("LOCAL PRICE\nPER UNIT", _skin);
		titleLabelB.setAlignment(Align.center);
		Label titleLabelC = new Label("REMOTE PRICE\nPER UNIT", _skin);
		titleLabelC.setAlignment(Align.center);
		Label titleLabelD = new Label("STOCK", _skin);
		Label titleLabelE = new Label("CARGO", _skin);
		Label titleLabelF = new Label("BUY", _skin);
		disclaimer = new Label("", _skin);
		disclaimer.setAlignment(Align.center);
		
		innerTable.add(disclaimer).colspan(8);
		innerTable.row();
		innerTable.add(titleLabelA).colspan(2);
		innerTable.add(titleLabelB);
		innerTable.add(titleLabelC);
		innerTable.add(titleLabelD).colspan(2);
		innerTable.add(titleLabelE);
		innerTable.add(titleLabelF);
		innerTable.row();
		for (final Goods _g : Goods.values()) {
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_g);
				}
			});
			innerTable.add( tempIButton );
			
			Label temp = new Label( _g.toDisplayString(), _skin );
			labelName.put(_g, temp);
			innerTable.add(temp);
			
			temp = new Label( "10", _skin.get("background", LabelStyle.class) );
			labelLocalPrice.put(_g, temp);
			innerTable.add( temp );
			
			temp = new Label( "101", _skin.get("background", LabelStyle.class) );
			labelRemotePrice.put(_g, temp);
			innerTable.add( temp );
			
			Slider sliderTemp = new Slider(0, 1, 1, false, _skin ); //set slider
			sliderStock.put(_g, sliderTemp);
			innerTable.add(sliderTemp);
			
			temp = new Label( "1000", _skin );
			labelStock.put(_g, temp);
			innerTable.add( temp ).width(50);	
			
			temp = new Label( "1000", _skin.get("background", LabelStyle.class) );
			labelCargo.put(_g, temp);
			innerTable.add( temp );	
			
			TextButtonGoods buttonTemp = new TextButtonGoods("BUY", _skin.get("large", TextButtonStyle.class), _g);
			buttonTemp.addListener(buyClick);
			buttonBuy.put(_g, buttonTemp);
			innerTable.add( buttonTemp );	
			
			innerTable.row();
		}
		
		buyWindow.add(innerTable).colspan(4);
		buyWindow.row();
		
		prevPlanet = new TextButton("<---", _skin);
		prevPlanet.addListener(new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("PrevButton","Click");
				StarmapScreen.setPlanetClickedID( Starmap.prevNearbyPlanet() );
			}
		});
		buyWindow.add(prevPlanet).left();
		
		nextPlanet = new TextButton("--->", _skin);
		nextPlanet.addListener(new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("nextButton","Click");
				StarmapScreen.setPlanetClickedID( Starmap.nextNearbyPlanet() );
			}
		});
		buyWindow.add(nextPlanet).left();

		warpInfo = new Label("", _skin);
		warpInfo.setAlignment(Align.center);
		buyWindow.add(warpInfo).center();
		
		warp = new ImageButton(_skin.get("engage", ImageButtonStyle.class));
		warp.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("WARP", "NativeHeap:"+Gdx.app.getNativeHeap()/1048576+" MB " +
        		"JavaHeap:"+Gdx.app.getJavaHeap()/1048576+" MB");
				//Here we go! get angle
				final Planet _target = Starmap.getPlanet( StarmapScreen.getPlanetClickedID() );
				final float _targetX = _target.getX() + _target.radius;
				final float _targetY = _target.getY() + _target.radius;
				final float _dY = _targetY - Player.getPlayer().getY();
				final float _dX = _targetX - Player.getPlayer().getX();
				final float _dist = Player.getPlanet().dst(_targetX, _targetY);
				final float _time = 5*_dist / Starmap.SPEED;
				final float acceleration = Player.getShip().getAcc();
				float _a = (float) Math.toDegrees( Math.atan2(_dY, _dX) ) - 90f;
				Starmap.doTravelTime(Starmap.getTravelTimeGalactic(curPlanet,targetPlanet,acceleration),
						Starmap.getTravelTimeShip(curPlanet,targetPlanet,acceleration));
				SequenceAction moveSequence = new SequenceAction();
				moveSequence.addAction( Actions.rotateTo(_a, 1f, Interpolation.circle) );
				moveSequence.addAction( Actions.moveTo(_targetX, _targetY, _time, Interpolation.exp5) );
				moveSequence.addAction( Actions.run(new Runnable() {
			        public void run () {
						Player.getPlayer().move( Starmap.getPlanet( StarmapScreen.getPlanetClickedID() ) );
						ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().thePlanetScreen );
						StarmapScreen.setPlanetClickedID(-1);
			        }
				}
			    ));
				Player.getPlayer().addAction(moveSequence);
			}
		});
		buyWindow.add(warp).right();
		
		buyWindow.addListener( new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				//catch
				return true;
			}
		});

	}
	
	public static void updateList(boolean _intial, int _planetClickedID) {
		planetClickedID = _planetClickedID;
		updateList(_intial);
	}
	
	public static void updateList(boolean _intial) {
		if (planetClickedID < 0) return;
		
		curPlanet = Player.getPlanet();
		targetPlanet = Starmap.getPlanet(planetClickedID);
		
		final String _titleStr = "Buying from "+curPlanet.getFullName();
		buyWindow.setTitle(_titleStr);
		buyWindow.setMovable(true);
		final float acceleration = Player.getShip().getAcc();
		final int _ly = (int) Math.floor( Starmap.getDistanceLightyear(curPlanet, targetPlanet) );
		disclaimer.setText("Comparing prices to "+targetPlanet.getFullName()
				+".\n Caution: Prices correct as of Stardate "+(Starmap.getStarDate() - _ly) 
				+" (" + _ly + " years ago).");
		
		warpInfo.setText(targetPlanet.getName() + " is " + _ly + " lightyears away from " + curPlanet.getName()
				+".\nTravel will take "+String.format("%.2f",Starmap.getTravelTimeGalactic(curPlanet,targetPlanet,acceleration))
				+" GalacticYears and "+String.format("%.2f",Starmap.getTravelTimeShip(curPlanet,targetPlanet,acceleration))
				+" ShipYears.");
		for (Goods _g : Goods.values()) {

			final int _localPrice = curPlanet.getPriceBuy(_g);
			final int _remotePrice = targetPlanet.getPriceSell(_g, _ly);
			
			if (targetPlanet.getSells(_g) == false) {
				labelRemotePrice.get(_g).setText( "---" );
			} else {
				labelRemotePrice.get(_g).setText("$"+Integer.toString(_remotePrice));
			}
			
			if (curPlanet.getSells(_g) == false || targetPlanet.getSells(_g) == false) {
				labelRemotePrice.get(_g).setColor(1f, 1f, 1f, 1f);
			} else if (_remotePrice < _localPrice) {
				labelRemotePrice.get(_g).setColor(1f, 0f, 0f, 1f);
			} else {
				labelRemotePrice.get(_g).setColor(0f, 1f, 0f, 1f);
			}
			
			int _cargo = Player.getStock(_g);
			labelCargo.get(_g).setText( Integer.toString(_cargo) );

			if (curPlanet.getSells(_g) == false) {
				labelName.get(_g).setText(_g.toDisplayString());
				labelLocalPrice.get(_g).setText( "---" );
				sliderStock.get(_g).setRange(0, 1);
				sliderStock.get(_g).setVisible(false);
				labelStock.get(_g).setText("---");
				sliderStock.get(_g).setTouchable(Touchable.disabled);
				buttonBuy.get(_g).setTouchable(Touchable.disabled);
				buttonBuy.get(_g).setVisible(false);
				continue;
			} else {
				labelName.get(_g).setText(_g.toDisplayString() + "[" + curPlanet.getStock(_g)  + "]");
				if (curPlanet.getStock(_g) == 0) {
					sliderStock.get(_g).setTouchable(Touchable.disabled);
				} else {
					sliderStock.get(_g).setTouchable(Touchable.enabled);
				}
				labelLocalPrice.get(_g).setText("$"+ Integer.toString(_localPrice) );
				buttonBuy.get(_g).setTouchable(Touchable.enabled);
				buttonBuy.get(_g).setVisible(true);
				sliderStock.get(_g).setVisible(true);
			}
			
			int canAffordPrice = (int)Math.floor( (float)Player.getCredz() / (float)_localPrice);
			int canAffordSpace = Player.getFreeCargo();
			int canAfford = Math.min(canAffordPrice, canAffordSpace);
			canAfford = Math.max(canAfford, 0);
			if (_intial == true) {
				int toSell = curPlanet.getStock(_g);
				if (toSell == 0) {
					sliderStock.get(_g).setRange(0, 1);
					sliderStock.get(_g).setTouchable(Touchable.disabled);
				} else {
					sliderStock.get(_g).setRange(0, toSell);
					sliderStock.get(_g).setValue(Math.min(toSell, canAfford));
					sliderStock.get(_g).setTouchable(Touchable.enabled);
				}
			}
			
			int _chosen = (int) sliderStock.get(_g).getValue();
			labelStock.get(_g).setText( Integer.toString(_chosen) );
			if (_chosen > canAfford) {
				labelStock.get(_g).setColor(1f, 0f, 0f, 1f);
			} else {
				labelStock.get(_g).setColor(1f, 1f, 1f, 1f);
			}
		}
	}

	public static void addToTable(Table leftTable) {
		if (buyWindowPopulated == true) return;
		buyWindowPopulated = true;
		populateBuyWindow();
		leftTable.add(buyWindow);
	}
	
}
