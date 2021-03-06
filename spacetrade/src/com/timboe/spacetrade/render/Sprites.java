package com.timboe.spacetrade.render;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.world.Starmap;

public class Sprites {
	public static final Sprites singelton = new Sprites();

	public static final Sprites getSprites() {
		return singelton;
	}
	
	private final Sprite shipSprite;
	private final Array<Sprite> planetSprites = new Array<Sprite>();
	
	private Sprites() {
		shipSprite = new Sprite(Textures.getShip());
		//shipSprite.scale(2f);
		shipSprite.setOrigin(Textures.getShip().getRegionWidth()/2, Textures.getShip().getRegionHeight()/2);
		
		for (int _p = 0; _p < Starmap.getNPlanets(); ++_p) {
			Sprite _ps = new Sprite(Textures.getStar());
			_ps.setOrigin(0, 0);
			planetSprites.add( _ps );
		}
	}
	
	public Sprite getPlayerSprite() {
		return shipSprite;
	}
	
	public Sprite getPlanetSprite(int _ID) {
		return planetSprites.get(_ID);
	}
	
}
