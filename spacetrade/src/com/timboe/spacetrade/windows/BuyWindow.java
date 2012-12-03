package com.timboe.spacetrade.windows;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.TextButtonGoods;

public class BuyWindow {
	private static final EnumMap<Goods, Label> labelName = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelPrice = new EnumMap<Goods, Label>(Goods.class);
//	private static final EnumMap<Goods, Label> labelPricePaid = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Slider> sliderStock = new EnumMap<Goods, Slider>(Goods.class);
	private static final EnumMap<Goods, Label> labelStock = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, TextButton> buttonBuy = new EnumMap<Goods, TextButton>(Goods.class);
	private static final EnumMap<Goods, Label> labelCargo = new EnumMap<Goods, Label>(Goods.class);
	
	private static Window buyWindow = null;
	private static boolean buyWindowPopulated = false;

	private static ChangeListener buyClick;
	private static Planet curPlanet;
	
	public static Window getWindow() {
		if (buyWindowPopulated == true) return buyWindow;
		buyWindowPopulated = true;
		populatebuyWindow();
		return buyWindow;
	}
	
	public static void updateList(boolean _intial) {
		if (buyWindowPopulated == false) getWindow();
		
		curPlanet = Player.getPlanet();
		final String _titleStr = "Buying from "+curPlanet.getFullName();
		buyWindow.setTitle(_titleStr);
		for (Goods _g : Goods.values()) {
			labelCargo.get(_g).setText( Integer.toString(Player.getStock(_g)) );
			
			int canAffordPrice = (int)Math.floor( (float)Player.getCredz() / (float)curPlanet.getPriceBuy(_g));
			int canAffordSpace = Player.getFreeCargo();
			int canAfford = Math.min(canAffordPrice, canAffordSpace);
			canAfford = Math.max(canAfford, 0);

			if (curPlanet.getSells(_g) == false) {
				labelName.get(_g).setText(_g.toDisplayString());
				labelPrice.get(_g).setText( "---" );
				sliderStock.get(_g).setRange(0, 1);
				sliderStock.get(_g).setTouchable(Touchable.disabled);
				sliderStock.get(_g).setVisible(false);
//				labelPricePaid.get(_g).setText("---");
				labelStock.get(_g).setText( Integer.toString(Player.getStock(_g)) );
				buttonBuy.get(_g).setTouchable(Touchable.disabled);
				buttonBuy.get(_g).setVisible(false);
				continue;
			} else {
			
				buttonBuy.get(_g).setTouchable(Touchable.enabled);
				buttonBuy.get(_g).setVisible(true);
				sliderStock.get(_g).setVisible(true);
			}
			
			labelName.get(_g).setText(_g.toDisplayString() + "[" + curPlanet.getStock(_g)  + "]");
			
			int cost = curPlanet.getPriceBuy(_g);
			labelPrice.get(_g).setText( "$" + Integer.toString(cost) );
			
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
	
	private static void populatebuyWindow() {
		
		Skin _skin = Textures.getSkin();

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
		
		buyWindow = new Window("", _skin);
		buyWindow.setMovable(false);
		buyWindow.debug();
		
		Label titleLabelA = new Label("GOODS", _skin);
		Label titleLabelB = new Label("PRICE PER\nUNIT", _skin);
		titleLabelB.setAlignment(Align.center);
//		Label titleLabelC = new Label("PROFIT", _skin);
//		titleLabelC.setAlignment(Align.center);
		Label titleLabelD = new Label("AMOUNT", _skin);
		Label titleLabelE = new Label("CARGO", _skin);
		
		buyWindow.defaults().pad(5);
		buyWindow.add(titleLabelA).colspan(2);
		buyWindow.add(titleLabelB);
//		buyWindow.add(titleLabelC);
		buyWindow.add(titleLabelD).colspan(3);
		buyWindow.add(titleLabelE);
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
			buyWindow.add(temp);
			labelName.put(_g, temp);
			
			temp = new Label( "10", _skin.get("background", LabelStyle.class) );
			labelPrice.put(_g, temp);
			buyWindow.add( temp );
			
//			temp = new Label( "100", _skin.get("background", LabelStyle.class) );
//			labelPricePaid.put(_g, temp);
//			buyWindow.add( temp ).pad(10);
			
			Slider sliderTemp = new Slider(0, 1, 1, false, _skin ); //set slider
			sliderStock.put(_g, sliderTemp);
			buyWindow.add(sliderTemp).height(50);
			
			temp = new Label( "1000", _skin );
			labelStock.put(_g, temp);
			buyWindow.add( temp ).width(50);	
			
			TextButtonGoods buttonTemp = new TextButtonGoods("BUY", _skin.get("large", TextButtonStyle.class), _g);
			buttonTemp.addListener(buyClick);
			buttonBuy.put(_g, buttonTemp);
			buyWindow.add( buttonTemp ).height(50);	
			
			temp = new Label( "1", _skin.get("background", LabelStyle.class) );
			labelCargo.put(_g, temp);
			buyWindow.add( temp );	
			
			buyWindow.row();
		}
		updateList(true);
	}
}
