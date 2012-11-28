package com.timboe.spacetrade.world;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.timboe.spacetrade.enumerator.Equipment;

public class ImageButtonEquipment extends ImageButton {

	private Equipment e;
	private boolean a;
	
	public ImageButtonEquipment(Skin skin, Equipment _e, boolean _add) {
		super(skin);
		e = _e;
		a = _add;
	}
	
	public ImageButtonEquipment(ImageButtonStyle imageButtonStyle, Equipment _e, boolean _add) {
		super(imageButtonStyle);
		e = _e;
		a = _add;
	}

	public Equipment getEquipment() {
		return e;
	}
	
	public boolean getAdd() {
		return a;
	}

}
