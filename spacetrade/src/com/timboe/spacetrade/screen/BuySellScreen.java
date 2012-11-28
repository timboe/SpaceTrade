package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Meshes;
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
		SellWindow.updateList(false);
		BuyWindow.updateList(false);

		spriteBatch.setProjectionMatrix(transform_BG);
		spriteBatch.begin();
		spriteBatch.setColor( Color.WHITE );
		spriteBatch.draw(Textures.getStarscape( Player.getPlanetID() ),0,0);
		spriteBatch.setColor( PlanetFX.getLandColor( Player.getPlanetID() ) ); //TODO put proper colour back in here
		if (Player.getPlanet().getSize() == WorldSize.Small) { //image is 705x800
			spriteBatch.draw(Textures.getPlanetBlur(),550,80,580,635);
		} else if (Player.getPlanet().getSize() == WorldSize.Medium) {
			spriteBatch.draw(Textures.getPlanetBlur(),490,0);
		} else if (Player.getPlanet().getSize() == WorldSize.Large) {
			spriteBatch.draw(Textures.getPlanetBlur(),455,-20,765,840);
		}
		spriteBatch.setColor( Color.WHITE );
		spriteBatch.draw(Textures.getBlackSquare(),-500,-500); //BUG need to draw something to reset colour
		spriteBatch.end();
	}
	
	public void renderFX(float delta) {
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		transform_FX.rotate(0, 1, 0, delta);
		shader.begin();
	    Vector3 lightPos = new Vector3(0,0,0.005f);
	    lightPos.x = Gdx.input.getX();
	    lightPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
	    shader.setUniformf("light", lightPos);
	    shader.setUniformMatrix("u_projTrans", transform_FX);
	    PlanetFX.getNormals(Player.getPlanetID()).bind(1);
	    PlanetFX.getTexture(Player.getPlanetID()).bind(0);
	    Meshes.getPlanet(Player.getPlanet().getSize()).render(shader, GL20.GL_TRIANGLES);
	    shader.end();
	    Gdx.gl20.glDisable(GL20.GL_CULL_FACE);	
	}

	@Override 
	public void show() {
		leftTable.clear();
		leftTable.padLeft(50);
		leftTable.align(Align.left);
		if (doBuy == false) {
			leftTable.add(SellWindow.getWindow()).colspan(2);
			SellWindow.updateList(true);

		} else {
			leftTable.add(BuyWindow.getWindow()).colspan(2);
			BuyWindow.updateList(true);
		}
		leftTable.row().padBottom(10);
		leftTable.add(doSellButton).left();
		leftTable.add(doBuyButton).right();
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
