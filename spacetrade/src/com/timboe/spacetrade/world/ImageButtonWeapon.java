package com.timboe.spacetrade.world;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.timboe.spacetrade.enumerator.Weapons;

public class ImageButtonWeapon extends ImageButton {

	private Weapons w;
	private boolean a;
	
	public ImageButtonWeapon(Skin skin, Weapons _w, boolean _add) {
		super(skin);
		w = _w;
		a = _add;
	}
	
	public ImageButtonWeapon(ImageButtonStyle imageButtonStyle, Weapons _w, boolean _add) {
		super(imageButtonStyle);
		w = _w;
		a = _add;
	}

	public Weapons getWeaponClass() {
		return w;
	}
	
	public boolean getAdd() {
		return a;
	}

}
