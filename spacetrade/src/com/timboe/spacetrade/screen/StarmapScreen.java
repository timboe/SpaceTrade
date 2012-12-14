package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.windows.WarpBuyWindow;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class StarmapScreen extends SpaceTradeRender {
	
	private float offset = 0f;
	private ParticleEffectPool effectPool;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	private CameraController gestureControler;
	private OrthographicCamera gestureCam;
	
//	private SelectBox planetSelect = null;
		
	static int planetClickedID = -1;
	public static void setPlanetClickedID(int _id) {
		planetClickedID = _id;
		if (_id >= 0) fullRefresh = true;
	}
	static boolean fullRefresh = false;
	
	private static int planetHighlighID = -1;
	public static void setPlanetHighlightID(int _id) {
		planetHighlighID = _id;
	}
	public static int getPlanetHighlightID() {
		return planetHighlighID;
	}
	
	class CameraController implements GestureListener {
		float velX, velY;
		boolean flinging = false;
		float initialScale = 1;

		public boolean touchDown (float x, float y, int pointer, int button) {
			flinging = false;
			initialScale = gestureCam.zoom;
			return false;
		}

		@Override
		public boolean fling (float velocityX, float velocityY, int button) {
			flinging = true;
			velX = gestureCam.zoom * velocityX * 0.5f;
			velY = gestureCam.zoom * velocityY * 0.5f;
			return false;
		}

		@Override
		public boolean pan (float x, float y, float deltaX, float deltaY) {
			//Gdx.app.log("Backdrop", "In PAN X:"+x+" Y:"+y+" dX:"+deltaX+" dY:"+deltaY);
			gestureCam.position.add(-deltaX * gestureCam.zoom, deltaY * gestureCam.zoom, 0);
			return false;
		}

		public void update (float delta) {
			if (flinging) {
				velX *= 0.98f;
				velY *= 0.98f;
				gestureCam.position.add(-velX * delta, velY * delta, 0);
				if (Math.abs(velX) < 0.01f) velX = 0;
				if (Math.abs(velY) < 0.01f) velY = 0;
			}
			//bounce constrain here
			constrain(false);
		}
		
		public void constrain(boolean jump) {
			final float diffX = Textures.getGalaxyTexture().getWidth() - SpaceTrade.CAMERA_WIDTH/2;
			if (gestureCam.position.x < SpaceTrade.CAMERA_WIDTH/2) {
				if (jump == true) gestureCam.position.x = SpaceTrade.CAMERA_WIDTH/2;
				else {
					flinging = true;
					velX = -5*Math.abs(gestureCam.position.x - SpaceTrade.CAMERA_WIDTH/2);
				}
			} else if (gestureCam.position.x > diffX) {
				if (jump == true) gestureCam.position.x = diffX;
				else {
					flinging = true;
					velX = 5*Math.abs(gestureCam.position.x - diffX);
				}
			}
			final float diffY = Textures.getGalaxyTexture().getHeight() - SpaceTrade.CAMERA_HEIGHT/2;
			if (gestureCam.position.y < SpaceTrade.CAMERA_HEIGHT/2) {
				if (jump == true) gestureCam.position.y = SpaceTrade.CAMERA_HEIGHT/2;
				else {
					flinging = true;
					velY = 5*Math.abs(SpaceTrade.CAMERA_HEIGHT/2 - gestureCam.position.y);
				}
			} else if (gestureCam.position.y > diffY) {
				if (jump == true) gestureCam.position.y = diffY;
				else {
					flinging = true;
					velY = -5*Math.abs(diffY - gestureCam.position.y);
				}
			}
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {return false;}
		@Override
		public boolean longPress(float x, float y) {return false;}
		@Override
		public boolean zoom(float initialDistance, float distance) {return false;}
		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {return false;}
	}
	
	public StarmapScreen() {
		ParticleEffect pEffect = new ParticleEffect();
		pEffect.load(Gdx.files.internal("data/galaxyEffect3"), Gdx.files.internal("data/"));
		effectPool = new ParticleEffectPool(pEffect, 50, 250);
		
		PooledEffect effect = effectPool.obtain();
		effect.setPosition(0, 400);
		effects.add(effect);	
		
		gestureCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gestureControler = new CameraController();
		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, gestureControler);
		secondaryStage = new Stage(); //Uses for clickable planets
		secondaryStage.setCamera(gestureCam);
				
		init();
	}

	@Override
	protected void renderBackground(float delta) {
		gestureControler.update(delta);
		gestureCam.update();
		secondaryStage.setCamera(gestureCam);
		
		WarpBuyWindow.updateList(fullRefresh, planetClickedID);
		fullRefresh = false;
		
		spriteBatch.setProjectionMatrix(gestureCam.combined);
		spriteBatch.begin();
		spriteBatch.draw(Textures.getGalaxyTexture(), 0, 0);
		spriteBatch.end();
	}
	
	@Override
	protected void renderFX(float delta) {
		spriteBatch.setProjectionMatrix(gestureCam.combined);
		spriteBatch.begin();
		for (int i = effects.size - 1; i >= 0; i--) {
	        PooledEffect effect = effects.get(i);
	        effect.draw(spriteBatch, delta);
	        if (effect.isComplete()) {
	                effect.free();
	                effects.removeIndex(i);
	        }
		}
		spriteBatch.end();
		
		secondaryStage.act(delta);
		secondaryStage.draw();
	
		g2.setProjectionMatrix(secondaryStage.getCamera().combined);
		if (SpaceTrade.debug == true) {
			Table.drawDebug(secondaryStage);
			g2.begin(ShapeType.Rectangle);
			g2.setColor(0f, 1f, 0f, 0f);
			for (Planet _p : Starmap.getPlanets()) {
				_p.drawBasic(g2);
			}
			g2.end();
		}
		
		//Draw range
		final float _sx = Player.getPlayer().getX();
		final float _sy = Player.getPlayer().getY();
		final float _r = Player.getShip().getRangePixels();
		final int _stepSize = 36; //degree
		final float _miniStep = (float)_stepSize/8f;
		 offset += delta*5;
		for (float S = 0; S < 5; S+=0.5f) {
			for (int _step = 0; _step <= 360; _step += _stepSize) {
				final float _offset = _step + offset;
				g2.begin(ShapeType.Curve);
				g2.curve(_sx + (float)((_r+S) * Math.cos( Math.toRadians( _offset + (0 * _miniStep) ) )),
						 _sy + (float)((_r+S) * Math.sin( Math.toRadians( _offset + (0 * _miniStep) ) )),
						 _sx + (float)((_r+S) * Math.cos( Math.toRadians( _offset + (1 * _miniStep) ) )),
						 _sy + (float)((_r+S) * Math.sin( Math.toRadians( _offset + (1 * _miniStep) ) )),
						 _sx + (float)((_r+S) * Math.cos( Math.toRadians( _offset + (2 * _miniStep) ) )),
						 _sy + (float)((_r+S) * Math.sin( Math.toRadians( _offset + (2 * _miniStep) ) )),
						 _sx + (float)((_r+S) * Math.cos( Math.toRadians( _offset + (3 * _miniStep) ) )),
						 _sy + (float)((_r+S) * Math.sin( Math.toRadians( _offset + (3 * _miniStep) ) ))  );
				g2.end();
			}
		}
	}
	
	@Override
	public void renderForeground(float delta) {
		if (planetClickedID >= 0 && Player.getPlayer().isMoving() == false) {
			leftTable.setVisible(true);
		} else {
			leftTable.setVisible(false);
		}
		
		stage.act(delta);
		stage.draw();
		if (SpaceTrade.debug == true) Table.drawDebug(stage);
	}
	
	@Override 
	public void show() {
//		if (planetSelect == null) {
//			planetSelect = new SelectBox(Starmap.getPlanetNames(), Textures.getSkin());
//
//		}
//		leftTable.add(planetSelect);
		
		gestureCam.position.set(Player.getPlayer().getX(),  Player.getPlayer().getY(), 0);
		gestureControler.flinging = false;
		gestureControler.constrain(true);

		leftTable.clear();
		leftTable.add(WarpBuyWindow.getWindow());
		
		WarpBuyWindow.updateList(true, planetClickedID);
		if (secondaryStage.getActors().size == 0) {
			for (Planet _p : Starmap.getPlanets()) {
				secondaryStage.addActor(_p);
			}
			secondaryStage.addActor(Player.getPlayer());
		}
		super.show();
		//masterTable.getCell(leftTable).left().top();
		//masterTable.invalidateHierarchy();
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
//	@Override
//	public void dispose() {
//		Gdx.input.setInputProcessor(null);
//		theStarmapRenderer.dispose();
//	}

//
//}

