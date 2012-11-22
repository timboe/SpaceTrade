package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.utility.Serialiser;
import com.timboe.spacetrade.utility.Utility;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class SpaceTradeRender implements Screen {
	protected Table leftTable;
	protected Table masterTable;
	protected Stage stage;
	protected Stage frontStage = null;
	private InputMultiplexer inputMultiplex = null;

	
	public SpaceTradeRender() {
		masterTable = new Table();
		if (SpaceTrade.debug == true) masterTable.debug();
		masterTable.align(Align.bottom | Align.left);
		masterTable.setSize(Utility.CAMERA_WIDTH, Utility.CAMERA_HEIGHT);
		
		stage = new Stage();

		leftTable = new Table();
		if (SpaceTrade.debug == true) leftTable.debug();
		leftTable.align( Align.center);
		leftTable.setSize(Utility.GAME_WIDTH, Utility.GAME_HEIGHT);
		
		//init(); should be caller after superclass constructor
	}
	
	public void init() {
		masterTable.clear();
		masterTable.add(leftTable).width(Utility.GAME_WIDTH).height(Utility.GAME_HEIGHT);
		masterTable.add(RightBar.getRightBarTable()).width(Utility.GUI_WIDTH).height(Utility.GAME_HEIGHT);
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
	
	public Stage getStage() {
		return stage;
	}
	
	public Stage getFrontStage() {
		return frontStage;
	}
	
	public boolean needsGL20 () {
		return true;
	}
	
	public void dispose () {
		stage.dispose();
	}
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		if (SpaceTrade.debug == true) Table.drawDebug(stage);
		if (frontStage != null) {
			frontStage.act(delta);
			frontStage.draw();
		}
	}

	
	
	public void resize (int width, int height) {
		Gdx.app.log("Resize", "ReSize in Render ["+this+"] ("+width+","+height+")");
		stage.setViewport(Utility.CAMERA_WIDTH, Utility.CAMERA_HEIGHT, true);
		stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
		if (frontStage != null) {
			frontStage.setCamera(stage.getCamera());
		}
	}
	
	@Override
	public void show() {
		if (frontStage == null) {
			Gdx.input.setInputProcessor( getStage() );
		} else {
			if (inputMultiplex == null) {
				inputMultiplex = new InputMultiplexer( getStage(), getFrontStage() );
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
