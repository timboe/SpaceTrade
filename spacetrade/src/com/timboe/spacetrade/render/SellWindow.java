package com.timboe.spacetrade.render;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.TextButtonGoods;

public class SellWindow {
	private static final EnumMap<Goods, Label> labelPrice = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelPricePaid = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Slider> sliderStock = new EnumMap<Goods, Slider>(Goods.class);
	private static final EnumMap<Goods, Label> labelStock = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, TextButton> buttonSell = new EnumMap<Goods, TextButton>(Goods.class);
	private static Window sellWindow = null;
	private static boolean sellWindowPopulated = false;

	private static ChangeListener sellClik;
	private static Planet curPlanet;
	
	public static void addToTable(Table leftTable) {
		if (sellWindowPopulated == true) return;
		sellWindowPopulated = true;
		populateSellWindow();
		leftTable.add(sellWindow);
	}
	
	public static void updateList(boolean _intial) {
		
		curPlanet = Player.getPlayer().getPlanet();
		final String _titleStr = "Selling on "+curPlanet.getFullName();
		sellWindow.setTitle(_titleStr);
		for (Goods _g : Goods.values()) {
			if (curPlanet.getSells(_g) == false) {
				labelPrice.get(_g).setText( "---" );
				sliderStock.get(_g).setRange(0, 1);
				sliderStock.get(_g).setTouchable(Touchable.disabled);
				sliderStock.get(_g).setVisible(false);
				labelPricePaid.get(_g).setText("---");
				labelStock.get(_g).setText( Integer.toString(Player.getPlayer().getStock(_g)) );
				buttonSell.get(_g).setTouchable(Touchable.disabled);
				buttonSell.get(_g).setVisible(false);
				continue;
			} else {
				if (Player.getPlayer().getStock(_g) == 0) {
					sliderStock.get(_g).setTouchable(Touchable.disabled);
				} else {
					sliderStock.get(_g).setTouchable(Touchable.enabled);
				}
				buttonSell.get(_g).setTouchable(Touchable.enabled);
				buttonSell.get(_g).setVisible(true);
				sliderStock.get(_g).setVisible(true);
			}
			
			int cost = curPlanet.getPrice(_g);
			labelPrice.get(_g).setText( Integer.toString(cost) );
			
			if (_intial == true) {
				int toSell = Player.getPlayer().getStock(_g);
				if (toSell == 0) {
					sliderStock.get(_g).setRange(0, 1);
					sliderStock.get(_g).setTouchable(Touchable.disabled);
				} else {
					sliderStock.get(_g).setRange(0, toSell);
					sliderStock.get(_g).setValue(toSell);
					sliderStock.get(_g).setTouchable(Touchable.enabled);
				}
			}
			
			int avPaid = Player.getPlayer().getAvPaidPrice(_g);
			if (avPaid == 0) {
				labelPricePaid.get(_g).setColor(1f, 1f, 1f, 1f);
				labelPricePaid.get(_g).setText("0");
			} else {
				if (avPaid < cost) {
					labelPricePaid.get(_g).setColor(0f, 1f, 0f, 1f);
				} else {
					labelPricePaid.get(_g).setColor(1f, 0f, 0f, 1f);
				}
				labelPricePaid.get(_g).setText(Integer.toString(cost - avPaid));
			}


			int chosen = (int) sliderStock.get(_g).getValue();
			labelStock.get(_g).setText( Integer.toString(chosen) );
		}

	}
	
	private static void populateSellWindow() {
		
		Skin _skin = Textures.getSkin();
		
		sellClik = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("SellButton","Interact:"+event.toString()+" "+((TextButtonGoods)actor).getGoods());
				final Goods _g = ((TextButtonGoods)actor).getGoods();
				final int _amount = (int) sliderStock.get(_g).getValue();
				final int _profit = curPlanet.getPrice(_g) * _amount;
				Player.getPlayer().modCredz(_profit);
				Player.getPlayer().removeStock(_g, _amount);
				curPlanet.modStock(_g, _amount);
				final int _remainingStock = Player.getPlayer().getStock(_g);
				if (_remainingStock == 0) {
					sliderStock.get(_g).setRange(0, 1);
				} else {
					sliderStock.get(_g).setRange(0, _remainingStock);
					if (_amount > _remainingStock) {
						sliderStock.get(_g).setValue(_remainingStock);
					} else {
						sliderStock.get(_g).setValue(_amount);
					}
				}
			}
		};
		
		sellWindow = new Window("MyWindow", _skin);
		sellWindow.debug();
		
		Label titleLabelA = new Label("GOODS", _skin);
		Label titleLabelB = new Label("PRICE PER\nUNIT", _skin);
		titleLabelB.setAlignment(Align.center);
		Label titleLabelC = new Label("PROFIT PER\nUNIT", _skin);
		titleLabelC.setAlignment(Align.center);
		Label titleLabelD = new Label("STOCK", _skin);
		Label titleLabelE = new Label("SELL", _skin);
		
		sellWindow.defaults().pad(10);
		sellWindow.add(titleLabelA);
		sellWindow.add(titleLabelB);
		sellWindow.add(titleLabelC);
		sellWindow.add(titleLabelD).colspan(2);
		sellWindow.add(titleLabelE);
		sellWindow.row();
		for (Goods _g : Goods.values()) {
			sellWindow.add( new Label( _g.toDisplayString(), _skin ) );
			
			Label temp = new Label( "10", _skin );
			labelPrice.put(_g, temp);
			sellWindow.add( temp );
			
			temp = new Label( "100", _skin );
			labelPricePaid.put(_g, temp);
			sellWindow.add( temp ).pad(10);
			
			Slider sliderTemp = new Slider(0, 1, 1, false, _skin ); //set slider
			sliderStock.put(_g, sliderTemp);
			sellWindow.add(sliderTemp);
			
			temp = new Label( "1000", _skin );
			labelStock.put(_g, temp);
			sellWindow.add( temp ).width(50);	
			
			TextButtonGoods buttonTemp = new TextButtonGoods("SELL", _skin, _g);
			buttonTemp.addListener(sellClik);
			buttonSell.put(_g, buttonTemp);
			sellWindow.add( buttonTemp );	
			
			sellWindow.row();
		}
		updateList(true);
	}
}
