package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Textures {
	public static final Textures singelton = new Textures();

	public static final Textures getTextures() {
		return singelton;
	}
	
	private final Texture starTexture;
	private final Texture shipTexture;
	private final Texture galaxyTexture;

	private final Skin mainSkin;
	
	private Textures() {
		starTexture = new Texture(Gdx.files.internal("data/star.png"));
		mainSkin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));
		shipTexture = new Texture(Gdx.files.internal("data/ship.png"));
		galaxyTexture = new Texture(Gdx.files.internal("data/galaxy.jpg"));

	}
	
	public Texture getStar() {
		return starTexture;
	}
	
	public Skin getSkin() {
		return mainSkin;
	}
	
	public BitmapFont getFont() {
		return mainSkin.getFont("default-font");
	}

	public Texture getShip() {
		return shipTexture;
	}
	
	public Texture getGalaxyTexture() {
		return galaxyTexture;
	}
	
	public void dispose() {
		starTexture.dispose();
		shipTexture.dispose();
		mainSkin.dispose();
		galaxyTexture.dispose();
	}
	
	
	
	
}
