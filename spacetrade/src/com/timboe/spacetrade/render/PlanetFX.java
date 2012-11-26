package com.timboe.spacetrade.render;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.utility.SimplexNoise;
import com.timboe.spacetrade.utility.Utility;
import com.timboe.spacetrade.world.Starmap;

public class PlanetFX {

	static final Array<Texture> planetTexture = new Array<Texture>(Starmap.getNPlanets());
	static final Array<Texture> planetNormals = new Array<Texture>(Starmap.getNPlanets());
	static final Array<Color> planetLandColour = new Array<Color>(Starmap.getNPlanets());
	static final Random worldRand = new Random(0);
	
	static float persistence = 0f;
	static float frequency = 0f;
	static float amplitude  = 0.9f;
	static int octaves = 0;
	static int randomseed = 0;
	static float maxU = 512f;
	static float maxV = 256f;
	
	public static void preFetch(int _planetId) {
		if (planetTexture.size == 0) {
			init();
		}
		if (planetTexture.get(_planetId) == null) {
			generatePlanet(_planetId);
		}
	}
	
	public static Texture getTexture(int _planetId) {
		preFetch(_planetId);
		return planetTexture.get(_planetId);
	}
		
	public static Texture getNormals(int _planetId) {
		preFetch(_planetId);
		return planetNormals.get(_planetId);
	}
	
	public static Color getLandColor(int _planetId) {
		preFetch(_planetId);
		return planetLandColour.get(_planetId);
	}

	private static void init() {
		for (int i = 0; i < Starmap.getNPlanets(); ++i) {
			planetTexture.add(null);
			planetNormals.add(null);
			planetLandColour.add(null);
			//generatePlanet(i); //Only to test memory usage
		}
	}

	private static void generatePlanet(int _planetId) {
		
		//NOISE VARS
    	randomseed = 2 + _planetId*_planetId;
    	worldRand.setSeed(randomseed);
		
		octaves = Utility.getRandI(3) + 4;//4-6
		frequency = (Utility.getRandI(3) + 2) * (1f/512f); //2-4 *1/maxU
		persistence = (Utility.getRandI(3) + 4) * 0.1f; //0.4 - 0.6
		
        Pixmap worldPixOcean = new Pixmap((int) maxU, (int) maxV, Format.RGBA8888);
        Pixmap worldPixLand = new Pixmap((int) maxU, (int) maxV, Format.RGBA8888);
        Pixmap worldPixNormal = new Pixmap((int) maxU, (int) maxV, Format.RGB888);
        
        float min=999;
        float max=-999;
        float[][] data = new float[(int) maxU][(int) maxV];
        float theta = 0 ;
        float r = maxV;
        float phi = 0;
        for (int _y = 0; _y < maxV; ++_y) {
        	for (int _x = 0; _x < maxU; ++_x) {
        		phi = (float) (((float)_x / maxU) * 2 * Math.PI);
        		//theta = (float) (2 * Math.atan( Math.exp((float)(_y) / (r/Math.PI)) ) - (Math.PI/2f));
        		theta = 2f*(float) (2 * Math.atan( Math.exp((float)(_y) / (r/Math.PI)) ) - (Math.PI/2f));

        		float X = (float) (r * Math.sin(theta) * Math.cos(phi));
        		float Y = (float) (r * Math.cos(theta));
        		float Z = (float) (r * Math.sin(theta) * Math.sin(phi));
          	
        		float h = getHeight(X,Y,Z);
              	min = Math.min(min, h);
              	max = Math.max(max,h);
              	if (h > 1) h = 1;
              	if (h < -1) h = -1;
              	
              	data[_x][_y] = h;
        	}
        	//Gdx.app.log("toSphere", "V:"+_y+" -> theta:"+Math.toDegrees(theta)+" phi:"+Math.toDegrees(phi));
        }
	
    	Color world = getWorldColor();
    	Color ocean = getWorldColor();
    	planetLandColour.set(_planetId, world);
        for (int _y = 0; _y < maxV; ++_y) {
        	for (int _x = 0; _x < maxU; ++_x) {
	        	float height = toHeight(data[_x][_y]); //map 0 to 1
	        	worldPixLand.setColor(world.r * height, world.g * height, world.b * height, 1f);
	        	worldPixLand.drawPixel(_x, _y);
	
	        	float opaque = 1f;
				if (data[_x][_y] > 0) {
					opaque = 0f;
				} else { //map 
	        		opaque = 1-(data[_x][_y] + 1f) ;
	        	}
	        	worldPixOcean.setColor(ocean.r, ocean.g, ocean.b, opaque);
	        	worldPixOcean.drawPixel(_x, _y);
	        	
	        	//normal
	        	int _xp1 = _x + 1;
	        	if (_xp1 == maxU) _xp1 = 0;
	        	int _xm1 = _x - 1;
	        	if (_xm1 == -1) _xm1 = (int) (maxU - 1);
	        	int _yp1 = _y + 1;
	        	if (_yp1 == maxV) _yp1 = 0;
	        	int _ym1 = _y - 1;
	        	if (_ym1 == -1) _ym1 = (int) (maxV - 1);
            	float hxm = toNormHeight(data[_xm1][_y]);
            	float hxp = toNormHeight(data[_xp1][_y]);
            	float hym = toNormHeight(data[_x][_ym1]);
            	float hyp = toNormHeight(data[_x][_yp1]);
            	Vector3 vx = new Vector3(0, 1, hxm - hxp);
            	Vector3 vy = new Vector3(1, 0, hym - hyp);
            	vx.crs(vy); // vx X vy
            	vx.nor(); //normalise
            	float R= ((vx.x + 1f)/2f);  
            	float G = ((vx.y + 1f)/2f);  
            	float B = 1 - ((vx.z + 1f)/2f);  
            	worldPixNormal.setColor(R, G, B, 1f);
                worldPixNormal.drawPixel(_x, _y);
        	}
        }
        worldPixLand.drawPixmap(worldPixOcean, 0, 0);

        planetTexture.set(_planetId, new Texture(worldPixLand));
        planetNormals.set(_planetId, new Texture(worldPixNormal));
        
        worldPixOcean.dispose();
        worldPixLand.dispose();
        worldPixNormal.dispose(); 
        Gdx.app.log("Noise","Min:"+(min)+" max:"+(max)+" land:"+world+" ocean:"+ocean);
	}

	
    private static Color getWorldColor() {
    	int nC = 0;
    	boolean cR = false, cG = false, cB = false;
    	while (nC == 0 || nC == 3) {
    		nC = 0;
    		cR=false;
    		cG=false;
    		cB=false;
    		if (worldRand.nextFloat() < 0.5f == true) {
    			++nC;
    			cR=true;
    		}
    		if (worldRand.nextFloat() < 0.5f == true) {
    			++nC;
    			cG=true;
    		}
    		if (worldRand.nextFloat() < 0.5f == true) {
    			++nC;
    			cB=true;
    		}
    	}
    	float r = 0f, g = 0f, b=0f;
    	if (cR == true) r = 0.5f + (worldRand.nextFloat()/2f);
    	if (cG == true) g = 0.5f + (worldRand.nextFloat()/2f);
    	if (cB == true) b = 0.5f + (worldRand.nextFloat()/2f);
    	return new Color(r, g, b, 1f);
    }
	
	
	private static float toHeight(float _h) {
		_h += 1;
		_h /= 2f;
    	if (_h < 0f) _h = 0f;
    	if (_h > 1f) _h = 1f;
    	return _h;
	}
	
	private static float toNormHeight(float _h) {
    	if (_h < 0) _h = 0;
    	if (_h > 1f) _h = 1f;
    	return _h;
	}
	
	
	private static float getHeight(float _x, float _y, float _z) {
		return amplitude * total(_x, _y, _z);
	}
	
	private static float total(float _i, float _j, float _k) {
	    //properties of one octave (changing each loop)
		float _t = 0.0f;
		float _amplitude = 1;
		float _freq = frequency;

	    for(int oc = 0; oc < octaves; ++oc) {
	        _t += getValue3D(_k * _freq + randomseed, _j * _freq + randomseed, _i * _freq + randomseed) * _amplitude;
	        _amplitude *= persistence;
	        _freq *= 2;
	    }

	    return _t;
	}

	
	private static float getValue3D(float _x, float _y, float _z) {
		return SimplexNoise.noise(_x,  _y, _z);
	}
	

}
