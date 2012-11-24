package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Textures {

	private static final Texture starTexture = new Texture(Gdx.files.internal("data/star.png"));
	private static final Texture shipTexture = new Texture(Gdx.files.internal("data/ship.png"));
	private static final Texture galaxyTexture = new Texture(Gdx.files.internal("data/galaxy.jpg"));
	private static final Texture nebula1Texture =  new Texture(Gdx.files.internal("data/nebula_1.jpg"));
	private static final Skin mainSkin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));;

	
	public static Texture getStar() {
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
	
	public static void dispose() {
		starTexture.dispose();
		shipTexture.dispose();
		mainSkin.dispose();
		galaxyTexture.dispose();
	}

	public static Texture getNebulaTexture(int _i) {
		return nebula1Texture;
	}
	
	
	
	
}
