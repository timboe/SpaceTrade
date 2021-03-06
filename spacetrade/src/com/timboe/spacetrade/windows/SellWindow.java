package com.timboe.spacetrade.windows;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.PlanetScreen;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.TextButtonGoods;

public class SellWindow {
	private static final EnumMap<Goods, Label> labelName = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelPrice = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Label> labelPricePaid = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, Slider> sliderStock = new EnumMap<Goods, Slider>(Goods.class);
	private static final EnumMap<Goods, Label> labelStock = new EnumMap<Goods, Label>(Goods.class);
	private static final EnumMap<Goods, TextButton> buttonSell = new EnumMap<Goods, TextButton>(Goods.class);
	private static final EnumMap<Goods, Label> labelCargo = new EnumMap<Goods, Label>(Goods.class);
	
	private static Window sellWindow = null;
	private static boolean sellWindowPopulated = false;

	private static ChangeListener sellClik;
	private static Planet curPlanet;
	
	public static Window getWindow() {
		if (sellWindowPopulated == true) return sellWindow;
		sellWindowPopulated = true;
		populateSellWindow();
		return sellWindow;
	}
	
	public static void updateList(boolean _intial) {
		if (sellWindowPopulated == false) getWindow();

		curPlanet = Player.getPlanet();
		final String _titleStr = "Selling on "+curPlanet.getFullName();
		sellWindow.setTitle(_titleStr);
		for (Goods _g : Goods.values()) {
			labelCargo.get(_g).setText( Integer.toString(Player.getStock(_g)) );
			
			if (curPlanet.getSells(_g) == false) {
				labelName.get(_g).setText(_g.toDisplayString());
				labelPrice.get(_g).setText( "---" );
				//sliderStock.get(_g).setRange(0, 1);
				//sliderStock.get(_g).setTouchable(Touchable.disabled);
				//sliderStock.get(_g).setVisible(false);
				labelPricePaid.get(_g).setText("---");
				labelStock.get(_g).setText( Integer.toString(Player.getStock(_g)) );
				buttonSell.get(_g).setText("DUMP");
				//buttonSell.get(_g).setTouchable(Touchable.disabled);
				//buttonSell.get(_g).setVisible(false);
			} else {
				buttonSell.get(_g).setText("SELL");
			}

			
			if (Player.getStock(_g) == 0) {
				sliderStock.get(_g).setTouchable(Touchable.disabled);
			} else {
				sliderStock.get(_g).setTouchable(Touchable.enabled);
			}
	
			
			labelName.get(_g).setText(_g.toDisplayString() + "[" + curPlanet.getStock(_g)  + "]");
			
			int cost = curPlanet.getPriceSell(_g);
			
			if (_intial == true) {
				int toSell = Player.getStock(_g);
				if (toSell == 0) {
					sliderStock.get(_g).setRange(0, 1);
					sliderStock.get(_g).setTouchable(Touchable.disabled);
				} else {
					sliderStock.get(_g).setRange(0, toSell);
					sliderStock.get(_g).setValue(toSell);
					sliderStock.get(_g).setTouchable(Touchable.enabled);
				}
			}
			
			int chosen = (int) sliderStock.get(_g).getValue();
			labelStock.get(_g).setText( Integer.toString(chosen) );
			
			if (curPlanet.getSells(_g) == false) continue;
			
			int avPaid = Player.getAvPaidPrice(_g);
			if (avPaid == 0) {
				labelPricePaid.get(_g).setColor(1f, 1f, 1f, 1f);
				labelPricePaid.get(_g).setText("$0");
			} else {
				if (avPaid < cost) {
					labelPricePaid.get(_g).setColor(0f, 1f, 0f, 1f);
				} else {
					labelPricePaid.get(_g).setColor(1f, 0f, 0f, 1f);
				}
				labelPricePaid.get(_g).setText("$" + Integer.toString((cost - avPaid)* chosen) );
			}
			
			labelPrice.get(_g).setText( "$" + Integer.toString(cost) );
			
		}

	}
	
	private static void removeStock(Goods _g, int _amount) {
		Player.removeStock(_g, _amount);
		curPlanet.modStock(_g, _amount);
		final int _remainingStock = Player.getStock(_g);
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
	
	private static void populateSellWindow() {
		
		Skin _skin = Textures.getSkin();
		
		sellClik = new  ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("SellButton","Interact:"+event.toString()+" "+((TextButtonGoods)actor).getGoods());
				final Goods _g = ((TextButtonGoods)actor).getGoods();
				final int _amount = (int) sliderStock.get(_g).getValue();
				
				if (Player.getPlanet().getSells(_g) == false) {
					//DUMP!
					new Dialog("Cargo Dumo", Textures.getSkin(), "dialog") {
						protected void result (Object object) {
							if ((Boolean)object == true) {
								removeStock(_g, _amount);
							}
						}
					}.text("\nAre you sure you wish to dump "+_amount+" "+_g.toDisplayString()+"?\n ")
						.button("   YES   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
						.button("   NO   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
						.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
						.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
				} else {
					final int _profit = curPlanet.getPriceSell(_g) * _amount;
					Player.modCredz(_profit); //Receive money
					removeStock(_g, _amount);
				}
			}
		};
		
		sellWindow = new Window("MyWindow", _skin);
		sellWindow.setMovable(false);
		sellWindow.debug();
		
		Label titleLabelA = new Label("GOODS", _skin);
		Label titleLabelB = new Label("PRICE PER\nUNIT", _skin);
		titleLabelB.setAlignment(Align.center);
		Label titleLabelC = new Label("PROFIT", _skin);
		titleLabelC.setAlignment(Align.center);
		Label titleLabelD = new Label("AMOUNT", _skin);
		Label titleLabelE = new Label("CARGO", _skin);
		
		sellWindow.defaults().pad(5);
		sellWindow.add(titleLabelA).colspan(2);
		sellWindow.add(titleLabelB);
		sellWindow.add(titleLabelC);
		sellWindow.add(titleLabelD).colspan(3);
		sellWindow.add(titleLabelE);
		sellWindow.row();
		for (final Goods _g : Goods.values()) {
			ImageButton tempIButton = new ImageButton(_skin.get("info", ImageButtonStyle.class));
			tempIButton.addCaptureListener( new  ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					Gdx.app.log("InfoButton","Interact:"+event.toString());
					Help.help(_g);
				}
			});
			sellWindow.add( tempIButton );
			
			Label temp = new Label( _g.toDisplayString(), _skin );
			sellWindow.add(temp);
			labelName.put(_g, temp);
			
			temp = new Label( "10", _skin.get("background", LabelStyle.class) );
			labelPrice.put(_g, temp);
			temp.setAlignment(Align.center);
			sellWindow.add( temp ).width(100);
			
			temp = new Label( "100", _skin.get("background", LabelStyle.class) );
			labelPricePaid.put(_g, temp);
			temp.setAlignment(Align.center);
			sellWindow.add( temp ).width(100);
			
			Slider sliderTemp = new Slider(0, 1, 1, false, _skin ); //set slider
			sliderStock.put(_g, sliderTemp);
			sellWindow.add(sliderTemp).height(50);
			
			temp = new Label( "1000", _skin );
			labelStock.put(_g, temp);
			sellWindow.add( temp ).width(35);	
			
			TextButtonGoods buttonTemp = new TextButtonGoods("SELL", _skin.get("large", TextButtonStyle.class), _g);
			buttonTemp.addListener(sellClik);
			buttonSell.put(_g, buttonTemp);
			sellWindow.add( buttonTemp ).height(50).width(120);
			
			temp = new Label( "1", _skin.get("background", LabelStyle.class) );
			labelCargo.put(_g, temp);
			temp.setAlignment(Align.center);
			sellWindow.add( temp ).width(35);	
			
			sellWindow.row();
		}
		updateList(true);
	}
}
