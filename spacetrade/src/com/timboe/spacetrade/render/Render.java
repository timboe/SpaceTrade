package com.timboe.spacetrade.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.timboe.spacetrade.utility.Utility;



public class Render {
	
	protected Table leftTable;
	protected Table masterTable;
	protected Skin skin;
	protected Stage stage;
	
	public Render() {
		skin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));
		stage = new Stage();

		leftTable = new Table();
		leftTable.debug();
		leftTable.align( Align.center);
		leftTable.setSize(Utility.GAME_WIDTH, Utility.GAME_HEIGHT);
		

	}
	
	protected void init() {
		masterTable = new Table();
		masterTable.debug();
		masterTable.align(Align.bottom | Align.left);
		masterTable.setSize(Utility.CAMERA_WIDTH, Utility.CAMERA_HEIGHT);
		//masterTable.pad(10);
		masterTable.add(leftTable).width(Utility.GAME_WIDTH).height(Utility.GAME_HEIGHT);
		masterTable.add(RightBar.getRightBarTable()).width(Utility.GUI_WIDTH).height(Utility.GAME_HEIGHT);
		stage.addActor(masterTable);
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public boolean needsGL20 () {
		return true;
	}
	
	public void dispose () {
		stage.dispose();
	}
	
	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		Table.drawDebug(stage);
	}
	
	
	public void resize (int width, int height) {
		Gdx.app.log("Resize", "ReSize in Render ["+this+"] ("+width+","+height+")");
		stage.setViewport(Utility.CAMERA_WIDTH, Utility.CAMERA_HEIGHT, true);
		stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
	}
	

}
