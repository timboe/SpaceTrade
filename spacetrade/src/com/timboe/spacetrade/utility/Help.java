package com.timboe.spacetrade.utility;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.Government;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;

public class Help {

	public static void errorOK(String _msg) {
		new Dialog("Error", Textures.getSkin(), "dialog") {
			protected void result (Object object) {
				System.out.println("Chosen: " + object);
			}
		}.text(_msg)
		.button("OK", true, Textures.getSkin().get("large", TextButtonStyle.class))
		.key(Keys.ENTER, true)
		.key(Keys.ESCAPE, true)
		.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage());
	}
	
	private static void helpMsg(String _msg) {
		new Dialog("Info", Textures.getSkin(), "dialog") {
			protected void result (Object object) {
				System.out.println("Chosen: " + object);
			}
		}.text(_msg)
		.button("OK", true, Textures.getSkin().get("large", TextButtonStyle.class))
		.key(Keys.ENTER, true)
		.key(Keys.ESCAPE, true)
		.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage());
	}
	
	public static void help(Government _gov) {
		switch (_gov) {
		case Aristocracy: helpMsg(""); break;
		case Autocracy: helpMsg(""); break;
		case Bureaucracy: helpMsg(""); break;
		case Democracy: helpMsg(""); break;
		case Dictatorship: helpMsg(""); break;
		case Ergatocracy: helpMsg(""); break;
		case Geniocracy: helpMsg(""); break;
		case Kratocracy: helpMsg(""); break;
		case Monarchy: helpMsg(""); break;
		case Oligarchy: helpMsg(""); break;
		case Plutocracy: helpMsg(""); break;
		case Republic: helpMsg(""); break;
		case Technocracy: helpMsg(""); break;
		case Theocracy: helpMsg(""); break;
		}
	}
	
	public static void help(Civilisation _civ) {
		switch (_civ) {
		case Agricultural: helpMsg(""); break;
		case Cybernetic: helpMsg(""); break;
		case Industrial: helpMsg(""); break;
		case Renaissance: helpMsg(""); break;
		case Technological: helpMsg(""); break;
		case Transcendental: helpMsg(""); break;
		}
	}
	
	public static void help(Goods _g) {
		switch (_g) {
		case AI: helpMsg("AI"); break;
		case Computers: helpMsg("COMP"); break;
		case Grain: helpMsg(""); break;
		case HeavyWater: helpMsg(""); break;
		case Machinery: helpMsg(""); break;
		case MedicalGel: helpMsg(""); break;
		case Minerals: helpMsg(""); break;
		case Singularity: helpMsg(""); break;
		case SpaceCrack: helpMsg(""); break;
		case Textiles: helpMsg(""); break;
		}
	}
	
	
	
	
}
