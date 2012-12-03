package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class Textures {

	private static TextureRegion starTexture = null;
	private static TextureRegion blackSquare = null;
	private static TextureRegion shipTexture = null;
	private static Texture galaxyTexture = null;
	private static Skin mainSkin;
	private static final Texture planetBlur = new Texture(Gdx.files.internal("data/planetBlur.png"));
	private static final Array<Texture> starscapes = new Array<Texture>(5);
	private static final Texture moonTex =  new Texture(Gdx.files.internal("data/moonTex.jpg"));
	private static final Texture moonNorm =  new Texture(Gdx.files.internal("data/moonNormal.jpg"));
	private static final Texture pink = new Texture(Gdx.files.internal("data/pink.png"));
	
	public static Texture getPink() {
		return pink;
	}
	
	public static TextureRegion getStar() {
		if (starTexture == null) {
			starTexture = getSkin().getRegion("star");
		}
		return starTexture;
	}
	
	public static Skin getSkin() {
		if (mainSkin == null) {
			mainSkin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));
			mainSkin.getFont("default-font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			mainSkin.getFont("default-outline-font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			mainSkin.getFont("large-font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		return mainSkin;
	}
	
	public static BitmapFont getFont() {
		return mainSkin.getFont("default-font");
	}
	
	public static BitmapFont getLargeFont() {
		return mainSkin.getFont("large-font");
	}
	
	public static BitmapFont getOutlineFont() {
		return mainSkin.getFont("default-outline-font");
	}

	public static TextureRegion getShip() {
		if (shipTexture == null){
			shipTexture = getSkin().getRegion("ship");
		}
		return shipTexture;
	}
	
	public static Texture getGalaxyTexture() {
		if (galaxyTexture == null) {
			galaxyTexture = new Texture(Gdx.files.internal("data/nebula_1.jpg"));
			galaxyTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		return galaxyTexture;
	}
	
	public static Texture getStarscape(int planetId) {
		if (starscapes.size == 0) {
			starscapes.add(new Texture(Gdx.files.internal("data/starField0.png")));
			starscapes.add(new Texture(Gdx.files.internal("data/starField1.png")));
			starscapes.add(new Texture(Gdx.files.internal("data/starField2.png")));
			starscapes.add(new Texture(Gdx.files.internal("data/starField3.png")));
			starscapes.add(new Texture(Gdx.files.internal("data/starField4.png")));
			for (Texture t : starscapes) {
				t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			}
		}
		return starscapes.get( planetId % starscapes.size );
	}
	
	public static void dispose() {
		if (mainSkin != null) mainSkin.dispose();
		if (galaxyTexture != null) galaxyTexture.dispose();
		planetBlur.dispose();
		for (Texture t : starscapes) {
			t.dispose();
		}
		moonTex.dispose();
		moonNorm.dispose();
	}

	public static TextureRegion getBlackSquare() {
		if (blackSquare == null) {
			blackSquare = getSkin().getRegion("black-box");
		}
		return blackSquare;
	}
	
	public static Texture getPlanetBlur() {
		return planetBlur;
	}
	
	public static Texture getMoonTexture() {
		return moonTex;
	}
	
	public static Texture getMoonNorm() {
		return moonNorm;
	}
	
	
}
