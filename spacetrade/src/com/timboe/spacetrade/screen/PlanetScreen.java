package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Meshes;
import com.timboe.spacetrade.render.PlanetFX;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;

public class PlanetScreen extends SpaceTradeRender {

	//private float rotation = 0;
    private ShaderProgram shader;

	public PlanetScreen() {
		shader = Meshes.createShader();	
		init();
	}
	
	@Override
	protected void renderBackground(float delta) {
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
	
	@Override
	protected void renderFX(float delta) {
//		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
//		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		transform_FX.rotate(0, 1, 0, delta*10f);
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
	
}
