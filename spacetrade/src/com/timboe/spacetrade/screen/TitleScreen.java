package com.timboe.spacetrade.screen;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.utility.Serialiser;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.render.Textures;

public class TitleScreen extends SpaceTradeRender {
	
	private Mesh sphereMesh;
	private Texture texture;
	private ShaderProgram shader;
	
	public TitleScreen() {
		
		TextButton newGame = new TextButton("New Game", Textures.getTextures().getSkin());
		newGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				RightBar.getRightBar().setTouchable(Touchable.enabled);
				//new!
				Starmap.populate();
				Player.newPlayer();
				//
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theShipScreen );
				Gdx.app.log("NewGame", "Player Credz:"+Player.getPlayer().getCredz());
				Player.getPlayer().modCredz(555);
				Gdx.app.log("NewGame", "Player Credz:"+Player.getPlayer().getCredz());
			}
		});
		
		TextButton resumeGame = new TextButton("Resume Game", Textures.getTextures().getSkin());
		resumeGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				RightBar.getRightBar().setTouchable(Touchable.enabled);
				Serialiser.loadState();
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theShipScreen );
				Gdx.app.log("LoadGame", "Player Credz:"+Player.getPlayer().getCredz());
			}
		});
		
		leftTable.defaults().pad(20);
		leftTable.add(newGame).width(400).height(50);
		leftTable.row();
		leftTable.add(resumeGame).width(400).height(50);
		
		RightBar.getRightBar().setTouchable(Touchable.disabled);
		
		InputStream in = Gdx.files.internal("data/world/sphere.obj").read();
		sphereMesh = ObjLoader.loadObj(in);
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//sphereMesh.scale(0.2f, 0.4f, 0.5f);
		//sphereMesh.
		System.out.println(sphereMesh.getNumVertices()+" "+ sphereMesh.getNumIndices());
		
		texture = new Texture(Gdx.files.internal("data/world/EarthMap_2500x1250.jpg"));
		texture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		
		String vertexShader = "attribute vec4 a_position;    \n" + 
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" + 
                "uniform mat4 u_worldView;\n" + 
                "varying vec4 v_color;" + 
                "varying vec2 v_texCoords;" + 
                "void main()                  \n" + 
                "{                            \n" + 
                "   v_color = vec4(1, 1, 1, 1); \n" + 
                "   v_texCoords = a_texCoord0; \n" + 
                "   gl_Position =  u_worldView * a_position;  \n"      + 
                "}                            \n" ;
		String fragmentShader = "#ifdef GL_ES\n" +
                  "precision mediump float;\n" + 
                  "#endif\n" + 
                  "varying vec4 v_color;\n" + 
                  "varying vec2 v_texCoords;\n" + 
                  "uniform sampler2D u_texture;\n" + 
                  "void main()                                  \n" + 
                  "{                                            \n" + 
                  "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"+
                  "}";
		
		shader = new ShaderProgram(vertexShader, fragmentShader);

		
		init();
	}
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();

		Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
		texture.bind();
		sphereMesh.render(shader, GL20.GL_TRIANGLES);
////	 Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
////	 texture.bind();
////	 sphereMesh.render(GL10.GL_TRIANGLE_STRIP);
		
		
		
	}


}
