package com.timboe.spacetrade.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.windows.EquipmentWindow;
import com.timboe.spacetrade.windows.ShipyardWindow;
import com.timboe.spacetrade.windows.WeaponsWindow;

public class ShipScreen extends SpaceTradeRender {
	
	public ShipScreen() {
		init();
	}

	@Override
	protected void renderBackground(float delta) {
		spriteBatch.setProjectionMatrix(transform_BG);
		spriteBatch.begin();
		spriteBatch.draw(Textures.getStarscape( Player.getPlanetID() ),0,0);
		spriteBatch.end();
	}
	
	@Override 
	public void show() {
		leftTable.clear();
		leftTable.defaults().pad(50);
		leftTable.align(Align.left);

		Window utilities = new Window("",Textures.getSkin().get("transparent", WindowStyle.class));
		utilities.add(WeaponsWindow.getWindow());
		utilities.row().pad(20);
		utilities.add(EquipmentWindow.getWindow());
		
		leftTable.add(ShipyardWindow.getWindow());
		leftTable.add(utilities);

		
		super.show();
	}
	
	public void init() {
		masterTable.clear();
		masterTable.add(leftTable).width(SpaceTrade.GAME_WIDTH).height(SpaceTrade.GAME_HEIGHT);
		masterTable.add(RightBar.getRightBarTable()).width(SpaceTrade.GUI_WIDTH).height(SpaceTrade.GAME_HEIGHT);
		stage.clear();
		stage.addActor(masterTable);
	}
	
}
