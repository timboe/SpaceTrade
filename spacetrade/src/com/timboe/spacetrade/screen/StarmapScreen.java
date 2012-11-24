package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.BuyWindow;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class StarmapScreen extends SpaceTradeRender {
	
	private ShapeRenderer g2 = new ShapeRenderer();
	private float offset = 0f;
	private ParticleEffectPool effectPool;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	
	static int planetClickedID = -1;
	public static void setPlanetClickedID(int _id) {
		planetClickedID = _id;
		if (_id >= 0) fullRefresh = true;
	}
	static boolean fullRefresh = false;
	
	public StarmapScreen() {
		frontStage = new Stage(); //Uses front stage
		
		ParticleEffect pEffect = new ParticleEffect();
		pEffect.load(Gdx.files.internal("data/galaxyEffect"), Gdx.files.internal("data/"));
		effectPool = new ParticleEffectPool(pEffect, 50, 250);
		
		PooledEffect effect = effectPool.obtain();
		effect.setPosition(0, 0);
		effects.add(effect);		
		
		init();
	}
	

	
//	@Override
//	public void hookStage() {
//		for (Planet _p : Starmap.getStarmap().getPlanets()) {
//			frontStage.addActor(_p);
//		}
//		frontStage.addActor(Player.getPlayer());
//	}
	
	
	@Override
	public void render(float delta) {
		BuyWindow.updateList(fullRefresh, planetClickedID);
		fullRefresh = false;
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//modify camera!
		//getStage().getCamera().translate(SpaceTrade.GAME_WIDTH/2, SpaceTrade.GAME_HEIGHT/2, 0);
		//getStage().getCamera().rotate(delta, 0, 0, 1);
		//getStage().getCamera().translate(-SpaceTrade.GAME_WIDTH/2, -SpaceTrade.GAME_HEIGHT/2, 0);

		
		Texture _gt = Textures.getGalaxyTexture();
		spriteBatch.setProjectionMatrix(getStage().getCamera().combined);
		//_sb.getProjectionMatrix().translate(0, Utility.GAME_HEIGHT, 0);
		//_sb.getProjectionMatrix().rotate(0f, 0f, 1f, offset);
		spriteBatch.begin();
		//_sb.draw(_gt,-1200, -Utility.GAME_HEIGHT - 70);
		spriteBatch.draw(_gt,0,0);

		for (int i = effects.size - 1; i >= 0; i--) {
	        PooledEffect effect = effects.get(i);
	        effect.draw(spriteBatch, delta);
	        if (effect.isComplete()) {
	                effect.free();
	                effects.removeIndex(i);
	        }
		}
		spriteBatch.end();
		
		if (SpaceTrade.debug == true) {
			g2.setProjectionMatrix(getStage().getCamera().combined);
			g2.begin(ShapeType.Circle);
			g2.setColor(0f, 1f, 0f, 0f);
			for (Planet _p : Starmap.getPlanets()) {
				_p.drawBasic(g2);
			}
			g2.end();
		}
		
		//Draw range
		final float _sx = Player.getPlayer().getX();
		final float _sy = Player.getPlayer().getY();
		final float _r = Player.getPlayer().getShip().getRange();
		final int _stepSize = 36; //degree
		final float _miniStep = (float)_stepSize/8f;
		for (int _step = 0; _step <= 360; _step += _stepSize) {
			 offset += delta;
			final float _offset = _step + offset;
			g2.begin(ShapeType.Curve);
			g2.curve(_sx + (float)(_r * Math.cos( Math.toRadians( _offset + (0 * _miniStep) ) )),
					 _sy + (float)(_r * Math.sin( Math.toRadians( _offset + (0 * _miniStep) ) )),
					 _sx + (float)(_r * Math.cos( Math.toRadians( _offset + (1 * _miniStep) ) )),
					 _sy + (float)(_r * Math.sin( Math.toRadians( _offset + (1 * _miniStep) ) )),
					 _sx + (float)(_r * Math.cos( Math.toRadians( _offset + (2 * _miniStep) ) )),
					 _sy + (float)(_r * Math.sin( Math.toRadians( _offset + (2 * _miniStep) ) )),
					 _sx + (float)(_r * Math.cos( Math.toRadians( _offset + (3 * _miniStep) ) )),
					 _sy + (float)(_r * Math.sin( Math.toRadians( _offset + (3 * _miniStep) ) ))  );
			g2.end();
		}
		
		if (planetClickedID >= 0 && Player.getPlayer().getActions().size == 0) {
			leftTable.setVisible(true);
		} else {
			leftTable.setVisible(false);
		}
		
		if (frontStage != null) {
			frontStage.act(delta);
			frontStage.draw();
		}
		stage.act(delta);
		stage.draw();
		Table.drawDebug(stage);

	}	

	@Override 
	public void show() {
		BuyWindow.addToTable(leftTable);
		BuyWindow.updateList(true, planetClickedID);
		if (frontStage.getActors().size == 0) {
			for (Planet _p : Starmap.getPlanets()) {
				frontStage.addActor(_p);
			}
			frontStage.addActor(Player.getPlayer());
		}
		super.show();
	}



	public static int getPlanetClickedID() {
		return planetClickedID;
	}

}
//package com.timboe.spacetrade.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.InputMultiplexer;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL10;
//import com.timboe.spacetrade.render.StarmapRender;
//import com.timboe.spacetrade.world.Planet;
//import com.timboe.spacetrade.world.Starmap;
//
//public class OLDStarmapScreen implements Screen, InputProcessor {
//	StarmapRender theStarmapRenderer;
//	
////	private ShapeRenderer g2 = new ShapeRenderer();
////  private Mesh mesh;
////	private Mesh sphereMesh;
////	private Texture texture;
//
//	
//	public StarmapScreen() {
//		theStarmapRenderer = new StarmapRender();
//		
//		
////      mesh = new Mesh(true, 3, 3, 
////              new VertexAttribute(Usage.Position, 3, "a_position"),
////              new VertexAttribute(Usage.ColorPacked, 4, "a_color"),
////              new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
////
////      mesh.setVertices(new float[] { -0.5f, -0.5f, 0, Color.toFloatBits(255, 0, 0, 255), 0, 1,
////                                     0.5f, -0.5f, 0, Color.toFloatBits(0, 255, 0, 255), 1, 1,
////                                     0, 0.5f, 0, Color.toFloatBits(0, 0, 255, 255), 0.5f, 0 });
////                                     
////      mesh.setIndices(new short[] { 0, 1, 2 });
////      
////		InputStream in = Gdx.files.internal("data/sphere.obj").read();
////		sphereMesh = ObjLoader.loadObj(in);
////		try {
////			in.close();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////		sphereMesh.scale(0.2f, 0.4f, 0.5f);
////		//sphereMesh.
////		System.out.println(sphereMesh.getNumVertices()+" "+ sphereMesh.getNumIndices());
////
////		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
////		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
////		//texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
//
//		Starmap.getStarmap().newYear(10);
//		for (Planet _p : Starmap.getStarmap().getPlanets()) {
//			_p.printStat();
//		}
//
//	}
//	
//	@Override
//	public void render(float delta) {
//		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		theStarmapRenderer.render();
//	
//		
////		 Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
////		 Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
////		 texture.bind();
////		 sphereMesh.render(GL10.GL_TRIANGLE_STRIP);
////		 
////		 mesh.render(GL10.GL_TRIANGLES, 0, 3);
//
//	}
//
//	@Override
//	public void resize(int width, int height) {
//		theStarmapRenderer.resize(width, height);
//	}
//
//	@Override
//	public void show() {
//		Gdx.input.setInputProcessor( new InputMultiplexer(this, theStarmapRenderer.starStage, theStarmapRenderer.getStage()) );
//		theStarmapRenderer.init();
//	}
//
//	@Override
//	public void hide() {
//		Gdx.input.setInputProcessor(null);
//	}
//
//	@Override
//	public void pause() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void resume() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void dispose() {
//		Gdx.input.setInputProcessor(null);
//		theStarmapRenderer.dispose();
//	}
//
//	//Input processor
//	@Override
//	public boolean keyDown(int keycode) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean keyUp(int keycode) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean keyTyped(char character) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		return false;
//	}
//
//	@Override
//	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean touchDragged(int screenX, int screenY, int pointer) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean mouseMoved(int screenX, int screenY) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean scrolled(int amount) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//}

