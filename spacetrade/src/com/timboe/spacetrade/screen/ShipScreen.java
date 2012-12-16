package com.timboe.spacetrade.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.PlanetFX;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.windows.EquipmentWindow;
import com.timboe.spacetrade.windows.ShipWindow;
import com.timboe.spacetrade.windows.ShipyardWindow;
import com.timboe.spacetrade.windows.WeaponsWindow;

public class ShipScreen extends SpaceTradeRender {
	
	public static boolean updateAll = false;
	
	private TextButton viewShipButton;
	private TextButton viewShopButton;
	private boolean viewShip = false;

	//private float Delta;

	
	public ShipScreen() {
		viewShopButton = new TextButton("SHOP", Textures.getSkin().get("large-toggle", TextButtonStyle.class));
		viewShipButton = new TextButton("VIEW SHIP", Textures.getSkin().get("large-toggle", TextButtonStyle.class));

		viewShopButton.setChecked(true);
		ButtonGroup group = new ButtonGroup(viewShopButton, viewShipButton);
		group.setMaxCheckCount(1);
		
		viewShopButton.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("doBuyButton","Interact:"+event.toString());
				viewShip = false;
				show();
			}
		});
		
		viewShipButton.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("doBuyButton","Interact:"+event.toString());
				viewShip = true;
				show();
			}
		});
		init();
	}

	@Override
	protected void renderBackground(float delta) {
		if (updateAll == true) {
			if (viewShip == true) {
				show(); //Currently needs full refresh, may change //TODO
			} else {
				EquipmentWindow.updateList();
				WeaponsWindow.updateList();
				ShipyardWindow.updateList();
			}
			updateAll = false;
		}
		renderPlanetBackdrop();
	}
	
	@Override
	protected void renderFX(float delta) {
		renderPlanet(delta, 
				PlanetFX.getTexture(Player.getPlanetID()),
				PlanetFX.getNormals(Player.getPlanetID()),
				Player.getPlanet().getSize());
		
//		//Ship test
//		Delta += delta;
//		Matrix4 transform_FX2 = screenCam.combined.cpy();
//		transform_FX2.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
//		transform_FX2.translate(-400, 0, 0);
//		transform_FX2.rotate(1, 0, 0, -15f);
//		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
//		transform_FX2.rotate(1, 1, 1, Delta*10);
//		shader.begin();
//        Vector3 lightPos = new Vector3(0,0,0.005f);
//        lightPos.x = Gdx.input.getX();
//        lightPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
//        shader.setUniformf("light", lightPos);
//        shader.setUniformMatrix("u_projTrans", transform_FX2);
//        //PlanetFX.getNormals(Player.getPlanetID()).bind(1);
//        Textures.getPink().bind(0);
//        Meshes.getPinkTube().render(shader, GL20.GL_TRIANGLES);
//        shader.end();
//        Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
	}
	
	@Override 
	public void show() {
		leftTable.clear();
		Table topButtons = new Table(Textures.getSkin());
		topButtons.defaults().pad(10);
		topButtons.debug();
		topButtons.add(viewShopButton).width(350).height(50);
		topButtons.add(viewShipButton).width(350).height(50);
		leftTable.align(Align.top);
		leftTable.add(topButtons).width( leftTable.getWidth() ).colspan(2);
		leftTable.row();
		

		if (viewShip == false) {
			Window bottomLContainer = new Window("", Textures.getSkin().get("transparent", WindowStyle.class));
			Window bottomRContainer = new Window("", Textures.getSkin().get("transparent", WindowStyle.class));
			bottomLContainer.add(ShipyardWindow.getWindow()).width(500);
			
			bottomRContainer.add(WeaponsWindow.getWindow()).width(550);
			bottomRContainer.row().pad(10);
			bottomRContainer.add(EquipmentWindow.getWindow()).width(550);
			
			leftTable.add(bottomLContainer);
			leftTable.add(bottomRContainer);
		} else {
			Window bottomContainer = new Window("", Textures.getSkin().get("transparent", WindowStyle.class));
			bottomContainer.debug();
			bottomContainer.defaults().pad(10).padRight(50);
			bottomContainer.add(ShipWindow.getWindow()).width(485).height(700);
			leftTable.add(bottomContainer).align(Align.right).colspan(2);
		}

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
