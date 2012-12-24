package com.timboe.spacetrade;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Json;
import com.timboe.spacetrade.screen.BuySellScreen;
import com.timboe.spacetrade.screen.GameOverScreeen;
import com.timboe.spacetrade.screen.PlanetScreen;
import com.timboe.spacetrade.screen.ShipScreen;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.screen.TitleScreen;
import com.timboe.spacetrade.render.Meshes;
import com.timboe.spacetrade.render.Textures;

public class SpaceTrade extends Game {
	Random r = new Random();

	public StarmapScreen theStarmap;
	public TitleScreen theTitle;
	public ShipScreen theShipScreen;
	public PlanetScreen thePlanetScreen;
	public BuySellScreen theBuySellScreen;
	public GameOverScreeen theGameOver;
	public NormalMapTest nmt;
	
	public static final float CAMERA_WIDTH = 1280;
	public static final float CAMERA_HEIGHT = 800;
	
	public static final float GUI_WIDTH = 128;
	public static final float GAME_HEIGHT = 800;
	public static final float GAME_WIDTH = CAMERA_WIDTH - GUI_WIDTH;
	
	public static int saveSlot;
	
	public static boolean debug = false;
	public static boolean priceVariation = false;
	
	public static final int masterSeed = 2;
	
	private static SpaceTrade self;
	public static SpaceTrade getSpaceTrade() {
		return self;
	}
	
	@Override
	public void create() {
//		
//		r.setSeed(1);
//		Json json = new Json();
//
//		System.out.println(json.prettyPrint(r));
//		System.out.println(json.prettyPrint(r));
//
//		Gdx.app.log("r", ""+r.nextInt());
//		System.out.println(json.prettyPrint(r));
//		System.out.println(json.prettyPrint(r));
//		
//		Gdx.app.log("r", ""+r.nextInt());
//		System.out.println(json.prettyPrint(r));
//		System.out.println(json.prettyPrint(r));

		self = this;
//		
		theTitle = new TitleScreen();
		theStarmap = new StarmapScreen();
		theShipScreen = new ShipScreen();
		thePlanetScreen = new PlanetScreen();
		theBuySellScreen = new BuySellScreen();
		theGameOver = new GameOverScreeen();
//		
		//Illumination2D i2d = new Illumination2D();
		
		//NormalMapTest nmt = new NormalMapTest();
		
		setScreen(theTitle);
	}
	
	@Override
	public void dispose() {
		if (theTitle != null) theTitle.dispose();
		if (theStarmap != null) theStarmap.dispose();
		if (theShipScreen != null) theShipScreen.dispose();
		if (thePlanetScreen != null) thePlanetScreen.dispose();
		if (theBuySellScreen != null) theBuySellScreen.dispose();
		if (theGameOver != null) theGameOver.dispose();
		Textures.dispose();
		Meshes.dispose();
	}


}
