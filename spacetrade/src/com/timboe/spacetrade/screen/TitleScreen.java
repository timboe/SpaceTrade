package com.timboe.spacetrade.screen;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.RightBar;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.world.Starmap;
import com.timboe.spacetrade.render.Textures;

public class TitleScreen extends SpaceTradeRender {
	
	private Mesh sphereMesh;
	private Texture texture;
	private ShaderProgram shader;
	// counter for texturing
	float Delta;                      
	List<Texture> modelTextures;
	ShaderProgram lightTexShader;
	ShaderProgram texShader;


	
	public TitleScreen() {
		
		TextButton newGame = new TextButton("New Game", Textures.getSkin());
		newGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				RightBar.setTouchable(Touchable.enabled);
				//new!
				Starmap.populate();
				Player.getPlayer().setCredz(1000);
				SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theStarmap );
				Gdx.app.log("NewGame", "Player Credz:"+Player.getPlayer().getCredz());
			}
		});
		
		TextButton resumeGame = new TextButton("Resume Game", Textures.getSkin());
		resumeGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//RightBar.setTouchable(Touchable.enabled);
				//Serialiser.loadState();
				//SpaceTrade.getSpaceTrade().setScreen( SpaceTrade.getSpaceTrade().theShipScreen );
				//Gdx.app.log("LoadGame", "Player Credz:"+Player.getPlayer().getCredz());
			}
		});
		
		leftTable.defaults().pad(20);
		leftTable.add(newGame).width(400).height(50);
		leftTable.row();
		leftTable.add(resumeGame).width(400).height(50);
		
		RightBar.setTouchable(Touchable.disabled);

		
		InputStream in = Gdx.files.internal("data/sphereMesh.obj").read();
		sphereMesh = ObjLoader.loadObj(in);
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//sphereMesh.scale(0.35f, 0.5f, 0.35f);
		sphereMesh.scale(300f, 350f, 300f);
		//sphereMesh.
		System.out.println(sphereMesh.getNumVertices()+" "+ sphereMesh.getNumIndices());
		
		texture = new Texture(Gdx.files.internal("data/sphereMesh.jpg"), Format.RGB565, true);
		texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		
		
		lightTexShader = new ShaderProgram(Gdx.files.internal("data/light-tex-vs.glsl"),
				Gdx.files.internal("data/light-tex-fs.glsl"));
		
		texShader = new ShaderProgram(Gdx.files.internal("data/tex-vs.glsl"),
				Gdx.files.internal("data/tex-fs.glsl"));

		init();
	}
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		GLCommon gl = Gdx.gl;
//		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		Texture _gt = Textures.getNebulaTexture(0);
		Camera cam = new OrthographicCamera();

		
		Matrix4 transform_BG = new Matrix4();
		Matrix4 transform_FG = new Matrix4();
		transform_BG = cam.combined.cpy();
		transform_BG.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_BG.translate(-SpaceTrade.CAMERA_WIDTH/2f, -SpaceTrade.CAMERA_HEIGHT/2f, 0f);

		transform_FG = cam.combined.cpy();
		transform_FG.scale(2f/SpaceTrade.CAMERA_WIDTH, 2f/SpaceTrade.CAMERA_HEIGHT, 0f);
		transform_FG.translate(200, 0f, 0f);
		transform_FG.rotate(0, 0, 1, 180);
		transform_FG.rotate(0, 1, 0, Delta*10f);

		
		spriteBatch.setProjectionMatrix(transform_BG);
		spriteBatch.begin();
		spriteBatch.draw(_gt,0,0);
		spriteBatch.end();

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_TEXTURE_2D);


	    
		Delta += 2*delta;
		texture.bind();
		texShader.begin();
		texShader.setUniformi("u_diffuse", 0);
		texShader.setUniformMatrix("u_projView", transform_FG);
	//	Matrix4 normal = new Matrix4();
	//	normal.idt();
	///	normal = cam.combined.cpy();
	//	normal.rotate(1, 1, 1, Delta*100f);
//		
	//	Matrix3 normal3 = new Matrix3();
	//	normal3.set(normal.toNormalMatrix());
		//lightTexShader.setUniformMatrix("u_normal", normal3);
		sphereMesh.render(texShader, GL10.GL_TRIANGLES);
		texShader.end();
		
		
		
		//sphereMesh.render(GL10.GL_TRIANGLES);
		

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_DEPTH_TEST);
////	 Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
////	 texture.bind();
////	 sphereMesh.render(GL10.GL_TRIANGLE_STRIP);
		
		Camera stageCam = new OrthographicCamera();
		stageCam.projection.set(transform_BG);
		//stage.setCamera(stageCam);
		stage.act(delta);
		stage.draw();
		if (SpaceTrade.debug == true) Table.drawDebug(stage);
		
		
		
	}


}
