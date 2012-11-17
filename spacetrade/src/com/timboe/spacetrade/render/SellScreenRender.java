package com.timboe.spacetrade.render;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.world.TextButtonGoods;

public class SellScreenRender extends Render {
	
	private final EnumMap<Goods, Label> labelPrice = new EnumMap<Goods, Label>(Goods.class);
	private final EnumMap<Goods, Label> labelPricePaid = new EnumMap<Goods, Label>(Goods.class);
	private final EnumMap<Goods, Slider> sliderStock = new EnumMap<Goods, Slider>(Goods.class);
	private final EnumMap<Goods, Label> labelStock = new EnumMap<Goods, Label>(Goods.class);
	private final EnumMap<Goods, TextButton> buttonSell = new EnumMap<Goods, TextButton>(Goods.class);

	
	ChangeListener sellClik;
	
	private Planet currentPlanet;
	
	private void updateList(boolean _intial) {
		for (Goods _g : Goods.values()) {
			if (currentPlanet.getSells(_g) == false) {
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
				buttonSell.get(_g).setTouchable(Touchable.enabled);
				sliderStock.get(_g).setTouchable(Touchable.enabled);
				buttonSell.get(_g).setVisible(true);
				sliderStock.get(_g).setVisible(true);
			}
			
			int cost = currentPlanet.getPrice(_g);
			labelPrice.get(_g).setText( Integer.toString(cost) );
			
			if (_intial == true) {
				int toSell = Player.getPlayer().getStock(_g);
				sliderStock.get(_g).setRange(0, toSell);
				sliderStock.get(_g).setValue(toSell);
			}
			
			int avPaid = Player.getPlayer().getAvPaidPrice(_g);
			if (avPaid < cost) {
				labelPricePaid.get(_g).setColor(0f, 1f, 0f, 1f);
			} else {
				labelPricePaid.get(_g).setColor(1f, 0f, 0f, 1f);
			}
			labelPricePaid.get(_g).setText(Integer.toString(cost - avPaid));

			int chosen = (int) sliderStock.get(_g).getValue();
			labelStock.get(_g).setText( Integer.toString(chosen) );
		}

	}
	
	public SellScreenRender() {
		
		sellClik = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("SellButton","Interact:"+event.toString()+" "+((TextButtonGoods)actor).getGoods());
				final Goods _g = ((TextButtonGoods)actor).getGoods();
				final int _amount = (int) sliderStock.get(_g).getValue();
				final int _profit = currentPlanet.getPrice(_g) * _amount;
				Player.getPlayer().modCredz(_profit);
				Player.getPlayer().removeStock(_g, _amount);
				currentPlanet.modStock(_g, _amount);
				updateList(true);
			}
		};
		
		Label titleLabelA = new Label("GOODS", skin);
		Label titleLabelB = new Label("PRICE PER\nUNIT", skin);
		titleLabelB.setAlignment(Align.center);
		Label titleLabelC = new Label("PROFIT PER\nUNIT", skin);
		titleLabelC.setAlignment(Align.center);
		Label titleLabelD = new Label("STOCK", skin);
		Label titleLabelE = new Label("SELL", skin);

		currentPlanet = Starmap.getStarmap().getPlanets().get(0);
		
		leftTable.defaults().pad(10);
		leftTable.add(titleLabelA);
		leftTable.add(titleLabelB);
		leftTable.add(titleLabelC);
		leftTable.add(titleLabelD).colspan(2);
		leftTable.add(titleLabelE);
		leftTable.row();
		for (Goods _g : Goods.values()) {
			leftTable.add( new Label( _g.toString(), skin ) );
			
			Label temp = new Label( "10", skin );
			labelPrice.put(_g, temp);
			leftTable.add( temp );
			
			temp = new Label( "100", skin );
			labelPricePaid.put(_g, temp);
			leftTable.add( temp ).pad(10);
			
			Slider sliderTemp = new Slider(0, 1, 1, false, skin); //set slider
			sliderStock.put(_g, sliderTemp);
			leftTable.add(sliderTemp);
			
			temp = new Label( "1000", skin );
			labelStock.put(_g, temp);
			leftTable.add( temp ).width(50);	
			
			TextButtonGoods buttonTemp = new TextButtonGoods("SELL", skin, _g);
			buttonTemp.addListener(sellClik);
			buttonSell.put(_g, buttonTemp);
			leftTable.add( buttonTemp );	
			
			leftTable.row();
		}
		updateList(true);
		
//		leftTable.row();
//		leftTable.add(nameText).width(100);
//		leftTable.row();
//		leftTable.add(addressLabel);
//		leftTable.add(addressText).width(100);
		
		
//		final TextButton flickButton = new TextButton("Flick Scroll", skin.get("toggle", TextButtonStyle.class));
//		flickButton.setChecked(true);
//		flickButton.addListener(new ChangeListener() {
//			public void changed (ChangeEvent event, Actor actor) {
//				scroll.setFlickScroll(flickButton.isChecked());
//			}
//		});
//
//		final TextButton fadeButton = new TextButton("Fade Scrollbars", skin.get("toggle", TextButtonStyle.class));
//		fadeButton.setChecked(true);
//		fadeButton.addListener(new ChangeListener() {
//			public void changed (ChangeEvent event, Actor actor) {
//				scroll.setFadeScrollBars(fadeButton.isChecked());
//			}
//		});
//
//		final TextButton smoothButton = new TextButton("Smooth Scrolling", skin.get("toggle", TextButtonStyle.class));
//		smoothButton.setChecked(true);
//		smoothButton.addListener(new ChangeListener() {
//			public void changed (ChangeEvent event, Actor actor) {
//				scroll.setSmoothScrolling(smoothButton.isChecked());
//			}
//		});
//
//		final TextButton onTopButton = new TextButton("Scrollbars On Top", skin.get("toggle", TextButtonStyle.class));
//		onTopButton.addListener(new ChangeListener() {
//			public void changed (ChangeEvent event, Actor actor) {
//				scroll.setScrollbarsOnTop(onTopButton.isChecked());
//			}
//		});
//		
//
//		table.add(slider);
//		table.row();
//		table.add(scroll).expand().fill().colspan(4);
//		table.row().space(10).padBottom(10);
//		table.add(flickButton).right().expandX();
//		table.add(onTopButton);
//		table.add(smoothButton);
//		table.add(fadeButton).left().expandX();
//		
//		TextureRegion upRegion = skin.getRegion("default-slider-knob");
//		TextureRegion downRegion = skin.getRegion("default-slider-knob");
//		BitmapFont buttonFont = skin.getFont("default-font");
		

		
	
//		Table table2 = new Table();
//		stage.addActor(table2);
//		table2.setFillParent(true);
//		table2.bottom();
//
//		TextButton button2 = new TextButton("Button 2", skin);
//		button2.addListener(new ChangeListener() {
//			public void changed (ChangeEvent event, Actor actor) {
//				System.out.println("2!");
//			}
//		});
//		button2.addListener(new InputListener() {
//			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//				System.out.println("touchDown 2");
//				return false;
//			}
//		});
//		table2.add(button2);
		
		init();
	}
	
	@Override
	public void init() {
		updateList(true);
		super.init();
	}
	
	@Override
	public void render() {
		updateList(false);
		super.render();
	}
	
	
}
