package com.timboe.spacetrade;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SpaceTradeDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "SpaceTrade";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 800;
		new LwjglApplication(new SpaceTrade(), cfg);
	}
}
