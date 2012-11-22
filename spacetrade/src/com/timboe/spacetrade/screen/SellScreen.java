package com.timboe.spacetrade.screen;

import com.timboe.spacetrade.render.SellWindow;
import com.timboe.spacetrade.render.SpaceTradeRender;

public class SellScreen extends SpaceTradeRender {
	
	public SellScreen() {
		init();
	}

	@Override
	public void render(float delta) {
		SellWindow.updateList(false);
		super.render(delta);
	}
	
	@Override 
	public void show() {
		SellWindow.addToTable(leftTable);
		SellWindow.updateList(true);
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
