package com.timboe.spacetrade.world;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.timboe.spacetrade.enumerator.ShipClass;

public class TextButtonShip extends TextButton {

	private ShipClass ship;
	
	public TextButtonShip(String text, Skin skin, ShipClass _s) {
		super(text, skin);
		ship = _s;
	}
	
	public TextButtonShip(String text, TextButtonStyle textButtonStyle, ShipClass _s) {
		super(text, textButtonStyle);
		ship = _s;
	}

	public ShipClass getShipClass() {
		return ship;
	}

}
