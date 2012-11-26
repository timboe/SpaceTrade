package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.utility.ScreenFade;
import com.timboe.spacetrade.utility.Serialiser;

//This is the master Screen and Render class which other game windows all inherit from

public class SpaceTradeRender implements Screen {
	protected Table leftTable;
	protected Table masterTable;
	protected Stage stage;
	protected Stage secondaryStage = null;
	protected ShapeRenderer g2 = new ShapeRenderer();
	private InputMultiplexer inputMultiplex = null;
	protected SpriteBatch spriteBatch = new SpriteBatch();
	protected Camera screenCam = new OrthographicCamera();
	private Image blackSquare;
	protected SpriteBatch spriteBatchFade = new SpriteBatch();
	
	protected Matrix4 transform_BG;
	protected Matrix4 transform_FX;

	public Image getBlackSquare() {
		return blackSquare;
	}
	
	public SpaceTradeRender() {
		masterTable = new Table();
		if (SpaceTrade.debug == true) masterTable.debug();
		masterTable.align(Align.bottom | Align.left);
		masterTable.setSize(SpaceTrade.CAMERA_WIDTH, SpaceTrade.CAMERA_HEIGHT);
		
		stage = new Stage();

		leftTable = new Table();
		if (SpaceTrade.debug == true) leftTable.debug();
		leftTable.align( Align.center);
		leftTable.setSize(SpaceTrade.GAME_WIDTH, SpaceTrade.GAME_HEIGHT);
		
		blackSquare = new Image(Textures.getBlackSquare());
		blackSquare.addAction( Actions.fadeOut(0));
		blackSquare.act(1);
		blackSquare.setSize(500, 500);
		
		//init(); should be caller after superclass constructor
	}
	
	public void init() {
		masterTable.clear();
		masterTable.add(leftTable).width(SpaceTrade.GAME_WIDTH).height(SpaceTrade.GAME_HEIGHT);
		masterTable.add(RightBar.getRightBarTable()).width(SpaceTrade.GUI_WIDTH).height(SpaceTrade.GAME_HEIGHT);
		stage.clear();
		stage.addActor(masterTable);
	}
	
//	public void hookStage() {
//		//Overrider
//	}
	
	//CAN (not must) be overriden
//	public void unHookStage() {
//		if (frontStage != null) {
//			frontStage.clear();
//		}
//	}
	
//	public Stage getStage() {
//		return stage;
//	}
	
//	public Stage getFrontStage() {
//		return frontStage;
//	}
	
	public boolean needsGL20 () {
		return true;
	}
	
	public void dispose () {
		stage.dispose();
	}
	
	public void render(float delta) {
		//delta = Math.max(delta, 30);
		renderClear(delta);
		renderBackground(delta);
		renderFX(delta);
		renderForeground(delta);
		renderFade(delta);
	}

	protected void renderFade(float delta) {
		ScreenFade.checkFade();
		Matrix4 transform_Fade = screenCam.combined.cpy();
		transform_Fade.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_Fade.translate(-SpaceTrade.CAMERA_WIDTH/2f, -SpaceTrade.CAMERA_HEIGHT/2f, 0f);
		
		spriteBatchFade.setProjectionMatrix(transform_Fade);
		spriteBatchFade.begin();
		blackSquare.act(delta);
		blackSquare.draw(spriteBatchFade, 1f);
		spriteBatchFade.end();
	}

	protected void renderForeground(float delta) {
		stage.act(delta);
		stage.draw();
		if (SpaceTrade.debug == true) Table.drawDebug(stage);
		if (secondaryStage != null) {
			secondaryStage.act(delta);
			secondaryStage.draw();
		}		
	}

	
	protected void renderFX(float delta) {
		
	}

	protected void renderBackground(float delta) {
		
	}

	protected void renderClear(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);		
	}
	
	public void resize (int width, int height) {
		Gdx.app.log("Resize", "ReSize in Render ["+this+"] ("+width+","+height+")");
		stage.setViewport(SpaceTrade.CAMERA_WIDTH, SpaceTrade.CAMERA_HEIGHT, true);
		stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
		blackSquare.setSize(width,height);
		screenCam = new OrthographicCamera();
		
		transform_BG = screenCam.combined.cpy();
		transform_BG.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_BG.translate(-SpaceTrade.CAMERA_WIDTH/2f, -SpaceTrade.CAMERA_HEIGHT/2f, 0f);
		
		transform_FX = screenCam.combined.cpy();
		transform_FX.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_FX.translate(200, 0, 0);
		transform_FX.rotate(1, 0, 0, -10f);
	
		if (secondaryStage != null) {
			secondaryStage.setCamera(stage.getCamera());
		}
	}
	
	@Override
	public void show() {
		if (secondaryStage == null) {
			Gdx.input.setInputProcessor( stage );
		} else {
			if (inputMultiplex == null) {
				inputMultiplex = new InputMultiplexer( stage, secondaryStage );
			}
			Gdx.input.setInputProcessor( inputMultiplex );
		}
//		hookStage();
		init();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
//		unHookStage();
	}
	

	@Override
	public void pause() {
//		unHookStage();
//		Starmap.unHookListners();
		Serialiser.saveState();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		//ADD RESTORE LINE
//		hookStage();		
	}
	
}
