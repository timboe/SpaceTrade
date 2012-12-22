package com.timboe.spacetrade.screen;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Array;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.PlanetFX;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.ship.Ship;
import com.timboe.spacetrade.utility.Rnd;
import com.timboe.spacetrade.windows.BankWindow;
import com.timboe.spacetrade.windows.PlanetWindow;
import com.timboe.spacetrade.windows.TravelWindow;
import com.timboe.spacetrade.world.Starmap;

public class PlanetScreen extends SpaceTradeRender {
	
	private static int tocks = 0;
	public static int turn = 0;
	private static float velX = 0f;
	private static Ship encounter = null;
	private Rnd rnd = new Rnd();
	public static boolean triggerRefresh = false;
	public static boolean showBank = false;
	
	//combat log
	public static Array<String> combatLog = new Array<String>();
	public static int combatLogSize = 0;
	private static List combatList = new List (combatLog.toArray(), Textures.getSkin());
	private static ScrollPane combatPane = new ScrollPane(combatList, Textures.getSkin());
	
	public static final int width = 1050;

	public PlanetScreen() {
		init();
	}
	
	private void doApproach() {
		if (tocks > 0) {
			xTarget = tocks * 25;
		} else {
			xTarget = 0;
		}
		if ( xOffset > xTarget ) {
			velX = 0.01f * Math.abs(xOffset - xTarget);
		} else {
			velX = 0f;
		}
		xOffset -= velX;
	}
	
	private void checkForEncounter() {
		while (tocks > 0) {
			if (rnd.getRandChance( Player.getPlanet().getActivity(ShipTemplate.Pirate).getChance() ) == true ) {
				encounter(ShipTemplate.Pirate);
				return;
			}
			if (rnd.getRandChance( Player.getPlanet().getActivity(ShipTemplate.Police).getChance() ) == true ) {
				encounter(ShipTemplate.Police);
				return;
			}
			if (rnd.getRandChance( Player.getPlanet().getActivity(ShipTemplate.Trader).getChance() ) == true ) {
				encounter(ShipTemplate.Trader);
				return;
			}
			endEncounter();
		}
	}
	
	private void encounter(ShipTemplate _st) {
		encounter = new Ship(_st);
		combatLog.add(Player.name+"INFO: "+encounter.getMod().toString() + " " + _st.toString() + " " + encounter.getShipClass().getName() 
				+ " encountered  at " + tocks + " tocks from " + Player.getPlanet().getName());
		Player.getShip().doScanOf(encounter);
	}
	
	public static String getTocksStr() {
		if (tocks == 1) {
			return " 1 Tock";
		} else {
			return ""+tocks+" Tocks";
		}
	}
	
	public static Ship getEncounter() {
		return encounter;
	}
	
	public static void endEncounter() {
		encounter = null;
		turn = 0;
		Player.getShip().doCooldown(true);
		Player.getShip().doMaintenance();
		if (--tocks == 0) {
			arrived(); //sets triggerRefresh=true and PlanetWindow.doFadeIn=true on completion
		}
	}
	
	@Override
	protected void renderBackground(float delta) {
		if (triggerRefresh == true) {
			triggerRefresh = false;
			show();
		}
		if (encounter == null && tocks > 0) {
			checkForEncounter();
		}
		doApproach();
		if (tocks > 0) {
			if (combatLogSize != combatLog.size) {
				combatLogSize = combatLog.size;
				combatList.setItems( combatLog.toArray() );
				combatList.invalidateHierarchy();
				combatPane.validate();
				combatPane.setScrollPercentY(100);
			}
			TravelWindow.updateList();
		} else if (showBank == true) {
			BankWindow.update(false);
		}
		renderPlanetBackdrop();
	}
	
	@Override
	protected void renderFX(float delta) {
		renderPlanet(delta, 
				PlanetFX.getTexture(Player.getPlanetID()),
				PlanetFX.getNormals(Player.getPlanetID()),
				Player.getPlanet().getSize());	}
	
	@Override
	public void show() {
		leftTable.clear();
		leftTable.defaults().pad(10);
		
		if (tocks > 0) {
			leftTable.center().top();
			leftTable.add(TravelWindow.getWindow());
			leftTable.row();
			leftTable.add(combatPane).width(width).height(200);
			combatPane.addAction(Actions.fadeIn(0));
			combatPane.act(1);
		} else {
			if (showBank == false) {
				leftTable.center().left();
				leftTable.add(PlanetWindow.getWindow());
			} else {
				leftTable.center();
				leftTable.add(BankWindow.getWindow());
			}
		}
		
		super.show();
	}

	public static void newDestination() {
		tocks = 20;
		xOffset = 20 * 25;
		combatLog.clear();
		combatLog.add(Player.name+" "+tocks+" tocks from "+Player.getPlanet().getName()+" / Initiating Approach Shell");
	}

	public static void setTocks(int i) {
		tocks = i;
		if (tocks == 0) {
			arrived();
		}
	}
	
	public static void arrived() {
		Player.getShip().maxShields();
		Player.getShip().resetHeat();
		TravelWindow.fadeOut();
		RightBar.update();
		combatPane.addAction(Actions.fadeOut(0.5f));
	}
	
}
