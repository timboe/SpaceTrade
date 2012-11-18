package com.timboe.spacetrade.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Textures {
	public static final Textures singelton = new Textures();

	public static final Textures getTextures() {
		return singelton;
	}
	
	private final Texture starTexture;
	private final Skin mainSkin;
	
	private Textures() {
		starTexture = new Texture(Gdx.files.internal("data/star.png"));
		mainSkin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));

	}
	
	public Texture getStar() {
		return starTexture;
	}
	
	public Skin getSkin() {
		return mainSkin;
	}
	
	
	
	
	
	
}
