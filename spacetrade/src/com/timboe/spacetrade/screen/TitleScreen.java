package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Meshes;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.render.Textures;

public class TitleScreen extends SpaceTradeRender {
	
	private ShaderProgram shader;
	private BitmapFont distanceFieldFont;
	//Texture distanceFieldTexture;
	

	
	public TitleScreen() {
		
		//font test
		Texture distanceFieldTexture = new Texture(Gdx.files.internal("data/skin/distancefield32verdana.png"), true);
		distanceFieldTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		distanceFieldFont = new BitmapFont(Gdx.files.internal("data/skin/distancefield32verdana.fnt"), new TextureRegion(distanceFieldTexture), true);
		distanceFieldFont.setColor(Color.WHITE);
		
		RightBar.getRightBarTable().addAction(Actions.fadeOut(0));
		RightBar.getRightBarTable().act(1);
		
		ShaderProgram.pedantic = false; // Useful when debugging this test
		
		shader = Meshes.createShader();	
		TextButton newGame = new TextButton("New Game", Textures.getSkin().get("large",TextButtonStyle.class));
		newGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				RightBar.setTouchable(Touchable.enabled);
				//new!
				Starmap.populate();
				Player.getPlayer().refresh();
				Player.setCredz(250000);
				ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().theStarmap );
				RightBar.getRightBarTable().addAction(Actions.delay(ScreenFade.speed));
				RightBar.getRightBarTable().addAction(Actions.fadeIn(ScreenFade.speed));
				Gdx.app.log("NewGame", "Player Credz:"+Player.getCredz());
			}
		});
		
		TextButton resumeGame = new TextButton("Resume Game", Textures.getSkin().get("large",TextButtonStyle.class));
		resumeGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//RightBar.setTouchable(Touchable.enabled);
				//Serialiser.loadState();
				//SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theShipScreen );
				//Gdx.app.log("LoadGame", "Player Credz:"+Player.getPlayer().getCredz());
			}
		});
		
		leftTable.defaults().pad(20);
		leftTable.add(newGame).width(400).height(50);
		leftTable.row();
		leftTable.add(resumeGame).width(400).height(50);
		
		RightBar.setTouchable(Touchable.disabled);		
		init();
	}
	
	@Override
	protected void renderBackground(float delta) {
		spriteBatch.setProjectionMatrix(transform_BG);
		spriteBatch.begin();
		spriteBatch.draw(Textures.getStarscape( 0 ),0,0);
		spriteBatch.end();
	}
	
	
//	@Override
//	public void render(float delta) {
//		renderClear(delta);
//		
//		BitmapFont font2 = new BitmapFont();
//        
//        spriteBatch.setProjectionMatrix(transform_BG);
//        spriteBatch.begin();
//		spriteBatch.setShader(distanceFieldShader);
//
//		float mod = 1;
//		
//
//		float scale = 0.5f;
//		distanceFieldFont.setScale(scale);
//		distanceFieldShader.setSmoothing((1f/8f) / (scale/mod));
//		distanceFieldFont.draw(spriteBatch, "BLAA :)", 50, 100);
//
//		scale = 1f;
//		distanceFieldFont.setScale(scale);
//		distanceFieldShader.setSmoothing((1f/8f) / (scale/mod));
//		distanceFieldFont.draw(spriteBatch, "BLAA :)", 50, 150);
//
//		scale = 2f;
//		distanceFieldFont.setScale(scale);
//		distanceFieldShader.setSmoothing((1f/8f) / (scale/mod));
//		distanceFieldFont.draw(spriteBatch, "BLAA :)", 50, 200);
//
//		scale = 4f;
//		distanceFieldFont.setScale(scale);
//		distanceFieldShader.setSmoothing((1f/8f) / (scale/mod));
//		distanceFieldFont.draw(spriteBatch, "BLAA :)", 50, 250);
//
//		
//		font2.draw(spriteBatch, "BLOO :(", 500, 200);
//        spriteBatch.end();
//	}
	
	@Override
	protected void renderFX(float delta) {
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		transform_FX.rotate(0, 1, 0, delta);
		shader.begin();
        Vector3 lightPos = new Vector3(0,0,0.005f);
        lightPos.x = Gdx.input.getX();
        lightPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
        shader.setUniformf("light", lightPos);
        shader.setUniformMatrix("u_projTrans", transform_FX);
        Textures.getMoonNorm().bind(1);
        Textures.getMoonTexture().bind(0);
        Meshes.getPlanet(WorldSize.Medium).render(shader, GL20.GL_TRIANGLES);
        shader.end();
        Gdx.gl20.glDisable(GL20.GL_CULL_FACE);        
	}

}
