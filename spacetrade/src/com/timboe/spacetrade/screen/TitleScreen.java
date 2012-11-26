package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Meshes;
import com.timboe.spacetrade.render.PlanetFX;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.render.Textures;

public class TitleScreen extends SpaceTradeRender {
	
	private Texture texture;
	//	List<Texture> modelTextures;
//	ShaderProgram lightTexShader;
	ShaderProgram texShader;
	private float rotation = 0;

	public TitleScreen() {
		
		TextButton newGame = new TextButton("New Game", Textures.getSkin());
		newGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				RightBar.setTouchable(Touchable.enabled);
				//new!
				Starmap.populate();
				Player.getPlayer().refresh();
				Player.setCredz(1000);
				ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().theStarmap );
				Gdx.app.log("NewGame", "Player Credz:"+Player.getCredz());
			}
		});
		
		TextButton resumeGame = new TextButton("Resume Game", Textures.getSkin());
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

		texture = new Texture(Gdx.files.internal("data/sphereMesh.jpg"), Format.RGB565, true);
		texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		
		texShader = new ShaderProgram(Gdx.files.internal("data/tex-vs.glsl"), Gdx.files.internal("data/tex-fs.glsl"));

		init();
	}
	
	@Override
	protected void renderBackground(float delta) {
		spriteBatch.setProjectionMatrix(transform_BG);
		spriteBatch.begin();
		spriteBatch.draw(Textures.getStarscape( 0 ),0,0);
		spriteBatch.end();
	}
	
	@Override
	protected void renderFX(float delta) {
		rotation += delta;
		
		Matrix4 transform_FG = screenCam.combined.cpy();
		transform_FG.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_FG.translate(200, 0, 0);
		transform_FG.rotate(1, 0, 0, -10);
		transform_FG.rotate(0, 1, 0, rotation*10f);
		
//		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
//		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		texture.bind();
		texShader.begin();
		texShader.setUniformi("u_diffuse", 0);
		texShader.setUniformMatrix("u_projView", transform_FG);
		Meshes.getPlanet(WorldSize.Medium).render(texShader, GL20.GL_TRIANGLES);
		texShader.end();
//		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
//		Gdx.gl.glDisable(GL20.GL_TEXTURE_2D);
	}

}
