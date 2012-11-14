package com.timboe.spacetrade.world;

import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;
import com.timboe.spacetrade.settings.Utility;

public class Starmap {
	
	private Utility util = Utility.getUtility();

	private final int starBuffer = 20;
	private final int nStars = 256;
	public final HashSet<Vector2> theStars = new HashSet<Vector2>(); 
	
	
	public Starmap() {
		populate();
	}
	
	private void populate() {
		while (theStars.size() < nStars) {
			int _x = util.getRandI( (int) (Utility.CAMERA_WIDTH  - (2 * starBuffer)) ) + starBuffer;
			int _y = util.getRandI( (int) (Utility.CAMERA_HEIGHT - (2 * starBuffer)) ) + starBuffer;
			//Check we're not too close
			boolean tooClose = false;
			for (Vector2 s : theStars) {
				if (s.dst(_x, _y) < starBuffer) tooClose = true;
			}
			if (tooClose == false) {
				theStars.add( new Vector2(_x, _y) );
			}
		}
	}
	
}
