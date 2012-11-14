package com.timboe.spacetrade.world;

import com.badlogic.gdx.math.Vector2;
import com.timboe.spacetrade.enumerator.Government;
import com.timboe.spacetrade.utility.Utility;

public class Planet {
	
	private Utility util = Utility.getUtility();
	Vector2 position;
	String name;
	Government gov_type;
	
	Planet(int _x, int _y) {
		position = new Vector2(_x, _y);
		name = util.getAdLib().planets.get();
		gov_type = Government.random();
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float dist(Vector2 _comp) {
		return dist(_comp.x, _comp.y);
	}
	
	public float dist(float _x, float _y) {
		return position.dst(_x, _y);
	}

}
