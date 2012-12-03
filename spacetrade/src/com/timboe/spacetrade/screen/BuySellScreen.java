package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.PlanetFX;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.windows.BuyWindow;
import com.timboe.spacetrade.windows.SellWindow;

public class BuySellScreen extends SpaceTradeRender {
	
    private boolean doBuy = false;
    private TextButton doBuyButton;
    private TextButton doSellButton;
	
	public BuySellScreen() {
		
		doBuyButton = new TextButton("BUY GOODS", Textures.getSkin().get("large-toggle", TextButtonStyle.class));
		doSellButton = new TextButton("SELL GOODS", Textures.getSkin().get("large-toggle", TextButtonStyle.class));

		doSellButton.setChecked(true);
		ButtonGroup group = new ButtonGroup(doBuyButton, doSellButton);
		group.setMaxCheckCount(1);
		
		doBuyButton.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("doBuyButton","Interact:"+event.toString());
				doBuy = true;
				show();
			}
		});
		
		doSellButton.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("doBuyButton","Interact:"+event.toString());
				doBuy = false;
				show();
			}
		});
		
		init();
	}

	
	@Override
	protected void renderBackground(float delta) {
		renderPlanetBackdrop();
	}
	
	@Override
	protected void renderFX(float delta) {
		renderPlanet(delta, 
				PlanetFX.getTexture(Player.getPlanetID()),
				PlanetFX.getNormals(Player.getPlanetID()),
				Player.getPlanet().getSize());
	}

	@Override 
	public void show() {
		leftTable.clear();
		Table topButtons = new Table(Textures.getSkin());
		topButtons.defaults().pad(10);
		topButtons.debug();
		topButtons.add(doSellButton);
		topButtons.add(doBuyButton);
		leftTable.align(Align.top);
		leftTable.add(topButtons).width( leftTable.getWidth() );
		leftTable.row();
		
		if (doBuy == false) {
			SellWindow.updateList(true);
			Window bottomContainer = new Window("", Textures.getSkin().get("transparent", WindowStyle.class));
			bottomContainer.debug();
			bottomContainer.defaults().pad(10);
			bottomContainer.add(SellWindow.getWindow()).width(980).height(650);
			leftTable.add(bottomContainer);
		} else {
			BuyWindow.updateList(true);
			Window bottomContainer = new Window("", Textures.getSkin().get("transparent", WindowStyle.class));
			bottomContainer.debug();
			bottomContainer.defaults().pad(10);
			bottomContainer.add(BuyWindow.getWindow()).width(980).height(650);
			leftTable.add(bottomContainer);
		}
			
//		leftTable.clear();
//		leftTable.align(Align.left);
//		leftTable.padLeft(30);
//		leftTable.defaults().pad(10);
//		leftTable.add(doSellButton).left();
//		leftTable.add(doBuyButton).right();
//		leftTable.row();
//		if (doBuy == false) {
//			SellWindow.updateList(true);
//			leftTable.add(SellWindow.getWindow()).colspan(2);
//		} else {
//			BuyWindow.updateList(true);
//			leftTable.add(BuyWindow.getWindow()).colspan(2);
//		}
//		leftTable.row().pad(0);
//		leftTable.add().width(750);
		super.show();
	}
}





//leftTable.row();
//leftTable.add(nameText).width(100);
//leftTable.row();
//leftTable.add(addressLabel);
//leftTable.add(addressText).width(100);


//final TextButton flickButton = new TextButton("Flick Scroll", skin.get("toggle", TextButtonStyle.class));
//flickButton.setChecked(true);
//flickButton.addListener(new ChangeListener() {
//	public void changed (ChangeEvent event, Actor actor) {
//		scroll.setFlickScroll(flickButton.isChecked());
//	}
//});
//
//final TextButton fadeButton = new TextButton("Fade Scrollbars", skin.get("toggle", TextButtonStyle.class));
//fadeButton.setChecked(true);
//fadeButton.addListener(new ChangeListener() {
//	public void changed (ChangeEvent event, Actor actor) {
//		scroll.setFadeScrollBars(fadeButton.isChecked());
//	}
//});
//
//final TextButton smoothButton = new TextButton("Smooth Scrolling", skin.get("toggle", TextButtonStyle.class));
//smoothButton.setChecked(true);
//smoothButton.addListener(new ChangeListener() {
//	public void changed (ChangeEvent event, Actor actor) {
//		scroll.setSmoothScrolling(smoothButton.isChecked());
//	}
//});
//
//final TextButton onTopButton = new TextButton("Scrollbars On Top", skin.get("toggle", TextButtonStyle.class));
//onTopButton.addListener(new ChangeListener() {
//	public void changed (ChangeEvent event, Actor actor) {
//		scroll.setScrollbarsOnTop(onTopButton.isChecked());
//	}
//});
//
//
//table.add(slider);
//table.row();
//table.add(scroll).expand().fill().colspan(4);
//table.row().space(10).padBottom(10);
//table.add(flickButton).right().expandX();
//table.add(onTopButton);
//table.add(smoothButton);
//table.add(fadeButton).left().expandX();
//
//TextureRegion upRegion = skin.getRegion("default-slider-knob");
//TextureRegion downRegion = skin.getRegion("default-slider-knob");
//BitmapFont buttonFont = skin.getFont("default-font");




//Table table2 = new Table();
//stage.addActor(table2);
//table2.setFillParent(true);
//table2.bottom();
//
//TextButton button2 = new TextButton("Button 2", skin);
//button2.addListener(new ChangeListener() {
//	public void changed (ChangeEvent event, Actor actor) {
//		System.out.println("2!");
//	}
//});
//button2.addListener(new InputListener() {
//	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//		System.out.println("touchDown 2");
//		return false;
//	}
//});
//table2.add(button2);
