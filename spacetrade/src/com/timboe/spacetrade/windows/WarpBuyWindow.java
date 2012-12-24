package com.timboe.spacetrade.windows;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.PlanetScreen;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.world.TextButtonGoods;

public class WarpBuyWindow {

	private static ChangeListener buyClick;
	private static Window buyWindow = new Window("", Textures.getSkin());
	private static Window warpWindow = new Window("Select Destination", Textures.getSkin());
	private static Window containerWindow = new Window("", Textures.getSkin().get("transparent",WindowStyle.class));;
	private static Label disclaimer = new Label("", Textures.getSkin());
	private static final ImageButton prevPlanet = new ImageButton(Textures.getSkin().get("left",ImageButtonStyle.class));
	private static final ImageButton close = new ImageButton(Textures.getSkin().get("cancel",ImageButtonStyle.class));
	private static final ImageButton nextPlanet = new ImageButton(Textures.getSkin().get("right",ImageButtonStyle.class));
	private static final ImageButton warp = new ImageButton(Textures.getSkin().get("engage",ImageButtonStyle.class));
	private static final Label warpInfo = new Label("", Textures.getSkin());
	private static final EnumMap<Goods, Label> labelName = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelLocalPrice = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelRemoteDiff= new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelRemotePrice = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Slider> sliderStock = new EnumMap<Goods, Slider>(Goods.class);
	private static final EnumMap<Goods, Label> labelStock = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelCargo = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, TextButtonGoods> buttonBuy = new EnumMap<Goods, TextButtonGoods>(Goods.class);
	private static int planetClickedID;
	
	private static Planet curPlanet;
	private static Planet targetPlanet;
	
	private static boolean windowPopulated = false;
	
	private static final int width = 1100;
	
	public static Window getWindow() {
		if (windowPopulated == true) return containerWindow;
		windowPopulated = true;
		populateWindow();
		return containerWindow;
	}
	

	private static void populateWindow() {
		
		final Skin _skin = Textures.getSkin();
		
		buyClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("BuyButton","Interact:"+event.toString()+" "+((TextButtonGoods)actor).getGoods());
				final Goods _g = ((TextButtonGoods)actor).getGoods();
				final int _amount = (int) sliderStock.get(_g).getValue();
				final int _price_per_unit = curPlanet.getPriceBuy(_g);
				final int _price = _price_per_unit * _amount;
				if (_price > Player.getAvailableCredzIncOD()) {
					Gdx.app.log("BuyButton", "Buy Failed, insufficient money!");
					Help.errorOK("\nYou don't have enough Credz for that!\nCost: $"+_price+"\nAvailable Credit: $"+Player.getAvailableCredzIncOD()+"\n ");
					return;
				}
				if (_amount > Player.getFreeCargo()) {
					Help.errorOK("\nYou don't have enough cargo space to store all that!\nRequired Space:"+_amount+"\nAvailable Space:"+Player.getFreeCargo()+"\nConsider purchasing a larger ship.\n ");
					Gdx.app.log("BuyButton", "Buy Failed, insufficient cargo holds!");
					return;
				}
				Player.modCredz(-_price); //note minus
				Player.addStock(_g, _amount, _price_per_unit);
				curPlanet.modStock(_g, -_amount); //note minus

				updateList(true);
			}
		};
		

		///////////////////
		
		warpWindow.debug();
		warpWindow.defaults().pad(5);
		
		prevPlanet.addListener(new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("PrevButton","Click");
				StarmapScreen.setPlanetClickedID( Starmap.prevNearbyPlanet() );
			}
		});
		warpWindow.add(prevPlanet).left();
		
		close.addListener(new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("closeButton","Click"); 
				StarmapScreen.setPlanetClickedID( -1 );
			}
		});
		warpWindow.add(close).left();
		
		nextPlanet.addListener(new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("nextButton","Click");
				StarmapScreen.setPlanetClickedID( Starmap.nextNearbyPlanet() );
			}
		});
		warpWindow.add(nextPlanet).left();

		warpInfo.setAlignment(Align.center);
		warpWindow.add(warpInfo).center().width(640);
		
		warp.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("WARP", "NativeHeap:"+Gdx.app.getNativeHeap()/1048576+" MB " +
        		"JavaHeap:"+Gdx.app.getJavaHeap()/1048576+" MB");
				//Here we go! get angle
				final Planet _target = Starmap.getPlanet( StarmapScreen.getPlanetClickedID() );
				final float _targetX = _target.getX() + _target.getRadius();
				final float _targetY = _target.getY() + _target.getRadius();
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
						PlanetScreen.newDestination();
						ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().thePlanetScreen );
						StarmapScreen.setPlanetClickedID(-1);
			        }
				}
			    ));
				Player.getPlayer().addAction(moveSequence);
			}
		});
		warpWindow.add(warp).right();
		
		warpWindow.addListener( new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true; //catch
			}
		});
		
		///////////////////
		
		buyWindow.debug();
		buyWindow.defaults().pad(5);
		
		Label titleLabelA = new Label("GOODS", _skin);
		Label titleLabelB = new Label("PRICE/UNIT", _skin);
		Label titleLabelB2 = new Label("+/-", _skin);
		titleLabelB.setAlignment(Align.center);
		Label titleLabelC = new Label("PROFIT*", _skin);
		titleLabelC.setAlignment(Align.center);
		Label titleLabelD = new Label("STOCK", _skin);
		Label titleLabelE = new Label("CARGO", _skin);
		Label titleLabelF = new Label("BUY", _skin);
		disclaimer.setAlignment(Align.center);
		

		buyWindow.add(titleLabelA).colspan(2);
		buyWindow.add(titleLabelB);
		buyWindow.add(titleLabelB2);
		buyWindow.add(titleLabelC);
		buyWindow.add(titleLabelD).colspan(2);
		buyWindow.add(titleLabelE);
		buyWindow.add(titleLabelF);
		buyWindow.row();
		for (final Goods _g : Goods.values()) {
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_g);
				}
			});
			buyWindow.add( tempIButton );
			
			Label temp = new Label( _g.toDisplayString(), _skin );
			labelName.put(_g, temp);
			buyWindow.add(temp);
			
			temp = new Label( "10", _skin.get("background", LabelStyle.class) );
			temp.setAlignment(Align.center);
			labelLocalPrice.put(_g, temp);
			buyWindow.add( temp ).width(100);
			
			temp = new Label( "1%", _skin.get("background", LabelStyle.class) );
			temp.setAlignment(Align.center);
			labelRemoteDiff.put(_g, temp);
			buyWindow.add( temp ).width(100);
			
			temp = new Label( "101", _skin.get("background", LabelStyle.class) );
			temp.setAlignment(Align.center);
			labelRemotePrice.put(_g, temp);
			buyWindow.add( temp ).width(100);
			
			Slider sliderTemp = new Slider(0, 1, 1, false, _skin ); //set slider
			sliderStock.put(_g, sliderTemp);
			buyWindow.add(sliderTemp);
			
			temp = new Label( "1000", _skin );
			labelStock.put(_g, temp);
			buyWindow.add( temp ).width(35);	
			
			temp = new Label( "1000", _skin.get("background", LabelStyle.class) );
			temp.setAlignment(Align.center);
			labelCargo.put(_g, temp);
			buyWindow.add( temp ).width(35);	
			
			TextButtonGoods buttonTemp = new TextButtonGoods("BUY", _skin.get("large", TextButtonStyle.class), _g);
			buttonTemp.addListener(buyClick);
			buttonBuy.put(_g, buttonTemp);
			buyWindow.add( buttonTemp );	
			
			buyWindow.row();
		}
		buyWindow.add(disclaimer).colspan(9);

		buyWindow.addListener( new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true; //catch
			}
		});
	
		//////////
		
		containerWindow.defaults().padTop(10);
		containerWindow.debug();
		containerWindow.add(warpWindow).width(width);
		containerWindow.row();
		containerWindow.add(buyWindow).width(width);

	}
	
	public static void updateList(boolean _intial, int _planetClickedID) {
		planetClickedID = _planetClickedID;
		updateList(_intial);
	}
	
	public static void updateList(boolean _intial) {
		if (planetClickedID < 0) return;
		
		curPlanet = Player.getPlanet();
		targetPlanet = Starmap.getPlanet(planetClickedID);
		
		final String _titleStr = "`Buy Before you Fly' from "+curPlanet.getName();
		buyWindow.setTitle(_titleStr);
		buyWindow.setMovable(false);
		
		warpWindow.setTitle("Selected Destination: "+targetPlanet.getFullName());
		warpWindow.setMovable(false);
		
		final float acceleration = Player.getShip().getAcc();
		final int _ly = (int) Math.floor( Starmap.getDistanceLightyear(curPlanet, targetPlanet) );
		disclaimer.setText("*Caution: Prices on "+targetPlanet.getName()+" correct as of Stardate "+(Starmap.getStarDate() - _ly) 
				+" (" + _ly + " years ago).");
		
//		warpInfo.setText(targetPlanet.getName() + " is " + _ly + " lightyears away from " + curPlanet.getName()
//				+".\nTravel will take "+String.format("%.2f",Starmap.getTravelTimeGalactic(curPlanet,targetPlanet,acceleration))
//				+" GalacticYears and "+String.format("%.2f",Starmap.getTravelTimeShip(curPlanet,targetPlanet,acceleration))
//				+" ShipYears.");
		
		String _range;
		if (_ly > Player.getShip().getRangeLightYears()) {
			_range = "Your ship is unable to travel this far!";
			warpInfo.setColor(Color.RED);
			warp.setDisabled(true);
		} else {
			_range = "Flight Time: "+String.format("%.2f",Starmap.getTravelTimeShip(curPlanet,targetPlanet,acceleration)) + " years.";
			warpInfo.setColor(Color.WHITE);
			warp.setDisabled(false);
		}
		
		warpInfo.setText("Pirates: "+targetPlanet.getActivity(ShipTemplate.Pirate).toString()
				+", Police: "+targetPlanet.getActivity(ShipTemplate.Police).toString()
				+", Traders: "+targetPlanet.getActivity(ShipTemplate.Trader).toString() + "\n" + _range);

		
		for (Goods _g : Goods.values()) {

			final int _localPrice = curPlanet.getPriceBuy(_g);
			final int _remotePrice = targetPlanet.getPriceSell(_g, _ly);
			final int _chosen = (int) sliderStock.get(_g).getValue();
			
			if (targetPlanet.getSells(_g) == false) {
				labelRemotePrice.get(_g).setText( "---" );
				labelRemoteDiff.get(_g).setText( "---" );
			} else {
				int _profitPerItem = _remotePrice - _localPrice;
				labelRemotePrice.get(_g).setText("$"+Integer.toString(_profitPerItem * _chosen));
				if (curPlanet.getSells(_g) == false) {
					labelRemoteDiff.get(_g).setText( "---" );
				} else {
					float _diff = ((float)(_remotePrice-_localPrice) / (float)_localPrice) * 100f;
					String _diffStr = "";
					if (_diff > 0) _diffStr += "+";
					if (Math.abs(_diff) < 10f) {
						_diffStr += String.format("%.1f", _diff) + "%";
					} else {
						_diffStr += ((Integer) Math.round(_diff)).toString() + "%";
					}
					labelRemoteDiff.get(_g).setText( _diffStr );
				}
			}
			
			if (curPlanet.getSells(_g) == false || targetPlanet.getSells(_g) == false) {
				labelRemotePrice.get(_g).setColor(1f, 1f, 1f, 1f);
				labelRemoteDiff.get(_g).setColor(1f, 1f, 1f, 1f);
			} else if (_remotePrice < _localPrice) {
				labelRemotePrice.get(_g).setColor(1f, 0f, 0f, 1f);
				labelRemoteDiff.get(_g).setColor(1f, 0f, 0f, 1f);
			} else {
				labelRemotePrice.get(_g).setColor(0f, 1f, 0f, 1f);
				labelRemoteDiff.get(_g).setColor(0f, 1f, 0f, 1f);
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
			
			int canAffordPrice = (int)Math.floor( (float)Player.getAvailableCredz() / (float)_localPrice);
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
			
			labelStock.get(_g).setText( Integer.toString(_chosen) );
			if (_chosen > canAfford) {
				labelStock.get(_g).setColor(1f, 0f, 0f, 1f);
			} else {
				labelStock.get(_g).setColor(1f, 1f, 1f, 1f);
			}
		}
	}
	
}
