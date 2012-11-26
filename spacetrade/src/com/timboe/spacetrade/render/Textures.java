package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class Textures {

	private static Texture starTexture = null;
	private static final Texture shipTexture = new Texture(Gdx.files.internal("data/ship.png"));
	private static final Texture galaxyTexture = new Texture(Gdx.files.internal("data/nebula_1.jpg"));
	private static final Skin mainSkin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));
	private static final Texture bsTexture = new Texture(Gdx.files.internal("data/blackSquare.jpg"));
	private static final Texture planetBlur = new Texture(Gdx.files.internal("data/planetBlur.png"));
	private static final Array<Texture> starscapes = new Array<Texture>(5);

	
	public static Texture getStar() {
		if (starTexture == null) {
			starTexture = new Texture(Gdx.files.internal("data/star.png"));
			starTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		return starTexture;
	}
	
	public static Skin getSkin() {
		return mainSkin;
	}
	
	public static BitmapFont getFont() {
		return mainSkin.getFont("default-font");
	}

	public static Texture getShip() {
		return shipTexture;
	}
	
	public static Texture getGalaxyTexture() {
		return galaxyTexture;
	}
	
	public static Texture getStarscape(int planetId) {
		if (starscapes.size == 0) {
			starscapes.add(new Texture(Gdx.files.internal("data/starField0.png")));
			starscapes.add(new Texture(Gdx.files.internal("data/starField1.png")));
			starscapes.add(new Texture(Gdx.files.internal("data/starField2.png")));
			starscapes.add(new Texture(Gdx.files.internal("data/starField3.png")));
			starscapes.add(new Texture(Gdx.files.internal("data/starField4.png")));
		}
		return starscapes.get( planetId % starscapes.size );
	}
	
	public static void dispose() {
		starTexture.dispose();
		shipTexture.dispose();
		mainSkin.dispose();
		galaxyTexture.dispose();
		bsTexture.dispose();
		planetBlur.dispose();
		for (Texture t : starscapes) {
			t.dispose();
		}
	}

	public static Texture getBlackSquare() {
		return bsTexture;
	}
	
	public static Texture getPlanetBlur() {
		return planetBlur;
	}
	
	
	
}
