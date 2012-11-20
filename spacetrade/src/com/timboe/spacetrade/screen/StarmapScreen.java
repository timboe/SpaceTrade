package com.timboe.spacetrade.screen;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.world.TextButtonGoods;

public class StarmapScreen extends SpaceTradeRender {
	
	private ShapeRenderer g2 = new ShapeRenderer();
	private float offset = 0f;
	private ParticleEffectPool effectPool;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	private SpriteBatch _sb = new SpriteBatch();
	private boolean buyWindowPopulated = false;
	
	private ChangeListener buyClick;
	private Window buyWindow;
	private Label disclaimer;
	private TextButton prevPlanet;
	private TextButton nextPlanet;
	private TextButton warp;
	private Label warpInfo;
	private final EnumMap<Goods, Label> labelLocalPrice = new EnumMap<Goods, Label>(Goods.class);
	private final EnumMap<Goods, Label> labelRemotePrice = new EnumMap<Goods, Label>(Goods.class);
	private final EnumMap<Goods, Slider> sliderStock = new EnumMap<Goods, Slider>(Goods.class);
	private final EnumMap<Goods, Label> labelStock = new EnumMap<Goods, Label>(Goods.class);
	private final EnumMap<Goods, Label> labelCargo = new EnumMap<Goods, Label>(Goods.class);
	private final EnumMap<Goods, TextButton> buttonBuy = new EnumMap<Goods, TextButton>(Goods.class);
	
	static int planetClickedID = -1;
	public static void setPlanetClickedID(int _id) {
		planetClickedID = _id;
		fullRefresh = true;
	}
	static boolean fullRefresh = false;
	
	public StarmapScreen() {
		frontStage = new Stage(); //Uses front stage
		
		ParticleEffect pEffect = new ParticleEffect();
		pEffect.load(Gdx.files.internal("data/galaxyEffect"), Gdx.files.internal("data/"));
		effectPool = new ParticleEffectPool(pEffect, 50, 100);
		
		PooledEffect effect = effectPool.obtain();
		effect.setPosition(0, 0);
		effects.add(effect);
		
		
		init();
	}
	
	public void populateBuyWindow() {
		if (buyWindowPopulated == true) return;
		buyWindowPopulated = true;
		
		Skin _skin = Textures.getTextures().getSkin();
		
		buyClick = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
//				Gdx.app.log("SellButton","Interact:"+event.toString()+" "+((TextButtonGoods)actor).getGoods());
//				final Goods _g = ((TextButtonGoods)actor).getGoods();
//				final int _amount = (int) sliderStock.get(_g).getValue();
//				final int _profit = curPlanet.getPrice(_g) * _amount;
//				Player.getPlayer().modCredz(_profit);
//				Player.getPlayer().removeStock(_g, _amount);
//				curPlanet.modStock(_g, _amount);
//				final int _remainingStock = Player.getPlayer().getStock(_g);
//				if (_remainingStock == 0) {
//					sliderStock.get(_g).setRange(0, 1);
//				} else {
//					sliderStock.get(_g).setRange(0, _remainingStock);
//					if (_amount > _remainingStock) {
//						sliderStock.get(_g).setValue(_remainingStock);
//					} else {
//						sliderStock.get(_g).setValue(_amount);
//					}
//				}
			}
		};
		
		buyWindow = new Window("MyWindow", _skin);
		if (SpaceTrade.debug == true) buyWindow.debug();
		Table innerTable = new Table();
		if (SpaceTrade.debug == true) innerTable.debug();
		innerTable.defaults().pad(5);
		
		Label titleLabelA = new Label("GOODS", _skin);
		Label titleLabelB = new Label("LOCAL PRICE\nPER UNIT", _skin);
		titleLabelB.setAlignment(Align.center);
		Label titleLabelC = new Label("REMOTE PRICE\nPER UNIT", _skin);
		titleLabelC.setAlignment(Align.center);
		Label titleLabelD = new Label("STOCK", _skin);
		Label titleLabelE = new Label("CARGO", _skin);
		Label titleLabelF = new Label("BUY", _skin);
		disclaimer = new Label("", _skin);
		disclaimer.setAlignment(Align.center);
		
		innerTable.add(disclaimer).colspan(7);
		innerTable.row();
		innerTable.add(titleLabelA);
		innerTable.add(titleLabelB);
		innerTable.add(titleLabelC);
		innerTable.add(titleLabelD).colspan(2);
		innerTable.add(titleLabelE);
		innerTable.add(titleLabelF);
		innerTable.row();
		for (Goods _g : Goods.values()) {
			innerTable.add( new Label( _g.toDisplayString(), _skin ) );
			
			Label temp = new Label( "10", _skin );
			labelLocalPrice.put(_g, temp);
			innerTable.add( temp );
			
			temp = new Label( "101", _skin );
			labelRemotePrice.put(_g, temp);
			innerTable.add( temp );
			
			Slider sliderTemp = new Slider(0, 1, 1, false, _skin ); //set slider
			sliderStock.put(_g, sliderTemp);
			innerTable.add(sliderTemp);
			
			temp = new Label( "1000", _skin );
			labelStock.put(_g, temp);
			innerTable.add( temp ).width(50);	
			
			temp = new Label( "1000", _skin );
			labelCargo.put(_g, temp);
			innerTable.add( temp ).width(50);	
			
			TextButtonGoods buttonTemp = new TextButtonGoods("BUY", _skin, _g);
			buttonTemp.addListener(buyClick);
			buttonBuy.put(_g, buttonTemp);
			innerTable.add( buttonTemp );	
			
			innerTable.row();
		}
		
		buyWindow.add(innerTable).colspan(4);
		buyWindow.row();
		
		prevPlanet = new TextButton("<---", _skin);
		prevPlanet.addListener(new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("PrevButton","Click");
			}
		});
		buyWindow.add(prevPlanet).left();
		
		nextPlanet = new TextButton("--->", _skin);
		nextPlanet.addListener(new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("nextButton","Click");
			}
		});
		buyWindow.add(nextPlanet).left();

		warpInfo = new Label("", _skin);
		warpInfo.setAlignment(Align.center);
		buyWindow.add(warpInfo).center();
		
		warp = new TextButton("ENGAGE!", _skin);
		warp.getLabel().setFontScale(3);
		warp.addListener(new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("warp","Click");
			}
		});
		buyWindow.add(warp).right();
		
		leftTable.add(buyWindow);
		
		updateList(true);
	}
	
	private void updateList(boolean _intial) {
		fullRefresh = false;
		if (planetClickedID < 0) return;
		
		Planet curPlanet = Player.getPlayer().getPlanet();
		Planet targetPlanet = Starmap.getStarmap().getPlanet(planetClickedID);
		
		final String _titleStr = "Buying from "+curPlanet.getFullName();
		buyWindow.setTitle(_titleStr);
		buyWindow.setMovable(true);
		Starmap.getStarmap();
		final int _ly = (int) Math.floor( Starmap.getDistanceLightyear(curPlanet, targetPlanet) );
		disclaimer.setText("Comparing prices to "+targetPlanet.getFullName()
				+".\n Caution: Prices correct as of Stardate "+(Starmap.getStarDate() - _ly) 
				+" (" + _ly + " years ago).");
		
		warpInfo.setText(targetPlanet.getName() + " is " + _ly + " lightyears away from " + curPlanet.getName()
				+".\nTravel will take "+String.format("%.2f",Starmap.getTravelTimeGalactic(curPlanet,targetPlanet,2f))
				+" GalacticYears and "+String.format("%.2f",Starmap.getTravelTimeShip(curPlanet, targetPlanet, 2f))
				+" ShipYears.");
		for (Goods _g : Goods.values()) {

			final int _localPrice = curPlanet.getPrice(_g);
			final int _remotePrice = targetPlanet.getPrice(_g, _ly);
			
			if (targetPlanet.getSells(_g) == false) {
				labelRemotePrice.get(_g).setText( "---" );
			} else {
				labelRemotePrice.get(_g).setText(Integer.toString(_remotePrice));
			}
			
			if (curPlanet.getSells(_g) == false || targetPlanet.getSells(_g) == false) {
				labelRemotePrice.get(_g).setColor(1f, 1f, 1f, 1f);
			} else if (_remotePrice < _localPrice) {
				labelRemotePrice.get(_g).setColor(1f, 0f, 0f, 1f);
			} else {
				labelRemotePrice.get(_g).setColor(0f, 1f, 0f, 1f);
			}

			if (curPlanet.getSells(_g) == false) {
				labelLocalPrice.get(_g).setText( "---" );
				sliderStock.get(_g).setRange(0, 1);
				sliderStock.get(_g).setVisible(false);
				labelStock.get(_g).setText("---");
				sliderStock.get(_g).setTouchable(Touchable.disabled);
				buttonBuy.get(_g).setTouchable(Touchable.disabled);
				buttonBuy.get(_g).setVisible(false);
				continue;
			} else {
				if (curPlanet.getStock(_g) == 0) {
					sliderStock.get(_g).setTouchable(Touchable.disabled);
				} else {
					sliderStock.get(_g).setTouchable(Touchable.enabled);
				}
				labelLocalPrice.get(_g).setText( Integer.toString(_localPrice) );
				buttonBuy.get(_g).setTouchable(Touchable.enabled);
				buttonBuy.get(_g).setVisible(true);
				sliderStock.get(_g).setVisible(true);
			}
			
			if (_intial == true) {
				int toSell = curPlanet.getStock(_g);
				if (toSell == 0) {
					sliderStock.get(_g).setRange(0, 1);
					sliderStock.get(_g).setTouchable(Touchable.disabled);
				} else {
					sliderStock.get(_g).setRange(0, toSell);
					sliderStock.get(_g).setValue(toSell);
					sliderStock.get(_g).setTouchable(Touchable.enabled);
				}
			}
			
			int _chosen = (int) sliderStock.get(_g).getValue();
			labelStock.get(_g).setText( Integer.toString(_chosen) );
			
			int _cargo = Player.getPlayer().getStock(_g);
			labelCargo.get(_g).setText( Integer.toString(_cargo) );
		}
		

	}
	
	public void hookStage() {
		for (Planet _p : Starmap.getStarmap().getPlanets()) {
			frontStage.addActor(_p);
		}
		frontStage.addActor(Player.getPlayer());
	}
	
	
	@Override
	public void render(float delta) {
		updateList(fullRefresh);

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Texture _gt = Textures.getTextures().getGalaxyTexture();
		_sb.setProjectionMatrix(getStage().getCamera().combined);
		//_sb.getProjectionMatrix().translate(0, Utility.GAME_HEIGHT, 0);
		//_sb.getProjectionMatrix().rotate(0f, 0f, 1f, offset);
		_sb.begin();
		//_sb.draw(_gt,-1200, -Utility.GAME_HEIGHT - 70);
		_sb.draw(_gt,0,0);

		for (int i = effects.size - 1; i >= 0; i--) {
	        PooledEffect effect = effects.get(i);
	        effect.draw(_sb, delta);
	        if (effect.isComplete()) {
	                effect.free();
	                effects.removeIndex(i);
	        }
		}
		_sb.end();
		
		if (SpaceTrade.debug == true) {
			g2.setProjectionMatrix(getStage().getCamera().combined);
			g2.begin(ShapeType.Circle);
			g2.setColor(0f, 1f, 0f, 0f);
			for (Planet _p : Starmap.getStarmap().getPlanets()) {
				_p.drawBasic(g2);
			}
			g2.end();
		}
		
		//Draw range
		final float _sx = Player.getPlayer().getX();
		final float _sy = Player.getPlayer().getY();
		final float _r = 100;
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
		
		if (planetClickedID >= 0) {
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
		populateBuyWindow();
		updateList(true);
		super.show();
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

