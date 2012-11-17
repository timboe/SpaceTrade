package com.timboe.spacetrade.world;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.timboe.spacetrade.enumerator.Goods;

public class TextButtonGoods extends TextButton {

	private Goods goods;
	
	public TextButtonGoods(String text, Skin skin, Goods _g) {
		super(text, skin);
		goods = _g;
	}
	
	public Goods getGoods() {
		return goods;
	}

}
